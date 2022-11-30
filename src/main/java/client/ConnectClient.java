package client;

import java.io.IOException;
import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import server.Message;
import server.MsgPackDecode;
import server.MsgPackEncode;

public class ConnectClient implements Runnable {

	public static void main(String[] args) throws IOException {
		new ConnectClient("127.0.0.1", 9090).start();
	}

	public void start() throws IOException {
		new Thread(this).start();
		sendMsg();
	}

	private void sendMsg() throws IOException {
		Message message = new Message();
		Scanner scanner = new Scanner(System.in);
		do {
			message.setMsgType(Byte.parseByte(scanner.nextLine()));
			message.setMsg(scanner.nextLine());
		}
		while (BTClientHandler.sendMsg(message));
	}

	private static final String HN_SERVER = "HN_LOGIC_CLIENT";
	private int port;
	private String ip;
	private BTClientHandler BTClientHandler = new BTClientHandler();

	public ConnectClient(String ip, int port) {
		this.port = port;
		this.ip = ip;
	}

	@Override
	public void run() {
		// netty
		EventLoopGroup workGroup = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		try {
			b.group(workGroup);
			b.channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
					ch.pipeline().addLast("msgpack decoder", new MsgPackDecode());
					ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
					ch.pipeline().addLast("msgpack encoder", new MsgPackEncode());
					ch.pipeline().addLast(HN_SERVER, BTClientHandler);
				}
			});
			ChannelFuture f = b.connect(ip, port).sync();
			f.channel().closeFuture().sync();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			workGroup.shutdownGracefully();
		}
	}
}
