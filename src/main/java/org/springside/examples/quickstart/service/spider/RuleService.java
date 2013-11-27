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
import org.springside.examples.quickstart.entity.Rule;
import org.springside.examples.quickstart.repository.RuleDao;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class RuleService {

	private RuleDao ruleDao;

	public Rule getRule(Long id) {
		return ruleDao.findOne(id);
	}

	public void saveRule(Rule entity) {
		ruleDao.save(entity);
	}

	public void deleteRule(Long id) {
		ruleDao.delete(id);
	}

	public List<Rule> getAllRule() {
		return (List<Rule>) ruleDao.findAll();
	}

	public Page<Rule> getUserRule(Map<String, Object> searchParams,
			int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Rule> spec = buildSpecification(searchParams);

		return ruleDao.findAll(spec, pageRequest);
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
	private Specification<Rule> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// filters
		// .put("user.id",
		// new SearchFilter("user.id", Operator.EQ, userId));
		Specification<Rule> spec = DynamicSpecifications.bySearchFilter(filters
				.values(), Rule.class);
		return spec;
	}

	@Autowired
	public void setRuleDao(RuleDao ruleDao) {
		this.ruleDao = ruleDao;
	}
}
