package org.springside.examples.quickstart.web.bd;

import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.examples.quickstart.entity.Group;
import org.springside.examples.quickstart.entity.Level;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.service.bd.LevelService;
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
@RequestMapping(value = "/bd/level")
public class LevelController {

	private static final String PAGE_SIZE = "50";

	private LevelService levelService;

	@Autowired
	public void setLevelService(LevelService levelService) {
		this.levelService = levelService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "code") String sortType,
			Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		// Long userId = getCurrentUserId();

		Page<Level> levels = levelService.getUserLevel(searchParams,
				pageNumber, pageSize, "code");

		model.addAttribute("levels", levels);
		model.addAttribute("sortType", "code");
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "bd/level/levelList";
	}
	
	@RequestMapping(value = "queryAll", method = RequestMethod.GET)
	@ResponseBody
	public Object queryGroups(Model model) {
		List<Level> levels = levelService.getAllLevel();
		return levels;
	}
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("level", new Level());
		model.addAttribute("action", "create");
		List<Level> levels = levelService.getAllLevel();
		model.addAttribute("levelList", levels);		
		return "bd/level/levelForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Level newLevel, Errors errors,
			RedirectAttributes redirectAttributes) {
		if (newLevel.getParent()== null || newLevel.getParent().getId() ==null) {
			newLevel.setParent(null);
		}
		
		if (errors.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", "必填信息不能为空");
			return "redirect:/bd/level/";
		}

		levelService.saveLevel(newLevel);

		redirectAttributes.addFlashAttribute("message", "创建分级成功");
		return "redirect:/bd/level/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("level", levelService.getLevel(id));
		model.addAttribute("action", "update");
		List<Level> levels = levelService.getAllLevel();
		model.addAttribute("levelList", levels);
		return "bd/level/levelForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("level") Level level,
			RedirectAttributes redirectAttributes) {
		levelService.saveLevel(level);
		redirectAttributes.addFlashAttribute("message", "更新分级成功");
		return "redirect:/bd/level/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		levelService.deleteLevel(id);
		redirectAttributes.addFlashAttribute("message", "删除分级成功");
		return "redirect:/bd/level/";
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
//		if (id != -1) {
//			model.addAttribute("level", levelService.getLevel(id));
//		}
	}

	/**
	 * 取出Shiro中的当前用户Id.
	 */
	private Long getCurrentUserId() {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user.id;
	}
}
