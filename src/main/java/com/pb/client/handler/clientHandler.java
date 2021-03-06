package com.pb.client.handler;

import com.pb.server.session.PBSession;
import com.server.constant.PBCONSTANT;
import com.server.model.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class clientHandler extends SimpleChannelInboundHandler<Message> {
	private MessageHandler messageHandler = new MessageHandler();
	/*@Override
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
	}*/

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		System.out.println("inactive");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg)
			throws Exception {
		switch (msg.getType()) {
			case PBCONSTANT.LOGIN_REPLY_FLAG:
				if (msg.get("st").equals("sc")) {
					System.out.println("Login Success!");
					PBCONSTANT.flag = 1;
				} else {
					PBCONSTANT.flag = -1;
				}
				break;
			case PBCONSTANT.MESSAGE_REPLY_FLAG:
				System.out.println("From " + msg.get("s_uid") + " :"
						+ msg.get("st"));
				break;
			case PBCONSTANT.MESSAGE_FLAG:
				PBSession session = new PBSession();
				session.setSession(ctx.channel());
				messageHandler.process(session, msg);
				break;
			default:
		}
	}

}
