I this step we learn how to:

1. Write unit test for main index page

2. Write unit tests for our rest API
```
	@Test
	public void checkThatWeCanAdd(TestContext context) {
	  Async async = context.async();

	  final String json = Json.encodePrettily(new ItemData("Test item", "Test item description"));
	  final String length = Integer.toString(json.length());
	  
	  int port = HttpServerVerticle.PORT_DEFAULT;

	  vertx.createHttpClient()
	  	   // prepare request and send it
	  	  .post(port, "localhost", "/api/items")
	      .putHeader("content-type", "application/json")
	      .putHeader("content-length", length)
	      
	      // handle response
	      .handler(response -> {

	    	// check response status code
	        context.assertEquals(response.statusCode(), 201);
	        // check response content type
	        context.assertTrue(response.headers().get("content-type").contains("application/json"));
	        // check response body
	        response.bodyHandler(body -> {

	          final ItemData item = Json.decodeValue(body.toString(), ItemData.class);

	          context.assertEquals(item.getName(), "Test item");
	          context.assertEquals(item.getDescription(), "Test item description");
	          context.assertNotNull(item.getId());

	          async.complete();
	        });
	      })
	      // save data
	      .write(json)
	      .end();
	}
```
3. There is a lots of boilerplate code, especially when running integration tests. Now we are going to compose our own integration test pipeline
by doing:
	* reserving a free point for our testing envirnment
	* generating the application configuration
	* starting the application
	* executing the application
	* stopping the application 

4. Add build-helper-maven-plugin to our plugin configuration:
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>build-helper-maven-plugin</artifactId>
  <version>1.9.1</version>
  <executions>
    <execution>
      <id>reserve-network-port</id>
      <goals>
        <goal>reserve-network-port</goal>
      </goals>
      <phase>process-sources</phase>
      <configuration>
        <portNames>
          <portName>http.port</portName>
        </portNames>
      </configuration>
    </execution>
  </executions>
</plugin>

5. Below build tag add:
<testResources>
  <testResource>
    <directory>src/test/resources</directory>
    <filtering>true</filtering>
  </testResource>
</testResources>

6. Create src/test/resources/it-config.json file with the following content
{
  "http.port": ${http.port}
}

7. For stopping and starting application use:
<plugin>
  <artifactId>maven-antrun-plugin</artifactId>
  <version>1.8</version>
  <executions>
    <execution>
      <id>start-vertx-app</id>
      <phase>pre-integration-test</phase>
      <goals>
        <goal>run</goal>
      </goals>
      <configuration>
        <target>
          <!--
          Launch the application as in 'production' using the fatjar.
          We pass the generated configuration, configuring the http port to the picked one
          -->
          <exec executable="${java.home}/bin/java"
                dir="${project.build.directory}"
                spawn="true">
            <arg value="-jar"/>
            <arg value="${project.artifactId}-${project.version}-fat.jar"/>
            <arg value="-conf"/>
            <arg value="${project.build.directory}/test-classes/my-it-config.json"/>
          </exec>
        </target>
      </configuration>
    </execution>
    <execution>
      <id>stop-vertx-app</id>
      <phase>post-integration-test</phase>
      <goals>
        <goal>run</goal>
      </goals>
      <configuration>
        <!--
          Kill the started process.
          Finding the right process is a bit tricky. Windows command in in the windows profile (below)
          -->
        <target>
          <exec executable="bash"
                dir="${project.build.directory}"
                spawn="false">
            <arg value="-c"/>
            <arg value="ps ax | grep -Ei '[\-]DtestPort=${http.port}\s+\-jar\s+${project.artifactId}' | awk 'NR==1{print $1}' | xargs kill -SIGTERM"/>
          </exec>
        </target>
      </configuration>
    </execution>
  </executions>
</plugin>

## or for windows

<profiles>
  <!-- A profile for windows as the stop command is different -->
  <profile>
    <id>windows</id>
    <activation>
      <os>
        <family>windows</family>
      </os>
    </activation>
    <build>
      <plugins>
        <plugin>
          <artifactId>maven-antrun-plugin</artifactId>
          <version>1.8</version>
          <executions>
            <execution>
              <id>stop-vertx-app</id>
              <phase>post-integration-test</phase>
              <goals>
                <goal>run</goal>
              </goals>
              <configuration>
                <target>
                  <exec executable="wmic"
                      dir="${project.build.directory}"
                      spawn="false">
                    <arg value="process"/>
                    <arg value="where"/>
                    <arg value="CommandLine like '%${project.artifactId}%' and not name='wmic.exe'"/>
                    <arg value="delete"/>
                  </exec>
                </target>
              </configuration>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
  </profile>
</profiles>

8. In order to execute our integration tests add the following plugin:
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-failsafe-plugin</artifactId>
  <version>2.18.1</version>
  <executions>
    <execution>
      <goals>
        <goal>integration-test</goal>
        <goal>verify</goal>
      </goals>
      <configuration>
        <systemProperties>
          <http.port>${http.port}</http.port>
        </systemProperties>
      </configuration>
    </execution>
  </executions>
</plugin>

9. Add the following librarys that makes rest testing more easy
<dependency>
  <groupId>com.jayway.restassured</groupId>
  <artifactId>rest-assured</artifactId>
  <version>2.4.0</version>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-core</artifactId>
  <version>2.0.0</version>
  <scope>test</scope>
</dependency>

10. Create a first integraiton tests:
package com.comarch.caseweek;

import com.jayway.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class MyRestIT {

  @BeforeClass
  public static void configureRestAssured() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = Integer.getInteger("http.port", 8080);
  }

  @AfterClass
  public static void unconfigureRestAssured() {
    RestAssured.reset();
  }
}

11. Add first test:
  @Test
  public void checkThatWeCanRetrieveIndividualProduct() {
    // Get the list of items, ensure it's a success and extract the first id.
    final int id = get("/api/items").then()
        .assertThat()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getInt("find { it.name=='item1' }.id");
  }


