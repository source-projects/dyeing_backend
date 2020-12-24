package com.main.glory.servicesImpl;

import com.main.glory.Dao.Jet.JetDataDao;
import com.main.glory.Dao.Jet.JetMastDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.response.BatchToPartyAndQuality;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.jet.request.AddJetData;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetMast;
import com.main.glory.model.jet.request.ChangeStatus;
import com.main.glory.model.jet.request.UpdateJetData;
import com.main.glory.model.jet.responce.GetAllJetMast;
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
    StockBatchServiceImpl stockBatchService;

    @Autowired
    ProductionPlanImpl productionPlanService;

    public void saveJet(JetMast jetMast) {

        jetMastDao.save(jetMast);

    }

    public void saveJetData( List<AddJetData> jetDataList) throws Exception{
        Double availableJetCapacity=0.0;

        Double totalBatchCapacity = 0.0;
        //first check the capacity and production detail is available or not
        for(AddJetData jetData:jetDataList){

            if(jetData.getControlId()==null)
                throw new Exception("control id can't be null ");

            if(jetData.getProductionId()==null)
                throw new Exception("prudction id can't be null ");

            Optional<JetMast> jetMastExist = jetMastDao.findById(jetData.getControlId());

            ProductionPlan productionPlanExist=productionPlanService.getProductionData(jetData.getProductionId());

            Optional<JetData> jetDataExistWithProducton=jetDataDao.findByControlIdAndProductionId(jetData.getControlId(),jetData.getProductionId());

            if(jetMastExist.isEmpty())
                throw new Exception("Jet Mast is not found");

            if(productionPlanExist==null)
                throw new Exception("production data not found");

            if(jetDataExistWithProducton.isPresent())
                throw new Exception("Production is already exist with jet");


            availableJetCapacity = jetMastExist.get().getCapacity();

            List<BatchData> batchCapacity = stockBatchService.getBatchById(productionPlanExist.getBatchId(),productionPlanExist.getStockId());
            for(BatchData batchData:batchCapacity)
            {
                if(batchData.getIsProductionPlanned()==true)
                    totalBatchCapacity+=batchData.getWt();
            }

        }
        //check the capacity
        System.out.println(availableJetCapacity+":"+totalBatchCapacity);
        if(totalBatchCapacity>availableJetCapacity)
        {
            throw new Exception("Batch WT is greather than Jet capacity please reduce or remove the Batch");
        }

        //insert the record if batch is fulfilling the requiremet
        for(AddJetData jetData:jetDataList){

            if(jetData.getProductionId()==null)
                throw new Exception("prudction id can't be null ");

            ProductionPlan productionPlanExist=productionPlanService.getProductionData(jetData.getProductionId());

            //change the status
            productionPlanExist.setStatus(true);
            productionPlanService.saveProductionPlan(productionPlanExist);

            //save jet with production
            JetData saveJetData=new JetData(jetData,productionPlanExist);
            jetDataDao.save(saveJetData);

        }


    }

    public List<GetJetData> getJetData(Long id) throws Exception{

        List<GetJetData> getJetDataList=new ArrayList<>();
        List<JetData> jetDataList = jetDataDao.findByControlId(id);
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
        int i=0,n=jetDataToUpdate.size();
        int k = 0;
        while(i<n) {
            k=0;
            Double availableJetCapacity = 0.0;

            Double totalBatchCapacity = 0.0;

            //first check the capacity and production detail is available or not
            for (AddJetData jetData : jetDataToUpdate.get(k).getAddJetDataList()) {

                if (jetData.getControlId() == null)
                    throw new Exception("control id can't be null ");

                if (jetData.getProductionId() == null)
                    throw new Exception("prudction id can't be null ");

                Optional<JetMast> jetMastExist = jetMastDao.findById(jetDataToUpdate.get(k).getControlId());

                ProductionPlan productionPlanExist = productionPlanService.getProductionData(jetData.getProductionId());


                if (jetMastExist.isEmpty())
                    throw new Exception("Jet Mast is not found");

                if (productionPlanExist == null)
                    throw new Exception("production data not found");



                availableJetCapacity = jetMastExist.get().getCapacity();

                List<BatchData> batchCapacity = stockBatchService.getBatchById(productionPlanExist.getBatchId(), productionPlanExist.getStockId());
                for (BatchData batchData : batchCapacity) {
                    if (batchData.getIsProductionPlanned() == true)
                        totalBatchCapacity += batchData.getWt();
                }


                //check the capacity
                System.out.println(availableJetCapacity + ":" + totalBatchCapacity);
                if (totalBatchCapacity > availableJetCapacity) {
                    throw new Exception("Batch WT is greather than Jet capacity please reduce or remove the Batch");
                }
                k++;
            }
            i++;
        }

       k=0;
        //insert the record if batch is fulfilling the requiremet
            for(AddJetData jetData:jetDataToUpdate.get(k).getAddJetDataList()){

                if(jetData.getProductionId()==null)
                    throw new Exception("prudction id can't be null ");

                ProductionPlan productionPlanExist=productionPlanService.getProductionData(jetData.getProductionId());

                JetData saveJetData=new JetData(jetData,productionPlanExist);
               // System.out.println(saveJetData.getControlId()+":"+jetDataToUpdate.get(k).getControlId());
                saveJetData.setControlId(jetDataToUpdate.get(k).getControlId());
                jetDataDao.save(saveJetData);

                k++;
            }

        }






    public List<GetJetData> getJetDataWithInQueueProdution(Long id) throws Exception{

        List<GetJetData> getJetDataList=new ArrayList<>();
        List<JetData> jetDataList = jetDataDao.findByControlId(id);
        if(jetDataList.isEmpty())
            throw new Exception("no data found");

        //fetch the data which are in Queue
        for(JetData jetData:jetDataList)
        {
            if(jetData.getStatus()==JetStatus.inQueue) {
                getJetDataList.add(new GetJetData(jetData));
            }
        }

        if(getJetDataList.isEmpty())
            throw new Exception("no data found");
        return getJetDataList;
    }

    public List<GetAllJetMast> getAllJetData() throws Exception{
        List<JetMast> jetMastList = jetMastDao.findAll();

        List<GetAllJetMast> getAllJetMast=new ArrayList<>();

        if(jetMastList.isEmpty())
            throw new Exception("not data found");

        int i=0;
        for(JetMast jetMast:jetMastList)
        {
            List<GetJetData> jetDataList = new ArrayList<>();
            List<JetData> existJetDataList =  jetDataDao.findByControlId(jetMast.getId());

            for(JetData jetData:existJetDataList)
            {
                if(jetData.getStatus()==JetStatus.inQueue)
                {
                    GetJetData getJetData=new GetJetData(jetData);
                    ProductionPlan productionPlan = productionPlanService.getProductionData(jetData.getProductionId());
                    getJetData.setBatchId(productionPlan.getBatchId());
                    jetDataList.add(getJetData);
                }

            }

            GetAllJetMast allJetMast = new GetAllJetMast();
            allJetMast.setId(jetMast.getId());
            allJetMast.setCapacity(jetMast.getCapacity());
            allJetMast.setName(jetMast.getName());

            if(!jetDataList.isEmpty())
                allJetMast.setJetDataList(jetDataList);

            getAllJetMast.add(allJetMast);
        }

        return getAllJetMast;

    }

    public void updateProductionStatus(ChangeStatus jetDataToUpdate) throws Exception{
        Optional<JetData> jetDataExist= jetDataDao.findByControlIdAndProductionId(jetDataToUpdate.getControlId(),jetDataToUpdate.getProdcutionId());

        if(jetDataExist.isEmpty())
            throw new Exception("No data found");

        if(jetDataExist.get().getStatus() == JetStatus.success)
            throw new Exception("data can't update because it is already done");

        List<String> getStatusList=new ArrayList<>();

        for(JetStatus jetStatus:JetStatus.values())
        {
            getStatusList.add(jetStatus.toString());
        }

        if(!getStatusList.contains(jetDataToUpdate.getStatus()))
            throw new Exception("no status found");

        jetDataExist.get().setStatus(JetStatus.valueOf(jetDataToUpdate.getStatus()));
        System.out.println(jetDataExist.get().getStatus());
        jetDataDao.save(jetDataExist.get());

    }
}
