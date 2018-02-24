package gakki.test;

import java.io.IOException;

import gakki.server.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

	private ChannelHandlerContext ctx;

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.err.println("服务端Handler创建...");
		super.handlerAdded(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("channelInactive");
		super.channelInactive(ctx);
	}

	/**
	 * tcp链路建立成功后调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		System.err.println("有客户端连接：" + ctx.channel().remoteAddress().toString());
	}

	/**
	 * 发送消息
	 */
	public boolean sendMsg(Message msg) throws IOException {
		System.err.println("服务器推送消息:" + msg);
		ctx.writeAndFlush(msg);
		return msg.getMsg().equals("q") ? false : true;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
		System.err.println("服务器接收到消息:" + msg);
		Message message = (Message) msg;
		ctx.writeAndFlush(message);
	}

	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.err.println("与客户端断开连接:" + cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}
}