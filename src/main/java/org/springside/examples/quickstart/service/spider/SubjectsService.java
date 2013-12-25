package org.springside.examples.quickstart.service.spider;

import java.util.List;
import java.util.Map;

import org.apache.shiro.mgt.SubjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.repository.SubjectsDao;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class SubjectsService {

	private SubjectsDao subjectsDao;
	
	
	public List<Subjects> saveAll(List<Subjects> subjs) {
		return (List<Subjects>)subjectsDao.save(subjs);
	}
	

	public Subjects getSubjects(Long id) {
		return subjectsDao.findOne(id);
	}

	public Subjects saveSubjects(Subjects entity) {
		return subjectsDao.save(entity);
	}

	public void deleteSubjects(Long id) {
		subjectsDao.delete(id);
	}

	public List<Subjects> getAllSubjects() {
		return (List<Subjects>) subjectsDao.findAll();
	}

	public Page<Subjects> querySubjectsByParam(
			Map<String, Object> searchParams, String sortType) {
		PageRequest pageRequest = buildPageRequest(1, 50, sortType);
		Specification<Subjects> spec = buildSpecification(searchParams);

		return subjectsDao.findAll(spec, pageRequest);
	}

	public Page<Subjects> getUserSubjects(Map<String, Object> searchParams,
			int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Subjects> spec = buildSpecification(searchParams);

		return subjectsDao.findAll(spec, pageRequest);
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
	private Specification<Subjects> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Subjects> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), Subjects.class);
		return spec;
	}

	@Autowired
	public void setSubjectsDao(SubjectsDao subjectsDao) {
		this.subjectsDao = subjectsDao;
	}
}
