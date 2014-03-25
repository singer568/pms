package org.springside.examples.quickstart.web.spider;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.examples.quickstart.entity.KeyWords;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.service.bd.KeyWordsService;
import org.springside.examples.quickstart.service.spider.SubjectsService;
import org.springside.modules.web.Servlets;

import com.google.common.collect.Maps;

/**
 * Task管理的Controller, 使用Restful风格的Subjectss:
 * 
 * List page : GET /task/ Create page : GET /task/create Create action : POST
 * /task/create Update page : GET /task/update/{id} Update action : POST
 * /task/update Delete action : GET /task/delete/{id}
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/policy/subjects")
public class SubjectsController {

	private static final String PAGE_SIZE = "50";

	private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
	static {
		sortTypes.put("auto", "自动");
		sortTypes.put("code", "编码");
		sortTypes.put("publishDate", "发布日期");
	}

	private SubjectsService subjectsService;
	private KeyWordsService keywordService;

	@Autowired
	public void setKeywordService(KeyWordsService keywordService) {
		this.keywordService = keywordService;
	}

	@Autowired
	public void setSubjectsService(SubjectsService subjectsService) {
		this.subjectsService = subjectsService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "publishDate") String sortType,
			Model model, ServletRequest request) {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		searchParams.put("EQ_dr", "0");
		// Object obj = searchParams.get("EQ_publishDate");
		// if (null != obj) {
		// searchParams.remove("EQ_publishDate");
		// searchParams.put("EQ_publishDate", convert2Date(obj
		// .toString()));
		// }

		Page<Subjects> subjects = subjectsService.getUserSubjects(searchParams,
				pageNumber, pageSize, "publishDate");

		model.addAttribute("subjects", subjects);
		model.addAttribute("sortType", "publishDate");
		model.addAttribute("sortTypes", sortTypes);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		List<KeyWords> lst = keywordService.getAllKeyWords();

		model.addAttribute("words", lst);

		return "policy/subjects/subjectsList";
	}

	@RequestMapping(value = "sendmail", method = RequestMethod.GET)
	public void sendmail(String ids, String mail, HttpServletRequest request,
			HttpServletResponse response) {

		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out;
		boolean isOk = false;
		String errMsg = "";
		try {
			isOk = subjectsService.sendMail(ids,mail);
		} catch (Exception e) {
			errMsg = "有异常抛出，请查看后台报错日志，异常类：" + e.getClass().getName() + "；异常信息："
					+ e.getMessage();
			e.printStackTrace();
		}

		try {
			out = response.getWriter();
			out.print(isNull(errMsg) ? (isOk == true ? "邮件发送成功" : "邮件发送失败")
					: errMsg);// 用于返回对象参数
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isNull(String msg) {
		if (null == msg || "".trim().equals(msg)) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		Subjects subj = subjectsService.getSubjects(id);
		subj.setDr(1);
		subjectsService.saveSubjects(subj);

		redirectAttributes.addFlashAttribute("message", "删除网址成功");

		return "redirect:/policy/subjects";
	}

	@RequestMapping(value = "query", method = RequestMethod.GET)
	public void query(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_dr", "0");

		Page<Subjects> subjects = subjectsService.getUserSubjects(searchParams,
				1, 10, "publishDate");
		List<Subjects> lst = subjects.getContent();
		JSONArray arr = new JSONArray(lst);
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(arr);// 用于返回对象参数
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			model.addAttribute("url", subjectsService.getSubjects(id));
		}
	}

}
