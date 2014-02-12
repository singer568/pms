package org.springside.examples.quickstart.repository;

import me.kafeitu.demo.activiti.entity.account.ActUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ActUserDao extends
		PagingAndSortingRepository<ActUser, String>,
		JpaSpecificationExecutor<ActUser> {

	Page<ActUser> findById(String id, Pageable pageRequest);

	@Modifying
	@Query("delete from ActUser where id=?1")
	void deleteById(String id);
}
