package me.kafeitu.demo.activiti.web.identify;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import me.kafeitu.demo.activiti.entity.account.ActUser;
import me.kafeitu.demo.activiti.util.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.quickstart.service.bd.ActUserService;

/**
 * 用户相关控制器
 * 
 * @author HenryYan
 */
@Controller
@RequestMapping("/user")
public class UseController {

	private static Logger logger = LoggerFactory.getLogger(UseController.class);

	private ActUserService actUserService;

	/**
	 * 登录系统
	 * 
	 * @param userName
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/logon")
	public String logon(@RequestParam("username") String userName,
			@RequestParam("password") String password, HttpSession session) {
		logger.debug("logon request: {username={}, password={}}", userName,
				password);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("EQ_id", userName);
		param.put("EQ_password", password);

		Page<ActUser> users = actUserService.queryActUserByParam(param);
		if (null == users || users.getContent().size() != 1) {
			return "redirect:/login?error=true";
		}
		ActUser user = users.getContent().get(0);
		UserUtil.saveUserToSession(session, user);
		return "redirect:/main/index";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "/login";
	}

	@Autowired
	public void setActUserService(ActUserService actUserService) {
		this.actUserService = actUserService;
	}

}
