package com.main.glory.Dao.productionPlan;

import com.main.glory.model.productionPlan.ProductionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionPlanDao extends JpaRepository<ProductionPlan,Long> {
}
