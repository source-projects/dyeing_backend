package com.main.glory.Dao.machine;

import com.main.glory.model.machine.Thermopack;
import com.main.glory.model.machine.response.ThermopackFilterRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


public interface ThermopackDao extends JpaRepository<Thermopack,Long> {
    @Query("select t from Thermopack t where t.controlId=:controlId AND t.dateToEnter <= :toDate AND t.dateToEnter>=:fromDate AND t.timeOf>=:fromTime AND t.timeOf<=:toTime AND t.controlId IS NOT NULL AND t.dateToEnter IS NOT NULL AND t.timeOf IS NOT NULL ORDER BY t.timeOf ASC")
    List<Thermopack> findByControlIdAndTimeThermopack(Long controlId, Long toTime, Long fromTime, Date fromDate, Date toDate);

    @Query("select t from Thermopack t where t.controlId=:controlId")
    List<Thermopack> findByControlId(Long controlId);

    @Query("select t from Thermopack t")
    List<Thermopack> getAllThermopack();

    //get record based on morning and shift
    @Query("select br from Thermopack br where br.controlId=:thermopackId AND br.dateToEnter = :fromDate AND br.timeOf >= :fromTime AND br.timeOf <= :toTime")
    List<Thermopack> findRecordBasedOnFilter(Long thermopackId, Date fromDate, Long fromTime, Long toTime);

    //record for night
    @Query("select br from Thermopack br where br.controlId=:thermopackId AND br.dateToEnter = :fromDate AND br.timeOf >= :fromTime OR br.timeOf <= :toTime")
    List<Thermopack> findRecordBasedOnFilterNight(Long thermopackId, Date fromDate, Long fromTime, Long toTime);

    @Query("select t from Thermopack t where t.controlId=:id")
    List<Thermopack> getAllThermopackRecordByControlId(Long id);

    @Modifying
    @Transactional
    @Query("update Thermopack t set t.controlId=:id1 where t.id=:id")
    void udpateThermopackRecord(Long id, Long id1);
}
