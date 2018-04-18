package ru.hutoroff.jagpb.data.mongo;

public class MongoAuthConfiguration {
	private String user;
	private String password;
	private String authBase = "test";

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthBase() {
		return authBase;
	}

	public void setAuthBase(String authBase) {
		this.authBase = authBase;
	}
}
