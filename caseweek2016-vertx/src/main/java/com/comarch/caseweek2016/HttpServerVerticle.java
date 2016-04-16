package com.comarch.caseweek2016;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;

public class HttpServerVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> future) throws Exception {

		vertx.createHttpServer()
			.requestHandler(HttpServerVerticle::handle)
			.listen(8080, result -> {
				if (result.succeeded())
					future.complete();
				else
					future.failed();
		});

	}

	public static void handle(HttpServerRequest event) {
		event.response().end("Hello World");
	}
}
