package org.springside.examples.quickstart.web.spider;

import java.util.Map;

import javax.servlet.ServletRequest;
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
import org.springside.examples.quickstart.entity.CatchTaskHistory;
import org.springside.examples.quickstart.entity.Group;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.service.spider.CatchTaskHistoryService;
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
@RequestMapping(value = "/spider/catchTaskHistory")
public class CatchTaskHistoryController {

	private static final String PAGE_SIZE = "50";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("code", "编码");
		sortTypes.put("name", "名称");
	}
	private CatchTaskHistoryService catchTaskHistoryService;

	@Autowired
	public void setCatchTaskHistoryService(
			CatchTaskHistoryService catchTaskHistoryService) {
		this.catchTaskHistoryService = catchTaskHistoryService;
	}
	

	
	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		// Long userId = getCurrentUserId();

		Page<CatchTaskHistory> catchTaskHistorys = catchTaskHistoryService
				.getUserCatchTaskHistory(searchParams, pageNumber, pageSize, sortType);

		model.addAttribute("catchTaskHistorys", catchTaskHistorys);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "spider/catchTaskHistory/catchTaskHistoryList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("catchTaskHistory", new CatchTaskHistory());
		model.addAttribute("action", "create");
		return "spider/catchTaskHistory/catchTaskHistoryForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid CatchTaskHistory newCatchTaskHistory,
			Errors errors, RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", "必填信息不能为空");
			return "redirect:/spider/catchTaskHistory/";
		}
		// User user = new User(getCurrentUserId());
		// newCatchTask.setUser(user);

		catchTaskHistoryService.saveCatchTaskHistory(newCatchTaskHistory);

		redirectAttributes.addFlashAttribute("message", "创建规则成功");
		return "redirect:/spider/catchTaskHistory/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("catchTaskHistory", catchTaskHistoryService
				.getCatchTaskHistory(id));
		model.addAttribute("action", "update");
		return "spider/catchTaskHistory/catchTaskHistoryForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute("catchTaskHistory") CatchTaskHistory catchTaskHistory,
			RedirectAttributes redirectAttributes) {
		catchTaskHistoryService.saveCatchTaskHistory(catchTaskHistory);
		redirectAttributes.addFlashAttribute("message", "更新规则成功");
		return "redirect:/spider/catchTaskHistory/";
	}

	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		return "redirect:/spider/catchUrlHistory/";
	}
	
	
	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		catchTaskHistoryService.deleteCatchTaskHistory(id);
		redirectAttributes.addFlashAttribute("message", "删除规则成功");
		return "redirect:/spider/catchTaskHistory/";
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
			model.addAttribute("catchTaskHistory", catchTaskHistoryService
					.getCatchTaskHistory(id));
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
