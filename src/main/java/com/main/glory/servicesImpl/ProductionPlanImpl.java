package com.main.glory.servicesImpl;

import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.productionPlan.ProductionPlanDao;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("productionPlanServiceImpl")
public class ProductionPlanImpl {

    @Autowired
    ProductionPlanDao productionPlanDao;

    @Autowired
    QualityServiceImp qualityServiceImp;

    @Autowired
    PartyServiceImp partyServiceImp;

    @Autowired
    ShadeServiceImpl shadeService;

    public void saveProductionPlan(ProductionPlan productionPlan) throws Exception {

        Optional<Quality> qualityIsExist= qualityServiceImp.getQualityByIDAndPartyId(productionPlan.getQualityEntryId(),productionPlan.getPartyId());

        Optional<ShadeMast> shadeMastExist=shadeService.getShadeMastById(productionPlan.getShadeId());
        if(qualityIsExist.isPresent() && shadeMastExist.isPresent())
        productionPlanDao.save(productionPlan);
        else
            throw new Exception("unable to insert the record");
    }

    public ProductionPlan getProductionData(Long id) throws Exception{
        Optional<ProductionPlan> productionPlan = productionPlanDao.findById(id);

        if(!productionPlan.isPresent())
            throw new Exception("data not found for id:"+id);

        return productionPlan.get();

    }

    public Boolean deleteById(Long id) {
        Optional<ProductionPlan> productionPlan = productionPlanDao.findById(id);
        if (productionPlan.isEmpty())
            return false;

        productionPlanDao.deleteById(id);
        return true;

    }
}
