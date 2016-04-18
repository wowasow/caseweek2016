package com.comarch.caseweek;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
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
		int port = HttpServerVerticle.PORT;
		String host = HttpServerVerticle.HOST;
		String path = HttpServerVerticle.PATH;

		HttpClient httpClient = vertx.createHttpClient();
		Async async = context.async();

		httpClient.getNow(port, host, path, event -> {
			int statusCode = event.statusCode();

			if (statusCode != HttpResponseStatus.OK.code())
				context.fail("Status code equals: " + statusCode);

			event.bodyHandler(buffer -> {
				boolean result = buffer.toString().contains("Hello World");
				context.assertTrue(result);
				async.complete();
			});

		});
	}

}
