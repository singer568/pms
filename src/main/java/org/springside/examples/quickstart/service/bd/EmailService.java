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
import org.springside.examples.quickstart.entity.Email;
import org.springside.examples.quickstart.repository.EmailDao;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class EmailService {

	private EmailDao emailDao;

	public Email getEmail(Long id) {
		return emailDao.findOne(id);
	}

	public void saveEmail(Email entity) {
		emailDao.save(entity);
	}

	public void deleteEmail(Long id) {
		emailDao.delete(id);
	}

	public List<Email> getAllEmail() {
		return (List<Email>) emailDao.findAll();
	}

	public Page<Email> queryEmailByParam(Map<String, Object> searchParams) {
		PageRequest pageRequest = buildPageRequest(1, 50, "code");
		Specification<Email> spec = buildSpecification(searchParams);

		return emailDao.findAll(spec, pageRequest);
	}

	
	public Page<Email> getUserEmail(Map<String, Object> searchParams,
			int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Email> spec = buildSpecification(searchParams);

		return emailDao.findAll(spec, pageRequest);
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
	private Specification<Email> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// filters
		// .put("user.id",
		// new SearchFilter("user.id", Operator.EQ, userId));
		Specification<Email> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), Email.class);
		return spec;
	}

	@Autowired
	public void setEmailDao(EmailDao emailDao) {
		this.emailDao = emailDao;
	}
}
