package com.main.glory.Dao;


import com.main.glory.model.qty.QuantityRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuantityRangeDao extends JpaRepository<QuantityRange,Long> {

    @Query("select s from QuantityRange s ORDER BY s.value")
    List<QuantityRange> getAllRange();
}
