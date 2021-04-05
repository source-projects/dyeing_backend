package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.admin.BatchSequneceDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.*;
import com.main.glory.model.StockDataBatchData.response.*;
import com.main.glory.model.admin.BatchSequence;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("stockBatchServiceImpl")
public class StockBatchServiceImpl {

    @Autowired
    DispatchMastImpl dispatchMastService;


    @Autowired
    BatchSequneceDao batchSequneceDao;

    @Autowired
    QualityNameDao qualityNameDao;

    @Autowired
    ShadeServiceImpl shadeService;

    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @Autowired
    JetServiceImpl jetService;

    @Autowired
    ProductionPlanImpl productionPlanService;

    @Autowired
    DyeingSlipServiceImpl dyeingSlipService;
    @Autowired
    StockMastDao stockMastDao;

    @Autowired
    UserDao userDao;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    BatchDao batchDao;


    @Autowired
    QualityServiceImp qualityServiceImp;
    @Autowired
    PartyDao partyDao;


    public List<StockMast> getAllStockBatch(Long qualityId) {

        List<StockMast> stock = stockMastDao.findByQualityId(qualityId);
        System.out.println(stock);
        return stock;

    }

    public Double getWtByControlAndBatchId(Long controlId,String batchId) throws Exception {
        //get total wt of batch who's production plan is done
        Double data = batchDao.getTotalWtByControlIdAndBatchId(controlId,batchId);
        if(data==null)
            throw new Exception("no batch wt found");

        return data;

    }


    @Transactional
    public Long saveStockBatch(AddStockBatch stockMast, String id) throws Exception {

        Long max=0l,batchId=0l;
        Date dt = new Date(System.currentTimeMillis());
        stockMast.setCreatedDate(dt);
        stockMast.setIsProductionPlanned(false);
        Optional<Quality> quality=qualityDao.findById(stockMast.getQualityId());

            if (!quality.isPresent()) {
                throw new Exception("Insert Quality first");
            }
            else
            {

                for(BatchData batchData:stockMast.getBatchData())
                {
                    if(batchData.getBatchId()==null)
                        throw new Exception("batch id can't be null");
                    batchId=Long.parseLong(batchData.getBatchId());
                    if(batchId > max)
                    {
                        max=batchId;
                    }
                }

                //for data entry user
                UserData userData = userDao.getUserById(Long.parseLong(id));
                if(userData.getIsMaster()==false)
                {
                    //fetch the party record to set the usert head
                    Party party = partyDao.findByPartyId(stockMast.getPartyId());
                    stockMast.setUserHeadId(party.getUserHeadId());
                }
                //check the invoice sequence
                BatchSequence batchSequence = batchSequneceDao.getBatchSequence();
                if(batchSequence==null)
                    throw new Exception("add batch sequence first");

                if(max >= batchSequence.getSequence())
                {
                    batchSequneceDao.updateBatchSequence(batchSequence.getId(),max+1);
                }


                //add record
                StockMast x =new StockMast(stockMast);
                stockMastDao.save(x);

                //update the quality wt per 100 as well
                qualityDao.updateQualityWtAndMtrKgById(stockMast.getQualityId(),stockMast.getWtPer100m(),100/stockMast.getWtPer100m());
                return x.getId();





            }

    }



    @Transactional
    public List<GetAllStockWithPartyNameResponse> getAllStockBatch(String getBy, Long id) throws Exception {
        Optional<List<GetAllStockWithPartyNameResponse>> data = null;
        List<GetAllStockWithPartyNameResponse> list = new ArrayList<>();

        Boolean flag = false;

        if(id ==  null){
            data = stockMastDao.getAllStockWithPartyName();
            if(data.isPresent())
            {
                List<GetAllBatchResponse> batchDataList = new ArrayList<>();
                for(GetAllStockWithPartyNameResponse batchData:data.get())
                {
                    batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());

                    String qualityName = qualityNameDao.getQualityNameDetailByQualitytEntryId(batchData.getQualityId());
                    list.add(new GetAllStockWithPartyNameResponse(batchData,batchDataList,qualityName));

                }


            }


        }
        else if(getBy.equals("own")){

            data = stockMastDao.getAllStockWithPartyNameByCreatedBy(id);

            if(data.isPresent())
            {
                List<GetAllBatchResponse> batchDataList = new ArrayList<>();
                for(GetAllStockWithPartyNameResponse batchData:data.get())
                {
                    batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());

                    String qualityName = qualityNameDao.getQualityNameDetailByQualitytEntryId(batchData.getQualityId());
                    list.add(new GetAllStockWithPartyNameResponse(batchData,batchDataList,qualityName));

                }


            }
        }

        else if(getBy.equals("group")){

            UserData userData = userDao.findUserById(id);

            if(userData.getUserHeadId().equals(userData.getId())) {
                //master user
                data = stockMastDao.getAllStockWithPartyNameByUserHeadIdAndCreatedBy(id,id);
            }
            else
            {
                UserData userOperator =userDao.getUserById(id);
                data = stockMastDao.getAllStockWithPartyNameByUserHeadIdAndCreatedBy(userOperator.getId(),userOperator.getUserHeadId());
            }
            if(data.isPresent())
            {
                List<GetAllBatchResponse> batchDataList = new ArrayList<>();
                for(GetAllStockWithPartyNameResponse batchData:data.get())
                {
                    batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());

                    String qualityName = qualityNameDao.getQualityNameDetailByQualitytEntryId(batchData.getQualityId());
                    list.add(new GetAllStockWithPartyNameResponse(batchData,batchDataList,qualityName));

                }


            }


        }

       /* if(data.isEmpty()) throw new Exception("no data found");
        else
        {
            for(GetAllStockWithPartyNameResponse stockMast : data.get())
            {

                List<BatchData> batchDataList = new ArrayList<>();
                for(BatchData b:stockMast.getBatchData())
                {
                    if(!b.getIsProductionPlanned())
                    {
                        planned=false;
                    }

                }
                if(planned)
                {
                    stockMast.setIsProductionPlanned(true);
                }
                else
                    stockMast.setIsProductionPlanned(false);
            }*/
            return list;




    }

    @Transactional
    public Optional<StockMast> getStockBatchById(Long id) throws Exception{
            Optional<StockMast> data = stockMastDao.findById(id);
            if(data.isPresent()){
                return data;
            }
            else
                throw new Exception ("no data found for StockId: "+id);
    }

    @Transactional
    public void updateBatch(AddStockBatch stockMast,String id) throws Exception {
        //first check the batch id is null or not
        try {
            Long batchId = 0l, max = 0l;

            Optional<StockMast> original = stockMastDao.findById(stockMast.getId());
            if (original.isEmpty()) {
                throw new Exception("No such batch present with id:" + stockMast.getId());
            }

            // Validate, if batch is not given to the production planning then throw the exception
            if (original.get().getIsProductionPlanned()) {
                throw new Exception("BatchData is already sent to production, for id:" + stockMast.getId());
            }


            //for delete the batch gr if not coming from FE

            //##Get the data first from the list
            Map<Long, Boolean> batchGr = new HashMap<>();
            List<BatchData> batchData = batchDao.findByControlId(stockMast.getId());
            for (BatchData batch : batchData) {
                batchGr.put(batch.getId(), false);
                //System.out.println(batch.getId());
            }

            //change the as per the data is coming from FE
            for (BatchData batch : stockMast.getBatchData()) {
                if (batch.getBatchId() == null || batch.getBatchId().isEmpty())
                    throw new Exception("batch id can't be null");
                //System.out.println("coming:"+batch.getId());
                if (batchGr.containsKey(batch.getId())) {
                    batchGr.replace(batch.getId(), true);
                }
                batchId = Long.parseLong(batch.getBatchId());
                if (batchId > max) {
                    max = batchId;
                }
            }

            //##Iterate the loop first for check the record that are production plan true or not and then delete the record who flag is false
            for (Map.Entry<Long, Boolean> entry : batchGr.entrySet()) {
                //System.out.println(entry.getKey()+":"+entry.getValue());
                if (entry.getValue() == false) {
                    BatchData batchDataProductionPlan = batchDao.getBatchDataById(entry.getKey());
                    if (batchDataProductionPlan.getIsProductionPlanned())
                        batchGr.replace(entry.getKey(), true);
                    //throw new Exception("remove the production first of batch:"+batchDataProductionPlan.getBatchId());

                }
            }
            //remove the record
            for (Map.Entry<Long, Boolean> entry : batchGr.entrySet()) {
                //System.out.println(entry.getKey()+":"+entry.getValue());
                if (entry.getValue() == false) {
                    batchDao.deleteById(entry.getKey());
                }
            }


            //for data entry user
            UserData userData = userDao.getUserById(Long.parseLong(id));
            if(userData.getIsMaster()==false)
            {
                //fetch the party record to set the usert head
                Party party = partyDao.findByPartyId(stockMast.getPartyId());
                stockMast.setUserHeadId(party.getUserHeadId());
            }
            //update record
            StockMast x = new StockMast(stockMast);
            stockMastDao.save(x);

            //update the quality wt per 100 as well
            qualityDao.updateQualityWtAndMtrKgById(stockMast.getQualityId(), stockMast.getWtPer100m(), 100 / stockMast.getWtPer100m());


            //update the sequence
            BatchSequence batchSequence = batchSequneceDao.getBatchSequence();
            if (max >= batchSequence.getSequence()) {
                batchSequneceDao.updateBatchSequence(batchSequence.getId(), max + 1);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteStockBatch(Long id) throws Exception{
        Optional<StockMast> stockMast = stockMastDao.findById(id);
        if(stockMast.isEmpty()){
            throw new Exception("No such stock batch present with id:"+id);
        }

        if(Objects.equals(stockMast.get().getIsProductionPlanned(),true)){
            throw new Exception("Can't delete the batch, already in production, for id:"+id);
        }
        List<ProductionPlan> productionPlans = productionPlanService.getProductionByStockId(id);
        if(!productionPlans.isEmpty())
            throw new Exception("can't delete the stock , because already sent to production");

        stockMastDao.deleteById(id);
    }


    public List<StockMast> findByQualityId(Long id) {
        return stockMastDao.findByQualityId(id);
    }

    public List<BatchData> getBatchById(String batchId,Long controlId) throws Exception{

        List<BatchData> batchData = batchDao.getBatchByBatchId(batchId);


        if(batchData.isEmpty())
            throw new Exception("Batch is not available for batchId:"+batchId);


        return  batchData;

    }

    public List<GetAllBatch> byQualityAndPartyWithoutProductionPlan(Long qualityId, Long partyId,String id) throws Exception{

        //get the user record first
        Long userId = Long.parseLong(id);


        UserData userData = userDao.getUserById(userId);
        Long userHeadId=null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());

        List<StockMast> stockMast=null;
        //filter the record
        if (permissions.getViewAll())
        {
            userId=null;
            userHeadId=null;
            stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId);
        }
        else if (permissions.getViewGroup()) {
            //check the user is master or not ?
            //admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId);
            } else if (userData.getUserHeadId() > 0) {
                //check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId,userId,userHeadId);

            }
        }
        else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId=null;
            stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId,userId,userHeadId);
        }



        if(stockMast.isEmpty())
        {
            throw new Exception("No batch found");
        }


        List<GetAllBatch> getAllBatchList =new ArrayList<>();
        List<String> batchName =new ArrayList<>();
        List<Boolean> productionPlanned =new ArrayList<>();
        List<Boolean> isBillGenerated =new ArrayList<>();
        List<Long> controlId =new ArrayList<>();

        GetAllBatch getAllBatch;

        for(StockMast stockMast1:stockMast)
        {

            List<BatchData> batch = batchDao.findByControlId(stockMast1.getId());

            for(BatchData batchData : batch)
            {
                if(batchData.getIsProductionPlanned()==false) {
                    //Take another arraylist because it is not working with Object arrayList
                    if (!batchName.contains(batchData.getBatchId())) {
                        batchName.add(batchData.getBatchId());
                        controlId.add(batchData.getControlId());
                        productionPlanned.add(batchData.getIsProductionPlanned());
                        isBillGenerated.add(batchData.getIsBillGenrated());
                    } else if (!controlId.contains(batchData.getControlId())) {
                        batchName.add(batchData.getBatchId());
                        controlId.add(batchData.getControlId());
                        productionPlanned.add(batchData.getIsProductionPlanned());
                        isBillGenerated.add(batchData.getIsBillGenrated());
                    }
                }

            }



        }
        Optional<Party> party=partyDao.findById(partyId);
        Optional<Quality> quality =qualityDao.findById(qualityId);

        //storing all the data of batchName to object
        for(int x=0;x<controlId.size();x++)
        {
            if(quality.get()!=null&&party.get()!=null )
            {
                if(!quality.isPresent() && !party.isPresent())
                    continue;

                getAllBatch=new GetAllBatch(party.get(),quality.get());
                getAllBatch.setBatchId(batchName.get(x));
                getAllBatch.setControlId(controlId.get(x));
                getAllBatch.setProductionPlanned(productionPlanned.get(x));
                getAllBatch.setIsBillGenerated(isBillGenerated.get(x));
                getAllBatchList.add(getAllBatch);

            }

        }


        if(getAllBatchList.isEmpty())
            throw new Exception("May all batches planned or not available ");
        return getAllBatchList;


    }

    public void updateBatchForMerge(List<MergeSplitBatch> batchData1) throws Exception{
        int k=0,i=0,n=batchData1.size();

        while(i<n) {
            k=0;
            for (BatchData batchData : batchData1.get(i).getBatchDataList()) {
                if(batchData.getIsProductionPlanned()==true)
                    throw new Exception("Production is planned already so Batch can't be updated for id:"+batchData.getBatchId());

                batchData.setBatchId(batchData1.get(i).getBatchId());
                batchData.setControlId(batchData1.get(i).getControlId());
                batchDao.save(batchData);
                k++;
            }
            i++;
        }

    }


    public List<GetAllBatch> getBatchWithoutProductionPlanByPartyAndQuality(Long qualityId, Long partyId) throws Exception{

        List<StockMast> stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId);
        if(stockMast==null)
        {
            throw new Exception("No such batch is available for partyId:"+partyId+" and QualityId:"+qualityId);
        }

        List<GetAllBatch> getAllBatchList =new ArrayList<>();
        List<String> batchName =new ArrayList<>();
        List<Boolean> productionPlanned =new ArrayList<>();
        List<Long> controlId =new ArrayList<>();

        GetAllBatch getAllBatch;

        for(StockMast stockMast1:stockMast)
        {

            List<BatchData> batch = batchDao.findByControlId(stockMast1.getId());

            for(BatchData batchData : batch)
            {
                if(batchData.getIsBillGenrated()==true)
                    continue;

                //Take another arraylist because it is not working with Object arrayList
                if(!batchName.contains(batchData.getBatchId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }
                else if(!controlId.contains(batchData.getControlId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }


            }



        }

        //storing all the data of batchName to object
        for(int x=0;x<controlId.size();x++)
        {
            getAllBatch=new GetAllBatch();
            getAllBatch.setBatchId(batchName.get(x));
            getAllBatch.setControlId(controlId.get(x));
            getAllBatch.setProductionPlanned(productionPlanned.get(x));
            getAllBatchList.add(getAllBatch);
        }


        if(getAllBatchList.isEmpty())
            throw new Exception("May all batches planned or not available ");
        return getAllBatchList;




    }

    public List<GetAllBatch> getAllBatchByMaster(Long userHeadId) throws Exception{


        List<StockMast> stockMastList = stockMastDao.findByUserHeadId(userHeadId);
        /*if(stockMast==null)
        {
            throw new Exception("No such batch is available for master:"+userHeadId);
        }

        List<GetAllBatch> getAllBatchList =new ArrayList<>();
        List<String> batchName =new ArrayList<>();
        List<Boolean> productionPlanned =new ArrayList<>();
        List<Long> controlId =new ArrayList<>();

        GetAllBatch getAllBatch;

        for(StockMast stockMast1:stockMast)
        {

            List<BatchData> batch = batchDao.findByControlId(stockMast1.getId());

            for(BatchData batchData : batch)
            {
                if(batchData.getIsBillGenrated()==true)
                    continue;

                //Take another arraylist because it is not working with Object arrayList
                if(!batchName.contains(batchData.getBatchId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }
                else if(!controlId.contains(batchData.getControlId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }

            }



        }

        //storing all the data of batchName to object
        for(int x=0;x<batchName.size();x++)
        {
            getAllBatch=new GetAllBatch();
               getAllBatch.setBatchId(batchName.get(x));
            getAllBatch.setControlId(controlId.get(x));
            getAllBatch.setProductionPlanned(productionPlanned.get(x));

            Optional<StockMast> stockMast1 = stockMastDao.findById(controlId.get(x));
            Optional<Party> party = partyDao.findById(stockMast1.get().getPartyId());
            Optional<Quality> quality = qualityDao.findById(stockMast1.get().getQualityId());

            getAllBatch.setPartyId(party.get().getId());
            getAllBatch.setPartyName(party.get().getPartyName());
            getAllBatch.setQualityEntryId(quality.get().getId());
            getAllBatch.setQualityId(quality.get().getQualityId());
            getAllBatch.setQualityName(quality.get().getQualityName());
            getAllBatch.setQualityType(quality.get().getQualityType());

            getAllBatchList.add(getAllBatch);
        }


        if(getAllBatchList.isEmpty())
            throw new Exception("May all batches planned or not available ");
        return getAllBatchList;
*/
        List<GetAllBatch> list =new ArrayList<>();
        //List<StockMast> stockMastList = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId);
        if(stockMastList.isEmpty())
        {
            throw new Exception("No batch found");
        }

      /*  Party party=partyDao.findByPartyId(partyId);
        Optional<Quality> quality=qualityDao.findById(qualityId);*/

        //get the batch which are remain to be the part of invoice by party and quality

        for(StockMast stockMast:stockMastList)
        {

            List<GetBatchByInvoice> batchAndStockList = batchDao.getBatcheByStockIdWithoutBillGenerated(stockMast.getId());
            for(GetBatchByInvoice g:batchAndStockList)
            {
                //check the batch is done with produciton or not
                ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(g.getBatchId());
                if(productionPlan.getStatus()==false)
                    continue;

                JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
                if(jetData.getStatus()==JetStatus.success)
                {
                    GetAllBatch getAllBatch=new GetAllBatch(g);
                    list.add(getAllBatch);
                }


            }
        }



        //gt the mege batch as well by stock id
        for(StockMast stockMast:stockMastList)
        {
            List<GetBatchByInvoice> batchAndStockList = batchDao.getMergeBatcheByStockIdWithoutBillGenerated(stockMast.getId());
            for(GetBatchByInvoice g:batchAndStockList)
            {
                //check the batch is done with produciton or not
                if (g.getMergeBatchId()==null)
                    continue;

                ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(g.getMergeBatchId());
                if(productionPlan.getStatus()==false)
                    continue;

                JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
                if(jetData.getStatus()==JetStatus.success)
                {
                    GetAllBatch getAllBatch=new GetAllBatch(g);
                    getAllBatch.setBatchId(g.getBatchId()==null?g.getBatchId():g.getMergeBatchId()+"-"+g.getBatchId());
                    list.add(getAllBatch);
                }


            }
        }



        //List<GetAllBatch> getAllBatchList = batchDao.getAllBatchWithoutBillGeneratedByPartyIdAndQualityId(partyId,qualityId);
        if(list.isEmpty())
            throw new Exception("no data found");


        return list;


    }

    public List<BatchToPartyAndQuality> getAllBatchDetail(String id)  throws Exception{

        //get the user record first
        Long userId = Long.parseLong(id);


        UserData userData = userDao.getUserById(userId);
        Long userHeadId=null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());
        List<GetBatchWithControlId> batchData = null;
        List<GetBatchWithControlId> batchDataForMergeBatch = null;//get all batch for based on mrge batch id

        List<BatchToPartyAndQuality> getAllBatchWithPartyAndQualities=new ArrayList<>();

        //filter the record
        if (permissions.getViewAll())
        {
            userId=null;
            userHeadId=null;
            batchData = batchDao.findAllBasedOnControlIdAndBatchId();
            batchDataForMergeBatch = batchDao.findAllMergeBatch();


        }
        else if (permissions.getViewGroup()) {
            //check the user is master or not ?
            //admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                batchData = batchDao.findAllBasedOnControlIdAndBatchId();
                batchDataForMergeBatch = batchDao.findAllMergeBatch();

            } else if (userData.getUserHeadId() > 0) {
                //check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                batchData = batchDao.findAllBasedOnControlIdAndBatchIdByCreatedAndHeadId(userId,userHeadId);
                batchDataForMergeBatch = batchDao.findAllBasedOnControlIdAndBatchIdAndMergeBatchIdByCreatedAndHeadId(userId,userHeadId);


            }
        }
        else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId=null;
            batchData = batchDao.findAllBasedOnControlIdAndBatchIdByCreatedAndHeadId(userId,userHeadId);
            batchDataForMergeBatch = batchDao.findAllBasedOnControlIdAndBatchIdAndMergeBatchIdByCreatedAndHeadId(userId,userHeadId);
        }

        //batchData.addAll(batchDataForMergeBatch);



        for(GetBatchWithControlId batch : batchData)
        {
            Optional<StockMast> stockMast=stockMastDao.findById(batch.getControlId());
            if(stockMast.get().getQualityId()!=null && stockMast.get().getPartyId()!=null)
            {
                Optional<Quality> quality=qualityDao.findById(stockMast.get().getQualityId());

                Optional<Party> party=partyDao.findById(stockMast.get().getPartyId());


                BatchToPartyAndQuality batchToPartyAndQuality=new BatchToPartyAndQuality(quality.get(),party.get(),batch);

                //check that the process and party shade is exist or not
                //if not then set the detail by null
                ProductionPlan productionPlan=productionPlanService.getProductionDataByBatchAndStock(batch.getBatchId(),batch.getControlId());
                if(productionPlan==null) {
                    batchToPartyAndQuality.setPartyShadeNo(null);
                    batchToPartyAndQuality.setProcessName(null);
                }
                else
                {
                    //get the shade and process
                    Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
                    DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());

                    if(dyeingProcessMast==null || shadeMast.isEmpty())
                        continue;


                    batchToPartyAndQuality.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
                    batchToPartyAndQuality.setProcessName(dyeingProcessMast.getProcessName());
                }
                //add the record
                getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);
            }
        }


        //merge batch filter api
        for(GetBatchWithControlId batch : batchDataForMergeBatch)
        {
            //get batches based on batch id and stock id by mergebatchId

            List<GetBatchWithControlId> basedOnBatch = batchDao.getBatcheAndStockIdByMergeBatchId(batch.getMergeBatchId());
            BatchToPartyAndQuality batchToPartyAndQuality=new BatchToPartyAndQuality();
            for(GetBatchWithControlId batchByMergeBatch:basedOnBatch)
            {
                //System.out.println("stockId:"+batchByMergeBatch.getControlId());

                Optional<StockMast> stockMast=stockMastDao.findById(batchByMergeBatch.getControlId());
                if(stockMast.get().getQualityId()!=null && stockMast.get().getPartyId()!=null)
                {

                    Optional<Quality> quality=qualityDao.findById(stockMast.get().getQualityId());

                    Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality.get().getQualityNameId());
                    Optional<Party> party=partyDao.findById(stockMast.get().getPartyId());

                    //check that the process and party shade is exist or not
                    //if not then set the detail by null
                    /*ProductionPlan productionPlan=productionPlanService.getProductionDataByBatchAndStock(batchByMergeBatch.getBatchId(),batchByMergeBatch.getControlId());
                    if(productionPlan==null) {
                        batchToPartyAndQuality.setPartyShadeNo(null);
                        batchToPartyAndQuality.setProcessName(null);
                    }
                    else
                    {
                        //get the shade and process
                        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
                        DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());

                        if(dyeingProcessMast==null || shadeMast.isEmpty())
                            continue;


                        batchToPartyAndQuality.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
                        batchToPartyAndQuality.setProcessName(dyeingProcessMast.getProcessName());
                    }*/

                    batchToPartyAndQuality.setPartyName(batchToPartyAndQuality.getPartyName()==null?party.get().getPartyName():batchToPartyAndQuality.getPartyName()+","+party.get().getPartyName());
                    batchToPartyAndQuality.setQualityId(batchToPartyAndQuality.getQualityId()==null?quality.get().getQualityId():batchToPartyAndQuality.getQualityId()+","+quality.get().getQualityId());
                    batchToPartyAndQuality.setPartyId(batchToPartyAndQuality.getPartyId()==null?party.get().getId().toString():batchToPartyAndQuality.getPartyId()+","+party.get().getId().toString());
                    batchToPartyAndQuality.setQualityEntryId(batchToPartyAndQuality.getQualityEntryId()==null?quality.get().getId().toString():batchToPartyAndQuality.getQualityEntryId()+","+quality.get().getId());
                    batchToPartyAndQuality.setQualityName(batchToPartyAndQuality.getQualityName()==null?qualityName.get().getQualityName():batchToPartyAndQuality.getQualityName()+","+qualityName.get().getQualityName());


                }

                batchToPartyAndQuality.setTotalMtr(batchDao.getTotalMtrByMergeBatchId(batch.getMergeBatchId()));
                batchToPartyAndQuality.setTotalWt(batchDao.getTotalWtByMergeBatchId(batch.getMergeBatchId()));

            }
            batchToPartyAndQuality.setMergeBatchId(batch.getMergeBatchId());
            batchToPartyAndQuality.setBatchId(batch.getMergeBatchId());
            //add the record
            getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);

        }
        if(getAllBatchWithPartyAndQualities.isEmpty())
            throw new Exception("no data found");

        return  getAllBatchWithPartyAndQualities;
    }

    public BatchToPartyAndQuality getPartyAndQualityByBatch(Long controlId, String batchId) throws Exception{
        Optional<StockMast> stockMast = stockMastDao.findById(controlId);

        if(!stockMast.isPresent())
            throw new Exception("Stock is not found for batchId:"+batchId);

        Optional<Party> party=partyDao.findById(stockMast.get().getPartyId());
        if(!party.isPresent())
            throw new Exception("Party not found for batchId:"+batchId);

        Optional<Quality> quality = qualityDao.findById(stockMast.get().getQualityId());

        if(!quality.isPresent())
            throw new Exception("Quality not found for batchId:"+batchId);

        BatchToPartyAndQuality batchToPartyAndQuality=new BatchToPartyAndQuality();
        batchToPartyAndQuality.setPartyId(party.get().getId().toString());
        batchToPartyAndQuality.setPartyName(party.get().getPartyName());
        batchToPartyAndQuality.setQualityEntryId(quality.get().getId().toString());
        batchToPartyAndQuality.setQualityId(quality.get().getQualityId());
        batchToPartyAndQuality.setQualityName(quality.get().getQualityName());
        batchToPartyAndQuality.setBatchId(batchId);
        batchToPartyAndQuality.setControlId(controlId);
        return batchToPartyAndQuality;
    }

    public Boolean deleteStockBatchWithControlAndBatchID(Long controlId, String batchId) throws Exception {

        List<BatchData> batchData = batchDao.findByControlIdAndBatchId(controlId,batchId);
        if(batchData.isEmpty())
            throw new Exception("Batch not found for id:"+batchId);
        for(BatchData batch:batchData)
        {
            batchDao.deleteById(batch.getId());
        }
        return true;

    }


    public void deleteBatchGr(Long id) throws Exception{
        Optional<BatchData> batchData = batchDao.findById(id);
        if(!batchData.isPresent())
            throw new Exception("batch gr not found");

        batchDao.deleteById(batchData.get().getId());

    }

    public void updateBatchSplit(List<MergeSplitBatch> batchData1)throws Exception {

        int k=0,i=0,n=batchData1.size();

        while(i<n) {
            k=0;
            if(batchData1.get(i).getIsSplit())
            {
                List<BatchData> checkBatchIsAvailable = batchDao.findByControlIdAndBatchId(batchData1.get(i).getControlId(),batchData1.get(i).getBatchId());
                if(!checkBatchIsAvailable.isEmpty())
                {
                    throw new Exception("Batch id is already exist");
                }
            }
            for (BatchData batchData : batchData1.get(i).getBatchDataList()) {

                batchData.setBatchId(batchData1.get(i).getBatchId());
                batchData.setControlId(batchData1.get(i).getControlId());
                batchDao.save(batchData);
                k++;
            }
            i++;
        }


    }

    public Boolean IsBatchAvailable(Long controlId, String batchId) {
        List<BatchData> checkBatchIsAvaialble =  batchDao.findByControlIdAndBatchId(controlId,batchId);
        if(checkBatchIsAvaialble.isEmpty())
        {
            return true;
        }

        return false;

    }

    public List<StockMast> getBatchByPartyId(Long partyId) {

        Optional<List<StockMast>> stockMasts = stockMastDao.findByPartyId(partyId);


        return stockMasts.get();
    }

    public GetBatchWithControlId getBatchQTYById(String batchId, Long stockId) {
        GetBatchWithControlId getAllBatchResponse = batchDao.findByBatchIdAndControId(batchId,stockId);

        if(getAllBatchResponse!=null)
            return getAllBatchResponse;

        return null;
    }
    public GetBatchWithControlId getBatchWithoutFinishMtrQTYById(String batchId, Long stockId) {
        GetBatchWithControlId getAllBatchResponse = batchDao.findByBatchIdAndControIdWithoutFinishMtr(batchId,stockId);

        if(getAllBatchResponse.getWT()!=null)
        System.out.println("w:"+getAllBatchResponse.getWT());
        
        if(getAllBatchResponse!=null)
            return getAllBatchResponse;

        return null;
    }

    //get stock data
    public StockMast getStockById(Long stockId) throws Exception {
        Optional<StockMast> stockMast  = stockMastDao.findById(stockId);
        if(stockMast.isEmpty())
            throw new Exception("no data found");
        return stockMast.get();
    }

    //get all batches without production plan
    public List<GetBatchWithControlId> getAllBatchWithoutProductionPlan() throws Exception {

        List<GetBatchWithControlId> batchWithControlIdList=batchDao.findAllBatcheWithoutProductionPlan();
        if(batchWithControlIdList.isEmpty())
            throw new Exception("no batch found without production plan");

        return  batchWithControlIdList;

    }

    //get stock which are without batches
    public List<GetAllStockWithoutBatches> getStockListWithoutBatches() throws Exception {
        List<GetAllStockWithoutBatches> getAllStockWithoutBatchesList =new ArrayList<>();

        List<StockMast> stockMastList=stockMastDao.getAllStock();
        for(StockMast stockMast:stockMastList)
        {
            List<BatchData> batchDataList = batchDao.findByControlId(stockMast.getId());
            if(batchDataList.isEmpty())
            {
                GetAllStockWithoutBatches getAllStockWithoutBatches=new GetAllStockWithoutBatches(stockMast);
                getAllStockWithoutBatchesList.add(getAllStockWithoutBatches);
            }

        }
        if(getAllStockWithoutBatchesList.isEmpty())
            throw new Exception("no stock is available with empty batch");

        return getAllStockWithoutBatchesList;
    }

    public List<StockMast> getStockBasedOnFilter(GetStockBasedOnFilter filter) throws Exception {
        try {
            Date fromDate = null;
            Date toDate = null;
            if(filter.getToDate().isEmpty() && filter.getFromDate().isEmpty() && filter.getStock().getPartyCode().isEmpty() && filter.getStock().getPartyId()==null && filter.getStock().getQualityEntryId()==null && filter.getStock().getBillNo().isEmpty())
            {
                throw new Exception("please enter valid data");
            }
            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                    "yyyy-MM-dd");

            if (filter.getFromDate()!="")
                fromDate = datetimeFormatter1.parse(filter.getFromDate());

            if (filter.getToDate()!="")
                toDate = datetimeFormatter1.parse(filter.getToDate());

            List<StockMast> stockMastList = null;

            if(filter.getStock().getPartyCode()!="")
            {
                Party party = partyDao.findByPartyCode(filter.getStock().getPartyCode());
                if(party==null)
                    throw new Exception("no party found");

                if (filter.getStock().getPartyId()!=null)
                {

                    if(!party.getId().equals(filter.getStock().getPartyId()))
                        throw new Exception("party no found for party code:"+filter.getStock().getPartyCode());

                    stockMastList = stockMastDao.findByQualityIdAndPartyIdAndDateFilter(party.getId(), filter.getStock().getQualityEntryId(), filter.getStock().getBillNo(), toDate, fromDate);
                    if (stockMastList.isEmpty())
                        throw new Exception("no stock data found");
                    return stockMastList;


                }
                else
                {
                    stockMastList = stockMastDao.findByQualityIdAndPartyIdAndDateFilter(party.getId(), filter.getStock().getQualityEntryId(), filter.getStock().getBillNo(), toDate, fromDate);
                    if (stockMastList.isEmpty())
                        throw new Exception("no stock data found");
                    return stockMastList;

                }

            }
            else
            {

                stockMastList = stockMastDao.findByQualityIdAndPartyIdAndDateFilter(filter.getStock().getPartyId(), filter.getStock().getQualityEntryId(), filter.getStock().getBillNo(), toDate, fromDate);
                if (stockMastList.isEmpty())
                    throw new Exception("no stock data found");
                return stockMastList;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<StockMast> getStockByPartyId(Long partyId) throws Exception {
        Optional<List<StockMast>> stockMastList = stockMastDao.findByPartyId(partyId);
        if(stockMastList.isEmpty())
            throw new Exception("no record found");

        return stockMastList.get();

    }

    //batches by quality
    public List<BatchData> getAllBatchByQualityId(Long qualityId) throws Exception {
        List<BatchData> batchDataList=new ArrayList<>();

        Optional<Quality> qualityExist=qualityDao.findById(qualityId);
        if(qualityExist.isEmpty())
            throw new Exception("no quality found for id:"+qualityId);
        List<StockMast> stockMastList=stockMastDao.findByQualityId(qualityId);

        for(StockMast stockMast:stockMastList)
        {
            List<BatchData> batchData=batchDao.findByControlId(stockMast.getId());
            if(!batchData.isEmpty())
            {
                for(BatchData batch:batchData)
                {
                    batchDataList.add(batch);
                }

            }


        }

        if(batchDataList.isEmpty())
            throw new Exception("no batch found for quality:"+qualityId);

        return batchDataList;


    }

    //get complete stock batch detail of batch based on party and quality
    public List<StockMast> getStockBatchListById(Long qualityId, Long partyId) throws Exception {



        List<StockMast> stockMasts=stockMastDao.findByPartyIdAndQualityId(partyId,qualityId);
        if(stockMasts.isEmpty()) {
            throw new Exception("no data found");
        }

        return stockMasts;


    }

    public List<GetAllBatch> getBatchListByPartyWithoutProductionPlan(Long partyId) throws Exception {
        List<GetAllBatch> list=new ArrayList<>();

        Optional<Party> partyExist=partyDao.findById(partyId);
        if(partyExist.isEmpty())
            throw new Exception("no party data found");

        Optional<List<Quality>> qualityListByParty=qualityDao.findByPartyId(partyId);

        if(qualityListByParty.isEmpty())
            throw new Exception("no quality data found for party");

        for(Quality quality:qualityListByParty.get())
        {
            List<StockMast> stockMastList = stockMastDao.findByQualityIdAndPartyId(quality.getId(),partyId);

            //batch list based on stock id
            for(StockMast stockMast:stockMastList)
            {
                List<GetBatchWithControlId> batchWithStockList=batchDao.getBatchAndStockListWithoutProductionPlanByStockId(stockMast.getId());
                for(GetBatchWithControlId getBatchWithControlId:batchWithStockList)
                {
                    GetAllBatch getAllBatch=new GetAllBatch(partyExist.get(),quality);
                    getAllBatch.setProductionPlanned(false);
                    getAllBatch.setIsBillGenerated(false);
                    getAllBatch.setBatchId(getBatchWithControlId.getBatchId());
                    getAllBatch.setControlId(getBatchWithControlId.getControlId());

                    list.add(getAllBatch);
                }
            }

            //by mrg batch id
            //batch list based on stock id with mergebatchId
            for(StockMast stockMast:stockMastList)
            {
                List<GetBatchWithControlId> batchWithStockList=batchDao.getBatchAndStockListWithoutProductionPlanByStockIdAndBasedOnMergeBatchId(stockMast.getId());
                GetAllBatch getAllBatch=new GetAllBatch(partyExist.get(),quality);
                for(GetBatchWithControlId getBatchWithControlId:batchWithStockList)
                {

                    getAllBatch.setProductionPlanned(false);
                    getAllBatch.setIsBillGenerated(false);
                    getAllBatch.setBatchId(getBatchWithControlId.getMergeBatchId());

                    list.add(getAllBatch);
                }
            }


        }


        if(list.isEmpty())
            throw new Exception("no data found for party:"+partyId);
        return list;
    }

    public List<GetAllBatch> getBatchListByQualityWithoutProductionPlan(Long qualityId) throws Exception {
        List<GetAllBatch> list=new ArrayList<>();

        Optional<Quality> qualityExist=qualityDao.findById(qualityId);

        if(qualityExist.isEmpty())
            throw new Exception("no quality found for id:"+qualityId);


        Optional<Party> party=partyDao.findById(qualityExist.get().getPartyId());
        if(party.isEmpty())
            throw new Exception("party not found for quality:"+qualityId);

        List<StockMast> stockMastList = stockMastDao.findByQualityIdAndPartyId(qualityId,qualityExist.get().getPartyId());



        //batch list based on stock id
        for(StockMast stockMast:stockMastList)
        {
            List<GetBatchWithControlId> batchWithStockList=batchDao.getBatchAndStockListWithoutProductionPlanByStockId(stockMast.getId());
            for(GetBatchWithControlId getBatchWithControlId:batchWithStockList)
            {
                GetAllBatch getAllBatch=new GetAllBatch(party.get(),qualityExist.get());
                getAllBatch.setProductionPlanned(false);
                getAllBatch.setIsBillGenerated(false);
                getAllBatch.setBatchId(getBatchWithControlId.getBatchId());
                getAllBatch.setControlId(getBatchWithControlId.getControlId());

                list.add(getAllBatch);
            }


        }
        //by mrg batch id
        //batch list based on stock id with mergebatchId
        for(StockMast stockMast:stockMastList)
        {
            List<GetBatchWithControlId> batchWithStockList=batchDao.getBatchAndStockListWithoutProductionPlanByStockIdAndBasedOnMergeBatchId(stockMast.getId());
            GetAllBatch getAllBatch=new GetAllBatch(party.get(),qualityExist.get());
            for(GetBatchWithControlId getBatchWithControlId:batchWithStockList)
            {

                getAllBatch.setProductionPlanned(false);
                getAllBatch.setIsBillGenerated(false);
                getAllBatch.setBatchId(getBatchWithControlId.getMergeBatchId());

                list.add(getAllBatch);
            }
        }


        if(list.isEmpty()) {
            throw new Exception("no batch found for quality:"+qualityId);
        }

        return list;
    }

    public List<GetAllBatch> byQualityAndPartyWithProductionPlan(Long qualityId, Long partyId) throws Exception {

        List<GetAllBatch> list =new ArrayList<>();
        List<StockMast> stockMastList = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId);
        if(stockMastList.isEmpty())
        {
            throw new Exception("No batch found");
        }

        Party party=partyDao.findByPartyId(partyId);
        Optional<Quality> quality=qualityDao.findById(qualityId);

        //get the batch which are remain to be the part of invoice by party and quality

        for(StockMast stockMast:stockMastList)
        {
            List<GetBatchByInvoice> batchAndStockList = batchDao.getBatcheByStockIdWithoutBillGenerated(stockMast.getId());
            for(GetBatchByInvoice g:batchAndStockList)
            {
                //check the batch is done with produciton or not
                ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(g.getBatchId());
                if(productionPlan.getStatus()==false)
                    continue;

                JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
                if(jetData.getStatus()==JetStatus.success)
                {
                    GetAllBatch getAllBatch=new GetAllBatch(g,party,quality);
                    list.add(getAllBatch);
                }


            }
        }



        //gt the mege batch as well by stock id
        for(StockMast stockMast:stockMastList)
        {
            List<GetBatchByInvoice> batchAndStockList = batchDao.getMergeBatcheByStockIdWithoutBillGenerated(stockMast.getId());
            for(GetBatchByInvoice g:batchAndStockList)
            {
                //check the batch is done with produciton or not
                if (g.getMergeBatchId()==null)
                    continue;

                ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(g.getMergeBatchId());
                if(productionPlan.getStatus()==false)
                    continue;

                JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
                if(jetData.getStatus()==JetStatus.success)
                {
                    GetAllBatch getAllBatch=new GetAllBatch(g,party,quality);
                    getAllBatch.setBatchId(g.getBatchId()==null?g.getBatchId():g.getMergeBatchId()+"-"+g.getBatchId());
                    list.add(getAllBatch);
                }


            }
        }



        //List<GetAllBatch> getAllBatchList = batchDao.getAllBatchWithoutBillGeneratedByPartyIdAndQualityId(partyId,qualityId);
        if(list.isEmpty())
            throw new Exception("no data found");


        return list;


    }

    public List<GetAllBatch> getBatchByPartyAndQuality(Long qualityId, Long partyId) throws Exception {
        List<StockMast> stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId);
        if(stockMast.isEmpty())
        {
            throw new Exception("No batch found");
        }


        List<GetAllBatch> getAllBatchList =new ArrayList<>();
        List<String> batchName =new ArrayList<>();
        List<Boolean> productionPlanned =new ArrayList<>();
        List<Boolean> isBillGenerated =new ArrayList<>();
        List<Long> controlId =new ArrayList<>();

        GetAllBatch getAllBatch;

        for(StockMast stockMast1:stockMast)
        {

            List<BatchData> batch = batchDao.findByControlId(stockMast1.getId());

            for(BatchData batchData : batch)
            {
                if(batchData.getIsBillGenrated()==true)
                    continue;

                //Take another arraylist because it is not working with Object arrayList
                if(!batchName.contains(batchData.getBatchId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                    isBillGenerated.add(batchData.getIsBillGenrated());
                }
                else if(!controlId.contains(batchData.getControlId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                    isBillGenerated.add(batchData.getIsBillGenrated());
                }

            }



        }
        Optional<Party> party=partyDao.findById(partyId);
        Optional<Quality> quality =qualityDao.findById(qualityId);

        //storing all the data of batchName to object
        for(int x=0;x<controlId.size();x++)
        {
            if(quality.get()!=null&&party.get()!=null)
            {
                if(!quality.isPresent() && !party.isPresent())
                    continue;

                getAllBatch=new GetAllBatch(party.get(),quality.get());
                getAllBatch.setBatchId(batchName.get(x));
                getAllBatch.setControlId(controlId.get(x));
                getAllBatch.setProductionPlanned(productionPlanned.get(x));
                getAllBatch.setIsBillGenerated(isBillGenerated.get(x));
                getAllBatchList.add(getAllBatch);

            }

        }


        if(getAllBatchList.isEmpty())
            throw new Exception("May all batches planned or not available ");
        return getAllBatchList;


    }

    public List<BatchData> getBatchWithControlIdAndBatchId(String batchId, Long stockId) {
        List<BatchData> batchDataList = batchDao.findBatchWithBillGenerated(batchId,stockId);
        return batchDataList;
    }

    public StockMast getStockByStockId(Long stockId) {
        StockMast stockMast = stockMastDao.findByStockId(stockId);

        return stockMast;
    }

    public List<GetAllBatch> getAllBatchWithoutFilter() throws Exception {
        List<GetAllBatch> list =new ArrayList<>();
        List<GetBatchWithControlId> data = batchDao.getAllBatchQty();//get all batches without any filter

        for(GetBatchWithControlId batch : data)
        {

            //System.out.println("batchwt:"+batch.getWT());

            StockMast stockMast = stockMastDao.findByStockId(batch.getControlId());
            if(stockMast==null)
                continue;

            GetQualityResponse quality=qualityServiceImp.getQualityByID(stockMast.getQualityId());
            if (quality==null)
                continue;

            Party party = partyDao.findByPartyId(stockMast.getPartyId());
            if(party==null)
                continue;

            GetAllBatch getAllBatch=new GetAllBatch(party,quality,batch);
            list.add(getAllBatch);

        }
        if(list.isEmpty())
            throw new Exception("no data found");
        return list;
    }

    public List<StockMast> getAllStockWithoutPlan() throws Exception {
        List<StockMast> list =new ArrayList<>();
        List<Long> listOfStockId =new ArrayList<>();
        List<GetBatchWithControlId> stockIList = batchDao.getAllBatchQtyWithoutPlan();
        for(GetBatchWithControlId getBatchWithControlId:stockIList)
        {
            if(!listOfStockId.contains(getBatchWithControlId.getControlId()))
            listOfStockId.add(getBatchWithControlId.getControlId());
        }

        for(Long l : listOfStockId)
        {
            StockMast stockMast = stockMastDao.findByStockId(l);
            list.add(stockMast);
        }


        if(list.isEmpty()) {
            throw new Exception("no record found");
        }

        return list;
    }

    //pagnation example
    public Page<StockMast> findPage(int pageno, int size)
    {
        Pageable pageable= PageRequest.of(pageno-1,size);
        return stockMastDao.findAll(pageable);

        //after return

        /*

        List<StokMast> lst =page.getContent();

         */
    }

    //get All batch who's bill is not generated
    public List<GetAllBatchWithProduction> getAllBatchWithoutBillGenerated(String id) throws Exception {
        List<GetAllBatchWithProduction> list=new ArrayList<>();
        List<GetAllBatch> dataList = batchDao.getAllBatchWithoutBillGenerated();



        //filter the batch
        //get the user record first
        Long userId = Long.parseLong(id);


        UserData userData = userDao.getUserById(userId);
        Long userHeadId=null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());

        List<GetBatchWithControlId> batchDataForMergeBatch = null;//get all batch for based on mrge batch id
        //filter the record
        if (permissions.getViewAll())
        {
            userId=null;
            userHeadId=null;
            dataList =  batchDao.getAllBatchWithoutBillGenerated();
            batchDataForMergeBatch=batchDao.getAllMergeBatchWithoutBillGenrated();
            
        }
        else if (permissions.getViewGroup()) {
            //check the user is master or not ?
            //admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                dataList =  batchDao.getAllBatchWithoutBillGenerated();
                batchDataForMergeBatch=batchDao.getAllMergeBatchWithoutBillGenrated();

            } else if (userData.getUserHeadId() > 0) {
                //check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                dataList =  batchDao.getAllBatchWithoutBillGenerated(userId,userHeadId);
                batchDataForMergeBatch= batchDao.getAllMergeBatchWithoutBillGenrated(userId,userHeadId);
            }
        }
        else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId=null;
            dataList =  batchDao.getAllBatchWithoutBillGenerated(userId,userHeadId);
            batchDataForMergeBatch= batchDao.getAllMergeBatchWithoutBillGenrated(userId,userHeadId);
        }






        //filter the data if the batch is done with jet
        for(GetAllBatch getAllBatch:dataList)
        {
            ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(getAllBatch.getBatchId());

            if(productionPlan==null)
                continue;

            if(productionPlan.getStatus()==false)
                continue;
           // System.out.println(productionPlan.getId());
            JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
            if(jetData==null)
                continue;
            //System.out.println(jetData.getStatus());
            if(jetData.getStatus()== JetStatus.success)
            {

                list.add(new GetAllBatchWithProduction(getAllBatch,productionPlan.getId()));
            }


        }

        //filter the data if the batch is done with jet
        for(GetBatchWithControlId getAllBatch:batchDataForMergeBatch)
        {
            ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(getAllBatch.getMergeBatchId());

            if(productionPlan==null)
                continue;

            if(productionPlan.getStatus()==false)
                continue;
            // System.out.println(productionPlan.getId());
            JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
            if(jetData==null)
                continue;
            //System.out.println(jetData.getStatus());
            if(jetData.getStatus()== JetStatus.success)
            {
                List<GetBatchWithControlId> listOfBatch = batchDao.getBatchesByMergeBatchId(getAllBatch.getMergeBatchId());

                for(GetBatchWithControlId batch:listOfBatch)
                {
                    GetAllBatchWithProduction batchDetail = new GetAllBatchWithProduction(batch,productionPlan.getId());
                    List<BatchData> batchDataList = batchDao.getBatchByMergeBatchIdAndBatchIdForFinishMtrSave(batch.getBatchId(),getAllBatch.getMergeBatchId());
                    if(batchDataList.isEmpty())
                        continue;
                    batchDetail.setBatchId(getAllBatch.getMergeBatchId()+"-"+batchDetail.getBatchId());
                    batchDetail.setProductionPlanned(true);
                    list.add(batchDetail);
                }

            }


        }

        if(list.isEmpty())
            throw new Exception("no batch found");

        return list;

    }

    public WTByStockAndBatch getWtByStockAndBatchId(Long stockId, String batchId) throws Exception {
        WTByStockAndBatch data = batchDao.getWtByStockAndBatchId(batchId,stockId);
        if(data==null)
            throw new Exception("no data found");
        return data;
    }

    public List<StockMast> getAllStockByPartyId(Long id) {
        List<StockMast> list = stockMastDao.getAllStockByPartyId(id);
        return list;
    }

    public List<StockMast> getStockByQualityEntryId(Long id) {
        List<StockMast> list = stockMastDao.getAllStockByQualityId(id);
        return list;
    }

    public Double getMtrByControlAndBatchId(Long stockId, String batchId) {
        return batchDao.getTotalMtrByControlIdAndBatchId(stockId,batchId);
    }

    public List<BatchDetail> getBatchDetailForReport(Long partyId, Long qualityId) {
        List<BatchDetail> batchDetailList = new ArrayList<>();

        List<StockMast> stockMastList = stockMastDao.getAllStockByPartyIdAndQualityId(partyId,qualityId);

        Quality quality = qualityDao.getqualityById(qualityId);
        Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality.getQualityNameId());
        if(stockMastList.isEmpty()) {
            return null;
        }
        for(StockMast stockMast:stockMastList)
        {
            List<BatchDetail> batchDetails = batchDao.getBatchDetailByStockId(stockMast.getId());
            if(batchDetails.isEmpty())
                continue;
            for(BatchDetail batchDetail:batchDetails)
            {
                if(batchDetail.getIsProductionPlanned()==true)
                {
                    if(batchDetail.getMergeBatchId()==null) {
                        ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(batchDetail.getBatchId());

                        if (productionPlan == null)
                            continue;
                        ShadeMast shadeMast = shadeService.getShadeById(productionPlan.getShadeId());
                        if (shadeMast == null)
                            continue;
                        BatchDetail batchDetail1 = new BatchDetail(batchDetail, quality, qualityName.get());
                        batchDetail1.setPartyShadeNo(shadeMast.getPartyShadeNo());
                        batchDetail1.setColorName(shadeMast.getColorName());
                        batchDetail1.setColorTone(shadeMast.getColorTone());
                        batchDetailList.add(batchDetail1);

                    }
                    else
                    {
                        ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(batchDetail.getMergeBatchId());
                        if (productionPlan == null)
                            continue;
                        ShadeMast shadeMast = shadeService.getShadeById(productionPlan.getShadeId());
                        if (shadeMast == null)
                            continue;
                        BatchDetail batchDetail1 = new BatchDetail(batchDetail, quality, qualityName.get());
                        batchDetail1.setPartyShadeNo(shadeMast.getPartyShadeNo());
                        batchDetail1.setColorName(shadeMast.getColorName());
                        batchDetail1.setColorTone(shadeMast.getColorTone());
                        batchDetailList.add(batchDetail1);
                    }
                }
                else {

                    batchDetailList.add(new BatchDetail(batchDetail,quality,qualityName.get()));
                }

            }
        }

        if(batchDetailList.isEmpty())
            return null;
        return batchDetailList;
    }

    public JobCard getJobCardByStockIdAndBatchId(Long stockId, String batchId) throws Exception {


        List<BatchData> batchData = batchDao.findByControlIdAndBatchId(stockId,batchId);
        if(batchData.isEmpty())
            throw new Exception("no batch record found");



        //header record
        Double totalWt = batchDao.getTotalWtByControlIdAndBatchId(stockId,batchId);
        Double totalMtr = batchDao.getTotalMtrByControlIdAndBatchId(stockId,batchId);
        Double totalFinish = batchDao.getTotalFinishMtrByBatchAndStock(batchId,stockId);
        Long totalPcs= batchDao.getTotalPcsByBatchAndStockId(stockId,batchId);
        StockMast stockMast = stockMastDao.findByStockId(stockId);
        Party party = partyDao.findByPartyId(stockMast.getPartyId());
        UserData userData = userDao.getUserById(stockMast.getUserHeadId());
        Quality quality = qualityDao.getqualityById(stockMast.getQualityId());
        Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality.getQualityNameId());

       /* System.out.println(stockMast.getId());
        System.out.println(party.getId());
        System.out.println(quality.getId());
        System.out.println(qualityName.get().getId());
        System.out.println(userData.getId());
        System.out.println(totalMtr);
        System.out.println(totalPcs);
        System.out.println(totalWt);
        System.out.println(totalPcs);*/
        JobCard jobCard=new JobCard(stockMast,party,userData,quality,qualityName.get(),totalMtr,totalPcs,totalWt);
        jobCard.setBatchId(batchId);
        jobCard.setTotalFinishMtr(totalFinish);
        jobCard.setBatchDataList(batchData);
        return jobCard;

    }


    public List<GetAllBatch> getAllBatchForAdditionalSlip(String id) throws Exception {
        //get the user record first
        Long userId = Long.parseLong(id);


        UserData userData = userDao.getUserById(userId);
        Long userHeadId=null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());


        List<GetAllBatch> list=new ArrayList<>();



        //get all batches which are in the queue
        List<JetData> jetDataList = jetService.getAllProductionInTheQueue();

        for(JetData jetData:jetDataList)
        {
            //get the production record
            ProductionPlan productionPlan = productionPlanService.getProductionDataById(jetData.getProductionId());
            if(productionPlan==null)
                continue;

            System.out.println(productionPlan.getBatchId());
            GetAllBatch getAllBatch=new GetAllBatch();
            //filter the record
            if (permissions.getViewAll())
            {
                userId=null;
                userHeadId=null;

                getAllBatch.setBatchId(productionPlan.getBatchId());//batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId());
            }
            else if (permissions.getViewGroup()) {
                //check the user is master or not ?
                //admin
                if (userData.getUserHeadId() == 0) {
                    userId = null;
                    userHeadId = null;
                    getAllBatch.setBatchId(productionPlan.getBatchId());//batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId());
                } else if (userData.getUserHeadId() > 0) {
                    //check weather master or operator
                    UserData userHead = userDao.getUserById(userData.getUserHeadId());
                    userId = userData.getId();
                    userHeadId = userHead.getId();
                    getAllBatch.setBatchId(productionPlan.getBatchId());//batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId(),userId,userHeadId);

                }
            }
            else if (permissions.getView()) {
                userId = userData.getId();
                userHeadId=null;
                getAllBatch.setBatchId(productionPlan.getBatchId());//batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId(),userId,userHeadId);
            }

            if(getAllBatch.getBatchId()!=null)
                list.add(getAllBatch);





        }


        if(list.isEmpty())
            throw new Exception("no record found");

        return list;
    }

    public List<GetAllBatch> getAllBatchForRedyeingSlip(String id) throws Exception {
        //get the user record first
        Long userId = Long.parseLong(id);


        UserData userData = userDao.getUserById(userId);
        Long userHeadId=null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());

        List<GetAllBatch> list=new ArrayList<>();

        //get all batches which are in the queue
        List<JetData> jetDataList = jetService.getAllProductionSuccessFromJet();

        for(JetData jetData:jetDataList)
        {
            GetAllBatch getAllBatch=null;
            //get the production record
            ProductionPlan productionPlan = productionPlanService.getProductionDataById(jetData.getProductionId());
            if(productionPlan==null)
                continue;

            //check the batch bill is generated or not
            if(batchDao.isFinishMtrSave(productionPlan.getBatchId()))
            continue;
            //GetAllBatch getAllBatch=null;
            //filter the record
            if (permissions.getViewAll())
            {
                userId=null;
                userHeadId=null;
                getAllBatch=null;//batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId());
            }
            else if (permissions.getViewGroup()) {
                //check the user is master or not ?
                //admin
                if (userData.getUserHeadId() == 0) {
                    userId = null;
                    userHeadId = null;
                    getAllBatch=null;//batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId());
                } else if (userData.getUserHeadId() > 0) {
                    //check weather master or operator
                    UserData userHead = userDao.getUserById(userData.getUserHeadId());
                    userId = userData.getId();
                    userHeadId = userHead.getId();
                    getAllBatch=null;//batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId(),userId,userHeadId);

                }
            }
            else if (permissions.getView()) {
                userId = userData.getId();
                userHeadId=null;
                getAllBatch=null;//batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId(),userId,userHeadId);
            }
            if(getAllBatch.getControlId()!=null)
                list.add(getAllBatch);

        }


        if(list.isEmpty())
            throw new Exception("no record found");

        return list;

    }

    public void createMergeBatchList(CreateMergeBatch record) throws Exception {
        //check first the all batch record is exist or not

        List<BatchData> batchDataRecordList =new ArrayList<>();
        for(BatchData batchData : record.getBatchDataList())
        {
            BatchData batchExist = batchDao.getBatchDataById(batchData.getId());
            if(batchExist==null)
                throw new Exception("batch record not found");

            batchExist.setMergeBatchId(record.getMergeBatchId());
            batchDataRecordList.add(batchExist);
        }

        if(!batchDataRecordList.isEmpty())
        {
            batchDao.saveAll(batchDataRecordList);
        }

    }

    public void updateMergeBatchList(CreateMergeBatch record) throws Exception {

        List<BatchData> batchDataRecordList =new ArrayList<>();
        //check the merge batch record is exit or not
        BatchData batchDataExist = batchDao.getMergeBatchExist(record.getMergeBatchId());
        if(batchDataExist==null)
            throw new Exception("no merge batch record found");

        //existing batches record with mergeBatchId
        //List<BatchData> existingBatchRecord =batchDao.getMergeBatchListByMergeBatchId(record.getMergeBatchId());
        List<Long> comingBatchRecordId =new ArrayList<>();
        List<Long> existingBatchRecordId =batchDao.getMergeBatchIdListByMergeBatchId(record.getMergeBatchId());
        for(BatchData batchData : record.getBatchDataList())
        {
            BatchData batchExist = batchDao.getBatchDataById(batchData.getId());
            if(batchExist==null)
                throw new Exception("batch record not found");

            batchExist.setMergeBatchId(record.getMergeBatchId());

            comingBatchRecordId.add(batchExist.getId());
            batchDataRecordList.add(batchExist);
        }
        batchDao.saveAll(batchDataRecordList);

        //change the status of batches which are existing but not comming from batch record
        existingBatchRecordId.forEach(e->{
            if(!comingBatchRecordId.contains(e))
            {
                batchDao.updateMergeIdByBatchEntryId(e,null);
            }

        });






    }

    public List<BatchToPartyAndQuality> getAllMergeBatchId() {
       // List<GetAllMergeBatchId> list = new ArrayList<>();

        List<BatchToPartyAndQuality> getAllBatchWithPartyAndQualities=new ArrayList<>();
        List<MergeBatchId> record = batchDao.getAllMergeBatchId();
        List<GetBatchWithControlId> batchDataForMergeBatch = null;

        batchDataForMergeBatch = batchDao.findAllMergeBatchWithoutFilter();

        //merge batch filter api
        for(GetBatchWithControlId batch : batchDataForMergeBatch)
        {
            //get batches based on batch id and stock id by mergebatchId

            List<GetBatchWithControlId> basedOnBatch = batchDao.getBatcheAndStockIdByMergeBatchIdWithoutFilter(batch.getMergeBatchId());
            BatchToPartyAndQuality batchToPartyAndQuality=new BatchToPartyAndQuality();
            for(GetBatchWithControlId batchByMergeBatch:basedOnBatch)
            {
                //System.out.println("stockId:"+batchByMergeBatch.getControlId());

                Optional<StockMast> stockMast=stockMastDao.findById(batchByMergeBatch.getControlId());
                if(stockMast.get().getQualityId()!=null && stockMast.get().getPartyId()!=null)
                {

                    Optional<Quality> quality=qualityDao.findById(stockMast.get().getQualityId());

                    Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality.get().getQualityNameId());
                    Optional<Party> party=partyDao.findById(stockMast.get().getPartyId());

                    //check that the process and party shade is exist or not
                    //if not then set the detail by null
                    /*ProductionPlan productionPlan=productionPlanService.getProductionDataByBatchAndStock(batchByMergeBatch.getBatchId(),batchByMergeBatch.getControlId());
                    if(productionPlan==null) {
                        batchToPartyAndQuality.setPartyShadeNo(null);
                        batchToPartyAndQuality.setProcessName(null);
                    }
                    else
                    {
                        //get the shade and process
                        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
                        DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());

                        if(dyeingProcessMast==null || shadeMast.isEmpty())
                            continue;


                        batchToPartyAndQuality.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
                        batchToPartyAndQuality.setProcessName(dyeingProcessMast.getProcessName());
                    }*/

                    batchToPartyAndQuality.setPartyName(batchToPartyAndQuality.getPartyName()==null?party.get().getPartyName():batchToPartyAndQuality.getPartyName()+","+party.get().getPartyName());
                    batchToPartyAndQuality.setQualityId(batchToPartyAndQuality.getQualityId()==null?quality.get().getQualityId():batchToPartyAndQuality.getQualityId()+","+quality.get().getQualityId());
                    batchToPartyAndQuality.setPartyId(batchToPartyAndQuality.getPartyId()==null?party.get().getId().toString():batchToPartyAndQuality.getPartyId()+","+party.get().getId().toString());
                    batchToPartyAndQuality.setQualityEntryId(batchToPartyAndQuality.getQualityEntryId()==null?quality.get().getId().toString():batchToPartyAndQuality.getQualityEntryId()+","+quality.get().getId());
                    batchToPartyAndQuality.setQualityName(batchToPartyAndQuality.getQualityName()==null?qualityName.get().getQualityName():batchToPartyAndQuality.getQualityName()+","+qualityName.get().getQualityName());
                    batchToPartyAndQuality.setBatchId(batchToPartyAndQuality.getBatchId()==null?batch.getBatchId():batchToPartyAndQuality.getBatchId()+","+batch.getBatchId());

                }
               /* Double totalMtr = batch.getMTR();//batchDao.getTotalMtrByMergeBatchId(batch.getMergeBatchId());
                Double totalWt = batch.getWT();//batchDao.getTotalWtByMergeBatchId(batch.getMergeBatchId());
                batchToPartyAndQuality.setTotalMtr(totalMtr);
                batchToPartyAndQuality.setTotalWt(totalWt);*/

            }
            batchToPartyAndQuality.setMergeBatchId(batch.getMergeBatchId());

            //add the record
            getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);

        }
        return getAllBatchWithPartyAndQualities;
    }

    public BatchToPartyQualityWithGr getMergeBatchByMergeBatchId(String mergeBatchId) throws Exception {
        BatchToPartyQualityWithGr batchToPartyAndQuality =new BatchToPartyQualityWithGr();
        BatchData mergeBatchExist = batchDao.getMergeBatchExist(mergeBatchId);

        List<BatchData> batchDataList = new ArrayList<>();
        if(mergeBatchExist==null)
            throw new Exception("no record found");

        List<GetBatchWithControlId> batchDataForMergeBatch = null;

        batchDataForMergeBatch = batchDao.findAllMergeBatchWithoutFilterByMergeBatchId(mergeBatchId);

        //merge batch filter api
        for(GetBatchWithControlId batch : batchDataForMergeBatch)
        {
            //get batches based on batch id and stock id by mergebatchId

            //List<GetBatchWithControlId> basedOnBatch = batchDao.getBatcheAndStockIdByMergeBatchId(batch.getMergeBatchId());
          /*  //BatchToPartyAndQuality batchToPartyAndQuality=new BatchToPartyAndQuality();
            for(GetBatchWithControlId batchByMergeBatch:basedOnBatch)
            {*/
                //System.out.println("stockId:"+batchByMergeBatch.getControlId());

                Optional<StockMast> stockMast=stockMastDao.findById(batch.getControlId());
                if(stockMast.get().getQualityId()!=null && stockMast.get().getPartyId()!=null)
                {

                    Optional<Quality> quality=qualityDao.findById(stockMast.get().getQualityId());

                    Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality.get().getQualityNameId());
                    Optional<Party> party=partyDao.findById(stockMast.get().getPartyId());

                    //check that the process and party shade is exist or not
                    //if not then set the detail by null
                    /*ProductionPlan productionPlan=productionPlanService.getProductionDataByBatchAndStock(batchByMergeBatch.getBatchId(),batchByMergeBatch.getControlId());
                    if(productionPlan==null) {
                        batchToPartyAndQuality.setPartyShadeNo(null);
                        batchToPartyAndQuality.setProcessName(null);
                    }
                    else
                    {
                        //get the shade and process
                        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
                        DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());

                        if(dyeingProcessMast==null || shadeMast.isEmpty())
                            continue;


                        batchToPartyAndQuality.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
                        batchToPartyAndQuality.setProcessName(dyeingProcessMast.getProcessName());
                    }*/

                    batchToPartyAndQuality.setPartyName(batchToPartyAndQuality.getPartyName()==null?party.get().getPartyName():batchToPartyAndQuality.getPartyName()+","+party.get().getPartyName());
                    batchToPartyAndQuality.setQualityId(batchToPartyAndQuality.getQualityId()==null?quality.get().getQualityId():batchToPartyAndQuality.getQualityId()+","+quality.get().getQualityId());
                    batchToPartyAndQuality.setPartyId(batchToPartyAndQuality.getPartyId()==null?party.get().getId().toString():batchToPartyAndQuality.getPartyId()+","+party.get().getId().toString());
                    batchToPartyAndQuality.setQualityEntryId(batchToPartyAndQuality.getQualityEntryId()==null?quality.get().getId().toString():batchToPartyAndQuality.getQualityEntryId()+","+quality.get().getId());
                    batchToPartyAndQuality.setQualityName(batchToPartyAndQuality.getQualityName()==null?qualityName.get().getQualityName():batchToPartyAndQuality.getQualityName()+","+qualityName.get().getQualityName());

                    batchToPartyAndQuality.setBatchId(batchToPartyAndQuality.getBatchId()==null?batch.getBatchId():batchToPartyAndQuality.getBatchId()+","+batch.getBatchId());


                }
                //Double totalMtr = batch.getMTR();//batchDao.getTotalMtrByMergeBatchId(batch.getMergeBatchId());
                //Double totalWt = batch.getWT();//batchDao.getTotalWtByMergeBatchId(batch.getMergeBatchId());
                batchToPartyAndQuality.setTotalMtr(batchDao.getTotalMtrByMergeBatchId(batch.getMergeBatchId()));
                batchToPartyAndQuality.setTotalWt(batchDao.getTotalWtByMergeBatchId(batch.getMergeBatchId()));

           // }
            batchDataList.addAll(batchDao.getBatchByBatchIdWithMergeBatchId(batch.getBatchId(),mergeBatchId));
            batchToPartyAndQuality.setMergeBatchId(batch.getMergeBatchId());
            //batchToPartyAndQuality.setBatchId(batch.getMergeBatchId());
            //add the record
            //getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);


        }
        batchToPartyAndQuality.setBatchDataList(batchDataList);


        return batchToPartyAndQuality;

    }

    public Double getWtByBatchId(String batchId) {
        return batchDao.getTotalWtByBatchId(batchId);
    }

    public Double getWtByMergeBatchId(String batchId) {
        return batchDao.getTotalWtByMergeBatchId(batchId);
    }

    public void deleteMergeBatchByMergeBatchId(String mergeBatchId) throws Exception {
        BatchData mergeBatchIdExist = batchDao.getMergeBatchExist(mergeBatchId);
        if(mergeBatchIdExist==null)
            throw new Exception("no record found");

        //check that the merge batch is production planned??
        if(mergeBatchIdExist.getIsProductionPlanned()==true)
            throw new Exception("unable to delete because production planned");

        List<BatchData> batchDataList = batchDao.getMergeBatchDataByMergeBatchId(mergeBatchId);

        for(BatchData batchData:batchDataList)
        {
            batchDao.updateMergeIdByBatchEntryId(batchData.getId(),null);
        }


    }


   /* public Quality getQualityByStockId(Long stockId) {
        return qualityDao.getQualityByStockId(stockId);
    }*/
}
