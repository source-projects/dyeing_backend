package com.main.glory.Dao.machine;

import com.main.glory.model.machine.BoilerMachineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface BoilerMachineRecordDao extends JpaRepository<BoilerMachineRecord,Long> {
    @Query(value = "select * from boiler_machine_record as s where s.control_id=:controlId AND s.time_of <=:toTime AND s.time_of>=:fromTime AND s.date_to_enter BETWEEN :toDate AND :fromDate ",nativeQuery = true)
    List<BoilerMachineRecord> findByControlIdAndDateTime(@Param("controlId") Long controlId,@Param("toTime") Long toTime,@Param("fromTime") Long fromTime,@Param("toDate") Date toDate,@Param("fromDate") Date fromDate);

    List<BoilerMachineRecord> findByControlId(Long controlId);

    @Query("select s from BoilerMachineRecord s where s.controlId=:controlId AND s.dateToEnter <= :toDate AND s.dateToEnter>=:fromDate AND s.timeOf>=:fromTime AND s.timeOf<=:toTime AND s.controlId IS NOT NULL AND s.dateToEnter IS NOT NULL AND s.timeOf IS NOT NULL")
    List<BoilerMachineRecord> findByControlIdAndDate(Long controlId, Long fromTime, Long toTime, Date toDate, Date fromDate);
}
