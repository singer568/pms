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
import org.springside.examples.quickstart.entity.CatchUrlHistory;
import org.springside.examples.quickstart.repository.CatchUrlHistoryDao;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class CatchUrlHistoryService {

	private CatchUrlHistoryDao catchUrlHistoryDao;

	public CatchUrlHistory getCatchUrlHistory(Long id) {
		return catchUrlHistoryDao.findOne(id);
	}

	public void saveCatchUrlHistory(CatchUrlHistory entity) {
		catchUrlHistoryDao.save(entity);
	}

	public void deleteCatchUrlHistory(Long id) {
		catchUrlHistoryDao.delete(id);
	}

	public List<CatchUrlHistory> getAllCatchUrlHistory() {
		return (List<CatchUrlHistory>) catchUrlHistoryDao.findAll();
	}

	public Page<CatchUrlHistory> getUserCatchUrlHistory(
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<CatchUrlHistory> spec = buildSpecification(searchParams);

		return catchUrlHistoryDao.findAll(spec, pageRequest);
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
	private Specification<CatchUrlHistory> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// filters
		// .put("user.id",
		// new SearchFilter("user.id", Operator.EQ, userId));
		Specification<CatchUrlHistory> spec = DynamicSpecifications
				.bySearchFilter(filters.values(), CatchUrlHistory.class);
		return spec;
	}

	@Autowired
	public void setCatchUrlHistoryDao(CatchUrlHistoryDao catchUrlHistoryDao) {
		this.catchUrlHistoryDao = catchUrlHistoryDao;
	}
}
