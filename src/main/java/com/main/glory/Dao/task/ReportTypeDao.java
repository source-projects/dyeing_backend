package com.main.glory.Dao.task;

import com.main.glory.model.task.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReportTypeDao extends JpaRepository<ReportType,Long> {

    @Query("select x from ReportType x where LOWER(x.formName)=LOWER(:formName)")
    ReportType getReportFormExist(String formName);

    @Query("select x from ReportType x")
    List<ReportType> getAllReportType();

    @Query("select x from ReportType x where x.id=:id")
    ReportType getReportTypeById(Long id);

    @Modifying
    @Transactional
    @Query("delete from ReportType r where r.id=:id")
    void deleteReportTypeById(Long id);
}
