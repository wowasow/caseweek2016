package com.comarch.caseweek.it;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;

public class RestIT {

  @BeforeClass
  public static void configureRestAssured() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = Integer.getInteger("http.port", 8080);
  }

  @AfterClass
  public static void unconfigureRestAssured() {
    RestAssured.reset();
  }
  
  @Test
  public void checkThatWeCanRetrieveIndividualProduct() {
    // Get the list of items, ensure it's a success and extract the first id.
    final int id = get("/api/items")
    	.then()
        .assertThat()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getInt("find { it.name=='item1' }.id");
  }
}
