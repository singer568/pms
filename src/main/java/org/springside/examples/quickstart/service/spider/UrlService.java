package org.springside.examples.quickstart.service.spider;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.entity.Url;
import org.springside.examples.quickstart.repository.UrlDao;
import org.springside.examples.quickstart.service.spider.util.CatchService;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class UrlService {

	private UrlDao urlDao;

	private CatchService catchService;

	public Url getUrl(Long id) {
		return urlDao.findOne(id);
	}

	public void saveUrl(Url entity) {
		urlDao.save(entity);
	}

	public void deleteUrl(Long id) {
		urlDao.delete(id);
	}
	public boolean validate(Long id) throws Exception {
		Url url = getUrl(id);
		List<Subjects> subjs = catchService.catchPolicy(url);
		if (null != subjs && subjs.size() > 0) {
			return true;
		}
		return false;
	}
	public List<Url> getAllUrl() {
		return (List<Url>) urlDao.findAll();
	}

	public Page<Url> queryUrlByParam(Map<String, Object> searchParams) {
		PageRequest pageRequest = buildPageRequest(1, 50, "code");
		Specification<Url> spec = buildSpecification(searchParams);

		return urlDao.findAll(spec, pageRequest);
	}

	public Page<Url> getUserUrl(Map<String, Object> searchParams,
			int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Url> spec = buildSpecification(searchParams);

		return urlDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("code".equals(sortType)) {
			sort = new Sort(Direction.ASC, "code");
		} else if ("name".equals(sortType)) {
			sort = new Sort(Direction.ASC, "name");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Url> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Url> spec = DynamicSpecifications.bySearchFilter(filters
				.values(), Url.class);
		return spec;
	}

	@Autowired
	public void setCatchService(CatchService catchService) {
		this.catchService = catchService;
	}

	@Autowired
	public void setUrlDao(UrlDao urlDao) {
		this.urlDao = urlDao;
	}
}
