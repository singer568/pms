package org.springside.examples.quickstart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.CatchTaskHistory;

public interface CatchTaskHistoryDao extends
		PagingAndSortingRepository<CatchTaskHistory, Long>,
		JpaSpecificationExecutor<CatchTaskHistory> {

	Page<CatchTaskHistory> findById(Long id, Pageable pageRequest);

	@Modifying
	@Query("delete from CatchTaskHistory where id=?1")
	void deleteById(Long id);
}
