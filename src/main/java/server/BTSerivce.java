package server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

public class BTSerivce {

	private static Map<ChannelId, Channel> channelMap = new ConcurrentHashMap<>();
	private static Map<Long, Member> registeredMember = new ConcurrentHashMap<>();
	private static Map<Channel, Member> memberMap = new ConcurrentHashMap<>();
	private static ObjectMapper mapper = new ObjectMapper();

	public static Map<ChannelId, Channel> getChannelMap() {
		return channelMap;
	}

	public static Map<Long, Member> getRegisteredMember() {
		return registeredMember;
	}

	public static Map<Channel, Member> getMemberMap() {
		return memberMap;
	}

	public static Message handlerRequest(ChannelHandlerContext cxt, Message msg) {
		Message resp = new Message();
		if (!islogin(cxt.channel())) {
			resp.setMsg("not login");
			return resp;
		}
		for (Channel ch : memberMap.keySet()) {
			if (cxt.channel().equals(ch))
				continue;
			ch.writeAndFlush(msg);
		}
		resp.setMsg("ok");
		return resp;
	}

	public static boolean islogin(Channel channel) {
		return memberMap.containsKey(channel);
	}

	public static Message login(ChannelHandlerContext cxt, Message msg) {
		Message resp = new Message();
		try {
			Member member = mapper.readValue(msg.getMsg(), Member.class);
			if (registeredMember.containsKey(member.getUid()) && registeredMember.get(member.getUid()).getPassword().equals(member.getPassword())) {
				resp.setMsg("login success");
				resp.setUid(member.getUid().intValue());
			}
			else {
				resp.setMsg("login failure");
			}
		}
		catch (Exception e) {
			resp.setMsg("msg invalid");
		}
		return resp;
	}

	public static Message register(ChannelHandlerContext ctx, Message msg) {
		Message resp = new Message();
		try {
			Member member = mapper.readValue(msg.getMsg(), Member.class);
			if (registeredMember.containsKey(member.getUid())) {
				resp.setMsg("register failure");
			}
			else {
				registeredMember.put(member.getUid(), member);
				memberMap.put(ctx.channel(), member);
				resp.setMsg("register success");
				resp.setUid(member.getUid().intValue());
			}
		}
		catch (Exception e) {
			resp.setMsg("msg invalid");
		}
		return resp;
	}
}
