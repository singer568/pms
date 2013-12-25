package org.springside.examples.quickstart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springside.examples.quickstart.entity.Subjects;

public interface SubjectsDao extends PagingAndSortingRepository<Subjects, Long>,
		JpaSpecificationExecutor<Subjects> {

	Page<Subjects> findById(Long id, Pageable pageRequest);

	@Modifying
	@Query("delete from Subjects where id=?1")
	void deleteById(Long id);
}
