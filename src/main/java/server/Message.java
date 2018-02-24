package server;

@org.msgpack.annotation.Message
public class Message {

	// 应用ID
	private byte appId;
	// 版本号
	private int version;
	// 用户ID
	private int uid;
	// 消息类型 0:登陆 1：文字消息
	private byte msgType;
	// 接收方
	private int receiveId;
	// 消息内容
	private String msg;

	public Message() {
	}

	/**
	 * 构造方法
	 * 
	 * @param appId 应用通道
	 * @param version 应用版本
	 * @param uid 用户ID
	 * @param msgType 消息类型
	 * @param receiveId 消息接收者
	 * @param msg 消息内容
	 */
	public Message(byte appId, int version, int uid, byte msgType, int receiveId, String msg) {
		this.appId = appId;
		this.version = version;
		this.uid = uid;
		this.msgType = msgType;
		this.receiveId = receiveId;
		this.msg = msg;
	}

	public byte getAppId() {
		return appId;
	}

	public void setAppId(byte appId) {
		this.appId = appId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public byte getMsgType() {
		return msgType;
	}

	public void setMsgType(byte msgType) {
		this.msgType = msgType;
	}

	public int getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
	}

	@Override
	public String toString() {
		return "msgType:" + this.msgType + ",msg:" + this.msg;
	}
}
