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
import org.springside.examples.quickstart.entity.CatchTaskHistory;
import org.springside.examples.quickstart.repository.CatchTaskHistoryDao;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class CatchTaskHistoryService {

	private CatchTaskHistoryDao catchTaskHistoryDao;

	public CatchTaskHistory getCatchTaskHistory(Long id) {
		return catchTaskHistoryDao.findOne(id);
	}

	public void saveCatchTaskHistory(CatchTaskHistory entity) {
		catchTaskHistoryDao.save(entity);
	}

	public void deleteCatchTaskHistory(Long id) {
		catchTaskHistoryDao.delete(id);
	}

	public List<CatchTaskHistory> getAllCatchTaskHistory() {
		return (List<CatchTaskHistory>) catchTaskHistoryDao.findAll();
	}

	public Page<CatchTaskHistory> getUserCatchTaskHistory(
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<CatchTaskHistory> spec = buildSpecification(searchParams);

		return catchTaskHistoryDao.findAll(spec, pageRequest);
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
	private Specification<CatchTaskHistory> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// filters
		// .put("user.id",
		// new SearchFilter("user.id", Operator.EQ, userId));
		Specification<CatchTaskHistory> spec = DynamicSpecifications
				.bySearchFilter(filters.values(), CatchTaskHistory.class);
		return spec;
	}

	@Autowired
	public void setCatchTaskHistoryDao(CatchTaskHistoryDao catchTaskHistoryDao) {
		this.catchTaskHistoryDao = catchTaskHistoryDao;
	}
}
