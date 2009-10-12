/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package net.liuxuan.OSMC.server;

import java.net.InetSocketAddress;

import net.liuxuan.OSMC.codec.RProtocolCodecFactory;
import net.liuxuan.OSMC.common.PropertiesReader;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts server without Spring.
 * 
 * @author Moses
 */
public class Server {
    final Logger log = LoggerFactory.getLogger(Server.class);
    private static final int SERVER_PORT = PropertiesReader
	    .readInt("server_port");

    // Set this to false to use object serialization instead of custom codec.
    private static final boolean USE_CUSTOM_CODEC = PropertiesReader
	    .readBoolean("USE_CUSTOM_CODEC");

    public static void main(String[] args) throws Throwable {
	NioSocketAcceptor acceptor = new NioSocketAcceptor();

	// Prepare the service configuration.
	if (USE_CUSTOM_CODEC) {
	    acceptor.getFilterChain().addLast("codec",
		    new ProtocolCodecFilter(new RProtocolCodecFactory()));
	} else {
	    acceptor.getFilterChain().addLast(
		    "codec",
		    new ProtocolCodecFilter(
			    new ObjectSerializationCodecFactory()));
	}
	acceptor.getFilterChain().addLast("logger", new LoggingFilter());

	acceptor.setHandler(new ServerSessionHandler());
	acceptor.bind(new InetSocketAddress(SERVER_PORT));

	System.out.println("开始在  " + SERVER_PORT + " 端口监听......");
    }
}
