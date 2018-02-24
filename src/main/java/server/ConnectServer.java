package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class ConnectServer {

	public static void main(String[] args) {
		new ConnectServer(9090).start();
	}

	BTServerHandler serverHandler = new BTServerHandler();
	private static final String HN_SERVER = "HN_LOGIC";
	private int port;

	public ConnectServer(int port) {
		this.port = port;
	}

	private void start() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		try {
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					BTSerivce.getChannelMap().put(ch.id(), ch);
					ch.closeFuture().addListener(new ChannelFutureListener() {

						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							// Channel 关闭后不再引用该Channel
							BTSerivce.getChannelMap().remove(future.channel().id());
						}
					});
					ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
					ch.pipeline().addLast("msgpack decoder", new MsgPackDecode());
					ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
					ch.pipeline().addLast("msgpack encoder", new MsgPackEncode());
					ch.pipeline().addLast(serverHandler);
				}
			});
			ChannelFuture f = b.bind(port).sync();
			System.out.println("-----服务端启动完成------");
			f.channel().closeFuture().sync();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
