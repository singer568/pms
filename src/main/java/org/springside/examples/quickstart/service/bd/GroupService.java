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
import org.springside.examples.quickstart.entity.Group;
import org.springside.examples.quickstart.repository.GroupDao;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class GroupService {

	private GroupDao groupDao;

	public Group getGroup(Long id) {
		return groupDao.findOne(id);
	}

	public void saveGroup(Group entity) {
		groupDao.save(entity);
	}

	public void deleteGroup(Long id) {
		groupDao.delete(id);
	}

	public List<Group> getAllGroup() {
		return (List<Group>) groupDao.findAll();
	}

	public Page<Group> getUserGroup(
			Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Group> spec = buildSpecification(searchParams);

		return groupDao.findAll(spec, pageRequest);
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
		}else if ("name".equals(sortType)) {
			sort = new Sort(Direction.ASC, "name");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Group> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
//		filters
//				.put("user.id",
//						new SearchFilter("user.id", Operator.EQ, userId));
		Specification<Group> spec = DynamicSpecifications.bySearchFilter(filters
				.values(), Group.class);
		return spec;
	}

	@Autowired
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
}
