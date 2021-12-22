package com.main.glory.Dao.hmi;

import com.main.glory.model.hmi.HMIMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HMIMastDao extends JpaRepository<HMIMast,Long> {


    @Query("select x from HMIMast x where x.productionId =:productionId")
    HMIMast getHMIMastByProductionId(Long productionId);
}
