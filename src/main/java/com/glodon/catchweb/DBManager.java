package com.glodon.catchweb;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DBManager {

	private Connection conn;
	// Host
	private String host = "";
	// Port
	private String port = "";
	// 数据库名称
	private String database = "";
	// MySQL配置时的用户名
	private String user = "";
	// MySQL配置时的密码
	private String password = "";
	// URL指向要访问的数据库
	private String url = "jdbc:mysql://192.168.8.36:3306/cms-jm";

	public DBManager() {

	}

	public DBManager(String host, String port, String database, String user,
			String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
		this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;

		try {
			// 加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// 连接数据库
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {

			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();

		} catch (SQLException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public Connection getConnection() {
		return conn;
	}

	public void destroy() {
	}

	public void insertArticleRecord(String channelID, String articleTitle,
			String articleURL){

		String selectSQL = "select * from arti_article where CHANNEL_ID='"
				+ channelID + "' and OUTER_URL= '" + articleURL + "'";
		try {
			Statement statement = conn.createStatement();
			// 结果集
			ResultSet rs = statement.executeQuery(selectSQL);
			if (rs.next()) {
				String updateSQL = "UPDATE arti_article SET OUTER_URL='"
						+ articleURL + "' WHERE CHANNEL_ID='" + channelID
						+ "' and TITLE='" + articleTitle + "'";
				//System.out.println(updateSQL);
				//已经存在的不进行更新了
				//statement.executeUpdate(updateSQL);
			} else {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = format.format(new Date());
				String insertSQL = "insert into arti_article(CHANNEL_ID, TITLE, OUTER_URL, WEBSITE_ID, CONFIG_ID, CTTCTG_ID, SORT_DATE,RELEASE_SYS_DATE, RELEASE_DATE)"
						+ "values('"
						+ channelID
						+ "', '"
						+ articleTitle
						+ "', '"
						+ articleURL
						+ "', 3,3,2, '"
						+ currentTime + "', '"
						+ currentTime + "', '"
						+ currentTime + "')";
				//System.out.println(insertSQL);
				statement.executeUpdate(insertSQL);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("操作数据库数据时发生错误!");
		}

	}

}
