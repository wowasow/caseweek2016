# init vertx project - initial step

This step should get you familiar with:
* generation of vertx project using Maven/Gradle
* importing project to your favourite IDE
* Learn about vertx project structure
* Learn about basic vertx concepts


1. Generate project structure (copy from github)
2*. Optional: Change sources to eclipse or intelij sources
3. Import project ot your favourite IDE 

In my case it is eclipse [add screens to presentation] however feel free to use 
the  best IDE that suits all your needs.

4. Describe structure of a newly created project

[print directory structure and eventually add those screens to presentation]
After successful project generation and importing it to the IDE you should be able to display the following project structure:

+---maven 				# maven wrapper jars
+---src 				# project sources
¦   +---main
¦   ¦   +---assembly 			# [0] for maven assembly plugin
¦   ¦   +---java 			# this is where java source files reside
¦   ¦   ¦   +---com
¦   ¦   ¦       +---comarch
¦   ¦   ¦           +---caseweek2016
¦   ¦   +---platform_lib		# [1]
¦   ¦   +---resources			
¦   ¦       +---platform_lib
|   |	    +---mod.json 		# [4]
|   |
¦   +---test 				# directory for all the tests
¦       +---java
¦       ¦   +---com
¦       ¦       +---comarch
¦       ¦           +---caseweek2016
¦       ¦               +---integration
¦       ¦               ¦   +---groovy
¦       ¦               ¦   +---java
¦       ¦               ¦   +---javascript
¦       ¦               ¦   +---python
¦       ¦               ¦   +---ruby
¦       ¦               +---unit
¦       +---resources
|	    +---platform_lib		# [2]
¦           +---integration_tests
¦               +---groovy
¦               +---javascript
¦               +---python
¦               +---ruby
|---pom.xml 				# maven configuration file

The generated project is a sample maven vertx module that listens for ping messages on vertx event bus. 
This is all that we need at the moment.

By default created module contains a simple Java verticle which listens on the event bus and responds to `ping!`
messages with `pong!`.

The example shows how to write a testin mutliply languages groovy, java, javascript, python, ruby.

The fatjar starter knows to add this directory (and any jars/zips inside it) to the Vert.x platform classpath when executing your module as a fatjar. 	

The most important directories and files are in the examples are:
	* src/main/java 		# this is where our sources are going to reside
	* src/main/test/java		# this is where our tests are going to reside
	* pom.xml			# main configuration point for every maven project

Lets take a look at pom.xml. There you should be able to see a bunch of properties: 
	* pullInDeps - (as described) whether module's dependencies are going to be dowloaded during packaing or runtime (when used for the first time)
	* createFatJar - package entire module as a fat jar (which contains moduls binaries and vertx platfor jars) that can be run from command line
			as java -jar ....
	* module.name - self explanatory
 	* mods.directory - where module is going to be assembled,

	* dependencies version - versions of dependencies that are required by out module
	* [maven plugin versions]

The properties are used in maven plugins used to compile/package the project. The most important dependencies used in the project are:
	* vertx-core - Vert.x core contains fairly low level functionality including support for HTTP, TCP, file system access, and various other features. 
		You can use this directly in your own applications, and it's used by many of the other components of Vert.x
	* vertx-platform 
	* vertx-hazelcast - It is the default cluster manager used in the Vert.x distribution, but it can be replaced with another implementation as Vert.x 
		cluster managers are pluggable. This implementation is packaged inside:
	* junit - library used for test suits etc.
	* testtools - testing our asynchronous applications

5. Lets change a couple of things in our pom:
	* [change vertx version to 3.2.1] and modyfi a code so that it is compiled with the newset version
	* [change vertx testools to vertx-unit:3.0.0]
	* change java version in maven-compiler plugin to jdk 1.8 so that it can be used with our module.
	* change vertx.createFatJar property to true so that we can build a fat jar and run it independently of our IDE
	* remove all the necessary 
	* note auto-redeploy

6. Lets review vertx main configuraiton file mod.json

7. Run the project by issuing 
	``` mvn clean install ``` - not necessary
	mvn vertx:runMod

TODO: get to know how to run all the verticles
TODO: in the next step we are going to deploy multiply verticles in the one step
	
[COMMAND]

9. Lets review generated verticle. 
	
	By extending AbstractVerticle [change Verticle to AbstractVerticle] we are creating a
	a component managed by a vertx container. This way wa can obtain access to vertx field that lets as communicate 
	with the container. 

	AbstractVerticle beside vertx field enforces us to implement its contract that is a start method that gets
	called when a verticle gets deployd given us a change to initialized all the business logic implemented in our
	application. There is also a stop method but we are going to put this one aside for a moment. 

	To communicate with our containera about the progress of module initialization we get a Future instance passed
	ass an argument to the start method. This one will allow us to inform about initialization success or failure.

	Because vertx is asynchronous/non-blocking vertex is not going to wait until start method completes. This
	effectly convince us the need of Futures in the vertx.   


8. Lets review ists test TDD development let's write a simple test explaining testing concepts. [maybe a good exercise]

	In vertx word to test vertices we are using well known JUnit with vertx custom VertxUnitRunner. This class allows us 
	to inject TestContext to our classes and therefore obtaining access to asynchronous testing potential provided by vertx.
	Lets say we need to test some verticles. First of all we have to create a vertx instance to deploy verticle being tested.
	This is should be done for all the testing method so lets create setUp method annotated with @SetUp JUnit annotation.
	In this method we are going to deploy tested verticle by to do this we need a vertx instance [code snippet]. The created
	object provides a handy method for deploying vertices called deployVerticle. By creating a new instance of PingVerticle and 
	passing it to deploy method we are delegating this task to vertx container. Now we need to know when the deployment is done. This is
	what the second argument of deployVerticle method is going to be used for. Handler is going to be invoked when 
	deployment gets finished. With the help of event object we are able to find out if deployment was finished successfuly or not 
	[code snippet, if statement]. Because the content of the testing method (that deploys verticle) is done asynchronously, it is 
	very likely that the method itself is going to end way before the deployment is finished. But if it was not successful and other
	testing methods relays on the verticle being successfully deployed. Well that what Async object is for. It allows us to make 
	test result dependent of asynchronous task execution. Just check the result of deployment and call async complete when it gets deployed 
	successfully or context.fail when it doesn't. Normally that would be sufficient, however we also want to assure that deplyment is
	done with success because other methods depend on it, this is way we need to call async.awaitSuccess() method.
	
	This procedure can be shorten by using async's assertSuccess directly as deployment's method argument.

	After setting up vertex container we should destroy it in the right manner, to do this just setup the correct tearDown method that is 
	going to be executede after all test in the test class

	Verify test execution by issuing 
	``` mvn integraiton-test ```

	NOTE: remember about correct naming of unit/integraiton tests
	NOTE: remember about calling assertion method that are accessible from TestContext instance and not those from JUnit
		because of vertx asynchronous nature

9. Now, taking a little different approach, we are going to try out some TDD. 

	Create a test for a second http verticle that is going to be based on your test. Here we are create a simple http server so that there 
	test here this functionality. Lets create a new class for testing and a skeleton class for future verticle.
	
	In this case our start method is going to create a HTTP server and attach a socket to it.
	here we should pass a request handle the is going to contain a logic for requests incomming to our
	server. This time it is crucial to use a future passed to the start method because server initializaiton may fail due to
	e.g. socket failure (complete/fail methods).

10. Create a test to third verticle [just as a exercise], maybe a socket client.

11. Create a 3nd verticle that is used to communicate with the generated  one or client to our http server.

12. Run a project again.

13. Modify the project so that it is packaged as a fat jar.

	[fatJar] -  A fat jar is a standalone executable Jar file containing all the dependencies required to run the application.

	and execute our fat jar using:
	java -jar <JAR_LOCATION>

	Append maven shade plugin and package application by using 
	``` mvn clean install ```

	and execute our fat jar using:
	java -jar <JAR_LOCATION>

Running alternatives:
	* fatjar - build using maven-shade-plugin
	* as a simple java project with main method (where Runner is used) from IDE or through 
		``` mvn compile exec:java ```
	* through vertx command line



notes about directories: 
	[0] The Assembly Plugin for Maven is primarily intended to allow users to aggregate the project output along with its dependencies, modules, site 
		documentation, and other files into a single distributable archive.

	[1] If you want override the default langs.properties, cluster.xml or any other config for the Vert.x platform (i.e.
		not for the module!) then you can add them in here and they will be added to the platform classpath when running
		your module using Gradle.

	[2] If you are using fatjars and you want override the default langs.properties, cluster.xml or any other config for the Vert.x platform (i.e.
		not for the module!) then you can add them in here and they will be added to the platform classpath when running
		your module using Gradle.

	[4] All Vert.x modules contain a mod.json descriptor. This a file containing some JSON which describes the module.
		 Amongst other things it usually contains a main field. This tells Vert.x which verticle to run when the module is deployed.
