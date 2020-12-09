package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.request.GetDispatchCompleteDetail;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

            if(batch.get().getIsFinishMtrSave()==false)
                throw new Exception("The finish meter is not stored so invoice can't created");

        }

        dispatchMast.getDispatchData().forEach(e->{
            Optional<BatchData> batchData1 = batchDao.findById(e.getBatchEntryId());
            if(batchData1.isPresent())
                batchData1.get().setIsBillGenrated(true);
        });

        dispatchMastDao.save(dispatchMast);
        return true;
    }

    public GetDispatchCompleteDetail getInvoiceById(Long id) throws Exception{

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
        invoiceData.setAmt(0.0);
        invoiceData.setTotalFinishMtr(totalFinishMtr);
        invoiceData.setTotalMtr(totalMtr);
        invoiceData.setBatchId(dispatchMast.get().getBatchId());
        invoiceData.setBatchDataList(batchDataList);

        return  invoiceData;



    }
}
