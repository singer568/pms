package org.springside.examples.quickstart.web.spider;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springside.examples.quickstart.entity.Url;
import org.springside.examples.quickstart.service.spider.UrlService;
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
@RequestMapping(value = "/spider/url")
public class UrlController {

	private static final String PAGE_SIZE = "50";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("code", "编码");
		sortTypes.put("name", "姓名");
	}

	private UrlService urlService;

	@Autowired
	public void setUrlService(UrlService urlService) {
		this.urlService = urlService;
	}
	
	@ResponseBody
	@RequestMapping(value = "queryUrl", method = RequestMethod.GET)
	public Object queryUrl(Model model, String url) {
		if (null == url || "".equals(url.trim())) {
			return 0;
		}
		Url urlObj = urlService.getUrl(url);
		if (null == urlObj) {
			return "0";
		} else 
			return 1;
	}
	

	@RequestMapping(value = "copy/{id}", method = RequestMethod.GET)
	public String copyForm(@PathVariable("id") Long id, Model model) {
		Url url = urlService.getUrl(id);
		url.setId(null);
		model.addAttribute("url", url);
		model.addAttribute("action", "create");
		return "spider/url/urlForm";
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

		Page<Url> urls = urlService.getUserUrl(searchParams, pageNumber,
				pageSize, sortType);

		model.addAttribute("urls", urls);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "spider/url/urlList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("url", new Url());
		model.addAttribute("action", "create");
		return "spider/url/urlForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Url newUrl, Errors errors,
			RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", "必填信息不能为空");
			return "redirect:/spider/url/";
		}
		if (newUrl.getLevel() == null || newUrl.getLevel().getId() == null) {
			newUrl.setLevel(null);
		}
		if (newUrl.getGroup() == null || newUrl.getGroup().getId() == null) {
			newUrl.setGroup(null);
		}
		urlService.saveUrl(newUrl);

		redirectAttributes.addFlashAttribute("message", "创建网址成功");
		return "redirect:/spider/url/";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("url", urlService.getUrl(id));
		model.addAttribute("action", "update");
		return "spider/url/urlForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("url") Url url,
			RedirectAttributes redirectAttributes) {
		if (url.getLevel() == null || url.getLevel().getId() == null) {
			url.setLevel(null);
		}
		if (url.getGroup() == null || url.getGroup().getId() == null) {
			url.setGroup(null);
		}
		urlService.saveUrl(url);
		
		redirectAttributes.addFlashAttribute("message", "更新网址成功");
		return "redirect:/spider/url/";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		urlService.deleteUrl(id);
		redirectAttributes.addFlashAttribute("message", "删除网址成功");
		return "redirect:/spider/url/";
	}

	@RequestMapping(value = "validate", method = RequestMethod.GET)
	public void validate(Long id, HttpServletRequest request,
			HttpServletResponse response) {

		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out;
		boolean isOk = false;
		String errMsg = "";
		try {
			isOk = urlService.validate(id);
		} catch (Exception e) {
			Url tmp = urlService.getUrl(id);
			tmp.setValid("0");
			urlService.saveUrl(tmp);
			errMsg = "有异常抛出，请查看后台报错日志，异常类：" + e.getClass().getName() + "；异常信息："
					+ e.getMessage();
			e.printStackTrace();
		}

		try {
			out = response.getWriter();
			out.print(isNull(errMsg) ? (isOk == true ? "合法的URL" : "非法的URL")
					: errMsg);// 用于返回对象参数
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isNull(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

}
