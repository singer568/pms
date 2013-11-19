package com.glodon.catchweb;

public class MySQLObj {
	
	private String Host;
	private String Port;
	private String Database;
	private String User;
	private String Password;
	
	public MySQLObj(){
		
	}

	public String getHost() {
		return Host;
	}

	public void setHost(String host) {
		Host = host;
	}

	public String getPort() {
		return Port;
	}

	public void setPort(String port) {
		Port = port;
	}

	public String getDatabase() {
		return Database;
	}

	public void setDatabase(String database) {
		Database = database;
	}

	public String getUser() {
		return User;
	}

	public void setUser(String user) {
		User = user;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
	
	
}
