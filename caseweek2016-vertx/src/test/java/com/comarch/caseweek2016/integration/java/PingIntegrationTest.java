package com.comarch.caseweek2016.integration.java;
/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.comarch.caseweek2016.PingVerticle;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

/**
 * Example Java integration test that deploys the module that this project
 * builds.
 *
 * Quite often in integration tests you want to deploy the same module for all
 * tests and you don't want tests to start before the module has been deployed.
 *
 * This test demonstrates how to do that.
 */
@RunWith(VertxUnitRunner.class)
public class PingIntegrationTest {

	private Vertx vertx;

	@Before
	public void setUp(TestContext context) {
		vertx = Vertx.vertx();
		vertx.deployVerticle(new PingVerticle(), context.asyncAssertSuccess());
	}
	
	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void testPing(TestContext context) {
		Async async = context.async();

		vertx.eventBus().send("ping-address", "ping!", new Handler<AsyncResult<Message<String>>>() {

			@Override
			public void handle(AsyncResult<Message<String>> reply) {

				if (reply.succeeded()) {
					Message<String> result = reply.result();
					Assert.assertEquals("pong!", result.body());
				}

				/*
				 * If we get here, the test is complete You must always call
				 * `testComplete()` at the end. Remember that testing is
				 * *asynchronous* so we cannot assume the test is complete by
				 * the time the test method has finished executing like in
				 * standard synchronous tests
				 */
				async.complete();
			}
		});

	}
	

}
