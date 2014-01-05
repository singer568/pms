package org.springside.examples.quickstart.web.spider;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.examples.quickstart.entity.CatchTask;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.service.spider.CatchTaskService;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;

/**
 * Task管理的Controller, 使用Restful风格的Urls:
 * 
 * List page : GET /task/ Create page : GET /task/create Create action : POST
 * /task/create Update page : GET /task/update/{id} Update action : POST
 * /task/update Delete action : GET /task/delete/{id}
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/spider/catchTask")
public class CatchTaskController {

	private static final String PAGE_SIZE = "50";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("code", "编码");
		sortTypes.put("name", "名称");
	}

	private CatchTaskService catchTaskService;

	@Autowired
	public void setCatchTaskService(CatchTaskService catchTaskService) {
		this.catchTaskService = catchTaskService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");

		Page<CatchTask> catchTasks = catchTaskService.getUserCatchTask(
				searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("catchTasks", catchTasks);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "spider/catchTask/catchTaskList";
	}

	@RequestMapping(value = "stopTask", method = RequestMethod.GET)
	public void stopTask(Long id, HttpServletRequest request,
			HttpServletResponse response) {
		catchTaskService.stopTask(id);// 刷新任务信息
		
		String str = "成功停止任务";
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(str);// 用于返回对象参数
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "refreshTask", method = RequestMethod.GET)
	public void refreshTask(Long id, HttpServletRequest request,
			HttpServletResponse response) {

		catchTaskService.refreshTask(id);// 刷新任务信息
		
		String str = "成功刷新任务";
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(str);// 用于返回对象参数
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "refreshAll", method = RequestMethod.GET)
	public void refreshAll(HttpServletRequest request,
			HttpServletResponse response) {

		catchTaskService.refreshTasks();// 刷新任务信息
		
		String str = "成功刷新所有任务";
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(str);// 用于返回对象参数
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("catchTask", new CatchTask());
		model.addAttribute("action", "create");
		return "spider/catchTask/catchTaskForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid CatchTask newCatchTask, Errors errors,
			RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", "必填信息不能为空");
			return "redirect:/spider/catchTask/";
		}

		catchTaskService.saveCatchTask(newCatchTask);

		// catchTaskService.refreshTasks();

		redirectAttributes.addFlashAttribute("message", "创建规则成功");
		return "redirect:/spider/catchTask/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("catchTask", catchTaskService.getCatchTask(id));
		model.addAttribute("action", "update");
		return "spider/catchTask/catchTaskForm";
	}

	@RequestMapping(value = "copy/{id}", method = RequestMethod.GET)
	public String copyForm(@PathVariable("id") Long id, Model model) {
		CatchTask task = catchTaskService.getCatchTask(id);
		task.setId(null);
		model.addAttribute("catchTask", task);
		model.addAttribute("action", "create");
		return "spider/catchTask/catchTaskForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute("catchTask") CatchTask catchTask,
			RedirectAttributes redirectAttributes) {
		catchTaskService.saveCatchTask(catchTask);
		redirectAttributes.addFlashAttribute("message", "更新规则成功");
		return "redirect:/spider/catchTask/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		catchTaskService.deleteCatchTask(id);
		redirectAttributes.addFlashAttribute("message", "删除规则成功");
		return "redirect:/spider/catchTask/";
	}

	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getTask(
			@RequestParam(value = "id", defaultValue = "-1") Long id,
			Model model) {
		if (id != -1) {
			model.addAttribute("catchTask", catchTaskService.getCatchTask(id));
		}
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
