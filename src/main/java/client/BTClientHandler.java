package client;

import java.io.IOException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import server.Message;

public class BTClientHandler extends SimpleChannelInboundHandler<Message> {

	private ChannelHandlerContext ctx;
	private Long uid = -1l;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.err.println("与服务器断开连接:" + cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("成功连接服务器");
		this.ctx = ctx;
		// Message message = new Message();
		// message.setMsg("xixi");
		// sendMsg(message);
	}

	public boolean sendMsg(Message msg) throws IOException {
		msg.setUid(uid.intValue());
		System.out.println("client:" + msg);
		ctx.channel().writeAndFlush(msg);
		return msg.getMsg().equals("q") ? false : true;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		// TODO Auto-generated method stub
		Message m = msg;
		if (msg.getMsg().indexOf("success") != -1) {
			uid = (long) msg.getUid();
		}
		System.out.println(m.getMsg());
	}
}
