package com.main.glory.Dao.productionPlan;

import com.main.glory.model.StockDataBatchData.response.GetBatchDetailByProduction;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.productionPlan.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductionPlanDao extends JpaRepository<ProductionPlan,Long> {

    @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p,(select c.colorTone from ShadeMast c where c.id=p.shadeId )) from ProductionPlan p where p.status=false")
    Optional<List<GetAllProductionWithShadeData>> getAllProductionWithColorTone();

    @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p,(select c.colorTone from ShadeMast c where c.id=p.shadeId )) from ProductionPlan p ")
    Optional<List<GetAllProductionWithShadeData>> getAllProduction();

    @Query("select p from ProductionPlan p where p.id=:id")
    Optional<ProductionPlan> getByProductionId(Long id);

    @Query("select p from ProductionPlan p where p.partyId=:partyId AND p.qualityEntryId=:qualityEntryId AND p.status=false")
    List<ProductionPlan> getProductionByPartyAndQuality(Long partyId, Long qualityEntryId);

    @Query("select p from ProductionPlan p where p.batchId=:batchId AND p.stockId=:controlId")
    ProductionPlan getProductionByBatchAndStockId(String batchId, Long controlId);

    @Query("select p from ProductionPlan p where p.id=:productionId AND p.batchId=:batchId")
    ProductionPlan getProductionByBatchAndProduction(String batchId, Long productionId);


}
