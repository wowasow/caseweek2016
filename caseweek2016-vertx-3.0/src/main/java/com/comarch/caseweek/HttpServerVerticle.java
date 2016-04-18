package com.comarch.caseweek;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;

public class HttpServerVerticle extends AbstractVerticle {

	public static final int PORT = 8080;
	public static final String HOST = "localhost";
	public static final String PATH = "/";

	@Override
	public void start(Future<Void> startFuture) throws Exception {

		HttpServer httpServer = vertx.createHttpServer();

		httpServer.requestHandler(request -> {
			request.response().end("<p>Hello World test</p>");
		});

		httpServer.listen(PORT, HOST, new Handler<AsyncResult<HttpServer>>() {
			
			@Override
			public void handle(AsyncResult<HttpServer> event) {
				if(event.succeeded())
					startFuture.complete();
				else 
					startFuture.fail(event.cause());
			}
		});

		super.start(startFuture);
	}
}
