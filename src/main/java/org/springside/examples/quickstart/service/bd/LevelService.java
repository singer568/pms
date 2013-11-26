package org.springside.examples.quickstart.service.bd;

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
import org.springside.examples.quickstart.entity.Level;
import org.springside.examples.quickstart.repository.LevelDao;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class LevelService {

	private LevelDao levelDao;

	public Level getLevel(Long id) {
		return levelDao.findOne(id);
	}

	public void saveLevel(Level entity) {
		levelDao.save(entity);
	}

	public void deleteLevel(Long id) {
		levelDao.delete(id);
	}

	public List<Level> getAllLevel() {
		return (List<Level>) levelDao.findAll();
	}

	public Page<Level> getUserLevel(Map<String, Object> searchParams,
			int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Level> spec = buildSpecification(searchParams);

		return levelDao.findAll(spec, pageRequest);
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
	private Specification<Level> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// filters
		// .put("user.id",
		// new SearchFilter("user.id", Operator.EQ, userId));
		Specification<Level> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), Level.class);
		return spec;
	}

	@Autowired
	public void setLevelDao(LevelDao levelDao) {
		this.levelDao = levelDao;
	}
}
