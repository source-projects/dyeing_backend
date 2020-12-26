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
import com.main.glory.model.dispatch.request.BatchAndStockId;
import com.main.glory.model.dispatch.request.CreateDispatch;
import com.main.glory.model.dispatch.request.UpdateInvoice;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        List<DispatchMast> list = dispatchMastDao.findAll();

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

    public List<GetBatchByInvoice> getDispatchByInvoiceNumber(String invoiceNo) throws Exception {


        List<GetBatchByInvoice> list = dispatchDataDao.findBatchAndStockByInvoice(invoiceNo);

        if(list.isEmpty())
            throw new Exception("no data found");


        return list;

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
            else//if it is not availale then insert the new entry
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

/*
    public Boolean saveDispatch(DispatchMast dispatchMast) throws Exception {

        //Added the check to check the given info is available or not
        //throwing the serializable error that's why surround the code with try-catch block

        List<BatchData> batchData = batchDao.findByControlIdAndBatchId(dispatchMast.getStockId(),dispatchMast.getBatchId());
        if(batchData.isEmpty())
            throw new Exception("batch not found");

        for(DispatchData dispatchData: dispatchMast.getDispatchData())
        {
            Optional<BatchData> batch = batchDao.findById(dispatchData.getBatchEntryId());

            if(!batch.isPresent())
                throw new Exception("batch gr not found");

        }

        dispatchMast.getDispatchData().forEach(e->{
            Optional<BatchData> batchData1 = batchDao.findById(e.getBatchEntryId());
            if(batchData1.isPresent() && batchData1.get().getIsFinishMtrSave()==true)
                batchData1.get().setIsBillGenrated(true);
        });

        dispatchMastDao.save(dispatchMast);
        return true;
    }

    public GetDispatchCompleteDetail getDispatchCompleteDetail(Long id) throws Exception{

        Double totalMtr=0.0;
        Double totalFinishMtr=0.0;
        GetDispatchCompleteDetail invoiceData=new GetDispatchCompleteDetail();

        Optional<DispatchMast> dispatchMast = dispatchMastDao.findById(id);

        if(!dispatchMast.isPresent())
            throw new Exception("Invoice not found for id:"+id);

        List<DispatchData> dispatchData = dispatchDataDao.findByControlId(id);

        Optional<StockMast> stockMast = stockBatchService.getStockBatchById(dispatchMast.get().getStockId());

        Optional<Party> party = partyDao.findById(stockMast.get().getPartyId());

        Optional<Quality> quality = qualityDao.findById(stockMast.get().getQualityId());

        List<BatchData> batchDataList=new ArrayList<>();
        for(DispatchData batchData:dispatchData)
        {
            Optional<BatchData> batch = batchDao.findById(batchData.getBatchEntryId());
            totalMtr+=batch.get().getMtr();
            totalFinishMtr+=batch.get().getFinishMtr();
            batchDataList.add(batch.get());
        }

        invoiceData.setPartyName(party.get().getPartyName());
        invoiceData.setPartyAddress(party.get().getPartyAddress1());
        invoiceData.setGSTIN(party.get().getGSTIN());
        invoiceData.setQualityId(quality.get().getQualityId());
        invoiceData.setQualityName(quality.get().getQualityName());
        invoiceData.setChlNo(stockMast.get().getChlNo());
        invoiceData.setPcs(dispatchData.size());
        invoiceData.setAmt(totalFinishMtr*quality.get().getRate());
        invoiceData.setTotalFinishMtr(totalFinishMtr);
        invoiceData.setTotalMtr(totalMtr);
        invoiceData.setBatchId(dispatchMast.get().getBatchId());
        invoiceData.setBatchDataList(batchDataList);

        if(invoiceData.getPartyName()==null && invoiceData.getQualityId()==null)
            throw new Exception("no data found");

        return  invoiceData;



    }

    public List<BatchData> getFinishMtrListBasedOnBatchIdAndControlId(String batchId, Long controlId) throws Exception{
        List<BatchData> batchDataList = batchDao.findByControlIdAndBatchIdWithFinishMtr(batchId,controlId);

        if(batchDataList.isEmpty())
            throw new Exception("Data not found");

        return batchDataList;

    }


    public List<GetAllDispatch> getAllDisptach() throws Exception {
        List<GetAllDispatch> getAllDispatchesList=new ArrayList<>();
        List<DispatchMast> dispatchMastList = dispatchMastDao.findAll();
        for(DispatchMast dispatchMast:dispatchMastList)
        {

            if(dispatchMast!=null)
            getAllDispatchesList.add(new GetAllDispatch(dispatchMast));

        }
        if(getAllDispatchesList.isEmpty())
            throw new Exception("no data found");
        return  getAllDispatchesList;
    }

    public Boolean updateDispatchData(DispatchMast dispatchMastToUpdate) throws Exception{
        Optional<DispatchMast> dispatchMastExist=dispatchMastDao.findById(dispatchMastToUpdate.getId());
        if(dispatchMastExist.isEmpty())
            throw new Exception("no data found");

        if(dispatchMastExist.get().getIsSendToParty()==true)
        throw new Exception("Data can't be update because invoice already send to party");


        //for delete the batch gr if not coming from FE

        //##Get the data first from the list
        Map<Long,Boolean> batchGr=new HashMap<>();
        List<DispatchData> dispatchDataList = dispatchDataDao.findByControlId(dispatchMastToUpdate.getId());
        for(DispatchData dispatchData: dispatchDataList)
        {
            batchGr.put(dispatchData.getBatchEntryId(),false);
            //System.out.println(batch.getId());
        }

        //change the as per the data is coming from FE
        for(DispatchData dispatchData:dispatchMastToUpdate.getDispatchData())
        {
            //System.out.println("coming:"+batch.getId());
            if(batchGr.containsKey(dispatchData.getBatchEntryId()))
            {
                batchGr.replace(dispatchData.getBatchEntryId(),true);
            }

        }
        //##Iterate the loop and delete the record who flag is false
        for(Map.Entry<Long,Boolean> entry:batchGr.entrySet())
        {
            System.out.println(entry.getKey()+":"+entry.getValue());
            if(entry.getValue()==false)
            {
                batchDao.updateBillStatus(entry.getKey());
                dispatchDataDao.deleteByBatchEntryId(entry.getKey());
            }
        }
        dispatchMastDao.save(dispatchMastToUpdate);
        return true;

    }

    public DispatchMast getInvoiceById(Long id) throws Exception{
        Optional<DispatchMast> dispatchMastExist = dispatchMastDao.findById(id);
        if(dispatchMastExist.isEmpty())
            throw new Exception("no data found");

        return dispatchMastExist.get();

    }*/
}
