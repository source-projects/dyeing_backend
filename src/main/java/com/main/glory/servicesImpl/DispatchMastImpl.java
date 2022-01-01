package com.main.glory.servicesImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.glory.model.StockDataBatchData.response.GetAllMergeBatchId;
import com.main.glory.model.StockDataBatchData.response.PendingBatchMast;
import com.main.glory.model.dispatch.DispatchFilter;
import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.admin.InvoiceSequenceDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.filters.Filter;
import com.main.glory.filters.FilterResponse;
import com.main.glory.filters.QueryOperator;
import com.main.glory.filters.SpecificationManager;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.admin.InvoiceSequence;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.bill.GetBill;
import com.main.glory.model.dispatch.bill.QualityList;
import com.main.glory.model.dispatch.request.*;
import com.main.glory.model.dispatch.response.*;
import com.main.glory.model.dispatch.response.report.ConsolidatedBillDataForExcel;
import com.main.glory.model.dispatch.response.report.ConsolidatedBillDataForPDF;
import com.main.glory.model.dispatch.response.report.ConsolidatedBillMast;
import com.main.glory.model.machine.request.PaginatedData;
import com.main.glory.model.party.Party;
import com.main.glory.model.paymentTerm.response.MonthlyDispatchPendingReport;
import com.main.glory.model.paymentTerm.response.MonthlyDispatchPendingReportData;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.UserData;
import com.main.glory.services.FilterService;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service("dispatchMastImpl")
public class DispatchMastImpl {

    @Autowired
    PaymentTermImpl paymentTermService;

    ConstantFile constantFile;

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
    SpecificationManager<DispatchMast> specificationManager;
    @Autowired
    FilterService<DispatchMast, DispatchMastDao> filterService;

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

    @Autowired
    AdminServciceImpl adminServcice;

    @Autowired
    QualityNameDao qualityNameDao;

    public Long saveDispatch(CreateDispatch dispatchList, Long userId) throws Exception {

        //another method is created createDispatchForPchallan()
        return null;
    }

    public List<BatchWithTotalMTRandFinishMTR> getBatchByParty(Long partyId) throws Exception {
        List<BatchWithTotalMTRandFinishMTR> list = new ArrayList<>();

        List<BatchWithTotalMTRandFinishMTR> batchDataListByParty = new ArrayList<>();
        List<StockMast> stockMastList = stockBatchService.getStockListByParty(partyId);
        Party party = partyServiceImp.getPartyById(partyId);
        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);

        /*
         * if(stockMastList.size()<=0) return batchDataListByParty;
         */

        for (StockMast stockMast : stockMastList) {
            // System.out.println(stockMast.getId());
            List<BatchWithTotalMTRandFinishMTR> batchDataList = batchDao
                    .getAllBatchByStockIdWithTotalFinishMtr(stockMast.getId());
            for (BatchWithTotalMTRandFinishMTR getBatchWithControlIdData : batchDataList) {
                BatchWithTotalMTRandFinishMTR getBatchWithControlId = new BatchWithTotalMTRandFinishMTR(
                        getBatchWithControlIdData);
                Quality quality = qualityServiceImp.getQualityByStockId(getBatchWithControlId.getControlId());
                if (quality != null) {
                    QualityName qualityName = quality.getQualityName();
                    getBatchWithControlId.setRate(quality.getRate());
                    getBatchWithControlId.setQualityId(quality.getQualityId());
                    getBatchWithControlId.setQualityName(qualityName.getQualityName());
                    getBatchWithControlId.setQualityEntryId(quality.getId());
                }
                batchDataListByParty.add(getBatchWithControlId);
            }

        }

        // check the pending bill amt
        /*
         * Double pendingBillAmt =
         * paymentTermService.getTotalPendingAmtByPartyId(partyId);
         * if(pendingBillAmt!=null) { if (!batchDataListByParty.isEmpty()) {
         * batchDataListByParty.forEach(e -> { if (pendingBillAmt >
         * party.getCreditLimit()) { list.add(new
         * BatchWithTotalMTRandFinishMTRWithPendingBill(e, true)); } else { list.add(new
         * BatchWithTotalMTRandFinishMTRWithPendingBill(e, false)); } });
         *
         * } }
         */

        /*
         * if(batchDataListByParty.isEmpty()) throw new
         * Exception("data not found for party:"+partyId);
         */
        return batchDataListByParty;
    }

    public List<GetAllDispatch> getAllDisptach(String signByParty) throws Exception {

        List<GetAllDispatch> dispatchDataList = new ArrayList<>();

        List<DispatchMast> dispatchList = null;
        if (signByParty == null || signByParty.isEmpty()) {
            dispatchList = dispatchMastDao.getAllInvoiceList();
        } else
            dispatchList = dispatchMastDao.getAllInvoiceListBySignByParty(Boolean.valueOf(signByParty));

        for (DispatchMast dispatchMast : dispatchList) {
            List<BatchWithTotalMTRandFinishMTR> batchList = new ArrayList<>();

            if (dispatchMast == null)
                continue;

            GetAllDispatch getAllDispatch = new GetAllDispatch(dispatchMast);

            Party party = partyServiceImp.getPartyById(dispatchMast.getParty().getId());
            if (party == null)
                continue;
            // get the batch data

            List<GetBatchByInvoice> batchListWithInvoiceList = dispatchDataDao
                    .getAllStockByInvoiceNumber(dispatchMast.getPostfix());

            for (GetBatchByInvoice batch : batchListWithInvoiceList) {
                // list of batches
                BatchWithTotalMTRandFinishMTR batchWithTotalMTRandFinishMTR = batchDao
                        .getAllBatchWithTotalMtrAndTotalFinishMtr(batch.getBatchId(), batch.getStockId());
                batchList.add(batchWithTotalMTRandFinishMTR);
            }
            getAllDispatch
                    .setSignByParty(dispatchMast.getSignByParty() == null ? false : dispatchMast.getSignByParty());
            getAllDispatch.setPartyId(party.getId());
            getAllDispatch.setPartyName(party.getPartyName());
            getAllDispatch.setBatchList(batchList);
            getAllDispatch.setNetAmt(dispatchMast.getNetAmt());
            Double mtr = 0.0;
            Double finish = 0.0;

            mtr = batchList.stream().mapToDouble(q -> q.getMTR()).sum();
            finish = batchList.stream().mapToDouble(q -> q.getTotalFinishMtr()).sum();
            getAllDispatch.setTotalMtr(StockBatchServiceImpl.changeInFormattedDecimal(mtr));
            getAllDispatch.setFinishMtr(StockBatchServiceImpl.changeInFormattedDecimal(finish));

            dispatchDataList.add(getAllDispatch);

        }

        return dispatchDataList;
    }

    public FilterResponse<GetAllDispatch> getAllDisptach(GetBYPaginatedAndFiltered requestParam) throws Exception {
        List<GetAllDispatch> dispatchDataList = new ArrayList<>();
        Pageable pageable = filterService.getPageable(requestParam.getData());
        List<Filter> filters = requestParam.getData().getParameters();
        HashMap<String, List<String>> subModelCase = new HashMap<String, List<String>>();
        subModelCase.put("invoiceNo", new ArrayList<String>(Arrays.asList("postfix")));
        subModelCase.put("partyName", new ArrayList<String>(Arrays.asList("party", "partyName")));

        // List<DispatchData> dispatchList = dispatchDataDao.getAllDispatch();
        String signByParty = requestParam.getSignByParty();
        Page queryResponse = null;

        List<DispatchMast> dispatchList = null;
        if (signByParty == null || signByParty.isEmpty()) {
            Specification<DispatchMast> spec = specificationManager.getSpecificationFromFilters(filters,
                    requestParam.getData().isAnd, subModelCase);
            queryResponse = dispatchMastDao.findAll(spec, pageable);
        } else {
            filters.add(
                    new Filter(new ArrayList<String>(Arrays.asList("signByParty")), QueryOperator.EQUALS, signByParty));
            Specification<DispatchMast> spec = specificationManager.getSpecificationFromFilters(filters,
                    requestParam.getData().isAnd, subModelCase);
            queryResponse = dispatchMastDao.findAll(spec, pageable);

        }

        dispatchList = queryResponse.getContent();

        /*
         * if (dispatchList.isEmpty()) throw new Exception("no data found");
         */
        for (DispatchMast dispatchMast : dispatchList) {
            List<BatchWithTotalMTRandFinishMTR> batchList = new ArrayList<>();

            /*
             * if (!invoiceNumber.contains(dispatchMast.getDispatchData())) {
             * invoiceNumber.add(dispatchMast.getDispatchData().toString());
             */
            if (dispatchMast == null)
                continue;

            GetAllDispatch getAllDispatch = new GetAllDispatch(dispatchMast);
            // System.out.println("invoice:" + dispatchData.getInvoiceNo());
            /*
             * DispatchMast dispatchMast =
             * dispatchMastDao.getDataByInvoiceNumber(dispatchData.getPostfix());
             *
             * if (dispatchMast == null) continue;
             */

            Party party = dispatchMast.getParty();
            if (party == null)
                continue;
            // get the batch data

            List<GetBatchByInvoice> batchListWithInvoiceList = dispatchDataDao
                    .getAllStockByInvoiceNumberWithPchallen(dispatchMast.getPostfix());
            //System.out.println("length of batchList-" + batchListWithInvoiceList.size());
            for (GetBatchByInvoice batch : batchListWithInvoiceList) {
                // list of batches
                BatchWithTotalMTRandFinishMTR batchWithTotalMTRandFinishMTR = dispatchDataDao
                        .getAllBatchWithTotalMtrAndTotalFinishMtr(batch.getBatchId(), batch.getStockId(), batch.getPchallanRef(), dispatchMast.getPostfix());
                batchList.add(batchWithTotalMTRandFinishMTR);
            }
            System.out.println("length of batchList-" + batchList.size());
            getAllDispatch
                    .setSignByParty(dispatchMast.getSignByParty() == null ? false : dispatchMast.getSignByParty());
            getAllDispatch.setPartyId(party.getId());
            getAllDispatch.setPartyName(party.getPartyName());
            getAllDispatch.setBatchList(batchList);
            getAllDispatch.setNetAmt(dispatchMast.getNetAmt());
            Double mtr = 0.0;
            Double finish = 0.0;
            /*
             * for (BatchWithTotalMTRandFinishMTR batchWithTotalMTRandFinishMTR : batchList)
             * { mtr += batchWithTotalMTRandFinishMTR.getMTR(); finish +=
             * batchWithTotalMTRandFinishMTR.getTotalFinishMtr(); }
             */
            mtr = batchList.stream().mapToDouble(q -> q.getMTR()).sum();
            finish = batchList.stream().mapToDouble(q -> q.getTotalFinishMtr()).sum();
            getAllDispatch.setTotalMtr(StockBatchServiceImpl.changeInFormattedDecimal(mtr));
            getAllDispatch.setFinishMtr(StockBatchServiceImpl.changeInFormattedDecimal(finish));

            dispatchDataList.add(getAllDispatch);
            // }

        }

        /*
         * if (signByParty.equals("true")) { dispatchDataList =
         * dispatchDataList.stream().filter(x -> x.getSignByParty() ==
         * true).collect(Collectors.toList()); } else if (signByParty.equals("false")) {
         * dispatchDataList = dispatchDataList.stream().filter(x -> x.getSignByParty()
         * == false).collect(Collectors.toList()); }
         */

        FilterResponse<GetAllDispatch> response = new FilterResponse<GetAllDispatch>(dispatchDataList,
                queryResponse.getNumber(), queryResponse.getNumberOfElements(), (int) queryResponse.getTotalElements());
        return response;

    }

    public PartyWithBatchByInvoice getDispatchByInvoiceNumber(String invoiceNo) throws Exception {

        List<BatchWithTotalMTRandFinishMTR> batchWithTotalMTRandFinishMTRList = new ArrayList<>();

        List<GetBatchByInvoice> list = dispatchDataDao.findBatchAndStockByInvoice(invoiceNo);

        /*
         * if(list.isEmpty()) throw new Exception("no data found");
         */

        for (GetBatchByInvoice batch : list) {
            BatchWithTotalMTRandFinishMTR batchWithTotalMTRandFinishMTR = new BatchWithTotalMTRandFinishMTR();
            batchWithTotalMTRandFinishMTR.setBatchId(batch.getBatchId());
            batchWithTotalMTRandFinishMTR.setControlId(batch.getStockId());

            List<DispatchData> dispatchDataList = dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),
                    batch.getBatchId(), invoiceNo);
            Double WT = 0.0;
            Double MTR = 0.0;
            Double totalFinishMtr = 0.0;
            Long totalPcs = 0l;

            for (DispatchData dispatchData : dispatchDataList) {
                BatchData batchData = dispatchData.getBatchData();
                if (batchData.getWt() != null) {
                    WT += batchData.getWt();
                    MTR += batchData.getMtr();
                    totalFinishMtr += batchData.getFinishMtr();
                    totalPcs++;

                }
            }
            if (!dispatchDataList.isEmpty()) {
                Quality quality = qualityDao.getqualityById(dispatchDataList.get(0).getQuality().getId());
                if (quality != null) {
                    QualityName qualityName = quality.getQualityName();
                    batchWithTotalMTRandFinishMTR.setQualityEntryId(quality.getId());
                    batchWithTotalMTRandFinishMTR.setQualityId(quality.getQualityId());
                    batchWithTotalMTRandFinishMTR.setQualityName(qualityName.getQualityName());
                    batchWithTotalMTRandFinishMTR.setRate(dispatchDataList.get(0).getQualityRate());

                }
            }
            batchWithTotalMTRandFinishMTR.setTotalFinishMtr(totalFinishMtr);
            batchWithTotalMTRandFinishMTR.setTotalPcs(totalPcs);
            batchWithTotalMTRandFinishMTR.setWT(stockBatchService.changeInFormattedDecimal(WT));
            batchWithTotalMTRandFinishMTR.setMTR(stockBatchService.changeInFormattedDecimal(MTR));
            // Optional<Party>
            // party=partyDao.findById(dispatchDataList.get(0).getStockId());
            // if(party.isPresent())
            // throw new Exception("no party data found");
            // batchWithTotalMTRandFinishMTR.setPartyId(party.get().getId());
            // batchWithTotalMTRandFinishMTR.setPartyName(party.get().getPartyName());
            batchWithTotalMTRandFinishMTRList.add(batchWithTotalMTRandFinishMTR);
        }

        StockMast stockMast = stockBatchService.getStockById(list.get(0).getStockId());
        Party party = partyDao.findByPartyId(stockMast.getParty().getId());

        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);

        PartyWithBatchByInvoice partyWithBatchByInvoice = new PartyWithBatchByInvoice(batchWithTotalMTRandFinishMTRList,
                party);

        // get the discount from party
        DispatchMast dispatchMast = dispatchMastDao.getDispatchMastByInvoiceNo(invoiceNo);
        if (dispatchMast != null) {
            partyWithBatchByInvoice.setPercentageDiscount(dispatchMast.getPercentageDiscount());
            partyWithBatchByInvoice.setRemark(dispatchMast.getRemark());
        }
        // status
        partyWithBatchByInvoice.setIsSendToParty(dispatchDataDao.getSendToPartyFlag(invoiceNo));

        return partyWithBatchByInvoice;

    }

    /*
     * public Boolean updateDispatch(UpdateInvoice updateInvoice) throws Exception{
     *
     *
     * //check the invoice number is exist String
     * invoiceExist=dispatchDataDao.findByInvoiceNo(updateInvoice.getInvoiceNo());
     * if(invoiceExist==null) throw new
     * Exception("no invoice found for invoice:"+updateInvoice.getInvoiceNo());
     *
     * //for the new data List<DispatchData> saveTheList=new ArrayList<>();
     *
     * //update invoice Map<String,Long> availbleBatchInInvoice = new HashMap<>();
     *
     * Map<String,Long> comingBatches = new HashMap<>();
     *
     * List<GetBatchByInvoice> availableBatches =
     * dispatchDataDao.findBatchAndStockByInvoice(updateInvoice.getInvoiceNo());
     *
     * if(availableBatches.isEmpty()) throw new
     * Exception("this invoice is empty create new one");
     *
     *//*
     *
     * if the batch list coming empty with the given invoice,so
     *
     * considering that invoice should be deleted
     *
     * delete this invoice
     *
     *//*
     *
     * if(updateInvoice.getBatchAndStockIdList().isEmpty()) { //get the batch entry
     * and change the flag List<DispatchData>
     * dispatchDataList=dispatchDataDao.getBatchByInvoiceNo(updateInvoice.
     * getInvoiceNo());
     *
     * for(DispatchData dispatchData:dispatchDataList) { //change the batch status
     * BatchData
     * batchData=batchDao.findByBatchEntryId(dispatchData.getBatchEntryId());
     * batchData.setIsBillGenrated(false); batchDao.save(batchData); } //delete all
     * the batches from the dispatch
     * dispatchDataDao.deleteByInvoiceNo(updateInvoice.getInvoiceNo());
     *
     * //delete the master entry also
     * dispatchMastDao.deleteByInvoicePostFix(Long.parseLong(updateInvoice.
     * getInvoiceNo().substring(3))); return true;
     *
     * }
     *
     *
     *
     *
     *
     * //coming batch list for(BatchAndStockId
     * createDispatch:updateInvoice.getBatchAndStockIdList()) {
     * comingBatches.put(createDispatch.getBatchId(),createDispatch.getStockId()); }
     *
     * //available batch list for(GetBatchByInvoice
     * getBatchByInvoice:availableBatches) {
     * availbleBatchInInvoice.put(getBatchByInvoice.getBatchId(),getBatchByInvoice.
     * getStockId()); }
     *
     * //Update functionality if coming batch is not in avaialable in the avaialable
     * batch list for (Map.Entry<String,Long> entry : comingBatches.entrySet()) {
     *
     *
     * //System.out.println(entry.getKey()); if(!availbleBatchInInvoice.isEmpty()) {
     *
     * //if the data is found then remove from the available batch list to delete
     * the batch later if(availbleBatchInInvoice.containsKey(entry.getKey())) {
     * if(availbleBatchInInvoice.get(entry.getKey()).equals(entry.getValue()))
     * availbleBatchInInvoice.remove(entry.getKey(),entry.getValue()); } else { //if
     * the batch is not availbale in existing list //get the batch data
     * List<BatchData> batchDataList =
     * batchDao.findByControlIdAndBatchId(entry.getValue(),entry.getKey());
     *
     * for(BatchData batchData:batchDataList) {
     *
     * if(batchData.getIsFinishMtrSave()==true &&
     * batchData.getIsBillGenrated()==false) { DispatchData dispatchData=new
     * DispatchData(batchData);
     *
     * dispatchData.setInvoiceNo(updateInvoice.getInvoiceNo());
     *
     * dispatchData.setCreatedBy(updateInvoice.getCreatedBy());
     *
     * dispatchData.setUpdatedBy(updateInvoice.getUpdatedBy());
     *
     * saveTheList.add(dispatchData); batchData.setIsBillGenrated(true);
     * batchDao.save(batchData); } } dispatchDataDao.saveAll(saveTheList);
     *
     * }
     *
     *
     * } else//if batch is not availale then insert the new entry { //get the batch
     * data List<BatchData> batchDataList =
     * batchDao.findByControlIdAndBatchId(entry.getValue(),entry.getKey());
     *
     * for(BatchData batchData:batchDataList) {
     *
     * if(batchData.getIsFinishMtrSave()==true &&
     * batchData.getIsBillGenrated()==false) { DispatchData dispatchData=new
     * DispatchData(batchData);
     *
     * dispatchData.setInvoiceNo(updateInvoice.getInvoiceNo());
     *
     * dispatchData.setCreatedBy(updateInvoice.getCreatedBy());
     *
     * dispatchData.setUpdatedBy(updateInvoice.getUpdatedBy());
     *
     * saveTheList.add(dispatchData); batchData.setIsBillGenrated(true);
     * batchDao.save(batchData); } } dispatchDataDao.saveAll(saveTheList); }
     *
     *
     * }
     *
     *
     *
     * //now remove the batch which is not coming from the FE and remain it in
     * available batch list of invoice if(!availbleBatchInInvoice.isEmpty()) { for
     * (Map.Entry<String,Long> entry : availbleBatchInInvoice.entrySet()) {
     * List<BatchData>
     * batchDataList=batchDao.findByControlIdAndBatchId(entry.getValue(),entry.
     * getKey()); for(BatchData batchData:batchDataList) {
     * batchData.setIsBillGenrated(false); batchDao.save(batchData);
     * dispatchDataDao.deleteByBatchEntryId(batchData.getId()); }
     *
     * } return true;
     *
     * } else return true;
     *
     *
     * }
     */

    public Boolean updateDispatchStatus(String invoiceNo) throws Exception {

        String invoiceNumberExist = dispatchDataDao.findByInvoiceNo(invoiceNo);
        if (invoiceNumberExist == null)
            throw new Exception("no invoice number found");

        dispatchDataDao.updateStatus(invoiceNumberExist);
        return true;
    }

    public PartyDataByInvoiceNumber checkInvoiceDataIsAvailable(String invoiceNo) throws Exception {

        List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList = new ArrayList<>();

        String invoiceExist = dispatchDataDao.findByInvoiceNo(invoiceNo);

        // quality list

        List<GetBatchByInvoice> batchList = dispatchDataDao.findBatchAndStockByInvoiceWithoutStatus(invoiceExist);

        for (GetBatchByInvoice batch : batchList) {
            if (batchList.isEmpty())
                continue;

            Double totalMtr = 0.0;
            Double finishMtr = 0.0;
            Long pcs = 0l;
            Double amt = 0.0;

            // System.out.println(batch.getStockId());
            StockMast stockMast = stockBatchService.getStockById(batch.getStockId());
            Optional<Quality> quality = qualityDao.findById(stockMast.getQuality().getId());

            if (quality.isEmpty())
                throw new Exception("no quality found");

            QualityBillByInvoiceNumber qualityBillByInvoiceNumber = new QualityBillByInvoiceNumber(quality.get());

            List<DispatchData> dispatchDataList = dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),
                    batch.getBatchId(), invoiceNo);

            for (DispatchData invoiceBatch : dispatchDataList) {

                BatchData batchData = invoiceBatch.getBatchData();
                if (batchData.getMtr() != null && batchData.getFinishMtr() != null) {
                    totalMtr += batchData.getMtr();
                    finishMtr += batchData.getFinishMtr();
                    pcs++;
                }

            }

            // Count the total amt based on quality rate and total finish mtr
            if (quality.get().getRate() != null && finishMtr > 0.0)
                amt = quality.get().getRate() * finishMtr;

            // set the quality with batch data
            qualityBillByInvoiceNumber.setBatchId(batch.getBatchId());
            qualityBillByInvoiceNumber.setTotalMtr(totalMtr);
            qualityBillByInvoiceNumber.setFinishMtr(finishMtr);
            qualityBillByInvoiceNumber.setPcs(pcs);
            qualityBillByInvoiceNumber.setAmt(amt);

            qualityBillByInvoiceNumber.setPChalNo(stockMast.getChlNo());
            qualityBillByInvoiceNumberList.add(qualityBillByInvoiceNumber);
        }

        // for batch list

        List<GetBatchByInvoice> batchList2 = dispatchDataDao.findBatchAndStockByInvoiceWithoutStatus(invoiceExist);

        List<BatchWithGr> batchWithGrList = new ArrayList<>();
        for (GetBatchByInvoice batch : batchList2) {

            BatchWithGr batchWithGr = new BatchWithGr(batch);
            List<BatchData> batchDataList = new ArrayList<>();

            // batches data

            List<DispatchData> dispatchDataList = dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),
                    batch.getBatchId(), invoiceNo);

            for (DispatchData invoiceBatch : dispatchDataList) {

                BatchData batchData = invoiceBatch.getBatchData();
                batchDataList.add(batchData);
            }
            batchWithGr.setBatchDataList(batchDataList);
            batchWithGrList.add(batchWithGr);

        }

        // for party data
        if (!batchList.isEmpty()) {
            StockMast stockMast = stockBatchService.getStockById(batchList.get(0).getStockId());
            Optional<Party> party = partyDao.findById(stockMast.getParty().getId());

            PartyDataByInvoiceNumber partyDataByInvoiceNumber = new PartyDataByInvoiceNumber(party.get(),
                    qualityBillByInvoiceNumberList, batchWithGrList);

            /*
             * if(partyDataByInvoiceNumber==null) throw new Exception("no data found");
             */
            return partyDataByInvoiceNumber;
        }

        return null;

    }

    public PartyDataByInvoiceNumber getPartyWithQualityDispatchBy(String invoiceNo) throws Exception {

        List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList = new ArrayList<>();

        DispatchMast dispatchMast = dispatchMastDao.getDataByInvoiceNumber(invoiceNo);
        String invoiceExist = dispatchDataDao.findByInvoiceNo(invoiceNo);
        if (invoiceExist == null || invoiceExist == "")
            throw new Exception(ConstantFile.Dispatch_Found);

        // quality list

        List<GetBatchByInvoice> batchList = dispatchDataDao.findBatchAndStockByInvoiceWithoutStatus(invoiceExist);

        for (GetBatchByInvoice batch : batchList) {
            if (batchList.isEmpty())
                continue;

            Double totalMtr = 0.0;
            Double finishMtr = 0.0;
            Long pcs = 0l;
            Double amt = 0.0;

            // System.out.println(batch.getStockId());
            StockMast stockMast = stockBatchService.getStockById(batch.getStockId());
            Optional<Quality> quality = qualityDao.findById(stockMast.getQuality().getId());

            if (quality.isEmpty())
                throw new Exception(ConstantFile.Quality_Data_Not_Exist);

            QualityBillByInvoiceNumber qualityBillByInvoiceNumber = new QualityBillByInvoiceNumber(quality.get());
            QualityName qualityName = quality.get().getQualityName();
            qualityBillByInvoiceNumber.setQualityName(qualityName.getQualityName());

            List<DispatchData> dispatchDataList = dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),
                    batch.getBatchId(), invoiceNo);

            String isMergeBatchId = "";
            for (DispatchData invoiceBatch : dispatchDataList) {
                // System.out.println("invoic entry:"+invoiceBatch.getBatchEntryId());
                BatchData batchData = invoiceBatch.getBatchData();
                if (batchData.getMtr() != null && batchData.getFinishMtr() != null) {
                    totalMtr += batchData.getMtr();
                    finishMtr += batchData.getFinishMtr();
                    pcs++;
                }

            }

            /*
             * //get the shade rate as well ProductionPlan
             * productionPlan=productionPlanService.getProductionDataByBatchAndStock(batch.
             * getBatchId(),batch.getStockId()); if(productionPlan==null) continue;
             *//*
             * Optional<ShadeMast> shadeMast = null; if(productionPlan.getShadeId()!=null)
             * shadeService.getShadeMastById(productionPlan.getShadeId());
             *//*
             */

            Double shadeRate = 0.0;
            /*
             * if(shadeMast.isPresent()) { shadeRate = shadeMast.get().getExtraRate(); }
             */

            // get the rate

            Double rate = dispatchDataList.get(0).getQualityRate();
            shadeRate = dispatchDataList.get(0).getShadeRate();

            // set the quality with batch data
            qualityBillByInvoiceNumber.setBatchId(batch.getBatchId());
            qualityBillByInvoiceNumber.setBatchId(batch.getBatchId());
            if (dispatchDataList.get(0).getBillingUnit().equalsIgnoreCase("meter")) {
                qualityBillByInvoiceNumber.setTotalMtr(totalMtr);
                qualityBillByInvoiceNumber.setFinishMtr(finishMtr);
            } else {

                totalMtr = (totalMtr / 100) * dispatchDataList.get(0).getWtPer100m();
                finishMtr = (finishMtr / 100) * dispatchDataList.get(0).getWtPer100m();
                qualityBillByInvoiceNumber.setTotalMtr(totalMtr);
                qualityBillByInvoiceNumber.setFinishMtr(finishMtr);
            }
            // Count the total amt based on quality rate and total finish mtr
            amt = (rate + shadeRate) * finishMtr;

            qualityBillByInvoiceNumber.setPcs(pcs);
            qualityBillByInvoiceNumber.setAmt(amt);
            qualityBillByInvoiceNumber.setRate(rate);
            qualityBillByInvoiceNumber.setShadeRate(shadeRate);
            qualityBillByInvoiceNumber.setPChalNo(stockMast.getChlNo());
            qualityBillByInvoiceNumberList.add(qualityBillByInvoiceNumber);
        }

        // for batch list

        List<GetBatchByInvoice> batchList2 = dispatchDataDao.findBatchAndStockByInvoiceWithoutStatus(invoiceExist);

        List<BatchWithGr> batchWithGrList = new ArrayList<>();
        for (GetBatchByInvoice batch : batchList2) {

            BatchWithGr batchWithGr = new BatchWithGr(batch);
            List<BatchData> batchDataList = new ArrayList<>();

            // batches data

            List<DispatchData> dispatchDataList = dispatchDataDao.findByBatchIdAndStockIdAndInviceNo(batch.getStockId(),
                    batch.getBatchId(), invoiceNo);

            for (DispatchData invoiceBatch : dispatchDataList) {
                BatchData batchData = invoiceBatch.getBatchData();
                batchDataList.add(batchData);
            }


            // change the list respone if the size is greater than 30 object

            batchDataList.sort(Comparator.comparing(BatchData::getSequenceId));
            if (batchDataList.size() > 30) {

                int i = 0;
                int object = batchDataList.size() / 30;
                int remainingGrFrom = batchDataList.size() % 30;
                int startIndex = 0;
                int limit = 30;
                // divide the entire object because of gr is greter than the limit

                if (remainingGrFrom > 0) {
                    object++;
                }
                for (int x = 1; x < object; x++) {
                    // to index value is not going to push into the list
                    // it mean if the start index is 0 and limit is 30 then the 30's index value is
                    // not going to push into the lisy
                    // only from 0-29 object are going to store in list
                    List<BatchData> newBatchList = batchDataList.subList(startIndex, limit);
                    BatchWithGr newBatchWithGr = new BatchWithGr(batch);
                    newBatchWithGr.setBatchDataList(newBatchList);
                    startIndex = limit;
                    limit += 30;
                    batchWithGrList.add(newBatchWithGr);

                }

                // for remaining gr
                if (remainingGrFrom > 0) {
                    List<BatchData> newBatchList = batchDataList.subList(startIndex, batchDataList.size());
                    BatchWithGr newBatchWithGr = new BatchWithGr(batch);
                    newBatchWithGr.setBatchDataList(newBatchList);
                    batchWithGrList.add(newBatchWithGr);
                }

                // batchWithGr.setBatchDataList(batchDataList);
                // batchWithGrList.add(batchWithGr);
            } else {
                // for perfect gr lst which is 30
                batchWithGr.setBatchDataList(batchDataList);
                batchWithGrList.add(batchWithGr);
            }

        }

        // for party data
        StockMast stockMast = stockBatchService.getStockById(batchList.get(0).getStockId());
        Optional<Party> party = partyDao.findById(stockMast.getParty().getId());

        PartyDataByInvoiceNumber partyDataByInvoiceNumber = new PartyDataByInvoiceNumber(party.get(),
                qualityBillByInvoiceNumberList, batchWithGrList, dispatchMast);

        partyDataByInvoiceNumber.setCreatedDate(dispatchMast.getCreatedDate());

        if (partyDataByInvoiceNumber == null)
            throw new Exception("no data found");

        partyDataByInvoiceNumber.setInvoiceNo(Long.parseLong(invoiceNo));

        return partyDataByInvoiceNumber;

    }

    public List<DispatchMast> getPendingDispatchByPartyId(Long partyId) throws Exception {
        Optional<Party> partyExist = partyDao.findById(partyId);

        if (partyExist.isEmpty())
            throw new Exception("no data found for party id:" + partyId);

        List<DispatchMast> list = dispatchMastDao.getPendingBillByPartyId(partyId);
        /*
         * if(list.isEmpty()) throw new
         * Exception("no pending invoice found for party:"+partyId);
         */

        return list;

    }

    public List<GetConsolidatedBill> getDispatchByFilter(DispatchFilter filter) throws Exception {
        Date from = null;
        Date to = null;
        // add one day because of timestamp issue
        Calendar c = Calendar.getInstance();

        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

        if (!filter.getFrom().isEmpty())
            from = datetimeFormatter1.parse(filter.getFrom());
        if (!filter.getTo().isEmpty()) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            c.add(Calendar.DATE, 1);// adding one day in to because of time issue in created date
            to = c.getTime();
        }

        Double amt = 0.0;
        Double totalFinishMtr = 0.0;
        Double batchFinishMtr = 0.0;
        Double totalBatchMtr = 0.0;
        List<GetConsolidatedBill> list = new ArrayList<>();

        // System.out.println(from+":"+to);
        List<DispatchMast> dispatchMastList = dispatchMastDao.getInvoiceByFilter(from, to, filter.getPartyId(),
                filter.getUserHeadId());
        for (DispatchMast dispatchMast : dispatchMastList) {
            Party party = partyServiceImp.getPartyById(dispatchMast.getParty().getId());
            if (party == null)
                continue;

            UserData userData = userService.getUserById(dispatchMast.getUserHeadData().getId());
            if (userData == null)
                continue;

            String invoiceNumber = String.valueOf(dispatchMast.getPostfix());
            List<GetBatchByInvoice> stockWithBatchList = dispatchDataDao.getAllStockByInvoiceNumber(invoiceNumber);
            for (GetBatchByInvoice getBatchByInvoice : stockWithBatchList) {
                if (getBatchByInvoice.getBatchId() == null)
                    continue;

                // get batch list by batch id and control id who's bill is generated
                List<BatchData> batchDataList = stockBatchService.getBatchWithControlIdAndBatchId(
                        getBatchByInvoice.getBatchId(), getBatchByInvoice.getStockId());
                if (batchDataList.isEmpty())
                    continue;

                StockMast stockMast = stockBatchService.getStockByStockId(getBatchByInvoice.getStockId());
                if (stockMast == null)
                    continue;

                GetQualityResponse quality = qualityServiceImp.getQualityByID(stockMast.getQuality().getId());
                if (quality == null)
                    continue;

                for (BatchData batchData : batchDataList) {
                    totalBatchMtr += batchData.getMtr();
                    batchFinishMtr += batchData.getFinishMtr();
                    totalFinishMtr += batchFinishMtr;
                }
                amt += batchFinishMtr * dispatchDataDao.getQualityRateByInvoiceAndBatchEntryId(invoiceNumber,
                        batchDataList.get(0).getId());
                batchFinishMtr = 0.0;

            }

            if (amt > 0.0) {
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
        /*
         * if (list.isEmpty()) throw new Exception("no invoice data found");
         */
        return list;

    }

    public List<GetBill> getDispatchBillByFilter(DispatchFilter filter) throws Exception {

        Date from = null;
        Date to = null;
        List<GetBill> list = new ArrayList<>();

        // add one day because of timestamp issue
        Calendar c = Calendar.getInstance();

        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

        if (!filter.getFrom().isEmpty())
            from = datetimeFormatter1.parse(filter.getFrom());
        if (!filter.getTo().isEmpty()) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            c.add(Calendar.DATE, 1);// adding one day in to because of time issue in created date
            to = c.getTime();
        }

        Double amt = 0.0;
        Double totalFinishMtr = 0.0;
        Double batchFinishMtr = 0.0;
        Double totalBatchMtr = 0.0;

        // System.out.println(from+":"+to);
        List<DispatchMast> dispatchMastList = dispatchMastDao.getInvoiceByFilter(from, to, filter.getPartyId(),
                filter.getUserHeadId());
        for (DispatchMast dispatchMast : dispatchMastList) {
            List<QualityList> qualityLists = new ArrayList<>();

            Party party = partyServiceImp.getPartyById(dispatchMast.getParty().getId());
            if (party == null)
                continue;

            UserData userData = userService.getUserById(dispatchMast.getUserHeadData().getId());
            if (userData == null)
                continue;

            String invoiceNumber = String.valueOf(dispatchMast.getPostfix());
            List<GetBatchByInvoice> stockWithBatchList = dispatchDataDao.getAllStockByInvoiceNumber(invoiceNumber);
            for (GetBatchByInvoice getBatchByInvoice : stockWithBatchList) {
                if (getBatchByInvoice.getBatchId() == null)
                    continue;

                // get batch list by batch id and control id who's bill is generated
                List<BatchData> batchDataList = stockBatchService.getBatchWithControlIdAndBatchId(
                        getBatchByInvoice.getBatchId(), getBatchByInvoice.getStockId());
                if (batchDataList.isEmpty())
                    continue;

                StockMast stockMast = stockBatchService.getStockByStockId(getBatchByInvoice.getStockId());
                if (stockMast == null)
                    continue;

                GetQualityResponse quality = qualityServiceImp.getQualityByID(stockMast.getQuality().getId());
                if (quality == null)
                    continue;

                for (BatchData batchData : batchDataList) {
                    totalBatchMtr += batchData.getMtr();
                    totalFinishMtr += batchData.getFinishMtr();
                }

                Double qualityRate = dispatchDataDao.getQualityRateByInvoiceAndBatchEntryId(invoiceNumber,
                        batchDataList.get(0).getId());
                QualityList qualityData = new QualityList();
                qualityData.setTotalMtr(totalBatchMtr);
                qualityData.setTotalFinishMtr(totalFinishMtr);
                qualityData.setQualityEntryId(quality.getId());
                qualityData.setQulityId(quality.getQualityId());
                qualityData.setAmt(qualityRate * totalFinishMtr);
                qualityData.setBatchId(getBatchByInvoice.getBatchId());
                qualityData.setRate(qualityRate);

                qualityLists.add(qualityData);

            }

            if (!qualityLists.isEmpty()) {
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
        /*
         * if(list.isEmpty()) throw new Exception("no data found ");
         */

        return list;

    }

    public List<DispatchMast> getDispatchByPartyId(Long id) {
        List<DispatchMast> dispatchMast = dispatchMastDao.getDipatchByPartyId(id);
        return dispatchMast;
    }

    // for receipt/preview of invoice before saving the record

    public PartyDataByInvoiceNumber getPartyWithQualityDispatchByBatchesAndStockId(CreateDispatch createDispatch)
            throws Exception {
        List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList = new ArrayList<>();
        List<BatchWithGr> batchWithGrList = new ArrayList<>();

        StockMast stockMast = stockBatchService
                .getStockById(createDispatch.getBatchAndStockIdList().get(0).getStockId());

        if (stockMast == null)
            throw new Exception(ConstantFile.StockBatch_Exist);

        Party party = partyDao.findByPartyId(stockMast.getParty().getId());
        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);

        // check the all the batches and stock is belong to same party or not
        for (BatchAndStockId batchAndStockId : createDispatch.getBatchAndStockIdList()) {

            StockMast stockMastExist = stockBatchService.getStockById(batchAndStockId.getStockId());

            // check the stock is exist with batch or not
            // if the create flag is true the check batch data for create
            if (createDispatch.getCreateFlag() == true) {

                List<BatchData> batchDataList = batchDao
                        .getBatchesByBatchIdAndFinishMtrSaveWithoutBillGenrated(batchAndStockId.getBatchId());
                if (batchDataList.isEmpty())
                    throw new Exception(ConstantFile.Batch_Data_Not_Exist);

            } else {
                // check only existing of batch records
                List<BatchData> batchDataList = batchDao.getBatchByBatchIdAndInvoiceNumber(batchAndStockId.getBatchId(),
                        String.valueOf(createDispatch.getInvoiceNo()));
                if (batchDataList.isEmpty())
                    throw new Exception(ConstantFile.Batch_Data_Not_Exist);

            }

            /*
             * if(party.getId()!=stockMastExist.getPartyId()) throw new
             * Exception("may the stock or batch is not belong to the same party");
             */

        }

        // for the quality record and batch gr record
        for (BatchAndStockId batchAndStockId : createDispatch.getBatchAndStockIdList()) {

            // quality record

            StockMast stockMastExist = stockBatchService.getStockById(batchAndStockId.getStockId());
            Quality quality = stockMastExist.getQuality();// qualityServiceImp.getQualityByEntryId(stockMastExist.getQuality().getId());
            QualityName qualityName = quality.getQualityName();
            if (quality == null)
                throw new Exception(ConstantFile.Quality_Data_Not_Exist);

            // QualityBillByInvoiceNumber qualityBillByInvoiceNumber =
            // batchDao.getQualityBillByStockAndBatchId(batchAndStockId.getStockId(),batchAndStockId.getBatchId(),false);

            Double totalMtr = 0.0; // ;=
            // batchDao.getTotalMtrByControlIdAndBatchId(batchAndStockId.getStockId(),batchAndStockId.getBatchId());
            Double totalFinishMtr = 0.0;// =
            // batchDao.getTotalFinishMtrByBatchAndStock(batchAndStockId.getBatchId(),batchAndStockId.getStockId());
            Long totalPcs = 0l;// =
            // batchDao.getTotalPcsByBatchAndStockId(batchAndStockId.getStockId(),batchAndStockId.getBatchId());

            // batch record
            List<BatchData> batchDataList = null;
            if (createDispatch.getCreateFlag() == true) {
                batchDataList = batchDao
                        .getBatchesByBatchIdAndFinishMtrSaveWithoutBillGenrated(batchAndStockId.getBatchId());

            } else {
                batchDataList = batchDao.getBatchByBatchIdAndPchallanRedAndInvoiceNumber(batchAndStockId.getBatchId(),
                        String.valueOf(createDispatch.getInvoiceNo()), batchAndStockId.getPchallanRef());
            }
            for (BatchData batchData : batchDataList) {
                totalFinishMtr += batchData.getFinishMtr();
                totalPcs++;
                totalMtr += batchData.getMtr();
            }

            QualityBillByInvoiceNumber qualityBillByInvoiceNumber = new QualityBillByInvoiceNumber(quality,
                    totalFinishMtr, totalMtr, totalPcs, qualityName, batchAndStockId.getBatchId(), stockMastExist);
            qualityBillByInvoiceNumber
                    .setAmt(stockBatchService.changeInFormattedDecimal(qualityBillByInvoiceNumber.getAmt()));
            // set the rate as well if it is coming and change the amt as well
            if (batchAndStockId.getRate() != null) {
                qualityBillByInvoiceNumber.setRate(batchAndStockId.getRate());
                qualityBillByInvoiceNumber.setAmt(stockBatchService.changeInFormattedDecimal(
                        qualityBillByInvoiceNumber.getFinishMtr() * batchAndStockId.getRate()));
            }

            qualityBillByInvoiceNumberList.add(qualityBillByInvoiceNumber);

            // data preview change
            if (batchDataList.size() > 30) {

                int i = 0;
                int object = batchDataList.size() / 30;
                int remainingGrFrom = batchDataList.size() % 30;
                int startIndex = 0;
                int limit = 30;
                // divide the entire object because of gr is greater than the limit

                if (remainingGrFrom > 0) {
                    object++;
                }
                for (int x = 1; x < object; x++) {
                    // to index value is not going to push into the list
                    // it mean if the start index is 0 and limit is 30 then the 30's index value is
                    // not going to push into the lisy
                    // only from 0-29 object are going to store in list
                    List<BatchData> newBatchList = batchDataList.subList(startIndex, limit);
                    BatchWithGr newBatchWithGr = new BatchWithGr(batchAndStockId.getBatchId(),
                            batchAndStockId.getStockId(), batchAndStockId.getPchallanRef());
                    newBatchWithGr.setBatchDataList(newBatchList);
                    startIndex = limit;
                    limit += 30;
                    batchWithGrList.add(newBatchWithGr);

                }

                // for remaining gr
                if (remainingGrFrom > 0) {
                    List<BatchData> newBatchList = batchDataList.subList(startIndex, batchDataList.size());
                    BatchWithGr newBatchWithGr = new BatchWithGr(batchAndStockId.getBatchId(),
                            batchAndStockId.getStockId(), batchAndStockId.getPchallanRef());
                    newBatchWithGr.setBatchDataList(newBatchList);
                    batchWithGrList.add(newBatchWithGr);
                }

                // batchWithGr.setBatchDataList(batchDataList);
                // batchWithGrList.add(batchWithGr);
            } else {
                // for perfect gr lst which is 30
                /*
                 * batchWithGr.setBatchDataList(batchDataList);
                 * batchWithGrList.add(batchWithGr);
                 */
                batchWithGrList.add(new BatchWithGr(batchDataList, batchAndStockId.getStockId(),
                        batchAndStockId.getBatchId(), batchAndStockId.getPchallanRef()));
            }

        }

        if (batchWithGrList.isEmpty() || qualityBillByInvoiceNumberList.isEmpty())
            throw new Exception(ConstantFile.Batch_Data_Not_Found);

        PartyDataByInvoiceNumber partyDataByInvoiceNumber = new PartyDataByInvoiceNumber(party,
                qualityBillByInvoiceNumberList, batchWithGrList);
        // set the percentage discount as well
        if (createDispatch.getPercentageDiscount() != null) {
            partyDataByInvoiceNumber.setPercentageDiscount(createDispatch.getPercentageDiscount());
        }

        if (partyDataByInvoiceNumber == null)
            throw new Exception("no data found");

        // if the create flag is false then bind the invoice no and date as well
        // otherwise bind that record which are coming
        if (createDispatch.getCreateFlag() == false) {

            DispatchMast dispatchMast = dispatchMastDao
                    .getDispatchMastByInvoiceNo(createDispatch.getInvoiceNo().toString());
            if (dispatchMast != null) {
                partyDataByInvoiceNumber.setInvoiceNo(Long.parseLong(dispatchMast.getPostfix()));
                partyDataByInvoiceNumber.setCreatedDate(dispatchMast.getCreatedDate());
                partyDataByInvoiceNumber.setRemark(dispatchMast.getRemark());
            }
        }

        // change remark as per coming from FE
        partyDataByInvoiceNumber.setRemark(createDispatch.getRemark() == null ? null : createDispatch.getRemark());

        return partyDataByInvoiceNumber;

    }

    public List<DispatchMast> getDispatchByCreatedByAndUserHeadId(Long id, Long id1) {
        return dispatchMastDao.getDispatchByCreatedByAndUserHeadId(id, id1);
    }

    public Boolean getPasswordToCreateInvoice(String password) {
        if (password.equals("gloryFab123@@"))
            return true;
        else
            return false;

    }

    public List<DispatchMast> getDispatchByDateFilter(Date fromDate, Date toDate) {
        return dispatchMastDao.getAllDispatchByDateFilter(fromDate, toDate);
    }

    public List<QualityWithRateAndTotalMtr> getAllQualityByInvoiceNo(Long postfix) {

        return dispatchDataDao.getAllQualityByInvoiceNo(String.valueOf(postfix));

    }

    public List<Long> getAllBatchEntryIdByQualityAndInvoice(Long qualityEntryId, Long postfix) {
        return dispatchDataDao.getAllBatchEntryIdByQualityAndInvoice(qualityEntryId, String.valueOf(postfix));
    }

    public void deleteDispatchByInvoiceNo(Long invoiceNo) throws Exception {
        // check invoice is exist or not
        DispatchMast dispatchMast = dispatchMastDao.getDataByInvoiceNumber(String.valueOf(invoiceNo));

        if (dispatchMast == null)
            throw new Exception(ConstantFile.Dispatch_Not_Exist);

        // change that flag of batch data list by invoice number
        List<Long> batchEntryIds = dispatchDataDao.getBatchEntryIdsByInvoiceNo(String.valueOf(invoiceNo));

        if (!batchEntryIds.isEmpty()) {
            batchDao.updateBillStatusAndFinishMtrAndFinishMtrSaveFlagInListOfBatchEntryId(batchEntryIds, false, 0.0,
                    false);
            // batchDao.updateBillStatusInListOfBatchEntryId(batchEntryIds, false);
        }

        // now delete the dispatch data and then mast info of dispatch
        // dispatchDataDao.deleteBatchEntryIdByInvoiceNo(String.valueOf(invoiceNo));

        dispatchMastDao.deleteByInvoicePostFix(String.valueOf(invoiceNo));
        dispatchDataDao.deleteByInvoiceNo(String.valueOf(invoiceNo));

    }

    public List<ConsolidatedBillMast> getConsolidateDispatchBillByFilter(DispatchFilter filter) throws Exception {
        Date from = null;
        Date to = null;
        List<ConsolidatedBillMast> list = new ArrayList<>();
        // add one day because of timestamp issue
        Calendar c = Calendar.getInstance();
        //System.out.println(1);
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

        if (filter.getFrom() !=null) {
            from = datetimeFormatter1.parse(filter.getFrom());
            c.setTime(from);
            // c.add(Calendar.DATE, 1);//adding one day in to because of time issue in
            // created date and 1 day is comming minus from FE
            from = c.getTime();
        }
        if (filter.getTo()!= null) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            // c.add(Calendar.DATE, 1);//don;t + date because on Palsana, server it is
            // working,but not working on EC2 because of timezone
            to = c.getTime();
        }

        Map<String, ConsolidatedBillMast> invoiceList = new HashMap<>();

        List<ConsolidatedBillDataForPDF> consolidatedBillDataForPDFList = dispatchDataDao.getAllConsolidateResponseForPDFReportByFilter(from, to, filter.getUserHeadId(),
                filter.getPartyId(), filter.getQualityNameId(), filter.getQualityEntryId(),filter.getSignByParty());

        consolidatedBillDataForPDFList.forEach(e -> {
            if(invoiceList.containsKey(e.getInvoiceNo()))
            {
                ConsolidatedBillMast consolidatedBillMast = invoiceList.get(e.getInvoiceNo());
                List<ConsolidatedBillDataForPDF> dataForPDFS = consolidatedBillMast.getList();
                dataForPDFS.add(new ConsolidatedBillDataForPDF(e));
                consolidatedBillMast.setList(dataForPDFS);
                invoiceList.put(consolidatedBillMast.getInvoiceNo(),consolidatedBillMast);
            }
            else
            {
                ConsolidatedBillMast consolidatedBillMast = new ConsolidatedBillMast(e);
                List<ConsolidatedBillDataForPDF> dataForPDFS = new ArrayList<>();
                dataForPDFS.add(new ConsolidatedBillDataForPDF(e));
                consolidatedBillMast.setList(dataForPDFS);
                invoiceList.put(consolidatedBillMast.getInvoiceNo(),consolidatedBillMast);

            }
        });



        if(invoiceList.size()>0)
        {
            list = new ArrayList<>(invoiceList.values());

        }

        return list;

    }

   /* public List<MonthlyDispatchReport> getMonthWiseReportDispatch(DispatchFilter filter) throws Exception {
        Date from = null;
        Date to = null;
        // add one day because of timestamp issue
        Calendar c = Calendar.getInstance();
        System.out.println(1);
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

        if (filter.getFrom()!=null) {
            from = datetimeFormatter1.parse(filter.getFrom());
            c.setTime(from);
            // c.add(Calendar.DATE, 1);//adding one day in to because of time issue in
            // created date and 1 day is comming minus from FE
            from = c.getTime();
        }
        if (filter.getTo()!=null) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            // c.add(Calendar.DATE, 1);//don;t + date because on Palsana, server it is
            // working,but not working on EC2 because of timezone
            to = c.getTime();
        }

        //System.out.println(from.toString() + ":" + to.toString());
        HashMap<Integer, MonthlyDispatchReport> data = new HashMap<Integer, MonthlyDispatchReport>();

        // quality entryId
        List<DispatchMast> dispatchMastList = dispatchMastDao.getInvoiceByDispatchFilter(from, to,
                filter.getUserHeadId(), filter.getPartyId(), filter.getSignByParty());
        if (filter.getQualityEntryId() != null) {
            dispatchMastList = dispatchMastList.stream()
                    .filter(p -> p.getDispatchDataList().stream()
                            .filter(child -> child.getQuality().getId().equals(filter.getQualityEntryId())).findAny()
                            .isPresent())
                    .collect(Collectors.toList());
        }

        if (filter.getQualityNameId() != null) {
            dispatchMastList = dispatchMastList.stream().filter(p -> p.getDispatchDataList().stream()
                    .filter(child -> child.getQuality().getQualityName().getId().equals(filter.getQualityNameId()))
                    .findAny().isPresent()).collect(Collectors.toList());
        }

        // System.out.print(dispatchMastList.size());
        for (DispatchMast dispatchMast : dispatchMastList) {
            Date createdDate = dispatchMast.getCreatedDate();

            int month = createdDate.getMonth() + 1;
            int year = createdDate.getYear() + 1900;
            int key = month * 100000 + year * 10;
            //System.out.print(dispatchMast.getDispatchDataList().size());
            if (dispatchMast.getDispatchDataList().size() == 0)
                continue;

            for (int j = 0; j < dispatchMast.getDispatchDataList().size(); j++) {

                Double finishMtr = StockBatchServiceImpl.changeInFormattedDecimal(
                        dispatchMast.getDispatchDataList().get(j).getBatchData().getFinishMtr());
                Double taxAmt = StockBatchServiceImpl.changeInFormattedDecimal(dispatchMast.getTaxAmt());
                Double discount = StockBatchServiceImpl.changeInFormattedDecimal(dispatchMast.getDiscount());
                Double netAmt = StockBatchServiceImpl.changeInFormattedDecimal(dispatchMast.getNetAmt());
                int type = 0;
                if (dispatchMast.getDispatchDataList().get(j).getBillingUnit().equals("weight"))
                    type = 1;

                if (data.containsKey(key + type)) {
                    MonthlyDispatchReport report = data.get(key + type);
                    //report.addDiscount(discount);
                    report.addFinishMtr(finishMtr);
                    //report.addNetAmt(netAmt);
                    //report.addTaxAmt(taxAmt);

                } else {
                    data.put(key + type, new MonthlyDispatchReport(month, finishMtr, taxAmt, discount, year, netAmt,
                            dispatchMast.getDispatchDataList().get(j).getBillingUnit()));
                }
            }
        }

        List<MonthlyDispatchReport> list = new ArrayList<MonthlyDispatchReport>(data.values());
        Collections.sort(list, new Comparator<MonthlyDispatchReport>() {

            @Override
            public int compare(MonthlyDispatchReport o1, MonthlyDispatchReport o2) {
                return o1.getYear() * 100 + o1.getMonth() - (o2.getYear() * 100 + o2.getMonth()); // salary is also
                // positive integer
            }

        });
        return list;

    }
*/

    public List<MonthlyDispatchReport> getMonthWiseReportDispatch(DispatchFilter filter) throws Exception {
        Date from = null;
        Date to = null;
        // add one day because of timestamp issue
        Calendar c = Calendar.getInstance();
        System.out.println(1);
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

        if (filter.getFrom()!=null) {
            from = datetimeFormatter1.parse(filter.getFrom());
            c.setTime(from);
            // c.add(Calendar.DATE, 1);//adding one day in to because of time issue in
            // created date and 1 day is coming minus from FE
            from = c.getTime();
        }
        if (filter.getTo()!=null) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            // c.add(Calendar.DATE, 1);//don;t + date because on Palsana, server it is
            // working,but not working on EC2 because of timezone
            to = c.getTime();
        }



        //List<MonthlyDispatchReport> list = dispatchDataDao.monthWisePDFReportByFilter(from,to,filter.getUserHeadId());

        return null;

    }


//    public List<MonthlyDispatchPendingReport> getMonthWiseReportPendingDispatch(DispatchFilter filter) throws Exception {
//        Date from = null;
//        Date to = null;
//
//        // add one day because of timestamp issue
//        Calendar c = Calendar.getInstance();
//
//        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
//
//        if (filter.getFrom() != null) {
//            from = datetimeFormatter1.parse(filter.getFrom());
//            c.setTime(from);
//            // c.add(Calendar.DATE, 1);//adding one day in to because of time issue in
//            // created date and 1 day is comming minus from FE
//            from = c.getTime();
//        }
//        if (filter.getTo() != null) {
//            to = datetimeFormatter1.parse(filter.getTo());
//            c.setTime(to);
//            // c.add(Calendar.DATE, 1);//don;t + date because on Palsana, server it is
//            // working,but not working on EC2 because of timezone
//            to = c.getTime();
//        }
//
//
////        System.out.println(from.toString() + ":" + to.toString());
//        //HashMap<Integer, HashMap<String,List<MonthlyDispatchPendingReportData>>> data = new HashMap<>();
//        List<MonthlyDispatchPendingReport> reportList = new ArrayList<>();
//
//        // quality entryId
//        List<DispatchMast> dispatchMastList = new ArrayList<>();
//        if (filter.getPendingPayment()!=null && filter.getPendingPayment() == true) {
//            dispatchMastList = dispatchMastDao.getInvoiceByDateFilterAndPaymentBunchIdNull(from, to);
//        } else {
//            dispatchMastList = dispatchMastDao.getInvoiceByDateFilterAndPaymentBunchIdNotNull(from, to);
//
//        }
//
//
//        // System.out.print(dispatchMastList.size());
//        for (DispatchMast dispatchMast : dispatchMastList) {
//
//            Date createdDate = dispatchMast.getCreatedDate();
//            int month = createdDate.getMonth() + 1;
//            int year = createdDate.getYear() + 1900;
//            int partyId = dispatchMast.getParty().getId().intValue();
//            int key = dispatchMast.getPercentageDiscount().intValue();
//            //int key = (int) (month + year + dispatchMast.getPercentageDiscount() * 100 + 1000000 * partyId);
//            //Double finishMtr = StockBatchServiceImpl.changeInFormattedDecimal(dispatchMast.getDispatchDataList().get(j).getBatchData().getFinishMtr());
//            //Double taxAmt = StockBatchServiceImpl.changeInFormattedDecimal(dispatchMast.getTaxAmt());
//            Double discount = dispatchMast.getDiscount();
//            Double netAmt = StockBatchServiceImpl.changeInFormattedDecimal(dispatchMast.getNetAmt());
//
//            if (data.containsKey(key)) {
//                MonthlyDispatchPendingReport report = data.get(key);
//                report.addNetAmt(netAmt);
//                report.addDiscount(discount);
//                //System.out.println(partyId+":"+discount);
//            } else {
//                //data.put(key, new MonthlyDispatchPendingReport(month, year, netAmt,partyId,discount,dispatchMast.getParty().getPartyName()));
//                MonthlyDispatchPendingReport record = new MonthlyDispatchPendingReport(dispatchMast);
//                List<MonthlyDispatchPendingReportData> list = new ArrayList<>();
//                list.add(new MonthlyDispatchPendingReportData(month, year, netAmt,partyId,discount,dispatchMast.getParty().getPartyName(),dispatchMast.getParty().getUserHeadData().getUserName()));
//                HashMap<String, List<MonthlyDispatchPendingReportData>> listMap = new HashMap<>();
//                listMap.put(Month.of(month).toString()+year,list);
//                record.setList(listMap);
//                reportList.add(record);
//            }
//
//        }
//
//        List<MonthlyDispatchPendingReport> list = new ArrayList<MonthlyDispatchPendingReport>(data.values());
//        // Collections.sort(list,new Comparator<MonthlyDispatchReport>(){
//
//        // @Override
//        // public int compare(MonthlyDispatchReport o1, MonthlyDispatchReport o2) {
//        // return o1.getYear()*100 +o1.getMonth()- (o2.getYear()*100 +o2.getMonth()); //
//        // salary is also positive integer
//        // }
//
//        // }
//        // );
//        return list;
//
//    }

    // only the the rate and discount is updating
    public Long updateDispatch(CreateDispatch createDispatch) throws Exception {

        // check that the invoice record is exist or not
        DispatchMast dispatchMast = dispatchMastDao
                .getDispatchMastByInvoiceNo(createDispatch.getInvoiceNo().toString());
        if (dispatchMast == null)
            throw new Exception(ConstantFile.Dispatch_Not_Exist);

        dispatchMast = new DispatchMast(createDispatch, dispatchMast);

        dispatchMastDao.save(dispatchMast);

        // change the batch rate as well
        for (BatchAndStockId batcheAndStockId : createDispatch.getBatchAndStockIdList()) {
            // update batch quality rate with batch id
            dispatchDataDao.updateQualityRateWithBatchIdAndInvoiceNo(String.valueOf(dispatchMast.getPostfix()),
                    batcheAndStockId.getBatchId(), batcheAndStockId.getRate());
        }
        return Long.parseLong(dispatchMast.getPostfix());

    }

    public void signDispatchByParty(List<UpdateSignDispatch> updateSignDispatchList) {

        updateSignDispatchList.forEach(e -> {
            DispatchMast dispatchMastExist = dispatchMastDao.getDispatchMastByInvoiceNo(e.getInvoiceNo().toString());
            if (dispatchMastExist != null) {
                dispatchMastExist.setSignByParty(e.getSignByParty() == null ? false : e.getSignByParty());
                dispatchMastExist.setSignUpdatedDate(new Date(System.currentTimeMillis()));
                dispatchMastDao.save(dispatchMastExist);
            }
        });
    }

    // pChallan by party id
    public List<BatchWithTotalMTRandFinishMTR> getPChallanByParty(Long partyId,Boolean rfInvoiceFlag) throws Exception {

        List<BatchWithTotalMTRandFinishMTR> list = new ArrayList<>();

        List<BatchWithTotalMTRandFinishMTR> batchDataListByParty = new ArrayList<>();
        List<StockMast> stockMastList = stockBatchService.getRfStockListByPartyAndRfInvoiceFlag(partyId,rfInvoiceFlag);
        // System.out.println(stockMastList.size());
        Party party = partyServiceImp.getPartyById(partyId);
        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);

        /*
         * if(stockMastList.size()<=0) return batchDataListByParty;
         */

        for (StockMast stockMast : stockMastList) {
            // System.out.println(stockMast.getId());
            List<BatchWithTotalMTRandFinishMTR> batchDataList = batchDao
                    .getAllPChallanByStockIdWithTotalFinishMtr(stockMast.getId());
            for (BatchWithTotalMTRandFinishMTR getBatchWithControlIdData : batchDataList) {
                BatchWithTotalMTRandFinishMTR getBatchWithControlId = new BatchWithTotalMTRandFinishMTR(getBatchWithControlIdData);
                Quality quality = stockMast.getQuality();
                if (quality != null) {
                    Double extraRate = 0.0;
                    //for extra rate && check is it merge batch id if yes then go to production plan table to get extra rate
                   /* List<GetAllMergeBatchId> getAllMergeBatchIdList = batchDao.getAllMergeBatchIdByBatchIdAndFinishMtrSaveAndBillIsNotGenrated(getBatchWithControlId.getBatchId());
                    if (getAllMergeBatchIdList.size() > 0) {

                        //get shade id and production detail by using mergebatchid
                        for (GetAllMergeBatchId e : getAllMergeBatchIdList) {
                            if(e.getMergeBatchId()!=null) {
                                ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(e.getMergeBatchId());
                                ShadeMast shadeMast = shadeService.getShadeById(productionPlan.getShadeId());
                                extraRate += shadeMast.getExtraRate() > 0 ? shadeMast.getExtraRate() : 0;
                            }
                        }

                    }
                    else
                    {
                        //check for simple batch id is extra rate is there
                        ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(getBatchWithControlId.getBatchId());
                        ShadeMast shadeMast = shadeService.getShadeById(productionPlan.getShadeId());
                        extraRate += shadeMast.getExtraRate() > 0 ? shadeMast.getExtraRate() : 0;
                    }*/

                    QualityName qualityName = quality.getQualityName();
                    getBatchWithControlId.setRate(quality.getRate() + extraRate);
                    getBatchWithControlId.setQualityId(quality.getQualityId());
                    getBatchWithControlId.setQualityName(qualityName.getQualityName());
                    getBatchWithControlId.setQualityEntryId(quality.getId());
                }
                batchDataListByParty.add(getBatchWithControlId);
            }

        }

        return batchDataListByParty;
    }

    // create dispatch api's service

    public Long createDispatchForPchallan(CreateDispatch dispatchList, Long userId) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        // check the invoice sequence exist or not
        InvoiceSequence invoiceSequenceExist = invoiceSequenceDao.getSequence();
        if (invoiceSequenceExist == null)
            throw new Exception(constantFile.Invoice_Sequence_Not_Found);
        // update the invoice sequence by one
        invoiceSequenceDao.updateSequenceByOne(invoiceSequenceExist.getId(), invoiceSequenceExist.getSequence() + 1);

        // update batch list
        List<Long> batchIdListToUpdate = new ArrayList<>();
        // check user exist or not
        UserData userData = userService.getUserById(userId);

        if (userData == null)
            throw new Exception(ConstantFile.User_Not_Exist);

        List<DispatchData> dispatchDataList = new ArrayList<>();
        DispatchMast dispatchMast = new DispatchMast(dispatchList);
        dispatchMast.setSignByParty(false);
        dispatchMast.setPrefix("inv");
        dispatchMast.setPostfix(invoiceSequenceExist.getSequence().toString());

        // party detail by stock

        StockMast stockMast = stockBatchService.getStockById(dispatchList.getBatchAndStockIdList().get(0).getStockId());

        Party party = stockMast.getParty();

        if (party == null)
            throw new Exception("no party found for id:" + stockMast.getParty().getId());

        // check the credit limit invoice password gloryFab123@@
        Double pendingAmt = paymentTermService.getTotalPendingAmtByPartyId(party.getId());
        if (pendingAmt != null && pendingAmt > party.getCreditLimit()) {
            // check the password and allow to create the invoice else throw the exception
            if (dispatchList.getPassword() == null || getPasswordToCreateInvoice(dispatchList.getPassword()) == false) {
                throw new Exception(ConstantFile.Dispatch_Password_Wrong);
            }
        }

        // check first the data is available or not
        for (BatchAndStockId createDispatch : dispatchList.getBatchAndStockIdList()) {
            List<BatchData> batchDataList = batchDao.findByControlIdAndPchallanRefAndBatchIdForBillGenrate(
                    createDispatch.getStockId(), createDispatch.getPchallanRef(), createDispatch.getBatchId());

            if (batchDataList.isEmpty())
                throw new Exception(constantFile.Batch_Data_Not_Found);
        }

        // iterate and change the status
        for (BatchAndStockId createDispatch : dispatchList.getBatchAndStockIdList()) {

            List<BatchData> batchDataList = batchDao.findByControlIdAndPchallanRefAndBatchIdForBillGenrate(
                    createDispatch.getStockId(), createDispatch.getPchallanRef(), createDispatch.getBatchId());

            ProductionPlan productionPlan = null;
            BatchData batchDataExist = batchDataList.get(0);
            if (batchDataExist.getMergeBatchId() != null) {
                productionPlan = productionPlanService.getProductionByBatchId(batchDataExist.getMergeBatchId());
            } else {
                productionPlan = productionPlanService.getProductionByBatchId(batchDataExist.getBatchId());
            }

            if (productionPlan == null)
                throw new Exception("no production plan found for batch");

            Optional<ShadeMast> shadeMast = null;
            if (productionPlan != null && productionPlan.getShadeId() != null) {
                shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
                if (shadeMast.get() == null)
                    throw new Exception("no shade record found");
            }

            StockMast stockMast1 = stockBatchService.getStockById(createDispatch.getStockId());
            Quality quality = stockMast1.getQuality();

            if (quality == null)
                throw new Exception("no quality found");

            if (batchDataList.isEmpty())
                throw new Exception("no batch data found");

            System.out.println("prod:" + mapper.writeValueAsString(productionPlan));
            for (BatchData batchData : batchDataList) {

                if (batchData.getIsFinishMtrSave() == true && batchData.getIsBillGenrated() == false) {
                    DispatchData dispatchData = null;

                    if (productionPlan.getIsDirect() != true && shadeMast.isPresent()) {
//                        System.out.println("shade:" + mapper.writeValueAsString(shadeMast));
//                        System.out.println("batch:" + mapper.writeValueAsString(batchData));
//                        System.out.println("quality:" + mapper.writeValueAsString(quality));
//                        System.out.println("Stock:" + mapper.writeValueAsString(stockMast1));
                        dispatchData = new DispatchData(batchData, shadeMast.get(), quality, stockMast1);
                        dispatchData.setShadeRate(shadeMast.get().getExtraRate());

                    } else {
                        dispatchData = new DispatchData(batchData, quality, stockMast1);
                        dispatchData.setShadeRate(0.0);
                    }

                    // check the quality rate is coming or not if coming then change the quality
                    // rate
                    if (createDispatch.getRate() != null && createDispatch.getRate() > 0) {
                        dispatchData.setQualityRate(createDispatch.getRate());
                    }

                    dispatchData.setInvoiceNo(dispatchMast.getPostfix());

                    dispatchData.setCreatedBy(dispatchList.getCreatedBy());
                    dispatchData.setIsSendToParty(false);
                    // saveTheList.add(dispatchData);
                    // save the complete batch with gr list to the dispatch data with same invoice
                    // number
                    // dispatchDataDao.save(dispatchData);
                    dispatchDataList.add(dispatchData);

                    // batchData.setIsBillGenrated(true);
                    batchIdListToUpdate.add(batchData.getId());
                    // batchDao.save(batchData);
                }
            }
        }

        // set the dispatch mast values
        dispatchMast.setDispatchDataList(dispatchDataList);
        dispatchMast.setUserHeadData(party.getUserHeadData());
        dispatchMast.setParty(party);
        if (userData.getIsMaster() == false || userData.getUserHeadId() == 0) {
            dispatchMast.setCreatedBy(party.getUserHeadData());
            dispatchMast.setUpdatedBy(party.getUserHeadData());
        } else {
            dispatchMast.setCreatedBy(userData);
            dispatchMast.setUpdatedBy(userData);
        }

        // increment the invoice number to dispatch mast
        // check that the invoice sequence is exist
        DispatchMast dispatchDataExist = dispatchMastDao
                .getDispatchMastByInvoiceNumber(invoiceSequenceExist.getSequence().toString());
        if (dispatchDataExist != null)
            throw new Exception(ConstantFile.PrinterIsBusy);

        dispatchMastDao.save(dispatchMast);

        // update the batch list
        batchDao.updateBillStatusInListOfBatchEntryId(batchIdListToUpdate, true);


        return invoiceSequenceExist.getSequence();

    }

    // only the the rate and discount is updating
    public Long updateDispatchWithPChallan(CreateDispatch createDispatch, Long id) throws Exception {

        UserData userData = userService.getUserById(id);
        if (userData == null) {
            throw new Exception(ConstantFile.User_Not_Exist);
        }

        // check that the invoice record is exist or not
        DispatchMast dispatchMast = dispatchMastDao
                .getDispatchMastByInvoiceNo(createDispatch.getInvoiceNo().toString());
        if (dispatchMast == null)
            throw new Exception(ConstantFile.Dispatch_Not_Exist);

        dispatchMast = new DispatchMast(createDispatch, dispatchMast);
        dispatchMast.setUpdatedBy(userData);

        dispatchMastDao.save(dispatchMast);

        // cheange the batch rate as well
        for (BatchAndStockId batcheAndStockId : createDispatch.getBatchAndStockIdList()) {
            // update batch quality rate with pchallan ref id
            dispatchDataDao.updateQualityRateWithPChallanRefAndBatchIdAndInvoiceNo(dispatchMast.getPostfix(),
                    batcheAndStockId.getPchallanRef(), batcheAndStockId.getRate(), batcheAndStockId.getBatchId());
        }
        return Long.parseLong(dispatchMast.getPostfix());

    }

    public PartyDataByInvoiceNumber getPChallanPartyWithQualityDispatchBy(String invoiceNo) throws Exception {
        List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList = new ArrayList<>();

        DispatchMast dispatchMast = dispatchMastDao.getDataByInvoiceNumber(invoiceNo);
        String invoiceExist = dispatchDataDao.findByInvoiceNo(invoiceNo);
        if (invoiceExist == null || invoiceExist == "")
            throw new Exception(ConstantFile.Dispatch_Not_Found);

        // quality list

        List<GetBatchByInvoice> batchList = dispatchDataDao.findPChallanAndBatchIdAndStockByInvoice(invoiceExist);

        for (GetBatchByInvoice batch : batchList) {
            if (batchList.isEmpty())
                continue;

            Double totalMtr = 0.0;
            Double finishMtr = 0.0;
            Long pcs = 0l;
            Double amt = 0.0;

            // System.out.println(batch.getStockId());
            StockMast stockMast = stockBatchService.getStockById(batch.getStockId());
            Optional<Quality> quality = qualityDao.findById(stockMast.getQuality().getId());

            if (quality.isEmpty())
                throw new Exception(ConstantFile.Quality_Data_Not_Exist);

            QualityBillByInvoiceNumber qualityBillByInvoiceNumber = new QualityBillByInvoiceNumber(quality.get());
            QualityName qualityName = quality.get().getQualityName();
            qualityBillByInvoiceNumber.setQualityName(qualityName.getQualityName());

            List<DispatchData> dispatchDataList = dispatchDataDao.findByPChallanRefAndBatchIdAndStockIdAndInvoiceNo(
                    batch.getStockId(), batch.getPchallanRef(), batch.getBatchId(), invoiceNo);

            String isMergeBatchId = "";
            for (DispatchData invoiceBatch : dispatchDataList) {
                // System.out.println("invoic entry:"+invoiceBatch.getBatchEntryId());
                BatchData batchData = invoiceBatch.getBatchData();
                if (batchData.getMtr() != null && batchData.getFinishMtr() != null) {
                    totalMtr += batchData.getMtr();
                    finishMtr += batchData.getFinishMtr();
                    pcs++;
                }

            }

            /*
             * //get the shade rate as well ProductionPlan
             * productionPlan=productionPlanService.getProductionDataByBatchAndStock(batch.
             * getBatchId(),batch.getStockId()); if(productionPlan==null) continue;
             *//*
             * Optional<ShadeMast> shadeMast = null; if(productionPlan.getShadeId()!=null)
             * shadeService.getShadeMastById(productionPlan.getShadeId());
             *//*
             */

            Double shadeRate = 0.0;
            /*
             * if(shadeMast.isPresent()) { shadeRate = shadeMast.get().getExtraRate(); }
             */

            // get the rate

            Double rate = dispatchDataDao.getQualityRateByInvoiceNoAndBatchIdAndPchallanRef(invoiceNo,
                    batch.getPchallanRef(), batch.getBatchId());
            // shadeRate =
            // dispatchDataDao.getShadeRateByInvoiceNoandBatchIdAndPchallanRef(invoiceNo,batch.getPchallanRef(),batch.getBatchId());
            shadeRate = dispatchDataList.get(0).getShadeRate();

            // set the quality with batch data
            qualityBillByInvoiceNumber.setPchallanRef(batch.getPchallanRef());
            qualityBillByInvoiceNumber.setBatchId(batch.getBatchId());
            qualityBillByInvoiceNumber.setTotalMtr(totalMtr);
            qualityBillByInvoiceNumber.setFinishMtr(finishMtr);

            if (dispatchDataList.get(0).getBillingUnit().equalsIgnoreCase("weight")) {
                totalMtr = (totalMtr / 100) * dispatchDataList.get(0).getWtPer100m();
                // finishMtr = (finishMtr / 100) * dispatchDataList.get(0).getWtPer100m();
                qualityBillByInvoiceNumber.setTotalMtr(totalMtr);
                // qualityBillByInvoiceNumber.setFinishMtr(finishMtr);
            }

            // Count the total amt based on quality rate and total finish mtr
            amt = (rate + shadeRate) * finishMtr;

            qualityBillByInvoiceNumber.setPcs(pcs);
            qualityBillByInvoiceNumber.setAmt(amt);
            qualityBillByInvoiceNumber.setRate(rate);
            qualityBillByInvoiceNumber.setShadeRate(shadeRate);
            qualityBillByInvoiceNumber.setPChalNo(stockMast.getChlNo());
            qualityBillByInvoiceNumberList.add(qualityBillByInvoiceNumber);
        }

        // for batch list

        // List<GetBatchByInvoice> batchList2 =
        // dispatchDataDao.findPChallanAndStockByInvoice(invoiceExist);

        List<BatchWithGr> batchWithGrList = new ArrayList<>();
        for (GetBatchByInvoice batch : batchList) {

            BatchWithGr batchWithGr = new BatchWithGr(batch);
            List<BatchData> batchDataList = new ArrayList<>();

            // batches data

            List<DispatchData> dispatchDataList = dispatchDataDao.findByPChallanRefAndBatchIdAndStockIdAndInvoiceNo(
                    batch.getStockId(), batch.getPchallanRef(), batch.getBatchId(), invoiceNo);

            for (DispatchData invoiceBatch : dispatchDataList) {
                BatchData batchData = invoiceBatch.getBatchData();
                batchDataList.add(batchData);
            }

            batchDataList.sort(Comparator.comparing(BatchData::getSequenceId));

            // change the list respone if the size is greater than 30 object

            if (batchDataList.size() > 30) {

                int i = 0;
                int object = batchDataList.size() / 30;
                int remainingGrFrom = batchDataList.size() % 30;
                int startIndex = 0;
                int limit = 30;
                // divide the entire object because of gr is greter than the limit

                for (int x = 0; x < object; x++) {
                    // to index value is not going to push into the list
                    // it mean if the start index is 0 and limit is 30 then the 30's index value is
                    // not going to push into the lisy
                    // only from 0-29 object are going to store in list
                    List<BatchData> newBatchList = batchDataList.subList(startIndex, limit);
                    newBatchList = conversionMtrAndFinishMtrWithWtUnit(newBatchList,
                            dispatchDataList.get(0).getWtPer100m(), dispatchDataList.get(0).getInwardUnit(),
                            dispatchDataList.get(0).getBillingUnit());
                    BatchWithGr newBatchWithGr = new BatchWithGr(batch);
                    newBatchWithGr.setBillingUnit(dispatchDataList.get(0).getBillingUnit());
                    newBatchWithGr.setBatchDataList(newBatchList);
                    startIndex = limit;
                    limit += 30;
                    batchWithGrList.add(newBatchWithGr);

                }

                // for remaining gr
                if (remainingGrFrom > 0) {
                    List<BatchData> newBatchList = batchDataList.subList(startIndex, batchDataList.size());
                    newBatchList = conversionMtrAndFinishMtrWithWtUnit(newBatchList,
                            dispatchDataList.get(0).getWtPer100m(), dispatchDataList.get(0).getInwardUnit(),
                            dispatchDataList.get(0).getBillingUnit());
                    BatchWithGr newBatchWithGr = new BatchWithGr(batch);
                    newBatchWithGr.setBillingUnit(dispatchDataList.get(0).getBillingUnit());
                    newBatchWithGr.setBatchDataList(newBatchList);
                    batchWithGrList.add(newBatchWithGr);
                }

                // batchWithGr.setBatchDataList(batchDataList);
                // batchWithGrList.add(batchWithGr);
            } else {
                // for perfect gr lst which is 30
                batchDataList = conversionMtrAndFinishMtrWithWtUnit(batchDataList,
                        dispatchDataList.get(0).getWtPer100m(), dispatchDataList.get(0).getInwardUnit(),
                        dispatchDataList.get(0).getBillingUnit());
                batchWithGr.setBatchDataList(batchDataList);
                batchWithGr.setBillingUnit(dispatchDataList.get(0).getBillingUnit());
                batchWithGrList.add(batchWithGr);
            }

        }

        // for party data
        StockMast stockMast = stockBatchService.getStockById(batchList.get(0).getStockId());
        Optional<Party> party = partyDao.findById(stockMast.getParty().getId());

        PartyDataByInvoiceNumber partyDataByInvoiceNumber = new PartyDataByInvoiceNumber(party.get(),
                qualityBillByInvoiceNumberList, batchWithGrList, dispatchMast);

        partyDataByInvoiceNumber.setCreatedDate(dispatchMast.getCreatedDate());

        if (partyDataByInvoiceNumber == null)
            throw new Exception("no data found");

        partyDataByInvoiceNumber.setInvoiceNo(Long.parseLong(invoiceNo));

        return partyDataByInvoiceNumber;

    }

    private List<BatchData> conversionMtrAndFinishMtrWithWtUnit(List<BatchData> newBatchList, Double wtPer100m,
                                                                String inwardUnit, String billingUnit) {
        List<BatchData> batchDataList = new ArrayList<>();

        // convert as per the requirement
        if (billingUnit.equalsIgnoreCase("weight")) {
            // change batch data list
            for (BatchData batchData : newBatchList) {
                BatchData batch = new BatchData(batchData);
                batch.setMtr(Precision.round(((batch.getMtr() / 100) * wtPer100m), 2));

                // batch.setFinishMtr((batchData.getFinishMtr()/100)*wtPer100m);
                batchDataList.add(batch);
            }

        } else {
            batchDataList = newBatchList;
        }

        return batchDataList;

    }

    public PartyDataByInvoiceNumber getPartyWithQualityDispatchByPChallanAndStockId(CreateDispatch createDispatch)
            throws Exception {
        List<QualityBillByInvoiceNumber> qualityBillByInvoiceNumberList = new ArrayList<>();
        List<BatchWithGr> batchWithGrList = new ArrayList<>();

        StockMast stockMast = stockBatchService
                .getStockById(createDispatch.getBatchAndStockIdList().get(0).getStockId());

        if (stockMast == null)
            throw new Exception(ConstantFile.StockBatch_Exist);

        Party party = stockMast.getParty();
        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);

        // check the all the batches and stock is belong to same party or not
        for (BatchAndStockId batchAndStockId : createDispatch.getBatchAndStockIdList()) {

            StockMast stockMastExist = stockBatchService.getStockById(batchAndStockId.getStockId());

            // check the stock is exist with batch or not
            // if the create flag is true the check batch data for create
            if (createDispatch.getCreateFlag() == true) {

                List<BatchData> batchDataList = batchDao
                        .getBatchesByPChallanRefIdAndFinishMtrSaveWithoutBillGenratedAndStockId(
                                batchAndStockId.getPchallanRef(), batchAndStockId.getStockId());
                if (batchDataList.isEmpty())
                    throw new Exception(ConstantFile.Batch_Data_Not_Exist);

            } else {
                // check only existing of batch records
                List<BatchData> batchDataList = batchDao.getBatchByBatchIdAndPchallanRedAndInvoiceNumber(
                        batchAndStockId.getBatchId(), String.valueOf(createDispatch.getInvoiceNo()),
                        batchAndStockId.getPchallanRef());
                if (batchDataList.isEmpty())
                    throw new Exception(ConstantFile.Batch_Data_Not_Exist);

            }

            /*
             * if(party.getId()!=stockMastExist.getPartyId()) throw new
             * Exception("may the stock or batch is not belong to the same party");
             */

        }

        // for the quality record and batch gr record
        for (BatchAndStockId batchAndStockId : createDispatch.getBatchAndStockIdList()) {

            // quality record

            String billingUnit = null;
            StockMast stockMastExist = stockBatchService.getStockById(batchAndStockId.getStockId());
            Quality quality = stockMastExist.getQuality();
            QualityName qualityName = quality.getQualityName();
            if (quality == null)
                throw new Exception(ConstantFile.Quality_Data_Not_Exist);

            // QualityBillByInvoiceNumber qualityBillByInvoiceNumber =
            // batchDao.getQualityBillByStockAndBatchId(batchAndStockId.getStockId(),batchAndStockId.getBatchId(),false);

            Double totalMtr = 0.0; // ;=
            // batchDao.getTotalMtrByControlIdAndBatchId(batchAndStockId.getStockId(),batchAndStockId.getBatchId());
            Double totalFinishMtr = 0.0;// =
            // batchDao.getTotalFinishMtrByBatchAndStock(batchAndStockId.getBatchId(),batchAndStockId.getStockId());
            Long totalPcs = 0l;// =
            // batchDao.getTotalPcsByBatchAndStockId(batchAndStockId.getStockId(),batchAndStockId.getBatchId());

            // batch record
            List<BatchData> batchDataList = null;
            if (createDispatch.getCreateFlag() == true) {
                batchDataList = batchDao.findByControlIdAndPchallanRefAndBatchIdForBillGenrate(
                        batchAndStockId.getStockId(), batchAndStockId.getPchallanRef(), batchAndStockId.getBatchId());
                billingUnit = quality.getBillingUnit();

            } else {
                batchDataList = batchDao.getBatchByBatchIdAndPchallanRedAndInvoiceNumber(batchAndStockId.getBatchId(),
                        String.valueOf(createDispatch.getInvoiceNo()), batchAndStockId.getPchallanRef());
                billingUnit = dispatchDataDao.getBillingUnitByInvoiceAndBatchEntryId(
                        String.valueOf(createDispatch.getInvoiceNo()), batchDataList.get(0).getId());
            }

            // sort data by sequence id
            batchDataList.sort(Comparator.comparing(BatchData::getSequenceId));
            for (BatchData batchData : batchDataList) {
                totalFinishMtr += batchData.getFinishMtr();
                totalPcs++;
                totalMtr += batchData.getMtr();
            }

            totalFinishMtr = stockBatchService.changeInFormattedDecimal(totalFinishMtr);
            totalMtr = stockBatchService.changeInFormattedDecimal(totalMtr);

            if (billingUnit.equalsIgnoreCase("weight")) {
                if (createDispatch.getCreateFlag() == true) {
                    totalMtr = (totalMtr / 100) * stockMast.getWtPer100m();
                } else {
                    totalMtr = (totalMtr / 100) * dispatchDataDao.getWtPer100mByInvoiceAndBatchEntryId(
                            String.valueOf(createDispatch.getInvoiceNo()), batchDataList.get(0).getId());
                }
            }

            QualityBillByInvoiceNumber qualityBillByInvoiceNumber = new QualityBillByInvoiceNumber(quality,
                    totalFinishMtr, totalMtr, totalPcs, qualityName, batchAndStockId.getPchallanRef(), stockMastExist,
                    batchAndStockId.getBatchId());

            // change the grey mtr total as per the billing unit

            qualityBillByInvoiceNumber
                    .setAmt(stockBatchService.changeInFormattedDecimal(qualityBillByInvoiceNumber.getAmt()));
            qualityBillByInvoiceNumber.setBillingUnit(billingUnit);
            // set the rate as well if it is coming and change the amt as well
            if (batchAndStockId.getRate() != null) {
                qualityBillByInvoiceNumber.setRate(batchAndStockId.getRate());
                qualityBillByInvoiceNumber.setAmt(stockBatchService.changeInFormattedDecimal(
                        qualityBillByInvoiceNumber.getFinishMtr() * batchAndStockId.getRate()));
            }

            qualityBillByInvoiceNumberList.add(qualityBillByInvoiceNumber);

            // data preview change
            if (batchDataList.size() > 30) {

                int i = 0;
                int object = batchDataList.size() / 30;
                int remainingGrFrom = batchDataList.size() % 30;
                int startIndex = 0;
                int limit = 30;
                // divide the entire object because of gr is greater than the limit

                /*
                 * if (remainingGrFrom > 0) { object++; }
                 */
                for (int x = 0; x < object; x++) {
                    // to index value is not going to push into the list
                    // it mean if the start index is 0 and limit is 30 then the 30's index value is
                    // not going to push into the lisy
                    // only from 0-29 object are going to store in list
                    List<BatchData> newBatchList = batchDataList.subList(startIndex, limit);

                    // change in gr in the format
                    newBatchList = StockBatchServiceImpl.changeInFormattedDecimal(newBatchList);
                    BatchWithGr newBatchWithGr = new BatchWithGr(batchAndStockId.getBatchId(),
                            batchAndStockId.getStockId(), batchAndStockId.getPchallanRef());

                    // convert batch gr also
                    if (createDispatch.getCreateFlag() == false) {
                        // change gr as per the quality wtper 100
                        UnitDetail unitDetail = dispatchDataDao.getUnitDetailByInvoiceNoAndBatchEntryId(
                                createDispatch.getInvoiceNo().toString(), newBatchList.get(0).getId());
                        if (unitDetail != null) {
                            newBatchList = conversionMtrAndFinishMtrWithWtUnit(newBatchList, unitDetail.getWtPer100m(),
                                    unitDetail.getInwardUnit(), unitDetail.getBillingUnit());
                            newBatchWithGr.setBillingUnit(unitDetail.getBillingUnit());
                        }
                    } else {
                        // change gr as per the stock wt100 and quality
                        newBatchWithGr.setBillingUnit(quality.getBillingUnit());
                        newBatchList = conversionMtrAndFinishMtrWithWtUnit(newBatchList, stockMastExist.getWtPer100m(),
                                quality.getUnit(), quality.getBillingUnit());

                    }
                    newBatchWithGr.setBatchDataList(newBatchList);
                    startIndex = limit;
                    limit += 30;
                    batchWithGrList.add(newBatchWithGr);

                }

                // for remaining gr
                BatchWithGr newBatchWithGr = new BatchWithGr(batchAndStockId.getBatchId(), batchAndStockId.getStockId(),
                        batchAndStockId.getPchallanRef());
                if (remainingGrFrom > 0) {
                    List<BatchData> newBatchList = batchDataList.subList(startIndex, batchDataList.size());
                    // change in gr in the format
                    newBatchList = StockBatchServiceImpl.changeInFormattedDecimal(newBatchList);

                    if (createDispatch.getCreateFlag() == false) {
                        // change gr as per the quality wtper 100
                        UnitDetail unitDetail = dispatchDataDao.getUnitDetailByInvoiceNoAndBatchEntryId(
                                createDispatch.getInvoiceNo().toString(), newBatchList.get(0).getId());
                        if (unitDetail != null) {
                            newBatchList = conversionMtrAndFinishMtrWithWtUnit(newBatchList, unitDetail.getWtPer100m(),
                                    unitDetail.getInwardUnit(), unitDetail.getBillingUnit());
                            newBatchWithGr.setBillingUnit(unitDetail.getBillingUnit());
                        }
                    } else {
                        // change gr as per the stock wt100 and quality
                        newBatchList = conversionMtrAndFinishMtrWithWtUnit(newBatchList, stockMastExist.getWtPer100m(),
                                quality.getUnit(), quality.getBillingUnit());
                        newBatchWithGr.setBillingUnit(quality.getBillingUnit());
                    }

                    newBatchWithGr.setBatchDataList(newBatchList);
                    batchWithGrList.add(newBatchWithGr);
                }

                // batchWithGr.setBatchDataList(batchDataList);
                // batchWithGrList.add(batchWithGr);
            } else {
                // for perfect gr lst which is 30

                // change in gr in the format
                batchDataList = StockBatchServiceImpl.changeInFormattedDecimal(batchDataList);
                BatchWithGr batchWithGr = new BatchWithGr(batchDataList, batchAndStockId.getStockId(),
                        batchAndStockId.getBatchId(), batchAndStockId.getPchallanRef());
                if (createDispatch.getCreateFlag() == false) {
                    // change gr as per the quality wtper 100
                    UnitDetail unitDetail = dispatchDataDao.getUnitDetailByInvoiceNoAndBatchEntryId(
                            createDispatch.getInvoiceNo().toString(), batchDataList.get(0).getId());
                    if (unitDetail != null) {
                        batchDataList = conversionMtrAndFinishMtrWithWtUnit(batchDataList, unitDetail.getWtPer100m(),
                                unitDetail.getInwardUnit(), unitDetail.getBillingUnit());
                        batchWithGr.setBillingUnit(unitDetail.getBillingUnit());
                    }
                } else {
                    // change gr as per the stock wt100 and quality
                    batchWithGr.setBillingUnit(quality.getBillingUnit());
                    batchDataList = conversionMtrAndFinishMtrWithWtUnit(batchDataList, stockMastExist.getWtPer100m(),
                            quality.getUnit(), quality.getBillingUnit());
                }
                batchWithGr.setBatchDataList(batchDataList);
                batchWithGrList.add(batchWithGr);
            }

        }

        if (batchWithGrList.isEmpty() || qualityBillByInvoiceNumberList.isEmpty())
            throw new Exception(ConstantFile.Batch_Data_Not_Found);

        PartyDataByInvoiceNumber partyDataByInvoiceNumber = new PartyDataByInvoiceNumber(party,
                qualityBillByInvoiceNumberList, batchWithGrList);
        // set the percentage discount as well
        if (createDispatch.getPercentageDiscount() != null) {
            partyDataByInvoiceNumber.setPercentageDiscount(createDispatch.getPercentageDiscount());
        }

        if (partyDataByInvoiceNumber == null)
            throw new Exception("no data found");

        // if the create flag is false then bind the invoice no and date as well
        // otherwise bind that record which are coming
        if (createDispatch.getCreateFlag() == false) {

            DispatchMast dispatchMast = dispatchMastDao
                    .getDispatchMastByInvoiceNo(createDispatch.getInvoiceNo().toString());
            if (dispatchMast != null) {
                partyDataByInvoiceNumber.setInvoiceNo(Long.parseLong(dispatchMast.getPostfix()));
                partyDataByInvoiceNumber.setCreatedDate(dispatchMast.getCreatedDate());
                partyDataByInvoiceNumber.setRemark(dispatchMast.getRemark());
            }
        }

        // change remark as per coming from FE
        partyDataByInvoiceNumber.setRemark(createDispatch.getRemark() == null ? null : createDispatch.getRemark());

        return partyDataByInvoiceNumber;

    }

    // get dispatch by id for update dispatch
    public PartyWithBatchByInvoice getDispatchForUpdatePChallanByInvoiceNumber(String invoiceNo) throws Exception {
        List<BatchWithTotalMTRandFinishMTR> batchWithTotalMTRandFinishMTRList = new ArrayList<>();

        List<GetBatchByInvoice> list = dispatchDataDao.findPChallanAndStockByInvoice(invoiceNo);

        if (list.isEmpty())
            throw new Exception("no data found");

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(list));

        for (GetBatchByInvoice batch : list) {
            BatchWithTotalMTRandFinishMTR batchWithTotalMTRandFinishMTR = new BatchWithTotalMTRandFinishMTR();
            batchWithTotalMTRandFinishMTR.setBatchId(batch.getBatchId());
            batchWithTotalMTRandFinishMTR.setPchallanRef(batch.getPchallanRef());
            batchWithTotalMTRandFinishMTR.setControlId(batch.getStockId());

            List<DispatchData> dispatchDataList = dispatchDataDao.findByPChallanRefAndStockIdAndBatchIdInvoiceNo(
                    batch.getStockId(), batch.getPchallanRef(), invoiceNo, batch.getBatchId());
            Double WT = 0.0;
            Double MTR = 0.0;
            Double totalFinishMtr = 0.0;
            Long totalPcs = batch.getBatchEntryId();// total count

            for (DispatchData dispatchData : dispatchDataList) {
                BatchData batchData = dispatchData.getBatchData();
                if (batchData.getWt() != null) {
                    WT += batchData.getWt();
                    MTR += batchData.getMtr();
                    totalFinishMtr += batchData.getFinishMtr();
                    // totalPcs++;

                }
            }
            if (!dispatchDataList.isEmpty()) {
                Quality quality = qualityDao.getqualityById(dispatchDataList.get(0).getQuality().getId());
                if (quality != null) {
                    QualityName qualityName = quality.getQualityName();
                    batchWithTotalMTRandFinishMTR.setQualityEntryId(quality.getId());
                    batchWithTotalMTRandFinishMTR.setQualityId(quality.getQualityId());
                    batchWithTotalMTRandFinishMTR.setQualityName(qualityName.getQualityName());
                    batchWithTotalMTRandFinishMTR.setRate(dispatchDataList.get(0).getQualityRate());

                }
            }
            batchWithTotalMTRandFinishMTR.setTotalFinishMtr(totalFinishMtr);
            batchWithTotalMTRandFinishMTR.setTotalPcs(totalPcs);
            batchWithTotalMTRandFinishMTR.setWT(stockBatchService.changeInFormattedDecimal(WT));
            batchWithTotalMTRandFinishMTR.setMTR(stockBatchService.changeInFormattedDecimal(MTR));
            // Optional<Party>
            // party=partyDao.findById(dispatchDataList.get(0).getStockId());
            // if(party.isPresent())
            // throw new Exception("no party data found");
            // batchWithTotalMTRandFinishMTR.setPartyId(party.get().getId());
            // batchWithTotalMTRandFinishMTR.setPartyName(party.get().getPartyName());
            batchWithTotalMTRandFinishMTRList.add(batchWithTotalMTRandFinishMTR);
        }

        StockMast stockMast = stockBatchService.getStockById(list.get(0).getStockId());
        Party party = stockMast.getParty();

        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);

        PartyWithBatchByInvoice partyWithBatchByInvoice = new PartyWithBatchByInvoice(batchWithTotalMTRandFinishMTRList,
                party);

        // get the discount from party
        DispatchMast dispatchMast = dispatchMastDao.getDispatchMastByInvoiceNo(invoiceNo);
        if (dispatchMast != null) {
            partyWithBatchByInvoice.setPercentageDiscount(dispatchMast.getPercentageDiscount());
            partyWithBatchByInvoice.setRemark(dispatchMast.getRemark());
            partyWithBatchByInvoice.setDeliveryMode(dispatchMast.getDeliveryMode());
        }
        // status
        partyWithBatchByInvoice.setIsSendToParty(dispatchDataDao.getSendToPartyFlag(invoiceNo));

        System.out.println("*******************************************");
        System.out.println(objectMapper.writeValueAsString(partyWithBatchByInvoice));
        return partyWithBatchByInvoice;

    }

    public FilterResponse<DispatchMast> getpaginatedDispatchData(PaginatedData data) {

        System.out.println("page size-" + Integer.toString(data.getPageSize()));
        System.out.println("page index-" + Integer.toString(data.getPageIndex()));

        Specification<DispatchMast> spec = specificationManager.getSpecificationFromFilters(data.getParameters(),
                data.isAnd(), null);
        String sortBy;
        if (data.getSortBy() == null)
            sortBy = "id";

        else
            sortBy = data.getSortBy();

        Direction sortOrder;

        if (data.getSortOrder() == null || data.getSortOrder() == "ASC")
            sortOrder = Direction.ASC;

        else
            sortOrder = Direction.DESC;

        System.out.println("page size-" + Integer.toString(data.getPageSize()));
        System.out.println("page index-" + Integer.toString(data.getPageIndex()));
        Pageable pageable = PageRequest.of(data.getPageIndex(), data.getPageSize(), sortOrder, sortBy);

        Page<DispatchMast> dispatchMastList;

        if (spec == null)
            dispatchMastList = dispatchMastDao.findAll(pageable);

        else
            dispatchMastList = dispatchMastDao.findAll(spec, pageable);

        FilterResponse<DispatchMast> response = new FilterResponse<DispatchMast>(dispatchMastList.toList(),
                dispatchMastList.getNumber(), dispatchMastList.getSize(), dispatchMastList.getTotalPages());
        return response;

    }

    public List<ConsolidatedBillDataForExcel> getConsolidateDispatchBillByFilterNew(DispatchFilter filter) throws ParseException {
        Date from = null;
        Date to = null;
        // add one day because of timestamp issue
        Calendar c = Calendar.getInstance();
        //System.out.println(1);
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

        if (filter.getFrom() != null) {
            from = datetimeFormatter1.parse(filter.getFrom());
            c.setTime(from);
            // c.add(Calendar.DATE, 1);//adding one day in to because of time issue in
            // created date and 1 day is comming minus from FE
            from = c.getTime();
        }
        if (filter.getTo() != null) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            // c.add(Calendar.DATE, 1);//don;t + date because on Palsana, server it is
            // working,but not working on EC2 because of timezone
            to = c.getTime();
        }
        List<ConsolidatedBillDataForExcel> list = dispatchDataDao.getAllConsolidateResponseByFilter(from, to, filter.getUserHeadId(),
                filter.getPartyId(), filter.getQualityNameId(), filter.getQualityEntryId(),filter.getSignByParty());
        return list;
    }
}
