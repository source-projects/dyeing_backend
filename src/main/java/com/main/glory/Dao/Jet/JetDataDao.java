package com.main.glory.Dao.Jet;

import com.main.glory.model.jet.JetData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

public interface JetDataDao extends JpaRepository<JetData,Long> {
    @Query("SELECT j from JetData j where j.controlId=:id AND j.status!='success' ORDER BY j.sequence ASC")
    List<JetData> findByControlId(Long id);


    @Query("SELECT j from JetData j where j.controlId=:controlId AND j.productionId=:productionId ")
    Optional<JetData> findByControlIdAndProductionId(Long controlId, Long productionId);

    @Query("SELECT j from JetData j where j.controlId=:id AND j.status='inQueue' ORDER BY j.sequence ASC")
    List<JetData> findByControlIdWithExistingProduction(Long id);

    @Modifying
    @Transactional
    @Query("delete from JetData j where j.productionId=:productionId")
    void deleteByProductionId(Long productionId);

    @Query("select j from JetData j where j.productionId=:id AND j.status='inQueue'")
    JetData findByProductionId(Long id);

    @Query("select j from JetData j where j.productionId=:id")
    JetData getJetDataByProductionId(Long id);

    @Query("select j from JetData j where j.controlId=:jetId AND j.productionId=:productionId")
    JetData jetDataExistWithJetIdAndProductionId(Long jetId, Long productionId);

    @Transactional
    @Modifying
    @Query("delete from JetData j where j.id=:id")
    void deleteJetDataById(Long id);

    @Transactional
    @Modifying
    @Query("update JetData j set j.controlId=:id where j.id=:jetData")
    void updateJetWithId(Long jetData, Long id);
}
