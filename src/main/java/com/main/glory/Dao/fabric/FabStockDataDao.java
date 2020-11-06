package com.main.glory.Dao.fabric;

import com.main.glory.model.fabric.FabStockData;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface FabStockDataDao extends JpaRepository<FabStockData, Long> {

    Optional<FabStockData> findByQualityId(Long quality_id);

    @Query(value = "SELECT * FROM fab_stock_data as f WHERE f.quality_id = :quality_id", nativeQuery = true)
    Optional<List<FabStockData>> getByQualityId(@Param("quality_id") Long quality_id);
}
