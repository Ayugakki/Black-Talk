package gakki.server;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class BTServerHandler extends SimpleChannelInboundHandler<Message> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("有客户端连接：" + ctx.channel().remoteAddress().toString());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel 通道 Read 读取 Complete 完成");
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("转发消息:" + msg);
		BTSerivce.handlerRequest(ctx, msg);
	}
}
