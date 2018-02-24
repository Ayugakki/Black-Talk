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
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		// TODO 服务器分发消息
		System.out.println("转发消息:" + msg);
		BTSerivce.handlerRequest(ctx, msg);
	}
}
