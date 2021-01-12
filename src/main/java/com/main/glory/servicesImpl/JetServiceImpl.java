package com.main.glory.servicesImpl;

import com.main.glory.Dao.Jet.JetDataDao;
import com.main.glory.Dao.Jet.JetMastDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.response.BatchToPartyAndQuality;
import com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.jet.request.*;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetMast;
import com.main.glory.model.jet.responce.GetAllJetMast;
import com.main.glory.model.jet.responce.GetJetData;
import com.main.glory.model.jet.responce.GetStatus;
import com.main.glory.model.productionPlan.ProductionPlan;
import org.hibernate.engine.jdbc.batch.spi.Batch;
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

    @Autowired
    BatchDao batchDao;

    public void saveJet(AddJet jetMast) throws Exception {


        Optional<JetMast> jetExist = jetMastDao.findByName(jetMast.getName());
        if(jetExist.isPresent()) {
            throw new Exception("jet is avaialble with name");
        }
        JetMast newJet=new JetMast(jetMast);
        jetMastDao.save(newJet);

    }

    public void saveJetData( List<AddJetData> jetDataList) throws Exception{
        Double availableJetCapacity=0.0;

        Double availableBatchInJetCapacity=0.0;
        Double newBatchCapacity = 0.0;

        //first check the capacity and production detail is available or not
        for(AddJetData addJetData:jetDataList)
        {
            if(addJetData.getControlId()==null)
                throw new Exception("control id can't be null ");

            if(addJetData.getProductionId()==null)
                throw new Exception("prudction id can't be null ");

            Optional<JetMast> jetMastExist = jetMastDao.findById(addJetData.getControlId());

            ProductionPlan productionPlanExist=productionPlanService.getProductionData(addJetData.getProductionId());

            Optional<JetData> jetDataExistWithProducton=jetDataDao.findByControlIdAndProductionId(addJetData.getControlId(),addJetData.getProductionId());

            if(jetMastExist.isEmpty())
                throw new Exception("Jet Mast is not found");

            if(productionPlanExist==null)
                throw new Exception("production data not found");

            if(jetDataExistWithProducton.isPresent())
                throw new Exception("Production is already exist with jet");


            //jetCapacity
            availableJetCapacity = jetMastExist.get().getCapacity();


            //existing jet capacity with batch
            List<JetData> exstingJetData = jetDataDao.findByControlId(jetMastExist.get().getId());
            for(JetData jetData:exstingJetData)
            {
                ProductionPlan productionPlan = productionPlanService.getProductionData(jetData.getProductionId());
                if (productionPlan!=null)
                {
                    GetBatchWithControlId batchDataQTY = stockBatchService.getBatchWithoutFinishMtrQTYById(productionPlan.getBatchId(),productionPlan.getStockId());
                //    System.out.println(batchDataQTY.getWT());
                    if(batchDataQTY.getWT()!=null)
                    availableBatchInJetCapacity+=batchDataQTY.getWT();

                }
            }



            //new batch capacity
            ProductionPlan productionPlan = productionPlanService.getProductionData(addJetData.getProductionId());
            if (productionPlan!=null)
            {
                GetBatchWithControlId batchDataQTY = stockBatchService.getBatchWithoutFinishMtrQTYById(productionPlan.getBatchId(),productionPlan.getStockId());
         //       System.out.println(batchDataQTY.getWT());
                if(batchDataQTY.getWT()!=null)
                newBatchCapacity+=batchDataQTY.getWT();

            }


            //add the capacity with new batch capacirt
            newBatchCapacity+=availableBatchInJetCapacity;

            //check the capacity is fullfill the requirement
            if(newBatchCapacity > availableJetCapacity)
                throw new Exception("Batch WT is greather than Jet capacity please reduce or remove the Batch");



            //change the status
            productionPlanExist.setStatus(true);
            productionPlanService.saveProductionPlan(productionPlanExist);


            long count=0;


            //get the count of production in existing jet
            List<JetData> existingJetData=jetDataDao.findByControlId(jetDataList.get(0).getControlId());
            if(existingJetData.isEmpty())
            {
                //save the jet data
                JetData saveJetData=new JetData(addJetData,productionPlanExist);
                saveJetData.setSequence(1l);
                jetDataDao.save(saveJetData);
            }
            else
            {
                for(JetData jetData:existingJetData)
                {
                    count++;
                }
                //save jet with production
                count=count+1;
                JetData saveJetData=new JetData(addJetData,productionPlanExist);

                saveJetData.setSequence(count);
                jetDataDao.save(saveJetData);


            }


        }





    }

    public List<GetJetData> getJetData(Long id) throws Exception{

        List<GetJetData> getJetDataList=new ArrayList<>();
        List<JetData> jetDataList = jetDataDao.findByControlIdWithExistingProduction(id);
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

    public void updateJetData(UpdateJetData jetDataToUpdate) throws Exception {

        //change the production sequnce in same jet

        if(jetDataToUpdate.getTo().getJetId().equals(jetDataToUpdate.getFrom().getJetId()))
        {
            //check the production is avaialbe or not
            ProductionPlan productionExist=productionPlanService.getProductionData(jetDataToUpdate.getFrom().getProductionId());

            if(productionExist==null)
                throw new Exception("no production data found");
            productionExist=productionPlanService.getProductionData(jetDataToUpdate.getTo().getProductionId());

            if(productionExist==null)
                throw new Exception("no production data found");

            //update the sequence of production as per the requirement
            //if the batch is able to fit into the new jet then update the data from the jet
            List<JetData> fromJetDataList = jetDataDao.findByControlIdWithExistingProduction(jetDataToUpdate.getFrom().getJetId());
            for (JetData jetData : fromJetDataList) {
                if (jetData.getSequence() > jetDataToUpdate.getFrom().getSequence()) {
                    long number = jetData.getSequence();
                    number--;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);

                }
            }

            jetDataDao.deleteByProductionId(jetDataToUpdate.getFrom().getProductionId());

            //now update the jet data from to jet list
            List<JetData> toJetDataList = jetDataDao.findByControlIdWithExistingProduction(jetDataToUpdate.getTo().getJetId());
            for (JetData jetData : toJetDataList) {
                if (jetData.getSequence() >= jetDataToUpdate.getTo().getSequence()) {
                    long number = jetData.getSequence();
                    number++;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);
                }
            }
            JetData newJetData = new JetData(jetDataToUpdate.getTo());
            jetDataDao.save(newJetData);



        }
        else {
            //change the production sequnce in between two jet


            Double newBatchCapacity = 0.0;
            Double jetExistingCapacity = 0.0;
            Double jetCapacity = 0.0;
            Optional<JetMast> jetMast = jetMastDao.findById(jetDataToUpdate.getTo().getJetId());
            if (jetMast.isEmpty())
                throw new Exception("no jet data found");

            jetCapacity = jetMast.get().getCapacity();

            List<JetData> jetDataList = jetDataDao.findByControlIdWithExistingProduction(jetMast.get().getId());

            //get the existing jet capacity
            if (!jetDataList.isEmpty()) {
                for (JetData jetData : jetDataList) {
                    ProductionPlan productionPlan = productionPlanService.getProductionData(jetData.getProductionId());
                    List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(productionPlan.getStockId(), productionPlan.getBatchId());

                    if (batchDataList.isEmpty())
                        throw new Exception("no batch or production data found");

                    for (BatchData batchData : batchDataList) {
                        if (batchData.getWt() != null)
                            jetExistingCapacity += batchData.getWt();
                    }

                }
            }

            //get the new batch capacity
            ProductionPlan productionPlan = productionPlanService.getProductionData(jetDataToUpdate.getFrom().getProductionId());
            List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(productionPlan.getStockId(), productionPlan.getBatchId());
            for (BatchData batchData : batchDataList) {
                newBatchCapacity += batchData.getWt();
            }


            //check existing batch capacity in jet with new batch capacity
            if ((newBatchCapacity + jetExistingCapacity) >= jetCapacity)
                throw new Exception("Batch WT is greather than Jet capacity please reduce or remove the Batch");


            //find production is with jet or not
            Optional<JetData> jetExistWithProduction = jetDataDao.findByControlIdAndProductionId(jetDataToUpdate.getFrom().getJetId(), jetDataToUpdate.getFrom().getProductionId());
            if (jetExistWithProduction.isEmpty())
                throw new Exception("no production data found with given jet");


            //if the batch is able to fit into the new jet then update the data from the jet
            List<JetData> fromJetDataList = jetDataDao.findByControlIdWithExistingProduction(jetDataToUpdate.getFrom().getJetId());
            for (JetData jetData : fromJetDataList) {
                if (jetData.getSequence() > jetDataToUpdate.getFrom().getSequence()) {
                    long number = jetData.getSequence();
                    number--;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);

                }
            }

            jetDataDao.deleteByProductionId(jetDataToUpdate.getFrom().getProductionId());

            //now update the jet data from to jet list
            List<JetData> toJetDataList = jetDataDao.findByControlIdWithExistingProduction(jetDataToUpdate.getTo().getJetId());
            for (JetData jetData : toJetDataList) {
                if (jetData.getSequence() >= jetDataToUpdate.getTo().getSequence()) {
                    long number = jetData.getSequence();
                    number++;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);
                }
            }
            JetData newJetData = new JetData(jetDataToUpdate.getTo());
            jetDataDao.save(newJetData);
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
        List<JetMast> jetMastList = jetMastDao.getAll();

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
            throw new Exception("data can't update because production is already completed");

        List<String> getStatusList=new ArrayList<>();

        for(JetStatus jetStatus:JetStatus.values())
        {
            getStatusList.add(jetStatus.toString());
        }

        if(!getStatusList.contains(jetDataToUpdate.getStatus()))
            throw new Exception("no status found");

        //if the status is success then update the sequence of respected jet
        if(jetDataToUpdate.getStatus().equals("success"))
        {
            //check the sequence of production is first or not


            if(jetDataExist.get().getSequence()!=1)
                throw new Exception("unable to update the production");

            //System.out.println(jetDataExist.get().getStatus());
            List<JetData> jetDataList = jetDataDao.findByControlId(jetDataToUpdate.getControlId());
            for(JetData jetData:jetDataList)
            {
                if(jetData.getProductionId()==jetDataExist.get().getProductionId())
                {
                    //update the status of production
                    jetDataExist.get().setStatus(JetStatus.success);
                    jetDataDao.save(jetDataExist.get());

                }
                else
                {
                    //update the sequence of production by +1 from the jet
                    long number=jetData.getSequence();
                    number--;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);
                }
            }

        }
        else
        {
            //update the any other status except success update that on
            if(!getStatusList.contains(jetDataToUpdate.getStatus()))
                throw new Exception("Wrong status found");

            jetDataExist.get().setStatus(JetStatus.valueOf(jetDataToUpdate.getStatus()));
            //System.out.println(jetDataExist.get().getStatus());
            jetDataDao.save(jetDataExist.get());
        }


    }

    public Boolean deleteJetDataByProductionId(Long id) throws Exception {
        JetData jetData=jetDataDao.findByProductionId(id);
        if(jetData==null) {
            throw new Exception("production can't be deleted");
        }

        //update the existing production from jet because the production is going to be deleted
        List<JetData> jetDataList=jetDataDao.findByControlIdWithExistingProduction(jetData.getControlId());
        for(JetData jetData1:jetDataList)
        {
            if(jetData1.getSequence()>jetData.getSequence())
            {
                long number=jetData1.getSequence();
                number--;
                jetData1.setSequence(number);
                jetDataDao.save(jetData1);
            }
        }

        //update the status from production table
        ProductionPlan productionPlan=productionPlanService.getProductionData(id);
        productionPlan.setStatus(false);
        productionPlanService.saveProductionPlan(productionPlan);


        jetDataDao.deleteByProductionId(id);
        return true;




    }
}
