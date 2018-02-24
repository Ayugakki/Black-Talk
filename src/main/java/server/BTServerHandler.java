package server;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class BTServerHandler extends SimpleChannelInboundHandler<Message> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("connected by :" + ctx.channel().remoteAddress().toString());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("read message complete");
		ctx.flush();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (BTSerivce.getMemberMap().containsKey(ctx.channel())) {
			System.out.println("member:" + BTSerivce.getMemberMap().get(ctx.channel()).getUid() + "login out");
			BTSerivce.getMemberMap().remove(ctx.channel());
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		Message message = new Message();
		// TODO 服务器分发消息
		if (msg.getMsgType() == 0) {
			// 注册
			message = BTSerivce.register(ctx, msg);
		}
		else if (msg.getMsgType() == 1) {
			// 登录
			message = BTSerivce.login(ctx, msg);
		}
		else if (msg.getMsgType() == 2) {
			// 聊天信息
			System.out.println("转发消息:" + msg);
			message = BTSerivce.handlerRequest(ctx, msg);
		}
		else {
			message.setMsg("msg invalid");
		}
		ctx.writeAndFlush(message);
	}
}
