package gakki.test;

import java.io.IOException;

import gakki.server.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private ChannelHandlerContext ctx;

	/**
	 * tcp链路简历成功后调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("成功连接服务器");
		this.ctx = ctx;
		Message message = new Message();
		message.setMsg("xixi");
		sendMsg(message);
	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public boolean sendMsg(Message msg) throws IOException {
		System.out.println("client:" + msg);
		ctx.channel().writeAndFlush(msg);
		return msg.getMsg().equals("q") ? false : true;
	}

	/**
	 * 收到消息后调用
	 * 
	 * @throws IOException
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
		Message m = (Message) msg;
		System.out.println(m.getUid() + ":" + m.getMsg());
	}

	/**
	 * 发生异常时调用
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.err.println("与服务器断开连接:" + cause.getMessage());
		ctx.close();
	}
}