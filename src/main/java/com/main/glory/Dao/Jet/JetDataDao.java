package com.main.glory.Dao.Jet;

import com.main.glory.model.jet.JetData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JetDataDao extends JpaRepository<JetData,Long> {
    @Query("SELECT j from JetData j where j.controlId=:id")
    List<JetData> findByControlId(Long id);


    @Query("SELECT j from JetData j where j.controlId=:controlId AND j.productionPlan.id=:productionId ")
    Optional<JetData> findByControlIdAndProductionId(Long controlId, Long productionId);
}
