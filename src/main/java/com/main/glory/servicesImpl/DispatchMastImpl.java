package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.admin.InvoiceSequenceDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.admin.InvoiceSequence;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.Filter;
import com.main.glory.model.dispatch.bill.GetBill;
import com.main.glory.model.dispatch.bill.QualityList;
import com.main.glory.model.dispatch.request.*;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.dispatch.response.GetConsolidatedBill;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("dispatchMastImpl")
public class DispatchMastImpl {

    @Autowired
    ProductionPlanImpl productionPlanService;

    @Autowired
    ShadeServiceImpl shadeService;

    @Autowired
    PartyServiceImp partyServiceImp;
    @Autowired
    QualityServiceImp qualityServiceImp;

    @Autowired
    DispatchMastDao dispatchMastDao;

    @Autowired
    DispatchDataDao dispatchDataDao;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    InvoiceSequenceDao invoiceSequenceDao;

    @Autowired
    BatchDao batchDao;

    @Autowired
    BatchServiceImpl batchService;

    @Autowired
    PartyDao partyDao;

    @Autowired
    QualityDao qualityDao;

    public Long saveDispatch(CreateDispatch dispatchList) throws Exception {

        /*List<DispatchMast> list = dispatchMastDao.getAllInvoiceList();

        List<DispatchData> saveTheList=new ArrayList<>();
        //if the sequence is there or not
        if(!list.isEmpty())
        {
            Long invoiceNumber = dispatchMastDao.getInvoiceNumber("inv");

            invoiceNumber+=1;
           //get the invoice and merge with "inv" and then create the invoice for multiple batch



            //party detail by stock

            StockMast stockMast=stockBatchService.getStockById(dispatchList.getBatchAndStockIdList().get(0).getStockId());

            Optional<Party> party = partyDao.findById(stockMast.getPartyId());

            if(party.isEmpty())
                throw new Exception("no party found for id:"+stockMast.getPartyId());


            //check the user are exist or not
            if(dispatchList.getCreatedBy()!=null && dispatchList.getUserHeadId()!= null) {

                UserData createdUserExist = userService.getUserById(dispatchList.getCreatedBy());

                if (createdUserExist == null)
                    throw new Exception("no user found for id:" + dispatchList.getCreatedBy());


                UserData headUserExist = userService.getUserByUserHeadIdAndId(dispatchList.getUserHeadId(),dispatchList.getCreatedBy());
                if (headUserExist == null)
                    throw new Exception("no user found with head");
            }
            else
                throw new Exception("user head id or createdBy id can't be null");





            //check first the data is available or not

            for(BatchAndStockId createDispatch:dispatchList.getBatchAndStockIdList()) {
                List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(createDispatch.getStockId(), createDispatch.getBatchId());

                if (batchDataList.isEmpty())
                    throw new Exception("no batch data found");
            }


            //iterate and change the status
            for(BatchAndStockId createDispatch:dispatchList.getBatchAndStockIdList()) {
                List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(createDispatch.getStockId(), createDispatch.getBatchId());

                if (batchDataList.isEmpty())
                    throw new Exception("no batch data found");

                for(BatchData batchData:batchDataList)
                {

                    if(batchData.getIsFinishMtrSave()==true && batchData.getIsBillGenrated()==false)
                    {
                        DispatchData dispatchData=new DispatchData(batchData);

                        dispatchData.setInvoiceNo("inv"+invoiceNumber);

                        dispatchData.setCreatedBy(dispatchList.getCreatedBy());
                        //saveTheList.add(dispatchData);
                        //save the complete batch with gr list to the dispatch data with same invoice number
                        dispatchDataDao.save(dispatchData);

                        batchData.setIsBillGenrated(true);
                        batchDao.save(batchData);
                    }
                }
            }






            //increament the invoice number to dispatch mast


            DispatchMast dispatchMast =new DispatchMast();
            dispatchMast.setPrefix("inv");
            dispatchMast.setPartyId(party.get().getId());
            dispatchMast.setCreatedBy(dispatchList.getCreatedBy());
            dispatchMast.setUpdatedBy(dispatchList.getUpdatedBy());
            dispatchMast.setUserHeadId(dispatchList.getUserHeadId());
            dispatchMast.setPostfix(invoiceNumber);
            dispatchMastDao.save(dispatchMast);
            return true;

        }
        else
        {
            //party detail by stock

            StockMast stockMast=stockBatchService.getStockById(dispatchList.getBatchAndStockIdList().get(0).getStockId());

            Optional<Party> party = partyDao.findById(stockMast.getPartyId());

            if(party.isEmpty())
                throw new Exception("no party found for id:"+stockMast.getPartyId());


            //check the user are exist or not
            UserData createdUserExist =userService.getUserById(dispatchList.getCreatedBy());

            if(createdUserExist==null)
                throw new Exception("no user found for id:"+dispatchList.getCreatedBy());


            UserData headUserExist = userService.getUserByUserHeadIdAndId(dispatchList.getUserHeadId(),dispatchList.getCreatedBy());
            if (headUserExist == null)
                throw new Exception("no user found with head");


            //if there is not sequence then  create it
            DispatchMast createDipatch=new DispatchMast();
            createDipatch.setPostfix(0l);
            createDipatch.setPrefix("inv");

            //System.out.println(createDipatch.getPostfix()+":"+createDipatch.getPrefix());
            dispatchMastDao.save(createDipatch);



            Long invoiceNumber = dispatchMastDao.getInvoiceNumber("inv");

            //do the same above thing if the invoice data is emty
            invoiceNumber+=1;
            //get the invoice and merge with "inv" and then create the invoice for multiple batch

            //check first the data is available or not

            for(BatchAndStockId createDispatch:dispatchList.getBatchAndStockIdList()) {
                List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(createDispatch.getStockId(), createDispatch.getBatchId());

                if (batchDataList.isEmpty())
                    throw new Exception("no batch data found");
            }


                //iterate and change the status
            for(BatchAndStockId createDispatch:dispatchList.getBatchAndStockIdList()) {
                List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(createDispatch.getStockId(), createDispatch.getBatchId());

                if (batchDataList.isEmpty())
                    throw new Exception("no batch data found");

                for(BatchData batchData:batchDataList)
                {

                    if(batchData.getIsFinishMtrSave()==true && batchData.getIsBillGenrated()==false)
                    {
                        DispatchData dispatchData=new DispatchData(batchData);

                        dispatchData.setInvoiceNo("inv"+invoiceNumber);

                        dispatchData.setCreatedBy(dispatchList.getCreatedBy());
                        //saveTheList.add(dispatchData);
                        //save the complete batch with gr list to the dispatch data with same invoice number
                        dispatchDataDao.save(dispatchData);

                        batchData.setIsBillGenrated(true);
                        batchDao.save(batchData);
                    }
                }
            }




            //increament the the invoice number to dispatch mast


            DispatchMast dispatchMast =new DispatchMast();

            dispatchMast.setPrefix("inv");
            dispatchMast.setPartyId(party.get().getId());
            dispatchMast.setCreatedBy(dispatchList.getCreatedBy());
            dispatchMast.setUpdatedBy(dispatchList.getUpdatedBy());
            dispatchMast.setUserHeadId(dispatchList.getUserHeadId());
            dispatchMast.setPostfix(invoiceNumber);

            dispatchMastDao.save(dispatchMast);
            return true;

        }

*/


        //check the invoice sequece exist or not
        InvoiceSequence invoiceSequenceExist =invoiceSequenceDao.getSequence();
        if(invoiceSequenceExist==null)
            throw new Exception("no sequence found");


        //invoice process


        //party detail by stock

        StockMast stockMast=stockBatchService.getStockById(dispatchList.getBatchAndStockIdList().get(0).getStockId());

        Optional<Party> party = partyDao.findById(stockMast.getPartyId());

        if(party.isEmpty())
            throw new Exception("no party found for id:"+stockMast.getPartyId());


       /* //check the user are exist or not
        if(dispatchList.getCreatedBy()!=null && dispatchList.getUserHeadId()!= null) {

            UserData createdUserExist = userService.getUserById(dispatchList.getCreatedBy());

            if (createdUserExist == null)
                throw new Exception("no user found for id:" + dispatchList.getCreatedBy());


            UserData headUserExist = userService.getUserByUserHeadIdAndId(dispatchList.getUserHeadId(),dispatchList.getCreatedBy());
            if (headUserExist == null)
                throw new Exception("no user found with head");
        }
        else
            throw new Exception("user head id or createdBy id can't be null");*/





        //check first the data is available or not

        for(BatchAndStockId createDispatch:dispatchList.getBatchAndStockIdList()) {
            List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(createDispatch.getStockId(), createDispatch.getBatchId());

            if (batchDataList.isEmpty())
                throw new Exception("no batch data found");
        }


        //iterate and change the status
        for(BatchAndStockId createDispatch:dispatchList.getBatchAndStockIdList()) {
            List<BatchData> batchDataList = batchDao.getBatchesByBatchIdAndFinishMtrSaveWithoutBillGenrated(createDispatch.getBatchId());


            //get the shade detail
            ProductionPlan productionPlan = productionPlanService.getProductionDataByBatchAndStock(createDispatch.getBatchId(), createDispatch.getStockId());
            /*if(productionPlan==null)
                throw new Exception("no production plan found for batch");*/

            ShadeMast shadeMast = null;
            if(productionPlan!=null && productionPlan.getShadeId()!=null)
            {
                shadeMast=shadeService.getShadeById(productionPlan.getShadeId());
                if(shadeMast==null)
                    throw new Exception("no shade record found");
            }


            Quality quality =null;// qualityServiceImp.getQualityByEntryId(productionPlan.getQualityEntryId());

            if(quality==null)
                throw new Exception("no quality found");

            if (batchDataList.isEmpty())
                throw new Exception("no batch data found");

            for(BatchData batchData:batchDataList)
            {

                if(batchData.getIsFinishMtrSave()==true && batchData.getIsBillGenrated()==false)
                {
                    DispatchData dispatchData=null;
                    if(shadeMast!=null)
                    {
                        dispatchData=new DispatchData(batchData,shadeMast,quality);
                        dispatchData.setShadeRate(shadeMast.getExtraRate());

                    }
                    else
                    {
                        dispatchData=new DispatchData(batchData,quality);
                        dispatchData.setShadeRate(0.0);
                    }




                    dispatchData.setInvoiceNo(invoiceSequenceExist.getSequence().toString());

                    dispatchData.setCreatedBy(dispatchList.getCreatedBy());
                    //saveTheList.add(dispatchData);
                    //save the complete batch with gr list to the dispatch data with same invoice number
                    dispatchDataDao.save(dispatchData);

                    batchData.setIsBillGenrated(true);
                    batchDao.save(batchData);
                }
            }
        }






        //increament the invoice number to dispatch mast


        DispatchMast dispatchMast =new DispatchMast();
        dispatchMast.setPrefix("inv");
        dispatchMast.setPartyId(party.get().getId());
        dispatchMast.setCreatedBy(dispatchList.getCreatedBy());
        dispatchMast.setUpdatedBy(dispatchList.getUpdatedBy());
        dispatchMast.setUserHeadId(dispatchList.getUserHeadId());
        dispatchMast.setPostfix(invoiceSequenceExist.getSequence());
        dispatchMastDao.save(dispatchMast);


        //update the invoice sequence by one
        invoiceSequenceDao.updateSequenceByOne(invoiceSequenceExist.getId(),invoiceSequenceExist.getSequence()+1);
        return invoiceSequenceExist.getSequence();

    }


    public List<BatchWithTotalMTRandFinishMTR> getBatchByParty(Long partyId)throws Exception {
        List<BatchWithTotalMTRandFinishMTR> batchDataListByParty=new ArrayList<>();
        List<StockMast> stockMastList = stockBatchService.getBatchByPartyId(partyId);

        if(stockMastList.isEmpty())
            throw new Exception("stock not found for party:"+partyId);


        for(StockMast stockMast:stockMastList)
        {
            //System.out.println(stockMast.getId());
            List<BatchWithTotalMTRandFinishMTR> batchDataList = batchDao.getAllBatchByStockIdWithTotalFinishMtr(stockMast.getId());
            for(BatchWithTotalMTRandFinishMTR getBatchWithControlIdData:batchDataList)
            {
                BatchWithTotalMTRandFinishMTR getBatchWithControlId = new BatchWithTotalMTRandFinishMTR(getBatchWithControlIdData);
                batchDataListByParty.add(getBatchWithControlId);
            }

        }

        if(batchDataListByParty.isEmpty())
            throw new Exception("data not found for party:"+partyId);
        return  batchDataListByParty;
    }

    public List<GetAllDispatch> getAllDisptach() throws Exception{

        List<GetAllDispatch> dispatchDataList=new ArrayList<>();
        List<DispatchData> dispatchList =dispatchDataDao.getAllDispatch();


        List<String> invoiceNumber=new ArrayList<>();

        if (dispatchList.isEmpty())
            throw new Exception("no data found");
        for(DispatchData dispatchData:dispatchList)
        {
            List<BatchWithTotalMTRandFinishMTR> batchList=new ArrayList<>();

            if(!invoiceNumber.contains(dispatchData.getInvoiceNo()))
            {
                invoiceNumber.add(dispatchData.getInvoiceNo());
                GetAllDispatch getAllDispatch=new GetAllDispatch(dispatchData);

                DispatchMast dispatchMast = dispatchMastDao.getDataByInvoiceNumber(Long.parseLong(dispatchData.getInvoiceNo()));
                Party party = partyServiceImp.getPartyDetailById(dispatchMast.getPartyId());
                if(party==null)
                    continue;
                //get the batch data

                List<GetBatchByInvoice> batchListWithInvoiceList = dispatchDataDao.getAllStockByInvoiceNumber(dispatchData.getInvoiceNo());

                for(GetBatchByInvoice batch : batchListWithInvoiceList)
                {
                    //list of batches
                    BatchWithTotalMTRandFinishMTR batchWithTotalMTRandFinishMTR = batchDao.getAllBatchWithTotalMtrAndTotalFinishMtr(batch.getBatchId(),batch.getStockId());
                    batchList.add(batchWithTotalMTRandFinishMTR);
                }
                getAllDispatch.setPartyId(party.getId());
                getAllDispatch.setPartyName(party.getPartyName());
                getAllDispatch.setBatchList(batchList);

                dispatchDataList.add(getAllDispatch);
            }


        }
        return dispatchDataList;
    }

    public PartyWithBatchByInvoice getDispatchByInvoiceNumber(String invoiceNo) throws Exception {

        List<BatchWithTotalMTRandFinishMTR> batchWithTotalMTRandFinishMTRList=new ArrayList<>();

        List<GetBatchByInvoice> list = dispatchDataDao.findBatchAndStockByInvoice(invoiceNo);

        if(list.isEmpty())
            throw new Exception("no data found");

        for(GetBatchByInvoice batch:list)
        {
            BatchWithTotalMTRandFinishMTR batchWithTotalMTRandFinishMTR=new BatchWithTotalMTRandFinishMTR();
            batchWithTotalMTRandFinishMTR.setBatchId(batch.getBatchId());
            batchWithTotalMTRandFinishMTR.setControlId(batch.getStockId());

            List<DispatchData> dispatchDataList = dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),batch.getBatchId(),invoiceNo);
            Double WT=0.0;
            Double MTR=0.0;
            Double totalFinishMtr=0.0;
            Long totalPcs=0l;

            for(DispatchData dispatchData:dispatchDataList)
            {
                BatchData batchData=batchDao.findByBatchEntryId(dispatchData.getBatchEntryId());
                if(batchData.getWt()!=null)
                {
                    WT+=batchData.getWt();
                    MTR+=batchData.getMtr();
                    totalFinishMtr=batchData.getFinishMtr();
                    totalPcs++;

                }
            }
            batchWithTotalMTRandFinishMTR.setTotalFinishMtr(totalFinishMtr);
            batchWithTotalMTRandFinishMTR.setTotalPcs(totalPcs);
            batchWithTotalMTRandFinishMTR.setWT(WT);
            batchWithTotalMTRandFinishMTR.setMTR(MTR);
//            Optional<Party> party=partyDao.findById(dispatchDataList.get(0).getStockId());
//            if(party.isPresent())
//                throw new Exception("no party data found");
//            batchWithTotalMTRandFinishMTR.setPartyId(party.get().getId());
//            batchWithTotalMTRandFinishMTR.setPartyName(party.get().getPartyName());
            batchWithTotalMTRandFinishMTRList.add(batchWithTotalMTRandFinishMTR);
        }




       StockMast stockMast = stockBatchService.getStockById(list.get(0).getStockId());
        Party party=partyDao.findByPartyId(stockMast.getPartyId());

        if(party==null)
            throw new Exception("no data found");




        PartyWithBatchByInvoice partyWithBatchByInvoice =new PartyWithBatchByInvoice(batchWithTotalMTRandFinishMTRList,party);


        //status
        Boolean isSendToParty=dispatchDataDao.getSendToPartyFlag(invoiceNo);
        if(isSendToParty==true)
        {
            partyWithBatchByInvoice.setIsSendToParty(isSendToParty);
        }
        else
        {
            partyWithBatchByInvoice.setIsSendToParty(isSendToParty);
        }
        return partyWithBatchByInvoice;

    }

    public Boolean updateDispatch(UpdateInvoice updateInvoice) throws Exception{


        //check the invoice number is exist
        String invoiceExist=dispatchDataDao.findByInvoiceNo(updateInvoice.getInvoiceNo());
        if(invoiceExist==null)
            throw new Exception("no invoice found for invoice:"+updateInvoice.getInvoiceNo());

        //for the new data
        List<DispatchData> saveTheList=new ArrayList<>();

        //update invoice
        Map<String,Long> availbleBatchInInvoice = new HashMap<>();

        Map<String,Long> comingBatches = new HashMap<>();

        List<GetBatchByInvoice> availableBatches = dispatchDataDao.findBatchAndStockByInvoice(updateInvoice.getInvoiceNo());

        if(availableBatches.isEmpty())
            throw new Exception("this invoice is empty create new one");

        /*

        if the batch list coming empty with the given invoice,so

        considering that invoice should be deleted

        delete this invoice

         */

        if(updateInvoice.getBatchAndStockIdList().isEmpty())
        {
            //get the batch entry and change the flag
            List<DispatchData> dispatchDataList=dispatchDataDao.getBatchByInvoiceNo(updateInvoice.getInvoiceNo());

            for(DispatchData dispatchData:dispatchDataList)
            {
                //change the batch status
                BatchData batchData=batchDao.findByBatchEntryId(dispatchData.getBatchEntryId());
                batchData.setIsBillGenrated(false);
                batchDao.save(batchData);
            }
            //delete all the batches from the dispatch
            dispatchDataDao.deleteByInvoiceNo(updateInvoice.getInvoiceNo());

            //delete the master entry also
            dispatchMastDao.deleteByInvoicePostFix(Long.parseLong(updateInvoice.getInvoiceNo().substring(3)));
            return true;

        }





        //coming batch list
        for(BatchAndStockId createDispatch:updateInvoice.getBatchAndStockIdList())
        {
            comingBatches.put(createDispatch.getBatchId(),createDispatch.getStockId());
        }

        //available batch list
        for(GetBatchByInvoice getBatchByInvoice:availableBatches)
        {
            availbleBatchInInvoice.put(getBatchByInvoice.getBatchId(),getBatchByInvoice.getStockId());
        }

        //Update functionality if coming batch is not in avaialable in the avaialable batch list
        for (Map.Entry<String,Long> entry : comingBatches.entrySet())
        {


            //System.out.println(entry.getKey());
            if(!availbleBatchInInvoice.isEmpty())
            {

                //if the data is found then remove from the available batch list to delete the batch later
                if(availbleBatchInInvoice.containsKey(entry.getKey()))
                {
                    if(availbleBatchInInvoice.get(entry.getKey()).equals(entry.getValue()))
                    availbleBatchInInvoice.remove(entry.getKey(),entry.getValue());
                }
                else
                {
                    //if the batch is not availbale in existing list
                    //get the batch data
                    List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(entry.getValue(),entry.getKey());

                    for(BatchData batchData:batchDataList)
                    {

                        if(batchData.getIsFinishMtrSave()==true && batchData.getIsBillGenrated()==false)
                        {
                            DispatchData dispatchData=new DispatchData(batchData);

                            dispatchData.setInvoiceNo(updateInvoice.getInvoiceNo());

                            dispatchData.setCreatedBy(updateInvoice.getCreatedBy());

                            dispatchData.setUpdatedBy(updateInvoice.getUpdatedBy());

                            saveTheList.add(dispatchData);
                            batchData.setIsBillGenrated(true);
                            batchDao.save(batchData);
                        }
                    }
                    dispatchDataDao.saveAll(saveTheList);

                }


            }
            else//if batch is not availale then insert the new entry
            {
                //get the batch data
                List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(entry.getValue(),entry.getKey());

                for(BatchData batchData:batchDataList)
                {

                    if(batchData.getIsFinishMtrSave()==true && batchData.getIsBillGenrated()==false)
                    {
                        DispatchData dispatchData=new DispatchData(batchData);

                        dispatchData.setInvoiceNo(updateInvoice.getInvoiceNo());

                        dispatchData.setCreatedBy(updateInvoice.getCreatedBy());

                        dispatchData.setUpdatedBy(updateInvoice.getUpdatedBy());

                        saveTheList.add(dispatchData);
                        batchData.setIsBillGenrated(true);
                        batchDao.save(batchData);
                    }
                }
                dispatchDataDao.saveAll(saveTheList);
            }


        }



        //now remove the batch which is not coming from the FE and remain it in available batch list of invoice
        if(!availbleBatchInInvoice.isEmpty())
        {
            for (Map.Entry<String,Long> entry : availbleBatchInInvoice.entrySet())
            {
                List<BatchData> batchDataList=batchDao.findByControlIdAndBatchId(entry.getValue(),entry.getKey());
                for(BatchData batchData:batchDataList)
                {
                    batchData.setIsBillGenrated(false);
                    batchDao.save(batchData);
                    dispatchDataDao.deleteByBatchEntryId(batchData.getId());
                }

            }
            return true;

        }
        else
            return true;


    }

    public Boolean updateDispatchStatus(String invoiceNo) throws Exception{

        String invoiceNumberExist =  dispatchDataDao.findByInvoiceNo(invoiceNo);
        if(invoiceNumberExist==null)
            throw new Exception("no invoice number found");

        dispatchDataDao.updateStatus(invoiceNumberExist);
        return true;
    }

    public PartyDataByInvoiceNumber checkInvoiceDataIsAvailable(String invoiceNo) throws Exception {

        List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList= new ArrayList<>();


        String invoiceExist = dispatchDataDao.findByInvoiceNo(invoiceNo);

        //quality list

        List<GetBatchByInvoice> batchList = dispatchDataDao.findBatchAndStockByInvoiceWithoutStatus(invoiceExist);

        for(GetBatchByInvoice batch:batchList)
        {
            if(batchList.isEmpty())
                continue;

            Double totalMtr=0.0;
            Double finishMtr=0.0;
            Long pcs=0l;
            Double amt=0.0;

            //System.out.println(batch.getStockId());
            StockMast stockMast=stockBatchService.getStockById(batch.getStockId());
            Optional<Quality> quality = qualityDao.findById(stockMast.getQualityId());

            if(quality.isEmpty())
                throw new Exception("no quality found");

            QualityBillByInvoiceNumber qualityBillByInvoiceNumber=new QualityBillByInvoiceNumber(quality.get());

            List<DispatchData> dispatchDataList=dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),batch.getBatchId(), invoiceNo);

            for(DispatchData invoiceBatch:dispatchDataList)
            {

                Optional<BatchData> batchData=batchDao.findById(invoiceBatch.getBatchEntryId());
                if(batchData.get().getMtr()!=null && batchData.get().getFinishMtr()!=null) {
                    totalMtr += batchData.get().getMtr();
                    finishMtr += batchData.get().getFinishMtr();
                    pcs++;
                }

            }

            //Count the total amt based on quality rate and total finish mtr
            if(quality.get().getRate()!=null && finishMtr>0.0)
                amt=quality.get().getRate()*finishMtr;


            //set the quality with batch data
            qualityBillByInvoiceNumber.setBatchId(batch.getBatchId());
            qualityBillByInvoiceNumber.setTotalMtr(totalMtr);
            qualityBillByInvoiceNumber.setFinishMtr(finishMtr);
            qualityBillByInvoiceNumber.setPcs(pcs);
            qualityBillByInvoiceNumber.setAmt(amt);

            qualityBillByInvoiceNumber.setPChalNo(stockMast.getChlNo());
            qualityBillByInvoiceNumberList.add(qualityBillByInvoiceNumber);
        }



        //for batch list


        List<GetBatchByInvoice> batchList2 = dispatchDataDao.findBatchAndStockByInvoiceWithoutStatus(invoiceExist);

        List<BatchWithGr> batchWithGrList =new ArrayList<>();
        for(GetBatchByInvoice batch:batchList2)
        {

            BatchWithGr batchWithGr=new BatchWithGr(batch);
            List<BatchData> batchDataList=new ArrayList<>();


            //batches data

            List<DispatchData> dispatchDataList=dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),batch.getBatchId(), invoiceNo);

            for(DispatchData invoiceBatch:dispatchDataList)
            {

                Optional<BatchData> batchData=batchDao.findById(invoiceBatch.getBatchEntryId());
                batchDataList.add(batchData.get());
            }
            batchWithGr.setBatchDataList(batchDataList);
            batchWithGrList.add(batchWithGr);


        }






        //for party data
        if(!batchList.isEmpty())
        {
            StockMast stockMast = stockBatchService.getStockById(batchList.get(0).getStockId());
            Optional<Party> party=partyDao.findById(stockMast.getPartyId());

            PartyDataByInvoiceNumber partyDataByInvoiceNumber=new PartyDataByInvoiceNumber(party.get(),qualityBillByInvoiceNumberList,batchWithGrList);


            if(partyDataByInvoiceNumber==null)
                throw new Exception("no data found");
            return partyDataByInvoiceNumber;
        }


        return null;


    }
    public PartyDataByInvoiceNumber getPartyWithQualityDispatchBy(String invoiceNo) throws Exception {

        List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList= new ArrayList<>();


        String invoiceExist = dispatchDataDao.findByInvoiceNo(invoiceNo);
        if(invoiceExist == null || invoiceExist=="")
            throw new Exception("no data found");


        //quality list

        List<GetBatchByInvoice> batchList = dispatchDataDao.findBatchAndStockByInvoiceWithoutStatus(invoiceExist);

        for(GetBatchByInvoice batch:batchList)
        {
            if(batchList.isEmpty())
                continue;

            Double totalMtr=0.0;
            Double finishMtr=0.0;
            Long pcs=0l;
            Double amt=0.0;

            //System.out.println(batch.getStockId());
            StockMast stockMast=stockBatchService.getStockById(batch.getStockId());
            Optional<Quality> quality = qualityDao.findById(stockMast.getQualityId());

            if(quality.isEmpty())
                throw new Exception("no quality found");

            QualityBillByInvoiceNumber qualityBillByInvoiceNumber=new QualityBillByInvoiceNumber(quality.get());
            Optional<QualityName> qualityName = qualityServiceImp.getQualityNameDataById(quality.get().getQualityNameId());
            qualityBillByInvoiceNumber.setQualityName(qualityName.get().getQualityName());

            List<DispatchData> dispatchDataList=dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),batch.getBatchId(), invoiceNo);

            for(DispatchData invoiceBatch:dispatchDataList)
            {
                //System.out.println("invoic entry:"+invoiceBatch.getBatchEntryId());
                BatchData batchData=batchDao.findByBatchEntryId(invoiceBatch.getBatchEntryId());
                if(batchData.getMtr()!=null && batchData.getFinishMtr()!=null) {
                    totalMtr += batchData.getMtr();
                    finishMtr += batchData.getFinishMtr();
                    pcs++;
                }

            }

            //get the shade rate as well
            ProductionPlan productionPlan=productionPlanService.getProductionDataByBatchAndStock(batch.getBatchId(),batch.getStockId());
            if(productionPlan==null)
                continue;
           /* Optional<ShadeMast> shadeMast = null;
            if(productionPlan.getShadeId()!=null)
            shadeService.getShadeMastById(productionPlan.getShadeId());*/

            Double shadeRate=0.0;
            /*if(shadeMast.isPresent())
            {
                shadeRate = shadeMast.get().getExtraRate();
            }*/

            //get the rate

            Double rate = dispatchDataList.get(0).getQualityRate();
            shadeRate = dispatchDataList.get(0).getShadeRate();


            //Count the total amt based on quality rate and total finish mtr
            amt=(rate+shadeRate)*finishMtr;






            //set the quality with batch data
            qualityBillByInvoiceNumber.setBatchId(batch.getBatchId());
            qualityBillByInvoiceNumber.setTotalMtr(totalMtr);
            qualityBillByInvoiceNumber.setFinishMtr(finishMtr);
            qualityBillByInvoiceNumber.setPcs(pcs);
            qualityBillByInvoiceNumber.setAmt(amt);
            qualityBillByInvoiceNumber.setRate(rate);
            qualityBillByInvoiceNumber.setShadeRate(shadeRate);
            qualityBillByInvoiceNumber.setPChalNo(stockMast.getChlNo());
            qualityBillByInvoiceNumberList.add(qualityBillByInvoiceNumber);
        }



        //for batch list


        List<GetBatchByInvoice> batchList2 = dispatchDataDao.findBatchAndStockByInvoiceWithoutStatus(invoiceExist);

        List<BatchWithGr> batchWithGrList =new ArrayList<>();
        for(GetBatchByInvoice batch:batchList2)
        {

            BatchWithGr batchWithGr=new BatchWithGr(batch);
            List<BatchData> batchDataList=new ArrayList<>();


            //batches data

            List<DispatchData> dispatchDataList=dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),batch.getBatchId(), invoiceNo);

            for(DispatchData invoiceBatch:dispatchDataList)
            {

                Optional<BatchData> batchData=batchDao.findById(invoiceBatch.getBatchEntryId());
                batchDataList.add(batchData.get());
            }
            batchWithGr.setBatchDataList(batchDataList);
            batchWithGrList.add(batchWithGr);


        }






        //for party data
        StockMast stockMast = stockBatchService.getStockById(batchList.get(0).getStockId());
        Optional<Party> party=partyDao.findById(stockMast.getPartyId());

        PartyDataByInvoiceNumber partyDataByInvoiceNumber=new PartyDataByInvoiceNumber(party.get(),qualityBillByInvoiceNumberList,batchWithGrList);


        if(partyDataByInvoiceNumber==null)
            throw new Exception("no data found");

        partyDataByInvoiceNumber.setInvoiceNo(invoiceNo);

        return partyDataByInvoiceNumber;


    }

    public List<DispatchMast> getPendingDispatchByPartyId(Long partyId) throws Exception {
        Optional<Party> partyExist=partyDao.findById(partyId);

        if(partyExist.isEmpty())
            throw new Exception("no data found for party id:"+partyId);

        List<DispatchMast> list=dispatchMastDao.getPendingBillByPartyId(partyId);
        if(list.isEmpty())
            throw new Exception("no pending invoice found for party:"+partyId);

        return list;

    }

    public List<GetConsolidatedBill> getDispatchByFilter(Filter filter) throws Exception {
        Date from=null;
        Date to=null;
        //add one day because of timestamp issue
        Calendar c = Calendar.getInstance();

        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                "yyyy-MM-dd");


        if(!filter.getFrom().isEmpty())
            from = datetimeFormatter1.parse(filter.getFrom());
        if(!filter.getTo().isEmpty()) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            c.add(Calendar.DATE, 1);//adding one day in to because of time issue in created date
            to=c.getTime();
        }

        Double amt=0.0;
        Double totalFinishMtr=0.0;
        Double batchFinishMtr=0.0;
        Double totalBatchMtr=0.0;
        List<GetConsolidatedBill> list = new ArrayList<>();

        //System.out.println(from+":"+to);
        List<DispatchMast> dispatchMastList = dispatchMastDao.getInvoiceByFilter(from,to,filter.getPartyId(),filter.getUserHeadId());
        for(DispatchMast dispatchMast:dispatchMastList)
        {
            Party party =partyServiceImp.getPartyById(dispatchMast.getPartyId());
            if(party==null)
                continue;


            UserData userData = userService.getUserById(dispatchMast.getUserHeadId());
            if(userData==null)
                continue;


            String invoiceNumber=dispatchMast.getPrefix()+dispatchMast.getPostfix();
            List<GetBatchByInvoice> stockWithBatchList = dispatchDataDao.getAllStockByInvoiceNumber(invoiceNumber);
            for(GetBatchByInvoice getBatchByInvoice:stockWithBatchList)
            {
                if(getBatchByInvoice.getBatchId()==null)
                    continue;

                List<BatchData> batchDataList = stockBatchService.getBatchWithControlIdAndBatchId(getBatchByInvoice.getBatchId(),getBatchByInvoice.getStockId());
                if(batchDataList.isEmpty())
                    continue;

                StockMast stockMast = stockBatchService.getStockByStockId(getBatchByInvoice.getStockId());
                if(stockMast==null)
                    continue;

                GetQualityResponse quality= qualityServiceImp.getQualityByID(stockMast.getQualityId());
                if(quality==null)
                    continue;

                for(BatchData batchData:batchDataList)
                {
                    totalBatchMtr+=batchData.getMtr();
                    batchFinishMtr +=batchData.getFinishMtr();
                    totalFinishMtr +=batchFinishMtr;
                }
                amt+=batchFinishMtr*quality.getRate();
                batchFinishMtr = 0.0;



            }


            if(amt>0.0) {
                GetConsolidatedBill getConsolidatedBill = new GetConsolidatedBill();
                getConsolidatedBill.setAmt(amt);
                getConsolidatedBill.setTotalFinishMtr(totalFinishMtr);
                getConsolidatedBill.setPartyName(party.getPartyName());
                getConsolidatedBill.setPartyId(party.getId());
                getConsolidatedBill.setHeadName(userData.getUserName());
                getConsolidatedBill.setUserHeadId(userData.getId());
                getConsolidatedBill.setInvoiceNo(invoiceNumber);
                getConsolidatedBill.setTotalBatchMtr(totalBatchMtr);
                list.add(getConsolidatedBill);

            }


        }
        if (list.isEmpty())
            throw new Exception("no invoice data found");
        return list;

    }

    public List<GetBill> getDispatchBillByFilter(Filter filter) throws Exception {

        Date from=null;
        Date to=null;
        List<GetBill> list = new ArrayList<>();


        //add one day because of timestamp issue
        Calendar c = Calendar.getInstance();

        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                "yyyy-MM-dd");


        if(!filter.getFrom().isEmpty())
         from = datetimeFormatter1.parse(filter.getFrom());
        if(!filter.getTo().isEmpty()) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            c.add(Calendar.DATE, 1);//adding one day in to because of time issue in created date
            to=c.getTime();
        }



        Double amt=0.0;
        Double totalFinishMtr=0.0;
        Double batchFinishMtr=0.0;
        Double totalBatchMtr=0.0;


        //System.out.println(from+":"+to);
        List<DispatchMast> dispatchMastList = dispatchMastDao.getInvoiceByFilter(from,to,filter.getPartyId(),filter.getUserHeadId());
        for(DispatchMast dispatchMast:dispatchMastList)
        {
            List<QualityList> qualityLists =new ArrayList<>();

            Party party =partyServiceImp.getPartyById(dispatchMast.getPartyId());
            if(party==null)
                continue;


            UserData userData = userService.getUserById(dispatchMast.getUserHeadId());
            if(userData==null)
                continue;

            String invoiceNumber=dispatchMast.getPrefix()+dispatchMast.getPostfix();
            List<GetBatchByInvoice> stockWithBatchList = dispatchDataDao.getAllStockByInvoiceNumber(invoiceNumber);
            for(GetBatchByInvoice getBatchByInvoice:stockWithBatchList)
            {
                if(getBatchByInvoice.getBatchId()==null)
                    continue;

                List<BatchData> batchDataList = stockBatchService.getBatchWithControlIdAndBatchId(getBatchByInvoice.getBatchId(),getBatchByInvoice.getStockId());
                if(batchDataList.isEmpty())
                    continue;

                StockMast stockMast = stockBatchService.getStockByStockId(getBatchByInvoice.getStockId());
                if(stockMast==null)
                    continue;

                GetQualityResponse quality= qualityServiceImp.getQualityByID(stockMast.getQualityId());
                if(quality==null)
                    continue;

                for(BatchData batchData: batchDataList)
                {
                    totalBatchMtr+=batchData.getMtr();
                    totalFinishMtr+=batchData.getFinishMtr();
                }

                QualityList qualityData= new QualityList();
                qualityData.setTotalMtr(totalBatchMtr);
                qualityData.setTotalFinishMtr(totalFinishMtr);
                qualityData.setQualityEntryId(quality.getId());
                qualityData.setQulityId(quality.getQualityId());
                qualityData.setAmt(quality.getRate()*totalFinishMtr);
                qualityData.setBatchId(getBatchByInvoice.getBatchId());
                qualityData.setRate(quality.getRate());

                qualityLists.add(qualityData);



            }

            if(!qualityLists.isEmpty()) {
                GetBill getBill = new GetBill();
                getBill.setHeadName(userData.getUserName());
                getBill.setPartyId(party.getId());
                getBill.setPartyName(party.getPartyName());
                getBill.setInvoiceNo(invoiceNumber);
                getBill.setUserHeadId(userData.getId());
                getBill.setQualityList(qualityLists);
                list.add(getBill);

            }
        }
        if(list.isEmpty())
            throw new Exception("no data found ");


        return list;

    }

    public List<DispatchMast> getDispatchByPartyId(Long id) {
        List<DispatchMast> dispatchMast = dispatchMastDao.getDipatchByPartyId(id);
        return dispatchMast;
    }


    //for receipt/preview of invoice before saving the record

    public PartyDataByInvoiceNumber getPartyWithQualityDispatchByBatchesAndStockId(CreateDispatch createDispatch) throws Exception {
        List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList= new ArrayList<>();
        List<BatchWithGr> batchWithGrList=new ArrayList<>();



        StockMast stockMast = stockBatchService.getStockById(createDispatch.getBatchAndStockIdList().get(0).getStockId());

        if(stockMast==null)
            throw new Exception("party not found");

        Party party = partyDao.findByPartyId(stockMast.getPartyId());
            if(party==null)
                throw new Exception("party not found");




        //check the all the batches and stock is belong to same party or not
        for(BatchAndStockId batchAndStockId : createDispatch.getBatchAndStockIdList()) {

            StockMast stockMastExist = stockBatchService.getStockById(batchAndStockId.getStockId());

            //check the stock is exist with batch or not

            List<BatchData> batchDataList = batchDao.getBatchesByBatchIdAndFinishMtrSaveWithoutBillGenrated(batchAndStockId.getBatchId());
            if(batchDataList.isEmpty())
                throw new Exception("no batch record found with stock");



            /*if(party.getId()!=stockMastExist.getPartyId())
                throw new Exception("may the stock or batch is not belong to the same party");*/


        }


        //for the quality record and batch gr record
        for(BatchAndStockId batchAndStockId : createDispatch.getBatchAndStockIdList()) {

            //quality record

            StockMast stockMastExist =stockBatchService.getStockById(batchAndStockId.getStockId());
            Quality quality = qualityServiceImp.getQualityByEntryId(stockMastExist.getQualityId());
            Optional<QualityName> qualityName = qualityServiceImp.getQualityNameDataById(quality.getQualityNameId());
            if(quality==null)
                throw new Exception("no quality found");

//            QualityBillByInvoiceNumber qualityBillByInvoiceNumber = batchDao.getQualityBillByStockAndBatchId(batchAndStockId.getStockId(),batchAndStockId.getBatchId(),false);


            Double totalMtr = batchDao.getTotalMtrByControlIdAndBatchId(batchAndStockId.getStockId(),batchAndStockId.getBatchId());
            Double totalFinishMtr = batchDao.getTotalFinishMtrByBatchAndStock(batchAndStockId.getBatchId(),batchAndStockId.getStockId());
            Long totalPcs = batchDao.getTotalPcsByBatchAndStockId(batchAndStockId.getStockId(),batchAndStockId.getBatchId());
            qualityBillByInvoiceNumberList.add(new QualityBillByInvoiceNumber(quality,totalFinishMtr,totalMtr,totalPcs,qualityName,batchAndStockId.getBatchId(),stockMast));



            //batch record
            List<BatchData> batchDataList = batchDao.getBatchesByBatchIdAndFinishMtrSaveWithoutBillGenrated(batchAndStockId.getBatchId());
            batchWithGrList.add(new BatchWithGr(batchDataList,batchAndStockId.getStockId(),batchAndStockId.getBatchId()));



        }


        if(batchWithGrList.isEmpty() || qualityBillByInvoiceNumberList.isEmpty())
            throw new Exception("no record found");

        PartyDataByInvoiceNumber partyDataByInvoiceNumber=new PartyDataByInvoiceNumber(party,qualityBillByInvoiceNumberList,batchWithGrList);


        if(partyDataByInvoiceNumber==null)
            throw new Exception("no data found");


        return partyDataByInvoiceNumber;


    }
}
