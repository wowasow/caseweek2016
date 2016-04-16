package com.comarch.caseweek2016.integration.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.comarch.caseweek2016.HttpServerVerticle;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class HttpServerVerticleTest {

	private static final String PATH = "/";
	private static final String HOST = "localhost";
	private static final int PORT = 8080;

	private Vertx vertx;

	@Before
	public void setUp(TestContext context) {
		vertx = Vertx.vertx();
		vertx.deployVerticle(new HttpServerVerticle(), context.asyncAssertSuccess());
	}
	
	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void testHttpServerResponse(TestContext context) {
		HttpClient httpClient = vertx.createHttpClient();
		Async async = context.async();

		httpClient.getNow(PORT, HOST, PATH, response -> {
			if(response.statusCode() != 200) 
				context.fail();

			response.bodyHandler(buffer -> {
				context.assertTrue(buffer.toString().contains("Hello World"));
				async.complete();
			});

		});
	}
	
}
