package com.pb.client.handler;

import com.pb.client.constant.PBCONSTANT;
import com.server.model.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class clientHandler extends SimpleChannelInboundHandler<Message> {

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idle = (IdleStateEvent) evt;
			switch (idle.state()) {
			case WRITER_IDLE:
				Message ping = new Message();
				ping.setContent(PBCONSTANT.PING);
				ping.setReceiver_uid(PBCONSTANT.SYSTEM);
				ping.setSender_uid(PBCONSTANT.user);
				ping.setTitle(PBCONSTANT.PING);
				ping.setType(PBCONSTANT.PING);
				ping.setTime(System.currentTimeMillis());
				ctx.channel().writeAndFlush(ping);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg)
			throws Exception {
		if (msg.getType().equals(PBCONSTANT.LOGIN_REPLY)) {
			if (msg.getContent().equals(PBCONSTANT.SUCCESS)) {
				System.out.println("Login Success!");
				PBCONSTANT.flag = 1;
			} else {
				PBCONSTANT.flag = -1;
			}
		} else {
			System.out.println("From " + msg.getSender_uid() + " :"
					+ msg.getContent());
		}
	}

}