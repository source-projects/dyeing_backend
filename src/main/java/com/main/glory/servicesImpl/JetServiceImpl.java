package com.main.glory.servicesImpl;

import com.main.glory.Dao.Jet.JetDataDao;
import com.main.glory.Dao.Jet.JetMastDao;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.jet.request.AddJetData;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetMast;
import com.main.glory.model.jet.request.UpdateJetData;
import com.main.glory.model.jet.responce.GetJetData;
import com.main.glory.model.jet.responce.GetStatus;
import com.main.glory.model.productionPlan.ProductionPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("jetServiceImpl")
public class JetServiceImpl {

    @Autowired
    JetMastDao jetMastDao;

    @Autowired
    JetDataDao jetDataDao;

    @Autowired
    ProductionPlanImpl productionPlanService;

    public void saveJet(JetMast jetMast) {

        jetMastDao.save(jetMast);

    }

    public void saveJetData( List<AddJetData> jetDataList) throws Exception{
        for(AddJetData jetData:jetDataList){

            if(jetData.getControlId()==null)
                throw new Exception("control id can't be null ");

            if(jetData.getProductionId()==null)
                throw new Exception("prudction id can't be null ");

            Optional<JetMast> jetMastExist = jetMastDao.findById(jetData.getControlId());

            ProductionPlan productionPlanExist=productionPlanService.getProductionData(jetData.getProductionId());

            if(jetMastExist.isEmpty())
                throw new Exception("Jet Mast is not found");

            if(productionPlanExist==null)
                throw new Exception("production data not found");

            JetData saveJetData=new JetData(jetData,productionPlanExist);
            jetDataDao.save(saveJetData);

        }


    }

    public List<GetJetData> getJetData(Long id) throws Exception{

        List<GetJetData> getJetDataList=new ArrayList<>();
        List<JetData> jetDataList = jetDataDao.findbyControlId(id);
        if(jetDataList.isEmpty())
            throw new Exception("No data found");

        for(JetData jetData:jetDataList)
        {
            getJetDataList.add(new GetJetData(jetData));
        }
        return getJetDataList;
    }

    public List<GetStatus> getJetStatusList() throws Exception{

        List<GetStatus> getStatusList=new ArrayList<>();

        for(JetStatus jetStatus:JetStatus.values())
        {
            getStatusList.add(new GetStatus(jetStatus));

        }
        if(getStatusList.isEmpty())
            throw new Exception("No status found");
        return getStatusList;


    }

    public void updateJetData(List<UpdateJetData> jetDataToUpdate) throws Exception {

        return;
    }

}
