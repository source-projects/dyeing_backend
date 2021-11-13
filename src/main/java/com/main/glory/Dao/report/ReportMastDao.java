package com.main.glory.Dao.report;

import com.main.glory.model.report.ReportMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportMastDao extends JpaRepository<ReportMast,Long> {

    @Query("select x from ReportMast x where LOWER(x.name)=LOWER(:name)")
    ReportMast getReportMastByName(String name);

    @Query("select x from ReportMast x where x.id=:id")
    ReportMast getReportMastById(Long id);

    @Query("select x from ReportMast x where LOWER(x.name)=LOWER(:name) AND x.id!=:id")
    ReportMast getReportMastByNameExceptId(String name, Long id);

    @Query("select x from ReportMast x")
    List<ReportMast> getAllReportMast();
}
