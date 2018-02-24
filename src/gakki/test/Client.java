package gakki.test;

import java.io.IOException;
import java.util.Scanner;

import gakki.server.Message;
import gakki.server.MsgPackDecode;
import gakki.server.MsgPackEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class Client implements Runnable {

	public static int UID = 8888;
	private ClientHandler clientHandler = new ClientHandler();

	public static void main(String[] args) throws IOException {
		new Client().start();
	}

	public void start() throws IOException {
		new Thread(this).start();
		runServerCMD();
	}

	public void sendMsg(Message msg) throws IOException {
		clientHandler.sendMsg(msg);
	}

	/** 启动客户端控制台 */
	private void runServerCMD() throws IOException {
		Message message = new Message();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		do {
			message.setMsg(scanner.nextLine());
		}
		while (clientHandler.sendMsg(message));
	}

	@Override
	public void run() {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
					ch.pipeline().addLast("msgpack decoder", new MsgPackDecode());
					ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
					ch.pipeline().addLast("msgpack encoder", new MsgPackEncode());
					ch.pipeline().addLast(clientHandler);
				}
			});
			ChannelFuture f = b.connect("127.0.0.1", 9090).sync();
			System.out.println("客户端绑定ip");
			f.channel().closeFuture().sync();
			System.out.println("close");
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			workerGroup.shutdownGracefully();
		}
	}
}
