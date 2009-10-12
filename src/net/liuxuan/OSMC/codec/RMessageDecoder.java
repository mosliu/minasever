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
package net.liuxuan.OSMC.codec;

import net.liuxuan.OSMC.codec.base.AbstractPacketDecoder;
import net.liuxuan.OSMC.common.CommonParameters;
import net.liuxuan.OSMC.message.AbstractPacket;
import net.liuxuan.OSMC.message.Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * A {@link RMessageDecoder} that decodes {@link AddMessage}.
 * 
 * @author The Apache MINA Project (dev@mina.apache.org)
 */
public class RMessageDecoder extends AbstractPacketDecoder {

    public RMessageDecoder() {
	super(CommonParameters.PACKET_HEADER);// 添加包头
    }

    @Override
    protected AbstractPacket decodeBody(IoSession session, IoBuffer in) {
	int length =0;
	if (in.remaining() < CommonParameters.INTEGER_LENGTH) {
	    // 无法读取包长度字节
	    return null;
	} else {
	    in.mark();
	    length = in.getInt();
	    if (in.remaining() < length) {
		in.reset();
		return null;
	    }
	}
	

	Message m = new Message();
	byte[] content = new byte[length];
	in.get(content);
	m.setContent(content);
	return m;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
	    throws Exception {
    }
}
