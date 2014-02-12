package org.springside.examples.quickstart.web.bd;

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
import org.springside.examples.quickstart.entity.KeyWords;
import org.springside.examples.quickstart.service.account.ShiroDbRealm.ShiroUser;
import org.springside.examples.quickstart.service.bd.KeyWordsService;
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
@RequestMapping(value = "/bd/keywords")
public class KeyWordsController {

	private static final String PAGE_SIZE = "50";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("code", "编码");
		sortTypes.put("name", "名称");
	}

	private KeyWordsService keywordsService;

	@Autowired
	public void setKeyWordsService(KeyWordsService keywordsService) {
		this.keywordsService = keywordsService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
//		Long userId = getCurrentUserId();

		Page<KeyWords> keywordss = keywordsService.getUserKeyWords(searchParams,
				pageNumber, pageSize, sortType);

		model.addAttribute("keywords", keywordss);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "bd/keywords/keywordsList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("keywords", new KeyWords());
		model.addAttribute("action", "create");
		return "bd/keywords/keywordsForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid KeyWords newKeyWords,Errors errors,
			RedirectAttributes redirectAttributes) {
		if(errors.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", "必填信息不能为空");	
			return "redirect:/bd/keywords/";
		}
//		User user = new User(getCurrentUserId());
//		newKeyWords.setUser(user);

		keywordsService.saveKeyWords(newKeyWords);
		
		redirectAttributes.addFlashAttribute("message", "创建关键词成功");
		return "redirect:/bd/keywords/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("keywords", keywordsService.getKeyWords(id));
		model.addAttribute("action", "update");
		return "bd/keywords/keywordsForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("keywords") KeyWords keywords,
			RedirectAttributes redirectAttributes) {
		keywordsService.saveKeyWords(keywords);
		redirectAttributes.addFlashAttribute("message", "更新关键词成功");
		return "redirect:/bd/keywords/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		keywordsService.deleteKeyWords(id);
		redirectAttributes.addFlashAttribute("message", "删除关键词成功");
		return "redirect:/bd/keywords/";
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
			model.addAttribute("keywords", keywordsService.getKeyWords(id));
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
