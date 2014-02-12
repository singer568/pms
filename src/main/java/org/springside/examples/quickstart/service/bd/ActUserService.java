package org.springside.examples.quickstart.service.bd;

import java.util.List;
import java.util.Map;

import me.kafeitu.demo.activiti.entity.account.ActUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.repository.ActUserDao;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class ActUserService {

	private ActUserDao actUserDao;

	public ActUser getActUser(String id) {
		return actUserDao.findOne(id);
	}

	public void saveActUser(ActUser entity) {
		actUserDao.save(entity);
	}

	public void deleteActUser(String id) {
		actUserDao.delete(id);
	}

	public List<ActUser> getAllActUser() {
		return (List<ActUser>) actUserDao.findAll();
	}

	public Page<ActUser> queryActUserByParam(Map<String, Object> searchParams) {
		PageRequest pageRequest = buildPageRequest(1, 50, "auto");
		Specification<ActUser> spec = buildSpecification(searchParams);

		return actUserDao.findAll(spec, pageRequest);
	}

	public Page<ActUser> getUserActUser(Map<String, Object> searchParams,
			int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<ActUser> spec = buildSpecification(searchParams);

		return actUserDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<ActUser> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// filters
		// .put("user.id",
		// new SearchFilter("user.id", Operator.EQ, userId));
		Specification<ActUser> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), ActUser.class);
		return spec;
	}

	@Autowired
	public void setActUserDao(ActUserDao actUserDao) {
		this.actUserDao = actUserDao;
	}
}
