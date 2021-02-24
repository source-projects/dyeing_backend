package com.main.glory.Dao.productionPlan;

import com.main.glory.model.StockDataBatchData.response.GetBatchDetailByProduction;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.productionPlan.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductionPlanDao extends JpaRepository<ProductionPlan,Long> {

    @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p,(select c.colorTone from ShadeMast c where c.id=p.shadeId )) from ProductionPlan p where p.status=false")
    Optional<List<GetAllProductionWithShadeData>> getAllProductionWithColorTone();

    @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p,(select c.colorTone from ShadeMast c where c.id=p.shadeId )) from ProductionPlan p ")
    Optional<List<GetAllProductionWithShadeData>> getAllProduction();

    @Query("select p from ProductionPlan p where p.id=:id")
    ProductionPlan getByProductionId(Long id);

    @Query("select p from ProductionPlan p where p.partyId=:partyId AND p.qualityEntryId=:qualityEntryId AND p.status=false")
    List<ProductionPlan> getProductionByPartyAndQuality(Long partyId, Long qualityEntryId);

    @Query("select p from ProductionPlan p where p.batchId=:batchId AND p.stockId=:controlId")
    ProductionPlan getProductionByBatchAndStockId(String batchId, Long controlId);

    @Query("select p from ProductionPlan p where p.id=:productionId AND p.batchId=:batchId")
    ProductionPlan getProductionByBatchAndProduction(String batchId, Long productionId);


    @Query("select p from ProductionPlan p ")
    List<ProductionPlan> getAllProductionWithoutFilter();

    @Query("select p from ProductionPlan p where p.shadeId=:shadeId")
    List<ProductionPlan> getAllProductionByShadeId(Long shadeId);

    @Modifying
    @Transactional
    @Query("update ProductionPlan p set p.shadeId=:shadeId where p.id=:id")
    void updateProductionWithShadeId(Long id, Long shadeId);

    @Query("select p from ProductionPlan p where p.qualityEntryId=:id")
    List<ProductionPlan> getAllProductionByQualityId(Long id);

    @Query("select p from ProductionPlan p where p.partyId=:id")
    List<ProductionPlan> getAllProuctionByPartyId(Long id);

    @Query("select p from ProductionPlan p where p.stockId=:id")
    List<ProductionPlan> getProductionByStockId(Long id);
}
