package server;

public class Member {

	private Long uid;
	private String password;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Member [uid=" + uid + ", password=" + password + "]";
	}
}
