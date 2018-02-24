package gakki.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

public class BTSerivce {

	private static Map<ChannelId, Channel> channelMap = new ConcurrentHashMap<>();

	public static Map<ChannelId, Channel> getChannelMap() {
		return channelMap;
	}

	public static void handlerRequest(ChannelHandlerContext cxt, Message msg) {
		for (Channel ch : channelMap.values()) {
			if (cxt.channel().equals(ch))
				continue;
			ch.writeAndFlush(msg);
		}
	}
}
