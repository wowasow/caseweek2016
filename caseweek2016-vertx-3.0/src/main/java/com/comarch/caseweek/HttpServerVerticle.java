package com.comarch.caseweek;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;

public class HttpServerVerticle extends AbstractVerticle {
	
//	private static final Logger LOGGER = Logger.getLogger(HttpServerVerticle.class.getName());

	@Override
	public void start(Future<Void> startFuture) throws Exception {
//		LOGGER.info("testsssssds");
//		LOGGER.info("testsds xxx");
//		LOGGER.info("testsds xxx");
		
		HttpServer httpServer = vertx.createHttpServer();

		httpServer.requestHandler(request -> {
			request.response().end("<p>Hello World test</p>");
		});

		httpServer.listen(8080);

		super.start(startFuture);
	}

}
