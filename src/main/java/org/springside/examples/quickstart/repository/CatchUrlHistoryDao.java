package org.springside.examples.quickstart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.CatchUrlHistory;

public interface CatchUrlHistoryDao extends
		PagingAndSortingRepository<CatchUrlHistory, Long>,
		JpaSpecificationExecutor<CatchUrlHistory> {

	Page<CatchUrlHistory> findById(Long id, Pageable pageRequest);

	@Modifying
	@Query("delete from CatchUrlHistory where id=?1")
	void deleteById(Long id);
}
