package com.comarch.caseweek;

import java.util.HashMap;
import java.util.Map;

import com.comarch.caseweek.domain.ItemData;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class HttpServerVerticle extends AbstractVerticle {

	private Map<Integer, ItemData> items;

	private static final String HTTP_HOST = "http.host";
	private static final String PORT_PROP = "http.port";

	public static final int PORT_DEFAULT = 8080;
	public static final String HOST_DEFAULT = "localhost";

	public static final String PATH = "/";

	public HttpServerVerticle() {
		items = new HashMap<>();

		for (int i = 0; i < 20; i++) {
			ItemData item = new ItemData("item" + i, "description" + i);
			items.put(item.getId(), item);
		}
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		
		// server configuration
		Integer port = config().getInteger(PORT_PROP, PORT_DEFAULT);
		String host = config().getString(HTTP_HOST, HOST_DEFAULT);
		
		// create router and register request handler
		Router router = Router.router(vertx);
		router.route(HttpMethod.GET, PATH).handler(this::handleRootPath);

		// static resources
		router.route("/assets/*").handler(StaticHandler.create("assets"));
		
		// rest api
		router.route("/api/items*").handler(BodyHandler.create());

		// get all methods
		router.get("/api/items").handler(this::handle);	
		
		// create an item
		router.post("/api/items").handler(this::create);

		// delete an item
		router.delete("/api/items/:id").handler(this::delete);
		
		// create http server
		HttpServer httpServer = vertx.createHttpServer();
		httpServer.requestHandler(router::accept);
		httpServer.listen(port, host, event -> {
			if (event.succeeded()) {
				startFuture.complete();
			} else {
				Throwable ex = event.cause();
				startFuture.fail(ex);
			}
		});
	}
	
	public void handleRootPath(RoutingContext routingContext) {
			HttpServerResponse response = routingContext.response();

			response
				.putHeader("content-type", "text/html")
				.end("HelloWorld from vertx-web");
	}

	public void handle(RoutingContext routingContext) {
		String itemsJSONStr = Json.encodePrettily(this.items.values());

		routingContext.response()
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(itemsJSONStr);
	}
	
	public void create(RoutingContext routingContext) {
		String jsonStr = routingContext.getBodyAsString();
		ItemData item = Json.decodeValue(jsonStr, ItemData.class);
		
		this.items.put(item.getId(), item);
		
		String resultStr = Json.encodePrettily(item);

		routingContext.response()
			.setStatusCode(HttpResponseStatus.CREATED.code())
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(resultStr);
	}
	
	public void delete(RoutingContext routingContext) {
		HttpServerRequest request = routingContext.request();
		HttpServerResponse response = routingContext.response();
		
		String strId = request.getParam("id");
		
		if(strId == null)  {
			response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
		} else {
			int integerId = Integer.parseInt(strId);
			this.items.remove(integerId);
			
			response.setStatusCode(HttpResponseStatus.NO_CONTENT.code());
		}
		
		response.end();
	}

}