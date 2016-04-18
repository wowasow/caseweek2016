package com.comarch.caseweek;

import java.io.IOException;
import java.net.ServerSocket;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class App {

    public static void main( String[] args ) throws IOException {
    	Vertx vertx = Vertx.vertx();

    	DeploymentOptions options = createHttpDeploymentOptions();

		vertx.deployVerticle(HttpServerVerticle.class.getName(), options);
    }

	private static DeploymentOptions createHttpDeploymentOptions() throws IOException {

		DeploymentOptions options = new DeploymentOptions();
		JsonObject jsonObj = new JsonObject();
		
		int port = pickRandmoPort();
		
		// properties
		jsonObj.put("http.port", port);
		jsonObj.put("http.host", "localhost");
		
		options.setConfig(jsonObj);
		return options;
	}

	private static int pickRandmoPort() throws IOException {

		ServerSocket socket = new ServerSocket(0);
		int port = socket.getLocalPort();
		socket.close();

		return port;
	}
}
