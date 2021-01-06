package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.request.*;
import com.main.glory.model.dispatch.response.BatchListWithInvoice;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service("dispatchMastImpl")
public class DispatchMastImpl {

    @Autowired
    DispatchMastDao dispatchMastDao;

    @Autowired
    DispatchDataDao dispatchDataDao;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    BatchDao batchDao;

    @Autowired
    BatchServiceImpl batchService;

    @Autowired
    PartyDao partyDao;

    @Autowired
    QualityDao qualityDao;

    public Boolean saveDispatch(CreateDispatch dispatchList) throws Exception {

        List<DispatchMast> list = dispatchMastDao.getAllInvoiceList();

        List<DispatchData> saveTheList=new ArrayList<>();
        //if the sequence is there or not
        if(!list.isEmpty())
        {
            Long invoiceNumber = dispatchMastDao.getInvoiceNumber("inv");

            invoiceNumber+=1;
           //get the invoice and merge with "inv" and then create the invoice for multiple batch

            for(BatchAndStockId createDispatch:dispatchList.getBatchAndStockIdList())
            {
                List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(createDispatch.getStockId(),createDispatch.getBatchId());

                for(BatchData batchData:batchDataList)
                {

                    if(batchData.getIsFinishMtrSave()==true && batchData.getIsBillGenrated()==false)
                    {
                        DispatchData dispatchData=new DispatchData(batchData);

                        dispatchData.setInvoiceNo("inv"+invoiceNumber);
                        dispatchData.setCreatedBy(dispatchList.getCreatedBy());

                        saveTheList.add(dispatchData);

                        batchData.setIsBillGenrated(true);
                        batchDao.save(batchData);
                    }
                }
            }


            if(saveTheList.isEmpty())
                throw new Exception("no data found");
            //save the complete batch with gr list to the dispatch data with same invoice number
            dispatchDataDao.saveAll(saveTheList);


            //increament the the invoice number to dispatch mast
            DispatchMast dispatchMast =new DispatchMast();
            dispatchMast.setPrefix("inv");
            dispatchMast.setPostfix(invoiceNumber);
            dispatchMastDao.save(dispatchMast);
            return true;

        }
        else
        {
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

            for(BatchAndStockId createDispatch:dispatchList.getBatchAndStockIdList())
            {
                List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(createDispatch.getStockId(),createDispatch.getBatchId());

                for(BatchData batchData:batchDataList)
                {

                    if(batchData.getIsFinishMtrSave()==true && batchData.getIsBillGenrated()==false)
                    {
                        DispatchData dispatchData=new DispatchData(batchData);

                        dispatchData.setInvoiceNo("inv"+invoiceNumber);

                        dispatchData.setCreatedBy(dispatchList.getCreatedBy());
                        saveTheList.add(dispatchData);
                        batchData.setIsBillGenrated(true);
                        batchDao.save(batchData);
                    }
                }
            }



            if(saveTheList.isEmpty())
                throw new Exception("no data found");
            //save the complete batch with gr list to the dispatch data with same invoice number
            dispatchDataDao.saveAll(saveTheList);

            //increament the the invoice number to dispatch mast
            DispatchMast dispatchMast =new DispatchMast();
            dispatchMast.setPrefix("inv");
            dispatchMast.setPostfix(invoiceNumber);
            dispatchMastDao.save(dispatchMast);

            return true;

        }


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
        List<DispatchData> dispatchList =dispatchDataDao.findAll();


        List<String> invoiceNumber=new ArrayList<>();

        if (dispatchList.isEmpty())
            throw new Exception("no data found");
        for(DispatchData dispatchData:dispatchList)
        {

            if(!invoiceNumber.contains(dispatchData.getInvoiceNo()))
            {
                invoiceNumber.add(dispatchData.getInvoiceNo());
                GetAllDispatch getAllDispatch=new GetAllDispatch(dispatchData);
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
                Optional<BatchData> batchData=batchDao.findById(dispatchData.getBatchEntryId());
                if(batchData.isPresent())
                {
                    WT+=batchData.get().getWt();
                    MTR+=batchData.get().getMtr();
                    totalFinishMtr=batchData.get().getFinishMtr();
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
        Optional<Party> party=partyDao.findById(stockMast.getPartyId());

        if(!party.isPresent())
            throw new Exception("no data found");



        PartyWithBatchByInvoice partyWithBatchByInvoice =new PartyWithBatchByInvoice(batchWithTotalMTRandFinishMTRList,party.get());

        return partyWithBatchByInvoice;

    }

    public Boolean updateDispatch(UpdateInvoice updateInvoice) throws Exception{


        //for the new data
        List<DispatchData> saveTheList=new ArrayList<>();

        //update invoice
        Map<String,Long> availbleBatchInInvoice = new HashMap<>();

        Map<String,Long> comingBatches = new HashMap<>();

        List<GetBatchByInvoice> availableBatches = dispatchDataDao.findBatchAndStockByInvoice(updateInvoice.getInvoiceNo());

        if(availableBatches.isEmpty())
            throw new Exception("this invoice is empty create new one");

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
                if(availbleBatchInInvoice.get(entry.getKey()).equals(entry.getValue()) && availbleBatchInInvoice.containsKey(entry.getKey()))
                {
                    availbleBatchInInvoice.remove(entry.getKey(),entry.getValue());
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

                        dispatchData.setCreatedBy(dispatchData.getCreatedBy());

                        dispatchData.setUpdatedBy(dispatchData.getUpdatedBy());

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

    public PartyDataByInvoiceNumber getPartyWithQualityDispatchBy(String invoiceNo) throws Exception {

        List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList= new ArrayList<>();



        String invoiceExist = dispatchDataDao.findByInvoiceNo(invoiceNo);
        if(invoiceExist == null || invoiceExist=="")
            throw new Exception("no data found");


        //quality list

        List<GetBatchByInvoice> batchList = dispatchDataDao.findBatchAndStockByInvoiceWithoutStatus(invoiceExist);
        for(GetBatchByInvoice batch:batchList)
        {

            Double totalMtr=0.0;
            Double finishMtr=0.0;
            Long pcs=0l;
            Double amt=0.0;

            System.out.println(batch.getStockId());
            StockMast stockMast=stockBatchService.getStockById(batch.getStockId());
            Optional<Quality> quality = qualityDao.findById(stockMast.getQualityId());

            QualityBillByInvoiceNumber qualityBillByInvoiceNumber=new QualityBillByInvoiceNumber(quality.get());

            List<DispatchData> dispatchDataList=dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),batch.getBatchId(), invoiceNo);

            for(DispatchData invoiceBatch:dispatchDataList)
            {

                Optional<BatchData> batchData=batchDao.findById(invoiceBatch.getBatchEntryId());
                totalMtr+=batchData.get().getMtr();
                finishMtr+=batchData.get().getFinishMtr();
                pcs++;

            }

            //Count the total amt based on quality rate and total finish mtr
            amt=quality.get().getRate()*finishMtr;


            //set the quality with batch data
            qualityBillByInvoiceNumber.setBatchId(batch.getBatchId());
            qualityBillByInvoiceNumber.setTotalMtr(totalMtr);
            qualityBillByInvoiceNumber.setFinishMtr(finishMtr);
            qualityBillByInvoiceNumber.setPcs(pcs);
            qualityBillByInvoiceNumber.setAmt(amt);


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

        return partyDataByInvoiceNumber;


    }

/*
    public List<InvoiceWithBatch> getInvoiceListBasedOnFilter(GetInvoiceBasedOnFilter filter) throws Exception {

        try {
            if (filter.getPartyId() == null && filter.getFromDate().isEmpty() && filter.getToDate().isEmpty())
                throw new Exception("please enter valid data");


            Date fromDate = null;
            Date toDate = null;

            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                    "yyyy-MM-dd");

            if (filter.getFromDate() != "")
                fromDate = datetimeFormatter1.parse(filter.getFromDate());

            if (filter.getToDate() != "")
                toDate = datetimeFormatter1.parse(filter.getToDate());

            List<InvoiceWithBatch> invoiceWithBatchList = null;


            List<BatchListWithInvoice> dispatchDataList = dispatchDataDao.getAllDispatchList(toDate, fromDate);

            if(dispatchDataList.isEmpty())
                throw new Exception("no invoice found");

            for (BatchListWithInvoice batchListWithInvoice : dispatchDataList) {
                System.out.println(batchListWithInvoice.getBatchId());
                System.out.println(batchListWithInvoice.getInvoicNo());
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }*/
}
