package com.comarch.caseweek2016;
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

import java.util.logging.Level;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * @author Wojciech Wrzalik <wojciech.wrzalik@comarch.com>
 */
public class PingVerticle extends AbstractVerticle {
	
	private final static java.util.logging.Logger LOGGER 
		= java.util.logging.Logger.getLogger(PingVerticle.class.getName());
	

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		vertx.eventBus().consumer("ping-address", new Handler<Message<String>>() {

			@Override
			public void handle(Message<String> message) {
				message.reply("pong!");
				LOGGER.log(Level.INFO, "Sent back pong");
			}
		});

		startFuture.complete();
	}

}
