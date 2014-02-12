package me.kafeitu.demo.activiti.util;

import javax.servlet.http.HttpSession;

import me.kafeitu.demo.activiti.entity.account.ActUser;

/**
 * 用户工具类
 * 
 * @author HenryYan
 */
public class UserUtil {

	public static final String USER = "user";

	/**
	 * 设置用户到session
	 * 
	 * @param session
	 * @param user
	 */
	public static void saveUserToSession(HttpSession session, ActUser user) {
		session.setAttribute(USER, user);
	}

	/**
	 * 从Session获取当前用户信息
	 * 
	 * @param session
	 * @return
	 */
	public static ActUser getUserFromSession(HttpSession session) {
		Object attribute = session.getAttribute(USER);
		return attribute == null ? null : (ActUser) attribute;
	}

}
