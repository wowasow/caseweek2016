package com.comarch.caseweek;

import io.vertx.core.Vertx;

/**
 * Hello world!
 */
public class App {

    public static void main( String[] args ) {
    	Vertx vertx = Vertx.vertx();
    	vertx.deployVerticle(HttpServerVerticle.class.getName());
    	vertx.deployVerticle(HttpServerVerticle.class.getName());
    }
}
