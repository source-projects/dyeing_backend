package com.main.glory.Dao.productionPlan;

import com.main.glory.model.productionPlan.GetAllProductionWithShadeData;
import com.main.glory.model.productionPlan.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductionPlanDao extends JpaRepository<ProductionPlan,Long> {
    @Query("select new com.main.glory.model.productionPlan.GetAllProductionWithShadeData(p,(select c.colorTone from ShadeMast c where c.id=p.shadeId )) from ProductionPlan p")
    Optional<List<GetAllProductionWithShadeData>> getAllProductionWithColorTone();
}
