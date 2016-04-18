package com.comarch.caseweek;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;

public class HttpServerVerticle extends AbstractVerticle {

	private static final String HTTP_HOST = "http.host";
	private static final String PORT_PROP = "http.port";

	public static final int PORT_DEFAULT = 8080;
	public static final String HOST_DEFAULT = "localhost";

	public static final String PATH = "/";

	@Override
	public void start(Future<Void> startFuture) throws Exception {

		Integer port = config().getInteger(PORT_PROP, PORT_DEFAULT);
		String host = config().getString(HTTP_HOST, HOST_DEFAULT);

		HttpServer httpServer = vertx.createHttpServer();

		// request handler
		httpServer.requestHandler(request -> {
			request.response().end("<p>Hello World test</p>");
		});

		httpServer.listen(port, host, event -> {
			if (event.succeeded()) {
				startFuture.complete();
			} else {
				Throwable ex = event.cause();
				startFuture.fail(ex);
			}
		});
	}
}
