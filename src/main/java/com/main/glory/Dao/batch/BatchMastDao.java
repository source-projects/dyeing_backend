package com.main.glory.Dao.batch;

import com.main.glory.model.batch.BatchMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BatchMastDao extends JpaRepository<BatchMast, Long> {

	@Query("Select new BatchMast(b.id, b.qualityId, b.remark, b.createdBy, b.createdDate, b.updatedDate, b.updatedBy, b.userHeadId, b.isProductionPlaned) from BatchMast b")
	public List<BatchMast> findAllByIdWithoutData();
}
