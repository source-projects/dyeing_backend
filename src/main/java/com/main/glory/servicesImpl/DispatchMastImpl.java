package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.request.GetDispatchCompleteDetail;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
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

    public List<GetBatchWithControlId> getBatchByParty(Long partyId)throws Exception {
        List<GetBatchWithControlId> batchDataListByParty=new ArrayList<>();
        List<StockMast> stockMastList = stockBatchService.getBatchByPartyId(partyId);

        if(stockMastList.isEmpty())
            throw new Exception("stock not found for party:"+partyId);


        for(StockMast stockMast:stockMastList)
        {
            //System.out.println(stockMast.getId());
            List<GetBatchWithControlId> batchDataList = batchDao.getAllQtyByStockAndParty(stockMast.getId());
            for(GetBatchWithControlId getBatchWithControlIdData:batchDataList)
            {
                GetBatchWithControlId getBatchWithControlId = new GetBatchWithControlId(getBatchWithControlIdData);
                batchDataListByParty.add(getBatchWithControlId);
            }

        }

        if(batchDataListByParty.isEmpty())
            throw new Exception("data not found for party:"+partyId);
        return  batchDataListByParty;
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

    }
}
