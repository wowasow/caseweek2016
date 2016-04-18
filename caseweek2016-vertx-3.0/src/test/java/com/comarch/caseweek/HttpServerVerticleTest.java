package com.comarch.caseweek;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.comarch.caseweek.domain.ItemData;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class HttpServerVerticleTest {

	private Vertx vertx;

	@Before
	public void setUp(TestContext context) {
		vertx = Vertx.vertx();

		// deploy verticle
		vertx.deployVerticle(new HttpServerVerticle(), context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void test(TestContext context) {
		int port = HttpServerVerticle.PORT_DEFAULT;
		String host = HttpServerVerticle.HOST_DEFAULT;
		String path = HttpServerVerticle.PATH;

		HttpClient httpClient = vertx.createHttpClient();
		Async async = context.async();

		httpClient.getNow(port, host, path, event -> {
			int statusCode = event.statusCode();

			if (statusCode != HttpResponseStatus.OK.code())
				context.fail("Status code equals: " + statusCode);

			event.bodyHandler(buffer -> {
				boolean result = buffer.toString().contains("Hello");
				context.assertTrue(result);
				async.complete();
			});

		});
	}
	
	@Test
	public void checkIndexPage(TestContext context) {
		Async async = context.async();
		
		int port = HttpServerVerticle.PORT_DEFAULT;
		String host = HttpServerVerticle.HOST_DEFAULT;
		
		HttpClient httpClient = vertx.createHttpClient();
		httpClient.getNow(port, host, "/assets/index.html", response -> {

			// check response headers
			int statusCode = response.statusCode();
			context.assertTrue(statusCode == HttpResponseStatus.OK.code());

			// check that response has the correct header
			String header = response.getHeader("content-type");
			context.assertEquals(header, "text/html");
			
			// check that the correct HTML page is returned
			response.bodyHandler(body -> {
				boolean result = body.toString().contains("<h1>My items</h1>");

				context.assertTrue(result);
				async.complete();
			});

		});
	}
	
	
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
	

	

}
