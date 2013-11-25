package me.kafeitu.demo.activiti.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录页面
 * 
 * @author HenryYan
 */
@Controller
public class KfLoginController {

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
	
}
