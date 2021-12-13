package com.main.glory.servicesImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.StockAndBatch.*;
import com.main.glory.Dao.admin.BatchSequneceDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.filters.Filter;
import com.main.glory.filters.FilterResponse;
import com.main.glory.filters.SpecificationManager;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.*;
import com.main.glory.model.StockDataBatchData.request.*;
import com.main.glory.model.StockDataBatchData.response.*;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.filters.QueryOperator;
import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.quality.response.QualityWithQualityNameParty;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import com.main.glory.services.FilterService;

import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("stockBatchServiceImpl")
public class StockBatchServiceImpl {
    /*
     *
     * @Autowired BatchReturnDao batchReturnDao;
     */

    @Autowired
    BatchReturnMastDao batchReturnMastDao;

    @Autowired
    BatchReturnDataDao batchReturnDataDao;

    @Autowired
    DispatchMastImpl dispatchMastService;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    ConstantFile constantFile;

    @Autowired
    BatchSequneceDao batchSequneceDao;

    @Autowired
    QualityNameDao qualityNameDao;

    @Autowired
    ShadeServiceImpl shadeService;

    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @Autowired
    SpecificationManager<StockMast> specificationManager;

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

    @Autowired
    FilterService<StockMast, StockMastDao> filterService;

    public List<StockMast> getAllStockBatch(Long qualityId) {

        List<StockMast> stock = stockMastDao.findByQualityId(qualityId);
        System.out.println(stock);
        return stock;

    }

    public Double getWtByControlAndBatchId(Long controlId, String batchId) throws Exception {
        // get total wt of batch who's production plan is done
        Double data = batchDao.getTotalWtByControlIdAndBatchId(controlId, batchId);
        if (data == null)
            throw new Exception("no batch wt found");

        return data;

    }

    public Long saveStockBatch(AddStockBatch stockMast, String id) throws Exception {
        List<BatchData> batchDataList = new ArrayList<>();
        Party party = partyDao.findByPartyId(stockMast.getPartyId());
        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);
        Long max = 0l, batchId = 0l;
        Date dt = new Date(System.currentTimeMillis());
        stockMast.setCreatedDate(dt);
        stockMast.setIsProductionPlanned(false);
        Optional<Quality> quality = qualityDao.findById(stockMast.getQualityId());

        if (!quality.isPresent()) {
            throw new Exception(ConstantFile.Quality_Data_Not_Found);
        } else {
            for (BatchData batchData : stockMast.getBatchData()) {
                if (batchData.getBatchId() == null)
                    throw new Exception(ConstantFile.Null_Record_Passed);

                batchData = new BatchData(batchData);
                batchDataList.add(batchData);
            }

            // stockMast.setBatchData(batchDataList);

            // for data entry user
            UserData userData = userDao.getUserById(Long.parseLong(id));
            if (userData.getIsMaster() == false) {
                // fetch the party record to set the usert head
                party = partyDao.findByPartyId(stockMast.getPartyId());
                stockMast.setUserHeadId(party.getUserHeadData().getId());
            }
            // check the invoice sequence
            /*
             * BatchSequence batchSequence = batchSequneceDao.getBatchSequence();
             * if(batchSequence==null) throw new
             * Exception(ConstantFile.Batch_Sequence_Not_Exist);
             *
             * if(max >= batchSequence.getSequence()) {
             * batchSequneceDao.updateBatchSequence(batchSequence.getId(),max+1); }
             */

            // add record
            StockMast x = new StockMast(stockMast, party, quality.get());

            StockMast create = stockMastDao.saveAndFlush(x);
            for (BatchData batchData : batchDataList) {
                batchData.setControlId(create.getId());
                batchDao.save(batchData);
            }

            // update the quality wt per 100 as well
            qualityDao.updateQualityWtAndMtrKgById(stockMast.getQualityId(), stockMast.getWtPer100m(),
                    100 / stockMast.getWtPer100m());
            if (create.getUserHeadId() == 0) {
                create.setUserHeadId(party.getUserHeadData().getId());
                stockMastDao.saveAndFlush(create);
            }
            return x.getId();

        }

    }

    public List<GetAllStockWithPartyNameResponse> getAllStockBatch(String getBy, Long id) throws Exception {
        Optional<List<GetAllStockWithPartyNameResponse>> data = null;
        List<GetAllStockWithPartyNameResponse> list = new ArrayList<>();

        Boolean flag = false;

        if (id == null) {
            data = stockMastDao.getAllStockWithPartyNameAndQualityName();
            if (data.isPresent()) {
                List<GetAllBatchResponse> batchDataList = new ArrayList<>();
                // List<GetAllBatchResponse> batchDataList2 = new ArrayList<>();
                for (GetAllStockWithPartyNameResponse batchData : data.get()) {

                    batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());
                    // String qualityName =
                    // qualityNameDao.getQualityNameDetailByQualitytEntryId(batchData.getQualityId());

                    BatchData trueBatch = batchDao.findIsProductionPlanTrueByControlId(batchData.getId());
                    if (trueBatch != null) {
                        batchData.setIsProductionPlanned(true);
                    } else {
                        batchData.setIsProductionPlanned(false);
                    }
                    /*
                     * if (batchData.getBatchData().stream().anyMatch(x ->
                     * x.getIsProductionPlanned() == true)) {
                     * batchData.setIsProductionPlanned(true); } else {
                     * batchData.setIsProductionPlanned(false); }
                     */

                    list.add(
                            new GetAllStockWithPartyNameResponse(batchData, batchDataList, batchData.getQualityName()));

                }

            }

        } else if (getBy.equals("own")) {

            data = stockMastDao.getAllStockWithPartyNameByCreatedBy(id);

            if (data.isPresent()) {
                List<GetAllBatchResponse> batchDataList = new ArrayList<>();
                for (GetAllStockWithPartyNameResponse batchData : data.get()) {
                    batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());

                    BatchData trueBatch = batchDao.findIsProductionPlanTrueByControlId(batchData.getId());
                    if (trueBatch != null) {
                        batchData.setIsProductionPlanned(true);
                    } else {
                        batchData.setIsProductionPlanned(false);
                    }
                    // String qualityName =
                    // qualityNameDao.getQualityNameDetailByQualitytEntryId(batchData.getQualityId());
                    list.add(
                            new GetAllStockWithPartyNameResponse(batchData, batchDataList, batchData.getQualityName()));

                }

            }
        } else if (getBy.equals("group")) {

            UserData userData = userDao.findUserById(id);

            if (userData.getUserHeadId().equals(userData.getId())) {
                // master user
                data = stockMastDao.getAllStockWithPartyNameByUserHeadIdAndCreatedBy(id, id);
            } else {
                UserData userOperator = userDao.getUserById(id);
                data = stockMastDao.getAllStockWithPartyNameByUserHeadIdAndCreatedBy(userOperator.getId(),
                        userOperator.getUserHeadId());
            }
            if (data.isPresent()) {
                List<GetAllBatchResponse> batchDataList = new ArrayList<>();
                for (GetAllStockWithPartyNameResponse batchData : data.get()) {
                    batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());

                    BatchData trueBatch = batchDao.findIsProductionPlanTrueByControlId(batchData.getId());
                    if (trueBatch != null) {
                        batchData.setIsProductionPlanned(true);
                    } else {
                        batchData.setIsProductionPlanned(false);
                    }
                    // String qualityName =
                    // qualityNameDao.getQualityNameDetailByQualitytEntryId(batchData.getQualityId());
                    list.add(
                            new GetAllStockWithPartyNameResponse(batchData, batchDataList, batchData.getQualityName()));

                }

            }

        }

        /*
         * list.forEach(stock -> { int count = 0;//count the production plan gr //check
         * the batches is produciton plan for (BatchData batchData :
         * stock.getBatchData()) {
         *
         * if (batchData.getIsProductionPlanned() == true) count++; }
         *
         * if (count == stock.getBatchData().size()) stock.setIsProductionPlanned(true);
         * else stock.setIsProductionPlanned(false);
         *
         * });
         */
        return list;

    }

    @Transactional
    public FilterResponse<GetAllStockWithPartyNameResponse> getAllStockBatchPaginatedAndFiltered(
            GetBYPaginatedAndFiltered requestParam, String id) throws Exception {
        List<GetAllStockWithPartyNameResponse> data = null;

        List<GetAllStockWithPartyNameResponse> list = new ArrayList<>();
        Pageable pageable = filterService.getPageable(requestParam.getData());
        Boolean flag = false;
        String getBy = requestParam.getGetBy();
        List<Filter> filtersParam = requestParam.getData().getParameters();
        HashMap<String, List<String>> subModelCase = new HashMap<String, List<String>>();
        subModelCase.put("qualityName", new ArrayList<String>(Arrays.asList("quality", "qualityName", "qualityName")));
        subModelCase.put("partyName", new ArrayList<String>(Arrays.asList("party", "partyName")));
        // subModelCase.put("userHeadId",new
        // ArrayList<String>(Arrays.asList("userHeadData","id")));
        // subModelCase.put("createdBy",new
        // ArrayList<String>(Arrays.asList("createdBy","id")));
        // subModelCase.put("userHeadName",new
        // ArrayList<String>(Arrays.asList("userHeadData","userName")));
        // subModelCase.put("createdByName",new
        // ArrayList<String>(Arrays.asList("createdBy","userName")));
        Specification<StockMast> filterSpec = specificationManager.getSpecificationFromFilters(filtersParam,
                requestParam.getData().isAnd, subModelCase);

        Page queryResponse = null;

        if (id == null || getBy.equals("all")) {
            queryResponse = stockMastDao.findAll(filterSpec, pageable);

        } else if (requestParam.getGetBy().equals("own")) {
            UserData userData = userDao.findUserById(Long.parseLong(id));
            if (userData.getUserHeadId().equals(0)) {
                queryResponse = stockMastDao.findAll(filterSpec, pageable);
            } else {
                List<Filter> filters = new ArrayList<Filter>();

                filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")), QueryOperator.EQUALS, id));

                Specification<StockMast> spec = specificationManager.getSpecificationFromFilters(filters, true,
                        subModelCase);
                spec = spec.and(filterSpec);

                queryResponse = stockMastDao.findAll(spec, pageable);

            }

        } else if (requestParam.getGetBy().equals("group")) {

            UserData userData = userDao.findUserById(Long.parseLong(id));
            if (userData.getUserHeadId().equals(0)) {
                queryResponse = stockMastDao.findAll(filterSpec, pageable);
            } else if (userData.getUserHeadId().equals(userData.getId())) {
                // master user
                List<Filter> filters = new ArrayList<Filter>();

                filters.add(new Filter(new ArrayList<String>(Arrays.asList("userHeadId")), QueryOperator.EQUALS, id));
                filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")), QueryOperator.EQUALS, id));

                Specification<StockMast> spec = specificationManager.getSpecificationFromFilters(filters, false,
                        subModelCase);

                spec = spec.and(filterSpec);

                queryResponse = stockMastDao.findAll(spec, pageable);
            } else {
                UserData userOperator = userDao.getUserById(Long.parseLong(id));
                List<Filter> filters = new ArrayList<Filter>();

                filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")), QueryOperator.EQUALS, id));
                Specification<StockMast> spec = specificationManager.getSpecificationFromFilters(filters,
                        requestParam.getData().isAnd, subModelCase);
                spec = spec.and(filterSpec);
                queryResponse = stockMastDao.findAll(spec, pageable);
            }

        }
        System.out.println("specification craeting completion");
        data = queryResponse.getContent();
        if (!data.isEmpty()) {

            for (StockMast stockMastData : data) {
                HashMap<String, GetAllBatchResponse> mtrSumData = new HashMap<String, GetAllBatchResponse>();

                GetAllStockWithPartyNameResponse batchData = new GetAllStockWithPartyNameResponse(stockMastData,
                        stockMastData.getParty() == null ? null : stockMastData.getParty().getPartyName(),
                        stockMastData.getQuality() == null ? null
                                : stockMastData.getQuality().getQualityName().getQualityName());
                Boolean trueBatch = false;
                for (BatchData batch : stockMastData.getBatchData()) {
                    if (mtrSumData.containsKey(batch.getBatchId())) {
                        //ObjectMapper objectMapper =new ObjectMapper();
                        //System.out.println("----"+ objectMapper.writeValueAsString(mtrSumData.get(batch.getBatchId())));
                        mtrSumData.get(batch.getBatchId()).updateGetAllBatchResponse(batch);
                    } else {
                        mtrSumData.put(batch.getBatchId(),
                                new GetAllBatchResponse(batch.getMtr(), batch.getWt(), batch.getBatchId()));
                    }
                    trueBatch = trueBatch || batch.getIsProductionPlanned();
                }
                // batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());
                // String qualityName =
                // qualityNameDao.getQualityNameDetailByQualitytEntryId(batchData.getQualityId());

                if (trueBatch) {
                    batchData.setIsProductionPlanned(true);
                } else {
                    batchData.setIsProductionPlanned(false);
                }

                list.add(new GetAllStockWithPartyNameResponse(batchData,
                        new ArrayList<GetAllBatchResponse>(mtrSumData.values()), batchData.getQualityName()));

            }

        }

        FilterResponse<GetAllStockWithPartyNameResponse> response = new FilterResponse<GetAllStockWithPartyNameResponse>(
                list, queryResponse.getNumber(), queryResponse.getNumberOfElements(),
                (int) queryResponse.getTotalElements());

        return response;
    }

    public List<GetAllStockWithPartyNameResponse> getAllAvailableStockBatch(String getBy, Long id) throws Exception {
        Optional<List<GetAllStockWithPartyNameResponse>> data = null;
        List<GetAllStockWithPartyNameResponse> list = new ArrayList<>();

        Boolean flag = false;

        if (id == null) {
            data = stockMastDao.getAllStockWithPartyName();
            if (data.isPresent()) {
                List<GetAllBatchResponse> batchDataList = new ArrayList<>();
                for (GetAllStockWithPartyNameResponse batchData : data.get()) {
                    batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());

                    String qualityName = qualityNameDao
                            .getQualityNameDetailByQualitytEntryId(batchData.getQuality().getId());
                    list.add(new GetAllStockWithPartyNameResponse(batchData, batchDataList, qualityName));

                }

            }

        } else if (getBy.equals("own")) {

            data = stockMastDao.getAllStockWithPartyNameByCreatedBy(id);

            if (data.isPresent()) {
                List<GetAllBatchResponse> batchDataList = new ArrayList<>();
                for (GetAllStockWithPartyNameResponse batchData : data.get()) {
                    batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());

                    String qualityName = qualityNameDao
                            .getQualityNameDetailByQualitytEntryId(batchData.getQuality().getId());
                    list.add(new GetAllStockWithPartyNameResponse(batchData, batchDataList, qualityName));

                }

            }
        } else if (getBy.equals("group")) {

            UserData userData = userDao.findUserById(id);

            if (userData.getUserHeadId().equals(userData.getId())) {
                // master user
                data = stockMastDao.getAllStockWithPartyNameByUserHeadIdAndCreatedBy(id, id);
            } else {
                UserData userOperator = userDao.getUserById(id);
                data = stockMastDao.getAllStockWithPartyNameByUserHeadIdAndCreatedBy(userOperator.getId(),
                        userOperator.getUserHeadId());
            }
            if (data.isPresent()) {
                List<GetAllBatchResponse> batchDataList = new ArrayList<>();
                for (GetAllStockWithPartyNameResponse batchData : data.get()) {
                    batchDataList = batchDao.findAllBatchesByControlId(batchData.getId());

                    String qualityName = qualityNameDao
                            .getQualityNameDetailByQualitytEntryId(batchData.getQuality().getId());
                    list.add(new GetAllStockWithPartyNameResponse(batchData, batchDataList, qualityName));

                }

            }

        }

        list.forEach(stock -> {
            int count = 0;// count the production plan gr
            // check the batches is produciton plan
            if(stock.getBatchData()!=null) {
                for (BatchData batchData : stock.getBatchData()) {

                    if (batchData.getIsProductionPlanned() == true)
                        count++;
                }

                if (count == stock.getBatchData().size())
                    stock.setIsProductionPlanned(true);
                else
                    stock.setIsProductionPlanned(false);
            }
        });

        // get only those who's batches production is not planned
        list = list
                .stream().filter(stock -> stock.getBatchData()!=null?stock.getBatchData().stream()
                        .filter(batch -> batch.getIsProductionPlanned() == false).findAny().isPresent():null)
                .collect(Collectors.toList());

        // to quick count using stream
        /*
         * System.out.println("sum:"+list.forEach(e->{
         * e.getBatchData().stream().filter(x-> x.getIsProductionPlanned()==false &&
         * x.getBatchId().equals("1654")).mapToDouble(x->x.getMtr()).sum(); }));
         */

        return list;
    }

    public StockMast getStockBatchById(Long id) throws Exception {
        StockMast data = stockMastDao.findByStockId(id);
        List<BatchData> batchDataList = batchDao.findByControlIdWithExtraBatch(data.getId(), false);

        StockMast stockMast = new StockMast(data);
        stockMast.setBatchData(batchDataList);
        if (stockMast != null) {
            int count = 0;// count the production plan gr
            // check the batches is produciton plan
            for (BatchData batchData : stockMast.getBatchData()) {

                if (batchData.getIsProductionPlanned() == true)
                    count++;
            }

            if (count == data.getBatchData().size())
                data.setIsProductionPlanned(true);
            else
                data.setIsProductionPlanned(false);

            //AddStockBatch record = new AddStockBatch(stockMast);
            return data;
        } else
            throw new Exception("no data found for StockId: " + id);
    }

    public void updateBatch(StockMast stockMast, String id) throws Exception {
        // first check the batch id is null or not

        List<BatchData> batchDataList = new ArrayList<>();
        Long batchId = 0l, max = 0l;

        Optional<StockMast> original = stockMastDao.findById(stockMast.getId());
        if (original.isEmpty()) {
            throw new Exception(ConstantFile.StockBatch_Not_Found + stockMast.getId());
        }

        // Validate, if batch is not given to the production planning then throw the
        // exception
        /*
         * if (original.get().getIsProductionPlanned()) { throw new
         * Exception("BatchData is already sent to production, for id:" +
         * stockMast.getId()); }
         */

        // for delete the batch gr if not coming from FE

        // ##Get the data first from the list
        Map<Long, Boolean> batchGr = new HashMap<>();
        List<BatchData> batchData = batchDao.findByControlId(stockMast.getId());
        for (BatchData batch : batchData) {
            batchGr.put(batch.getId(), false);

            if (batch.getIsExtra() == true) {
                batchDataList.add(batch);
            }
            // System.out.println(batch.getId());
        }

        // change the as per the data is coming from FE
        for (BatchData batch : stockMast.getBatchData()) {
            if (batch.getBatchId() == null || batch.getBatchId().isEmpty())
                throw new Exception("batch id can't be null");
            // System.out.println("coming:"+batch.getId());
            if (batchGr.containsKey(batch.getId())) {
                BatchData batchData1 = batchDao.getBatchDataById(batch.getId());
                batchData1.setBatchId(batch.getBatchId());
                batchData1.setMtr(batch.getMtr());
                batchData1.setWt(batch.getWt());
                batchData1.setIsProductionPlanned(batch.getIsProductionPlanned());

                batch = new BatchData(batchData1);
                batchGr.replace(batch.getId(), true);
            } else {
                batch = new BatchData(batch);
            }
            /*
             * ObjectMapper obj = new ObjectMapper();
             * System.out.println("-"+obj.writeValueAsString(batch));
             */
            batchId = Long.parseLong(batch.getBatchId());
            if (batchId > max) {
                max = batchId;
            }
            batch.setControlId(stockMast.getId());
            batchDataList.add(batch);
        }

        // for data entry user
        Party party = null;
        Quality quality = null;
        UserData userData = userDao.getUserById(Long.parseLong(id));
        if (userData.getIsMaster() == false || stockMast.getUserHeadId() == 0) {
            // fetch the party record to set the usert head
            party = partyDao.findByPartyId(stockMast.getParty().getId());
            stockMast.setUserHeadId(party.getUserHeadData().getId());
        } else {
            party = partyDao.findById(stockMast.getParty().getId()).get();
        }
        quality = qualityDao.findById(stockMast.getQuality().getId()).get();
        // update record
        stockMast.setQuality(quality);
        stockMast.setParty(party);

        stockMast.setBatchData(batchDataList);
        stockMastDao.save(stockMast);
        // batchDao.saveAll(batchDataList);
        // update the quality wt per 100 as well
        qualityDao.updateQualityWtAndMtrKgById(stockMast.getQuality().getId(), stockMast.getWtPer100m(),
                100 / stockMast.getWtPer100m());

        // update the sequence
        /*
         * BatchSequence batchSequence = batchSequneceDao.getBatchSequence(); if (max >=
         * batchSequence.getSequence()) {
         * batchSequneceDao.updateBatchSequence(batchSequence.getId(), max + 1); }
         */

        // remove the record jiska flag false ho
        for (Map.Entry<Long, Boolean> entry : batchGr.entrySet()) {
            // System.out.println(entry.getKey()+":"+entry.getValue());
            if (entry.getValue() == false) {
                // set null if the batch gr is not comming from FE
                batchDao.setBatchIdAndControlIdByEntryId(entry.getKey(), null, stockMast.getId());
                // batchDao.setControlIdByBatchEntryId(entry.getKey(),);
                // batchDao.deleteByIdWithProduction(entry.getKey());

            }
        }

    }

    public void deleteStockBatch(Long id) throws Exception {
        Optional<StockMast> stockMast = stockMastDao.findById(id);
        if (stockMast.isEmpty()) {
            throw new Exception(ConstantFile.StockBatch_Not_Found + id);
        }

        if (Objects.equals(stockMast.get().getIsProductionPlanned(), true)) {
            throw new Exception("Can't delete the batch, already in production, for id:" + id);
        }
        List<ProductionPlan> productionPlans = productionPlanService.getProductionByStockId(id);
        if (productionPlans != null)
            throw new Exception("can't delete the stock , because already sent to production");

        stockMastDao.deleteById(id);
    }

    public List<StockMast> findByQualityId(Long id) {
        return stockMastDao.findByQualityId(id);
    }

    public List<BatchData> getBatchById(String batchId, Long controlId) throws Exception {

        List<BatchData> batchData = batchDao.getBatchByBatchId(batchId);

        if (batchData.isEmpty())
            throw new Exception(ConstantFile.Batch_Data_Not_Found + batchId);

        return batchData;

    }

    public List<GetAllBatch> byQualityAndPartyWithoutProductionPlan(Long qualityId, Long partyId, String id)
            throws Exception {

        // get the user record first
        Long userId = Long.parseLong(id);

        UserData userData = userDao.getUserById(userId);
        Long userHeadId = null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());

        List<StockMast> stockMast = null;
        // filter the record
        if (permissions.getViewAll()) {
            userId = null;
            userHeadId = null;
            stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId, partyId);
        } else if (permissions.getViewGroup()) {
            // check the user is master or not ?
            // admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId, partyId);
            } else if (userData.getUserHeadId() > 0) {
                // check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId, partyId, userId, userHeadId);

            }
        } else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId = null;
            stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId, partyId, userId, userHeadId);
        }

        /*
         * if(stockMast.isEmpty()) { throw new
         * Exception(CommonMessage.StockBatch_Not_Found); }
         */

        List<GetAllBatch> getAllBatchList = new ArrayList<>();

        List<GetAllBatch> getAllBatch;

        for (StockMast stockMast1 : stockMast) {

            getAllBatch = batchDao.getBatchResponseByStockIdWithoutProductionPlan(stockMast1.getId());

            if (!getAllBatch.isEmpty())
                getAllBatchList.addAll(getAllBatch);
        }

        /*
         * if(getAllBatchList.isEmpty()) throw new
         * Exception("May all batches planned or not available ");
         */
        return getAllBatchList;

    }

    public void updateBatchForMerge(List<MergeSplitBatch> batchData1) throws Exception {
        int k = 0, i = 0, n = batchData1.size();

        while (i < n) {
            k = 0;
            for (BatchData batchData : batchData1.get(i).getBatchDataList()) {
                if (batchData.getIsProductionPlanned() == true)
                    throw new Exception(
                            "Production is planned already so Batch can't be updated for id:" + batchData.getBatchId());

                batchData.setBatchId(batchData1.get(i).getBatchId());
                batchData.setControlId(batchData1.get(i).getControlId());
                batchDao.save(batchData);
                k++;
            }
            i++;
        }

    }

    public List<GetAllBatch> getBatchWithoutProductionPlanByPartyAndQuality(Long qualityId, Long partyId)
            throws Exception {

        List<StockMast> stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId, partyId);
        if (stockMast == null) {
            throw new Exception("No such batch is available for partyId:" + partyId + " and QualityId:" + qualityId);
        }

        List<GetAllBatch> getAllBatchList = new ArrayList<>();
        List<String> batchName = new ArrayList<>();
        List<Boolean> productionPlanned = new ArrayList<>();
        List<Long> controlId = new ArrayList<>();

        GetAllBatch getAllBatch;

        for (StockMast stockMast1 : stockMast) {

            List<BatchData> batch = batchDao.findByControlId(stockMast1.getId());

            for (BatchData batchData : batch) {
                if (batchData.getIsBillGenrated() == true)
                    continue;

                // Take another arraylist because it is not working with Object arrayList
                if (!batchName.contains(batchData.getBatchId())) {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                } else if (!controlId.contains(batchData.getControlId())) {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }

            }

        }

        // storing all the data of batchName to object
        for (int x = 0; x < controlId.size(); x++) {
            getAllBatch = new GetAllBatch();
            getAllBatch.setBatchId(batchName.get(x));
            getAllBatch.setControlId(controlId.get(x));
            getAllBatch.setProductionPlanned(productionPlanned.get(x));
            getAllBatchList.add(getAllBatch);
        }

        if (getAllBatchList.isEmpty())
            throw new Exception("May all batches planned or not available ");
        return getAllBatchList;

    }

    public List<GetAllBatchWithProduction> getAllBatchByMaster(Long userHeadId) throws Exception {

        List<StockMast> stockMastList = stockMastDao.getAllStockByUserHeadId(userHeadId);
        List<GetAllBatchWithProduction> list = new ArrayList<>();
        if (stockMastList.isEmpty())
            return list;

        List<GetAllBatch> dataList = batchDao.getAllBatchWithoutBillGenerated(null,null,null,null);

        /*
         * //filter the batch //get the user record first //Long userId =
         * Long.parseLong(id);
         *
         *
         * UserData userData = userDao.getUserById(userId); // Long userHeadId=null;
         *
         * UserPermission userPermission = userData.getUserPermissionData(); Permissions
         * permissions = new Permissions(userPermission.getSb().intValue());
         */

        List<GetBatchWithControlId> batchDataForMergeBatch = null;// get all batch for based on mrge batch id
        HashSet<String> mergeBatchIdExistList = new HashSet<>();

        for (StockMast stockMast : stockMastList) {

            dataList = batchDao.getAllBatchWithoutBillGeneratedByStockId(stockMast.getId());
            batchDataForMergeBatch = batchDao.getAllMergeBatchWithoutBillGenratedByStockId(stockMast.getId());

            // filter the data if the batch is done with jet
            for (GetAllBatch getAllBatch : dataList) {
                ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(getAllBatch.getBatchId());

                if (productionPlan == null || productionPlan.getStatus() == false)
                    continue;
                // System.out.println(productionPlan.getId());
                JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
                if (jetData == null)
                    continue;
                // System.out.println(jetData.getStatus());
                if (jetData.getStatus() == JetStatus.success) {

                    list.add(new GetAllBatchWithProduction(getAllBatch, productionPlan.getId()));
                }

            }

            // filter the data if the batch is done with jet
            for (GetBatchWithControlId getAllBatch : batchDataForMergeBatch) {
                ProductionPlan productionPlan = productionPlanService
                        .getProductionByBatchId(getAllBatch.getMergeBatchId());

                if (productionPlan == null)
                    continue;

                if (productionPlan.getStatus() == false)
                    continue;
                // System.out.println(productionPlan.getId());
                JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
                if (jetData == null)
                    continue;
                // System.out.println(jetData.getStatus());
                if (jetData.getStatus() == JetStatus.success) {
                    List<GetBatchWithControlId> listOfBatch = batchDao
                            .getBatchesByMergeBatchId(getAllBatch.getMergeBatchId());

                    for (GetBatchWithControlId batch : listOfBatch) {

                        GetAllBatchWithProduction batchDetail = new GetAllBatchWithProduction(batch,
                                productionPlan.getId());
                        List<BatchData> batchDataList = batchDao.getBatchByMergeBatchIdAndBatchIdForFinishMtrSave(
                                batch.getBatchId(), getAllBatch.getMergeBatchId());
                        if (batchDataList.isEmpty())
                            continue;
                        batchDetail.setBatchId(getAllBatch.getMergeBatchId() + "-" + batchDetail.getBatchId());
                        batchDetail.setProductionPlanned(true);

                        if (!mergeBatchIdExistList.contains(batchDetail.getBatchId())) {
                            mergeBatchIdExistList.add(batchDetail.getBatchId());
                            list.add(batchDetail);
                        }

                    }

                }

            }
        }

        // List<GetAllBatch> getAllBatchList =
        // batchDao.getAllBatchWithoutBillGeneratedByPartyIdAndQualityId(partyId,qualityId);
        /*
         * if(list.isEmpty()) throw new Exception("no data found");
         */

        return list;

    }

    public FilterResponse<BatchToPartyAndQuality> getAllBatchDetailPaginated(GetBYPaginatedAndFiltered requestParam,
                                                                             String id, Boolean isProductionPlan) throws Exception {
        List<String> batchIds = new ArrayList<>();
        // get the user record first
        Long userId = Long.parseLong(id);

        UserData userData = userDao.getUserById(userId);
        Long userHeadId = null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());
        List<GetBatchWithControlId> batchData = null;
        List<GetBatchWithControlId> batchDataForMergeBatch = null;// get all batch for based on mrge batch id

        List<BatchToPartyAndQuality> getAllBatchWithPartyAndQualities = new ArrayList<>();

        Long partyId = null;
        Long qualityEntryId = null;
        String batchId = null;

        // batchData.addAll(batchDataForMergeBatch);
        for (int i = 0; i < requestParam.getData().getParameters().size(); i++) {
            Filter filter = requestParam.getData().getParameters().get(i);
            String field = filter.getField().get(0);
            String value = filter.getValue();

            if (field.equals("partyId"))
                partyId = Long.parseLong(value);

            if (field.equals("qualityEntryId"))
                qualityEntryId = Long.parseLong(value);

            if (field.equals("batchId"))
                batchId = value;

        }

        // filter the record
        if (permissions.getViewAll()) {
            userId = null;
            userHeadId = null;
            batchData = batchDao.findAllBasedOnControlIdAndBatchId(batchId,partyId,qualityEntryId);
            batchDataForMergeBatch = batchDao.findAllMergeBatch(batchId,partyId,qualityEntryId);

        } else if (permissions.getViewGroup()) {
            // check the user is master or not ?
            // admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                batchData = batchDao.findAllBasedOnControlIdAndBatchId(batchId,partyId,qualityEntryId);
                batchDataForMergeBatch = batchDao.findAllMergeBatch(batchId,partyId,qualityEntryId);

            } else if (userData.getUserHeadId() > 0) {
                // check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                batchData = batchDao.findAllBasedOnControlIdAndBatchId(batchId,partyId,qualityEntryId);
                batchDataForMergeBatch = batchDao.findAllMergeBatch(batchId,partyId,qualityEntryId);

            }
        } else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId = null;
            batchData = batchDao.findAllBasedOnControlIdAndBatchId(batchId,partyId,qualityEntryId);
            batchDataForMergeBatch = batchDao.findAllMergeBatch(batchId,partyId,qualityEntryId);
        }

        for (GetBatchWithControlId batch : batchData) {

            Optional<StockMast> stockMast = stockMastDao.findById(batch.getControlId());
            if (stockMast.get().getQuality() != null && stockMast.get().getParty() != null) {
                Quality quality = stockMast.get().getQuality();

                Party party = stockMast.get().getParty();
                if (partyId != null)
                    if (!partyId.equals(party.getId()))
                        continue;

                if (qualityEntryId != null)
                    if (!qualityEntryId.equals(quality.getId()))
                        continue;

                if (batchId != null)
                    if ((!batch.getBatchId().contains(batchId)))
                        continue;

//                if (isProductionPlan != null)
//                    if (isProductionPlan.equals(stockMast.get().getIsProductionPlanned()))
//                        continue;

                QualityName qualityName = quality.getQualityName();
                BatchToPartyAndQuality batchToPartyAndQuality = new BatchToPartyAndQuality(quality, party, batch,
                        qualityName);

                // check that the process and party shade is exist or not
                // if not then set the detail by null
                ProductionPlan productionPlan = productionPlanService
                        .getProductionDataByBatchAndStock(batch.getBatchId(), batch.getControlId());
                if (productionPlan == null) {
                    batchToPartyAndQuality.setPartyShadeNo(null);
                    batchToPartyAndQuality.setProcessName(null);
                } else {
                    // get the shade and process
                    Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
                    DyeingProcessMast dyeingProcessMast = dyeingProcessService
                            .getDyeingProcessById(shadeMast.get().getProcessId());

                    if (dyeingProcessMast == null || shadeMast.isEmpty())
                        continue;

                    batchToPartyAndQuality.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
                    batchToPartyAndQuality.setProcessName(dyeingProcessMast.getProcessName());
                }
                // add the record
                getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);
                batchIds.add(batchToPartyAndQuality.getBatchId());
            }
        }

        // merge batch filter api
        for (GetBatchWithControlId batch : batchDataForMergeBatch) {
            // get batches based on batch id and stock id by mergebatchId

            List<GetBatchWithControlId> basedOnBatch = batchDao
                    .getBatcheAndStockIdByMergeBatchId(batch.getMergeBatchId());
            BatchToPartyAndQuality batchToPartyAndQuality = new BatchToPartyAndQuality();
            for (GetBatchWithControlId batchByMergeBatch : basedOnBatch) {
                // System.out.println("stockId:"+batchByMergeBatch.getControlId());

                Optional<StockMast> stockMast = stockMastDao.findById(batchByMergeBatch.getControlId());
                if (stockMast.get().getQuality().getId() != null && stockMast.get().getParty().getId() != null) {

                    Quality quality = stockMast.get().getQuality();

                    QualityName qualityName = quality.getQualityName();
                    Party party = stockMast.get().getParty();
                    if (partyId != null)
                        if (!partyId.equals(party.getId()))
                            continue;

                    if (qualityEntryId != null)
                        if (!qualityEntryId.equals(quality.getId()))
                            continue;

                    if (batchId != null)
                        if ((!batch.getBatchId().contains(batchId)))
                            continue;

//                    if (isProductionPlan != null)
//                        if (isProductionPlan.equals(stockMast.get().getIsProductionPlanned()))
//                            continue;

                    batchToPartyAndQuality
                            .setPartyName(batchToPartyAndQuality.getPartyName() == null ? party.getPartyName()
                                    : batchToPartyAndQuality.getPartyName() + "," + party.getPartyName());
                    batchToPartyAndQuality
                            .setQualityId(batchToPartyAndQuality.getQualityId() == null ? quality.getQualityId()
                                    : batchToPartyAndQuality.getQualityId() + "," + quality.getQualityId());
                    batchToPartyAndQuality
                            .setPartyId(batchToPartyAndQuality.getPartyId() == null ? party.getId().toString()
                                    : batchToPartyAndQuality.getPartyId() + "," + party.getId().toString());
                    batchToPartyAndQuality.setQualityEntryId(
                            batchToPartyAndQuality.getQualityEntryId() == null ? quality.getId().toString()
                                    : batchToPartyAndQuality.getQualityEntryId() + "," + quality.getId());
                    batchToPartyAndQuality.setQualityName(
                            batchToPartyAndQuality.getQualityName() == null ? qualityName.getQualityName()
                                    : batchToPartyAndQuality.getQualityName() + "," + qualityName.getQualityName());

                }

                batchToPartyAndQuality.setTotalMtr(
                        changeInFormattedDecimal(batchDao.getTotalMtrByMergeBatchId(batch.getMergeBatchId())));
                batchToPartyAndQuality.setTotalWt(
                        changeInFormattedDecimal(batchDao.getTotalWtByMergeBatchId(batch.getMergeBatchId())));
                batchToPartyAndQuality.setMergeBatchId(batch.getMergeBatchId());
                batchToPartyAndQuality.setBatchId(batch.getMergeBatchId());
                if (!batchIds.contains(batchToPartyAndQuality.getBatchId())) {
                    // add the record
                    getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);
                    batchIds.add(batchToPartyAndQuality.getBatchId());
                }

            }




        }

        /*
         * if(getAllBatchWithPartyAndQualities.isEmpty()) throw new
         * Exception(CommonMessage.StockBatch_Not_Found);
         */

        int pageSize = requestParam.getData().getPageSize();
        int pageIndex = requestParam.getData().getPageIndex();
        FilterResponse<BatchToPartyAndQuality> response = new FilterResponse<BatchToPartyAndQuality>(
                getAllBatchWithPartyAndQualities.subList(
                        Integer.min(pageIndex * pageSize, getAllBatchWithPartyAndQualities.size()),
                        Integer.min((pageIndex + 1) * pageSize, getAllBatchWithPartyAndQualities.size())),
                pageIndex, pageSize, getAllBatchWithPartyAndQualities.size());
        return response;

    }

    public List<BatchToPartyAndQuality> getAllBatchDetail(String id) throws Exception {

        List<String> batchIds = new ArrayList<>();
        // get the user record first
        Long userId = Long.parseLong(id);

        UserData userData = userDao.getUserById(userId);
        Long userHeadId = null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());
        List<GetBatchWithControlId> batchData = null;
        List<GetBatchWithControlId> batchDataForMergeBatch = null;// get all batch for based on mrge batch id

        List<BatchToPartyAndQuality> getAllBatchWithPartyAndQualities = new ArrayList<>();

        // filter the record
        if (permissions.getViewAll()) {
            userId = null;
            userHeadId = null;
            batchData = batchDao.findAllBasedOnControlIdAndBatchId(null,null,null);
            batchDataForMergeBatch = batchDao.findAllMergeBatch(null,null,null);

        } else if (permissions.getViewGroup()) {
            // check the user is master or not ?
            // admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                batchData = batchDao.findAllBasedOnControlIdAndBatchId(null,null,null);
                batchDataForMergeBatch = batchDao.findAllMergeBatch(null,null,null);

            } else if (userData.getUserHeadId() > 0) {
                // check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                batchData = batchDao.findAllBasedOnControlIdAndBatchIdByCreatedAndHeadId(userId, userHeadId);
                batchDataForMergeBatch = batchDao
                        .findAllBasedOnControlIdAndBatchIdAndMergeBatchIdByCreatedAndHeadId(userId, userHeadId);

            }
        } else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId = null;
            batchData = batchDao.findAllBasedOnControlIdAndBatchIdByCreatedAndHeadId(userId, userHeadId);
            batchDataForMergeBatch = batchDao.findAllBasedOnControlIdAndBatchIdAndMergeBatchIdByCreatedAndHeadId(userId,
                    userHeadId);
        }

        // batchData.addAll(batchDataForMergeBatch);

        for (GetBatchWithControlId batch : batchData) {
            Optional<StockMast> stockMast = stockMastDao.findById(batch.getControlId());
            if (stockMast.get().getQuality().getId() != null && stockMast.get().getParty().getId() != null) {
                Optional<Quality> quality = qualityDao.findById(stockMast.get().getQuality().getId());

                Optional<Party> party = partyDao.findById(stockMast.get().getParty().getId());

                QualityName qualityName = quality.get().getQualityName();
                BatchToPartyAndQuality batchToPartyAndQuality = new BatchToPartyAndQuality(quality.get(), party.get(),
                        batch, qualityName);

                // check that the process and party shade is exist or not
                // if not then set the detail by null
                ProductionPlan productionPlan = productionPlanService
                        .getProductionDataByBatchAndStock(batch.getBatchId(), batch.getControlId());
                if (productionPlan == null) {
                    batchToPartyAndQuality.setPartyShadeNo(null);
                    batchToPartyAndQuality.setProcessName(null);
                } else {
                    // get the shade and process
                    Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
                    DyeingProcessMast dyeingProcessMast = dyeingProcessService
                            .getDyeingProcessById(shadeMast.get().getProcessId());

                    if (dyeingProcessMast == null || shadeMast.isEmpty())
                        continue;

                    batchToPartyAndQuality.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
                    batchToPartyAndQuality.setProcessName(dyeingProcessMast.getProcessName());
                }
                // add the record
                getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);
                batchIds.add(batchToPartyAndQuality.getBatchId());
            }
        }

        // merge batch filter api
        for (GetBatchWithControlId batch : batchDataForMergeBatch) {
            // get batches based on batch id and stock id by mergebatchId

            List<GetBatchWithControlId> basedOnBatch = batchDao
                    .getBatcheAndStockIdByMergeBatchId(batch.getMergeBatchId());
            BatchToPartyAndQuality batchToPartyAndQuality = new BatchToPartyAndQuality();
            for (GetBatchWithControlId batchByMergeBatch : basedOnBatch) {
                // System.out.println("stockId:"+batchByMergeBatch.getControlId());

                Optional<StockMast> stockMast = stockMastDao.findById(batchByMergeBatch.getControlId());
                if (stockMast.get().getQuality().getId() != null && stockMast.get().getParty().getId() != null) {

                    Optional<Quality> quality = qualityDao.findById(stockMast.get().getQuality().getId());

                    QualityName qualityName = quality.get().getQualityName();
                    Optional<Party> party = partyDao.findById(stockMast.get().getParty().getId());

                    // check that the process and party shade is exist or not
                    // if not then set the detail by null
                    /*
                     * ProductionPlan
                     * productionPlan=productionPlanService.getProductionDataByBatchAndStock(
                     * batchByMergeBatch.getBatchId(),batchByMergeBatch.getControlId());
                     * if(productionPlan==null) { batchToPartyAndQuality.setPartyShadeNo(null);
                     * batchToPartyAndQuality.setProcessName(null); } else { //get the shade and
                     * process Optional<ShadeMast> shadeMast =
                     * shadeService.getShadeMastById(productionPlan.getShadeId()); DyeingProcessMast
                     * dyeingProcessMast =
                     * dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());
                     *
                     * if(dyeingProcessMast==null || shadeMast.isEmpty()) continue;
                     *
                     *
                     * batchToPartyAndQuality.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
                     * batchToPartyAndQuality.setProcessName(dyeingProcessMast.getProcessName()); }
                     */

                    batchToPartyAndQuality
                            .setPartyName(batchToPartyAndQuality.getPartyName() == null ? party.get().getPartyName()
                                    : batchToPartyAndQuality.getPartyName() + "," + party.get().getPartyName());
                    batchToPartyAndQuality
                            .setQualityId(batchToPartyAndQuality.getQualityId() == null ? quality.get().getQualityId()
                                    : batchToPartyAndQuality.getQualityId() + "," + quality.get().getQualityId());
                    batchToPartyAndQuality
                            .setPartyId(batchToPartyAndQuality.getPartyId() == null ? party.get().getId().toString()
                                    : batchToPartyAndQuality.getPartyId() + "," + party.get().getId().toString());
                    batchToPartyAndQuality.setQualityEntryId(
                            batchToPartyAndQuality.getQualityEntryId() == null ? quality.get().getId().toString()
                                    : batchToPartyAndQuality.getQualityEntryId() + "," + quality.get().getId());
                    batchToPartyAndQuality.setQualityName(
                            batchToPartyAndQuality.getQualityName() == null ? qualityName.getQualityName()
                                    : batchToPartyAndQuality.getQualityName() + "," + qualityName.getQualityName());

                }

                batchToPartyAndQuality.setTotalMtr(
                        changeInFormattedDecimal(batchDao.getTotalMtrByMergeBatchId(batch.getMergeBatchId())));
                batchToPartyAndQuality.setTotalWt(
                        changeInFormattedDecimal(batchDao.getTotalWtByMergeBatchId(batch.getMergeBatchId())));

            }
            batchToPartyAndQuality.setMergeBatchId(batch.getMergeBatchId());
            batchToPartyAndQuality.setBatchId(batch.getMergeBatchId());

            if (!batchIds.contains(batchToPartyAndQuality.getBatchId())) {
                // add the record
                getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);
                batchIds.add(batchToPartyAndQuality.getBatchId());
            }

        }
        /*
         * if(getAllBatchWithPartyAndQualities.isEmpty()) throw new
         * Exception(CommonMessage.StockBatch_Not_Found);
         */

        return getAllBatchWithPartyAndQualities;
    }

    public BatchToPartyAndQuality getPartyAndQualityByBatch(Long controlId, String batchId) throws Exception {
        Optional<StockMast> stockMast = stockMastDao.findById(controlId);

        if (!stockMast.isPresent())
            throw new Exception(ConstantFile.Batch_Data_Not_Found + batchId);

        Optional<Party> party = partyDao.findById(stockMast.get().getParty().getId());
        if (!party.isPresent())
            throw new Exception(ConstantFile.Party_Not_Exist + " for " + batchId);

        Optional<Quality> quality = qualityDao.findById(stockMast.get().getQuality().getId());

        if (!quality.isPresent())
            throw new Exception(ConstantFile.Quality_Data_Not_Found + " for :" + batchId);

        BatchToPartyAndQuality batchToPartyAndQuality = new BatchToPartyAndQuality();
        batchToPartyAndQuality.setPartyId(party.get().getId().toString());
        batchToPartyAndQuality.setPartyName(party.get().getPartyName());
        batchToPartyAndQuality.setQualityEntryId(quality.get().getId().toString());
        batchToPartyAndQuality.setQualityId(quality.get().getQualityId());
        batchToPartyAndQuality.setQualityName(quality.get().getQualityName().getQualityName());
        batchToPartyAndQuality.setBatchId(batchId);
        batchToPartyAndQuality.setControlId(controlId);
        return batchToPartyAndQuality;
    }

    public Boolean deleteStockBatchWithControlAndBatchID(Long controlId, String batchId) throws Exception {

        List<BatchData> batchData = batchDao.findByControlIdAndBatchId(controlId, batchId);
        if (batchData.isEmpty())
            throw new Exception(ConstantFile.Batch_Data_Not_Found + batchId);
        for (BatchData batch : batchData) {
            batchDao.deleteById(batch.getId());
        }
        return true;

    }

    public void deleteBatchGr(Long id) throws Exception {
        Optional<BatchData> batchData = batchDao.findById(id);
        if (!batchData.isPresent())
            throw new Exception(ConstantFile.Batch_Data_Not_Found);

        batchDao.deleteById(batchData.get().getId());

    }

    public void updateBatchSplit(List<MergeSplitBatch> batchData1) throws Exception {

        int k = 0, i = 0, n = batchData1.size();

        while (i < n) {
            k = 0;
            if (batchData1.get(i).getIsSplit()) {
                List<BatchData> checkBatchIsAvailable = batchDao
                        .findByControlIdAndBatchId(batchData1.get(i).getControlId(), batchData1.get(i).getBatchId());
                if (!checkBatchIsAvailable.isEmpty()) {
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
        List<BatchData> checkBatchIsAvaialble = batchDao.findByControlIdAndBatchId(controlId, batchId);
        if (checkBatchIsAvaialble.isEmpty()) {
            return true;
        }

        return false;

    }

    public List<StockMast> getStockListByParty(Long partyId) {

        List<StockMast> stockMasts = stockMastDao.findByPartyId(partyId);

        return stockMasts;
    }

    public GetBatchWithControlId getBatchQTYById(String batchId, Long stockId) {
        GetBatchWithControlId getAllBatchResponse = batchDao.findByBatchIdAndControId(batchId, stockId);

        if (getAllBatchResponse != null)
            return getAllBatchResponse;

        return null;
    }

    public GetBatchWithControlId getBatchWithoutFinishMtrQTYById(String batchId, Long stockId) {
        GetBatchWithControlId getAllBatchResponse = batchDao.findByBatchIdAndControIdWithoutFinishMtr(batchId, stockId);

        if (getAllBatchResponse.getWT() != null)
            System.out.println("w:" + getAllBatchResponse.getWT());

        if (getAllBatchResponse != null)
            return getAllBatchResponse;

        return null;
    }

    // get stock data
    public StockMast getStockById(Long stockId) throws Exception {
        Optional<StockMast> stockMast = stockMastDao.findById(stockId);
        if (stockMast.isEmpty())
            throw new Exception("no data found");
        return stockMast.get();
    }

    // get all batches without production plan
    public List<GetBatchWithControlId> getAllBatchWithoutProductionPlan() throws Exception {

        List<GetBatchWithControlId> batchWithControlIdList = batchDao.findAllBatcheWithoutProductionPlan();
        if (batchWithControlIdList.isEmpty())
            throw new Exception("no batch found without production plan");

        return batchWithControlIdList;

    }

    // get stock which are without batches
    public List<GetAllStockWithoutBatches> getStockListWithoutBatches() throws Exception {
        List<GetAllStockWithoutBatches> getAllStockWithoutBatchesList = new ArrayList<>();

        List<StockMast> stockMastList = stockMastDao.getAllStock();
        for (StockMast stockMast : stockMastList) {
            List<BatchData> batchDataList = batchDao.findByControlId(stockMast.getId());
            if (batchDataList.isEmpty()) {
                GetAllStockWithoutBatches getAllStockWithoutBatches = new GetAllStockWithoutBatches(stockMast);
                getAllStockWithoutBatchesList.add(getAllStockWithoutBatches);
            }

        }
        if (getAllStockWithoutBatchesList.isEmpty())
            throw new Exception(ConstantFile.StockBatch_Without_Batch);

        return getAllStockWithoutBatchesList;
    }

    public List<StockMast> getStockBasedOnFilter(GetStockBasedOnFilter filter) throws Exception {
        try {
            Date fromDate = null;
            Date toDate = null;
            if (filter.getToDate().isEmpty() && filter.getFromDate().isEmpty()
                    && filter.getStock().getPartyCode().isEmpty() && filter.getStock().getPartyId() == null
                    && filter.getStock().getQualityEntryId() == null && filter.getStock().getBillNo().isEmpty()) {
                throw new Exception("please enter valid data");
            }
            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

            if (filter.getFromDate() != "")
                fromDate = datetimeFormatter1.parse(filter.getFromDate());

            if (filter.getToDate() != "")
                toDate = datetimeFormatter1.parse(filter.getToDate());

            List<StockMast> stockMastList = null;

            if (filter.getStock().getPartyCode() != "") {
                Party party = partyDao.findByPartyCode(filter.getStock().getPartyCode());
                if (party == null)
                    throw new Exception("no party found");

                if (filter.getStock().getPartyId() != null) {

                    if (!party.getId().equals(filter.getStock().getPartyId()))
                        throw new Exception("party no found for party code:" + filter.getStock().getPartyCode());

                    stockMastList = stockMastDao.findByQualityIdAndPartyIdAndDateFilter(party.getId(),
                            filter.getStock().getQualityEntryId(), filter.getStock().getBillNo(), toDate, fromDate);
                    if (stockMastList.isEmpty())
                        throw new Exception("no stock data found");
                    return stockMastList;

                } else {
                    stockMastList = stockMastDao.findByQualityIdAndPartyIdAndDateFilter(party.getId(),
                            filter.getStock().getQualityEntryId(), filter.getStock().getBillNo(), toDate, fromDate);
                    if (stockMastList.isEmpty())
                        throw new Exception("no stock data found");
                    return stockMastList;

                }

            } else {

                stockMastList = stockMastDao.findByQualityIdAndPartyIdAndDateFilter(filter.getStock().getPartyId(),
                        filter.getStock().getQualityEntryId(), filter.getStock().getBillNo(), toDate, fromDate);
                if (stockMastList.isEmpty())
                    throw new Exception("no stock data found");
                return stockMastList;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<StockMast> getStockByPartyId(Long partyId) throws Exception {
        List<StockMast> stockMastList = stockMastDao.findByPartyId(partyId);
        /*
         * if(stockMastList.isEmpty()) throw new Exception("no record found");
         */
        return stockMastList;

    }

    // batches by quality
    public List<BatchData> getAllBatchByQualityId(Long qualityId) throws Exception {
        List<BatchData> batchDataList = new ArrayList<>();

        Optional<Quality> qualityExist = qualityDao.findById(qualityId);
        if (qualityExist.isEmpty())
            throw new Exception(ConstantFile.Quality_Data_Not_Found + qualityId);
        List<StockMast> stockMastList = stockMastDao.findByQualityId(qualityId);

        for (StockMast stockMast : stockMastList) {
            List<BatchData> batchData = batchDao.findByControlId(stockMast.getId());
            if (!batchData.isEmpty()) {
                for (BatchData batch : batchData) {
                    batchDataList.add(batch);
                }

            }

        }

        if (batchDataList.isEmpty())
            throw new Exception("no batch found for quality:" + qualityId);

        return batchDataList;

    }

    // get complete stock batch detail of batch based on party and quality
    public List<StockMast> getStockBatchListById(Long qualityId, Long partyId) throws Exception {

        List<StockMast> stockMasts = stockMastDao.findByPartyIdAndQualityId(partyId, qualityId);
        if (stockMasts.isEmpty()) {
            throw new Exception(ConstantFile.StockBatch_Not_Found);
        }

        return stockMasts;

    }

    public List<GetAllBatch> getBatchListByPartyWithoutProductionPlan(Long partyId) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        List<GetAllBatch> list = new ArrayList<>();
        List<String> batchId = new ArrayList<>();

        Optional<Party> partyExist = partyDao.findById(partyId);
        if (partyExist.isEmpty())
            throw new Exception(constantFile.Party_Found);

        Optional<List<Quality>> qualityListByParty = qualityDao.findByPartyId(partyId);

        if (qualityListByParty.isEmpty())
            throw new Exception(constantFile.Quality_Data_Not_Found);

        for (Quality quality : qualityListByParty.get()) {
            QualityName qualityName = quality.getQualityName();
            List<StockMast> stockMastList = stockMastDao.findByQualityIdAndPartyId(quality.getId(), partyId);

            // batch list based on stock id
            for (StockMast stockMast : stockMastList) {
                List<GetBatchWithControlId> batchWithStockList = batchDao
                        .getBatchAndStockListWithoutProductionPlanByStockId(stockMast.getId());
                for (GetBatchWithControlId getBatchWithControlId : batchWithStockList) {
                    GetAllBatch getAllBatch = new GetAllBatch(partyExist.get(), quality, qualityName);
                    getAllBatch.setProductionPlanned(false);
                    getAllBatch.setIsBillGenerated(false);
                    getAllBatch.setBatchId(getBatchWithControlId.getBatchId());
                    getAllBatch.setControlId(getBatchWithControlId.getControlId());
                    getAllBatch.setTotalWt(changeInFormattedDecimal(getBatchWithControlId.getWT()));
                    getAllBatch.setTotalMtr(changeInFormattedDecimal(getBatchWithControlId.getMTR()));
                    batchId.add(getAllBatch.getBatchId());
                    // System.out.println();
                    System.out.println(objectMapper.writeValueAsString(getAllBatch));
                    list.add(getAllBatch);
                }
            }

            // by mrg batch id
            // batch list based on stock id with mergebatchId
            for (StockMast stockMast : stockMastList) {
                List<GetBatchWithControlId> batchWithStockList = batchDao
                        .getBatchAndStockListWithoutProductionPlanByStockIdAndBasedOnMergeBatchId(stockMast.getId());
                for (GetBatchWithControlId getBatchWithControlId : batchWithStockList) {
                    GetAllBatch getAllBatch = new GetAllBatch(partyExist.get(), quality, qualityName);

                    getAllBatch.setProductionPlanned(false);
                    getAllBatch.setIsBillGenerated(false);
                    getAllBatch.setBatchId(getBatchWithControlId.getMergeBatchId());
                    getAllBatch.setTotalWt(changeInFormattedDecimal(batchDao.getTotalWtByMergeBatchIdWithProductionFlag(
                            getBatchWithControlId.getMergeBatchId(), false)));
                    getAllBatch
                            .setTotalMtr(changeInFormattedDecimal(batchDao.getTotalMtrByMergeBatchIdWithProductionFlag(
                                    getBatchWithControlId.getMergeBatchId(), false)));

                    if (!batchId.contains(getBatchWithControlId.getMergeBatchId())) {
                        // System.out.println("merge:"+getBatchWithControlId.getMergeBatchId());
                        // getAllBatch.setBatchId(getBatchWithControlId.getMergeBatchId());
                        // System.out.println(objectMapper.writeValueAsString(getAllBatch));
                        list.add(getAllBatch);
                        // System.out.println("batch:"+getAllBatch.getBatchId());
                        batchId.add(getBatchWithControlId.getMergeBatchId());
                    }
                }
            }

        }

        // System.out.println(objectMapper.writeValueAsString(batchId));
        // System.out.println(objectMapper.writeValueAsString(list));

        /*
         * if(list.isEmpty()) throw new
         * Exception(commonMessage.StockBatch_Found_ByParty+partyId);
         */
        return list;
    }

    public static Double changeInFormattedDecimal(Double values) {
        // df2.setMaximumFractionDigits(2);
        if (values == null)
            return 0.0;
        else
            return Precision.round(values, 2);
    }

    public static List<BatchData> changeInFormattedDecimal(List<BatchData> batchDataList) {
        List<BatchData> newList = new ArrayList<>();
        // df2.setMaximumFractionDigits(2);
        if (batchDataList == null)
            return null;
        else {
            for (BatchData batchData : batchDataList) {
                BatchData batch = new BatchData(batchData);
                batch.setFinishMtr(Precision.round(batchData.getFinishMtr(), 2));
                batch.setMtr(Precision.round(batchData.getMtr(), 2));
                // batch.setWt(Precision.round(batchData.getFinishMtr(), 2));
                newList.add(batch);
            }
            return newList;
        }
    }

    public List<GetAllBatch> getBatchListByQualityWithoutProductionPlan(Long qualityId) throws Exception {
        List<GetAllBatch> list = new ArrayList<>();
        List<String> batchId = new ArrayList<>();
        Optional<Quality> qualityExist = qualityDao.findById(qualityId);

        if (qualityExist.isEmpty())
            throw new Exception(ConstantFile.Quality_Data_Not_Exist + qualityId);
        QualityName qualityName = qualityExist.get().getQualityName();

        Optional<Party> party = partyDao.findById(qualityExist.get().getParty().getId());
        if (party.isEmpty())
            throw new Exception(ConstantFile.Party_Not_Exist + qualityId);

        List<StockMast> stockMastList = stockMastDao.findByQualityIdAndPartyId(qualityId,
                qualityExist.get().getParty().getId());

        // batch list based on stock id
        for (StockMast stockMast : stockMastList) {
            List<GetBatchWithControlId> batchWithStockList = batchDao
                    .getBatchAndStockListWithoutProductionPlanByStockId(stockMast.getId());
            for (GetBatchWithControlId getBatchWithControlId : batchWithStockList) {
                GetAllBatch getAllBatch = new GetAllBatch(party.get(), qualityExist.get(), qualityName);
                getAllBatch.setProductionPlanned(false);
                getAllBatch.setIsBillGenerated(false);
                getAllBatch.setBatchId(getBatchWithControlId.getBatchId());
                getAllBatch.setControlId(getBatchWithControlId.getControlId());
                getAllBatch.setTotalMtr(
                        changeInFormattedDecimal(batchDao.getTotalMtrByBatchId(getBatchWithControlId.getBatchId())));
                getAllBatch.setTotalWt(
                        changeInFormattedDecimal(batchDao.getTotalWtByBatchId(getBatchWithControlId.getBatchId())));
                batchId.add(getAllBatch.getBatchId());
                list.add(getAllBatch);

            }

        }
        // by mrg batch id
        // batch list based on stock id with mergebatchId
        for (StockMast stockMast : stockMastList) {
            List<GetBatchWithControlId> batchWithStockList = batchDao
                    .getBatchAndStockListWithoutProductionPlanByStockIdAndBasedOnMergeBatchId(stockMast.getId());

            for (GetBatchWithControlId getBatchWithControlId : batchWithStockList) {
                GetAllBatch getAllBatch = new GetAllBatch(party.get(), qualityExist.get(), qualityName);
                getAllBatch.setProductionPlanned(false);
                getAllBatch.setIsBillGenerated(false);
                getAllBatch.setBatchId(getBatchWithControlId.getMergeBatchId());
                getAllBatch.setTotalWt(changeInFormattedDecimal(batchDao
                        .getTotalWtByMergeBatchIdWithProductionFlag(getBatchWithControlId.getMergeBatchId(), false)));
                getAllBatch.setTotalMtr(changeInFormattedDecimal(batchDao
                        .getTotalMtrByMergeBatchIdWithProductionFlag(getBatchWithControlId.getMergeBatchId(), false)));

                if (!batchId.contains(getBatchWithControlId.getMergeBatchId())) {
                    // System.out.println("merge:"+getBatchWithControlId.getMergeBatchId());
                    // getAllBatch.setBatchId(getBatchWithControlId.getMergeBatchId());
                    list.add(getAllBatch);
                    batchId.add(getBatchWithControlId.getMergeBatchId());
                }
            }
        }

        /*
         * if(list.isEmpty()) { throw new
         * Exception("no batch found for quality:"+qualityId); }
         */

        return list;
    }

    public List<GetAllBatch> byQualityAndPartyWithProductionPlan(Long qualityId, Long partyId) throws Exception {

        List<GetAllBatch> list = new ArrayList<>();
        List<StockMast> stockMastList = stockMastDao.findByQualityIdAndPartyId(qualityId, partyId);
        if (stockMastList.isEmpty()) {
            throw new Exception(ConstantFile.StockBatch_Found);
        }

        Party party = partyDao.findByPartyId(partyId);
        Optional<Quality> quality = qualityDao.findById(qualityId);

        // get the batch which are remain to be the part of invoice by party and quality

        for (StockMast stockMast : stockMastList) {
            List<GetBatchByInvoice> batchAndStockList = batchDao
                    .getBatcheByStockIdWithoutBillGenerated(stockMast.getId());
            for (GetBatchByInvoice g : batchAndStockList) {
                // check the batch is done with produciton or not
                ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(g.getBatchId());
                if (productionPlan == null || productionPlan.getStatus() == false)
                    continue;

                JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
                if (jetData.getStatus() == JetStatus.success) {
                    GetAllBatch getAllBatch = new GetAllBatch(g, party, quality);
                    list.add(getAllBatch);
                }

            }
        }

        // gt the mege batch as well by stock id
        for (StockMast stockMast : stockMastList) {
            List<GetBatchByInvoice> batchAndStockList = batchDao
                    .getMergeBatcheByStockIdWithoutBillGenerated(stockMast.getId());
            for (GetBatchByInvoice g : batchAndStockList) {
                // check the batch is done with produciton or not
                if (g.getMergeBatchId() == null)
                    continue;

                ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(g.getMergeBatchId());
                if (productionPlan.getStatus() == false)
                    continue;

                JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
                if (jetData.getStatus() == JetStatus.success) {
                    GetAllBatch getAllBatch = new GetAllBatch(g, party, quality);
                    getAllBatch.setBatchId(
                            g.getBatchId() == null ? g.getBatchId() : g.getMergeBatchId() + "-" + g.getBatchId());
                    list.add(getAllBatch);
                }

            }
        }

        // List<GetAllBatch> getAllBatchList =
        // batchDao.getAllBatchWithoutBillGeneratedByPartyIdAndQualityId(partyId,qualityId);
        if (list.isEmpty())
            throw new Exception(ConstantFile.StockBatch_Found);

        return list;

    }

    public List<GetAllBatch> getBatchByPartyAndQuality(Long qualityId, Long partyId) throws Exception {
        List<StockMast> stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId, partyId);
        if (stockMast.isEmpty()) {
            throw new Exception(ConstantFile.Batch_Data_Not_Found);
        }

        List<GetAllBatch> getAllBatchList = new ArrayList<>();
        List<String> batchName = new ArrayList<>();
        List<Boolean> productionPlanned = new ArrayList<>();
        List<Boolean> isBillGenerated = new ArrayList<>();
        List<Long> controlId = new ArrayList<>();

        GetAllBatch getAllBatch;

        for (StockMast stockMast1 : stockMast) {

            List<BatchData> batch = batchDao.findByControlId(stockMast1.getId());

            for (BatchData batchData : batch) {
                if (batchData.getIsBillGenrated() == true)
                    continue;

                // Take another arraylist because it is not working with Object arrayList
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
        Optional<Party> party = partyDao.findById(partyId);
        Optional<Quality> quality = qualityDao.findById(qualityId);
        QualityName qualityName = quality.get().getQualityName();

        // storing all the data of batchName to object
        for (int x = 0; x < controlId.size(); x++) {
            if (quality.get() != null && party.get() != null) {
                if (!quality.isPresent() && !party.isPresent())
                    continue;

                getAllBatch = new GetAllBatch(party.get(), quality.get(), qualityName);
                getAllBatch.setBatchId(batchName.get(x));
                getAllBatch.setControlId(controlId.get(x));
                getAllBatch.setProductionPlanned(productionPlanned.get(x));
                getAllBatch.setIsBillGenerated(isBillGenerated.get(x));
                getAllBatchList.add(getAllBatch);

            }

        }

        if (getAllBatchList.isEmpty())
            throw new Exception("May all batches planned or not available ");
        return getAllBatchList;

    }

    public List<BatchData> getBatchWithControlIdAndBatchId(String batchId, Long stockId) {
        List<BatchData> batchDataList = batchDao.findBatchWithBillGenerated(batchId, stockId);
        return batchDataList;
    }

    public StockMast getStockByStockId(Long stockId) {
        StockMast stockMast = stockMastDao.findByStockId(stockId);

        return stockMast;
    }

    public List<GetAllBatch> getAllBatchWithoutFilter() throws Exception {
        List<GetAllBatch> list = new ArrayList<>();
        List<GetBatchWithControlId> data = batchDao.getAllBatchQty();// get all batches without any filter

        for (GetBatchWithControlId batch : data) {

            // System.out.println("batchwt:"+batch.getWT());

            StockMast stockMast = stockMastDao.findByStockId(batch.getControlId());
            if (stockMast == null)
                continue;

            GetQualityResponse quality = qualityServiceImp.getQualityByID(stockMast.getQuality().getId());
            if (quality == null)
                continue;

            Party party = partyDao.findByPartyId(stockMast.getParty().getId());
            if (party == null)
                continue;

            GetAllBatch getAllBatch = new GetAllBatch(party, quality, batch);
            list.add(getAllBatch);

        }
        if (list.isEmpty())
            throw new Exception(ConstantFile.StockBatch_Not_Found);
        return list;
    }

    public List<StockMast> getAllStockWithoutPlan() throws Exception {
        List<StockMast> list = new ArrayList<>();
        List<Long> listOfStockId = new ArrayList<>();
        List<GetBatchWithControlId> stockIList = batchDao.getAllBatchQtyWithoutPlan();
        for (GetBatchWithControlId getBatchWithControlId : stockIList) {
            if (!listOfStockId.contains(getBatchWithControlId.getControlId()))
                listOfStockId.add(getBatchWithControlId.getControlId());
        }

        for (Long l : listOfStockId) {
            StockMast stockMast = stockMastDao.findByStockId(l);
            list.add(stockMast);
        }

        if (list.isEmpty()) {
            throw new Exception(constantFile.StockBatch_Not_Found);
        }

        return list;
    }

    // pagnation example
    public Page<StockMast> findPage(int pageno, int size) {
        Pageable pageable = PageRequest.of(pageno - 1, size);
        return stockMastDao.findAll(pageable);

        // after return

        /*
         *
         * List<StokMast> lst =page.getContent();
         *
         */
    }

    public FilterResponse<GetAllBatchWithProduction> getAllBatchWithoutBillGeneratedAllPaginated(
            GetBYPaginatedAndFiltered requestParam, String id) throws Exception {
        List<GetAllBatchWithProduction> list = new ArrayList<>();
        List<GetAllBatch> dataList = null;

        // filter the batch
        // get the user record first
        Long userId = Long.parseLong(id);
        Page queryResponse = null;
        UserData userData = userDao.getUserById(userId);
        Long userHeadId = null;


        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());
        Pageable pageable = filterService.getPageable(requestParam.getData());
        List<GetBatchWithControlId> batchDataForMergeBatch = new ArrayList<>();// get all batch for based on mrge batch id
        Long partyId = null;
        Long qualityEntryId = null;
        String batchId = null;
        Long userHeadFilterId = null;

        // batchData.addAll(batchDataForMergeBatch);
        for (int i = 0; i < requestParam.getData().getParameters().size(); i++) {
            Filter filter = requestParam.getData().getParameters().get(i);
            String field = filter.getField().get(0);
            String value = filter.getValue();

            if (field.equals("partyId"))
                partyId = Long.parseLong(value);

            if (field.equals("qualityEntryId"))
                qualityEntryId = Long.parseLong(value);

            if (field.equals("batchId"))
                batchId = value;

            if (field.equals("userHeadId"))
                userHeadFilterId = Long.parseLong(value);

        }

        // filter the record
        if (permissions.getViewAll()) {
            userId = null;
            userHeadId = null;
            dataList = batchDao.getAllBatchWithoutBillGenerated(partyId,qualityEntryId,userHeadFilterId,batchId);
            batchDataForMergeBatch = batchDao.getAllMergeBatchWithoutBillGenrated(partyId,qualityEntryId,userHeadFilterId,batchId);

        } else if (permissions.getViewGroup()) {
            // check the user is master or not ?
            // admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                dataList = batchDao.getAllBatchWithoutBillGenerated(partyId,qualityEntryId,userHeadFilterId,batchId);
                batchDataForMergeBatch = batchDao.getAllMergeBatchWithoutBillGenrated(partyId,qualityEntryId,userHeadFilterId,batchId);

            } else if (userData.getUserHeadId() > 0) {
                // check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                dataList = batchDao.getAllBatchWithoutBillGenerated(partyId,qualityEntryId,userHeadFilterId,batchId);
                batchDataForMergeBatch = batchDao.getAllMergeBatchWithoutBillGenrated(partyId,qualityEntryId,userHeadFilterId,batchId);
            }
        } else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId = null;
            dataList = batchDao.getAllBatchWithoutBillGenerated(partyId,qualityEntryId,userHeadFilterId,batchId);
            batchDataForMergeBatch = batchDao.getAllMergeBatchWithoutBillGenrated(partyId,qualityEntryId,userHeadFilterId,batchId);
        }

        // filter the data if the batch is done with jet
        for (GetAllBatch getAllBatch : dataList) {
            // System.out.println(getAllBatch.getBatchId());
            ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(getAllBatch.getBatchId());

            if (userHeadFilterId != null) {
                Optional<StockMast> stockMast = stockMastDao.findById(getAllBatch.getControlId());
                if (stockMast.isPresent()) {
                    if (!userHeadFilterId.equals( stockMast.get().getParty().getUserHeadData().getId()))
                        continue;
                } else {
                    continue;
                }
            }

            if (partyId != null)
                if (!partyId.equals( getAllBatch.getPartyId()))
                    continue;

            if (qualityEntryId != null)
                if (!qualityEntryId.equals(getAllBatch.getQualityEntryId()))
                    continue;

            if (batchId != null)
                if ((!getAllBatch.getBatchId().contains(batchId)))
                    continue;

            if (productionPlan == null)
                continue;

            if (productionPlan.getStatus() == false)
                continue;
            // System.out.println(productionPlan.getId());
            JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
            if (jetData == null)
                continue;
            // System.out.println(jetData.getStatus());
            if (jetData.getStatus() == JetStatus.success) {
                list.add(new GetAllBatchWithProduction(getAllBatch, productionPlan.getId()));
            }

        }

        // filter the data if the batch is done with jet
        for (GetBatchWithControlId getAllBatch : batchDataForMergeBatch) {
            ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(getAllBatch.getMergeBatchId());
            if (userHeadFilterId != null)
                if (userHeadFilterId != getAllBatch.getUserHeadId())
                    continue;

            if (productionPlan == null)
                continue;

            if (productionPlan.getStatus() == false)
                continue;
            // System.out.println(productionPlan.getId());
            JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
            if (jetData == null)
                continue;
            // System.out.println(jetData.getStatus());
            if (jetData.getStatus() == JetStatus.success) {
                List<GetBatchWithControlId> listOfBatch = batchDao
                        .getBatchesByMergeBatchId(getAllBatch.getMergeBatchId());

                for (GetBatchWithControlId batch : listOfBatch) {
                    GetAllBatchWithProduction batchDetail = new GetAllBatchWithProduction(batch,
                            productionPlan.getId());
                    List<BatchData> batchDataList = batchDao.getBatchByMergeBatchIdAndBatchIdForFinishMtrSave(
                            batch.getBatchId(), getAllBatch.getMergeBatchId());
                    if (batchDataList.isEmpty())
                        continue;
                    batchDetail.setBatchId(getAllBatch.getMergeBatchId() + "-" + batchDetail.getBatchId());
                    batchDetail.setProductionPlanned(true);

                    if (partyId != null)
                        if (!partyId.equals(batchDetail.getPartyId()))
                            continue;

                    if (qualityEntryId != null)
                        if (!qualityEntryId.equals( batchDetail.getQualityEntryId()))
                            continue;

                    if (batchId != null)
                        if ((!batchDetail.getBatchId().contains(batchId)))
                            continue;

                    list.add(batchDetail);
                }

            }

        }

        int pageSize = requestParam.getData().getPageSize();
        int pageIndex = requestParam.getData().getPageIndex();
        FilterResponse<GetAllBatchWithProduction> response = new FilterResponse<GetAllBatchWithProduction>(
                list.subList(Integer.min(pageIndex * pageSize, list.size()),
                        Integer.min((pageIndex + 1) * pageSize, list.size())),
                pageIndex, pageSize, list.size());

        return response;

    }

    // get All batch who's bill is not generated
    public List<GetAllBatchWithProduction> getAllBatchWithoutBillGenerated(String id) throws Exception {
        List<GetAllBatchWithProduction> list = new ArrayList<>();
        List<GetAllBatch> dataList = new ArrayList<>();

        // filter the batch
        // get the user record first
        Long userId = Long.parseLong(id);

        UserData userData = userDao.getUserById(userId);
        Long userHeadId = null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());

        List<GetBatchWithControlId> batchDataForMergeBatch = null;// get all batch for based on mrge batch id
        Pageable pageable = PageRequest.of(2, 20);
        if (permissions.getViewAll()) {
            userId = null;
            userHeadId = null;
            dataList = batchDao.getAllBatchWithoutBillGenerated(null,null,null,null);
            batchDataForMergeBatch = batchDao.getAllMergeBatchWithoutBillGenrated(null,null,null,null);

        } else if (permissions.getViewGroup()) {
            // check the user is master or not ?
            // admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                dataList = batchDao.getAllBatchWithoutBillGenerated(null,null,null,null);
                batchDataForMergeBatch = batchDao.getAllMergeBatchWithoutBillGenrated(null,null,null,null);

            } else if (userData.getUserHeadId() > 0) {
                // check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                dataList = batchDao.getAllBatchWithoutBillGenerated(userId, userHeadId);
                batchDataForMergeBatch = batchDao.getAllMergeBatchWithoutBillGenrated(userId, userHeadId);
            }
        } else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId = null;
            dataList = batchDao.getAllBatchWithoutBillGenerated(userId, userHeadId);
            batchDataForMergeBatch = batchDao.getAllMergeBatchWithoutBillGenrated(userId, userHeadId);
        }

        // filter the data if the batch is done with jet
        for (GetAllBatch getAllBatch : dataList) {
            // System.out.println(getAllBatch.getBatchId());
            ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(getAllBatch.getBatchId());

            if (productionPlan == null)
                continue;

            if (productionPlan.getStatus() == false)
                continue;
            // System.out.println(productionPlan.getId());
            JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
            if (jetData == null)
                continue;
            // System.out.println(jetData.getStatus());
            if (jetData.getStatus() == JetStatus.success) {
                list.add(new GetAllBatchWithProduction(getAllBatch, productionPlan.getId()));
            }

        }

        // filter the data if the batch is done with jet
        for (GetBatchWithControlId getAllBatch : batchDataForMergeBatch) {
            ProductionPlan productionPlan = productionPlanService.getProductionByBatchId(getAllBatch.getMergeBatchId());

            if (productionPlan == null)
                continue;

            if (productionPlan.getStatus() == false)
                continue;
            // System.out.println(productionPlan.getId());
            JetData jetData = jetService.getJetDataByProductionIdWithoutFilter(productionPlan.getId());
            if (jetData == null)
                continue;
            // System.out.println(jetData.getStatus());
            if (jetData.getStatus() == JetStatus.success) {
                List<GetBatchWithControlId> listOfBatch = batchDao
                        .getBatchesByMergeBatchId(getAllBatch.getMergeBatchId());

                for (GetBatchWithControlId batch : listOfBatch) {
                    GetAllBatchWithProduction batchDetail = new GetAllBatchWithProduction(batch,
                            productionPlan.getId());
                    List<BatchData> batchDataList = batchDao.getBatchByMergeBatchIdAndBatchIdForFinishMtrSave(
                            batch.getBatchId(), getAllBatch.getMergeBatchId());
                    if (batchDataList.isEmpty())
                        continue;
                    batchDetail.setBatchId(getAllBatch.getMergeBatchId() + "-" + batchDetail.getBatchId());
                    batchDetail.setProductionPlanned(true);

                    list.add(batchDetail);
                }

            }

        }

        return list;

    }

    public WTByStockAndBatch getWtByStockAndBatchId(Long stockId, String batchId) throws Exception {
        // wt bay batch id
        // first get batch is batchId orMergeBatchId

        Double wt;
        BatchData isBatchId = batchDao.getIsBatchId(batchId);
        if (isBatchId == null) {
            wt = batchDao.getTotalWtByMergeBatchId(batchId);
        } else {
            wt = batchDao.getTotalWtByBatchId(batchId);
        }

        WTByStockAndBatch data = new WTByStockAndBatch(wt);
        data.setBatchId(batchId);
        if (data == null)
            throw new Exception(ConstantFile.StockBatch_Not_Found);
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
        return batchDao.getTotalMtrByControlIdAndBatchId(stockId, batchId);
    }

    public List<BatchDetail> getBatchDetailForReport(Long partyId, Long qualityId) {
        List<BatchDetail> batchDetailList = new ArrayList<>();
        // List<BatchDetail> batchDetailListBasedOnFlag = new ArrayList<>();

        List<StockMast> stockMastList = stockMastDao.getAllStockByPartyIdAndQualityId(partyId, qualityId);

        Quality quality = qualityDao.getqualityById(qualityId);
        QualityName qualityName = quality.getQualityName();
        ;
        if (stockMastList.isEmpty()) {
            return null;
        }
        for (StockMast stockMast : stockMastList) {
            // batchDetailListBasedOnFlag =
            List<BatchDetail> batchDetails = batchDao.getBatchDetailByStockIdWithoutProductionPlan(stockMast.getId());
            ;
            batchDetails
                    .addAll(batchDao.getBatchDetailByStockIdWithProductionPlanWithoutFinishMtrSave(stockMast.getId()));
            batchDetails.addAll(batchDao.getBatchDetailByStockIdWithProductionPlanWithFinishMtrSave(stockMast.getId()));

            // getBatchDetailByStockIdWithProductionPlanWithoutFinishMtrSave
            if (batchDetails.isEmpty())
                continue;
            for (BatchDetail batchDetail : batchDetails) {
                if (batchDetail.getIsProductionPlanned() == true) {
                    ProductionPlan productionPlan;
                    ShadeMast shadeMast = null;
                    BatchData mergeBatchIdByBatchId = batchDao.getMergeBatchIdBatchIdWithProductionPlanAndFinishMtr(
                            batchDetail.getBatchId(), batchDetail.getIsProductionPlanned(),
                            batchDetail.getIsFinishMtrSave());
                    if (mergeBatchIdByBatchId.getMergeBatchId() == null) {
                        productionPlan = productionPlanService.getProductionByBatchId(batchDetail.getBatchId());
                    } else {
                        productionPlan = productionPlanService
                                .getProductionByBatchId(mergeBatchIdByBatchId.getMergeBatchId());
                    }
                    if (productionPlan == null)
                        continue;
                    if (productionPlan.getIsDirect() != true)
                        shadeMast = shadeService.getShadeById(productionPlan.getShadeId());

                    /*
                     * if (shadeMast == null) continue;
                     */
                    BatchDetail batchDetail1 = new BatchDetail(batchDetail, quality, qualityName);
                    if (shadeMast != null) {
                        batchDetail1.setPartyShadeNo(shadeMast.getPartyShadeNo());
                        batchDetail1.setColorName(shadeMast.getColorName());
                        batchDetail1.setColorTone(shadeMast.getColorTone());
                    }
                    batchDetailList.add(batchDetail1);
                } else {

                    batchDetailList.add(new BatchDetail(batchDetail, quality, qualityName));
                }

            }
        }

        /*
         * if(batchDetailList.isEmpty()) return null;
         */
        return batchDetailList;
    }

    public JobCard getJobCardByStockIdAndBatchId(String batchId) throws Exception {

        List<BatchData> batchData = batchDao.findByBatchId(batchId);
        if (batchData == null || batchData.isEmpty())
            throw new Exception(ConstantFile.Batch_Data_Not_Found);

        Long stockId = batchDao.getControlIdByBatchId(batchId);

        // header record
        Double totalWt = batchDao.getTotalWtByControlIdAndBatchId(stockId, batchId);
        Double totalMtr = batchDao.getTotalMtrByControlIdAndBatchId(stockId, batchId);
        Double totalFinish = batchDao.getTotalFinishMtrByBatchAndStock(batchId, stockId);
        Long totalPcs = batchDao.getTotalPcsByBatchAndStockIdWithoutFilter(stockId, batchId);
        StockMast stockMast = stockMastDao.findByStockId(stockId);
        Party party = partyDao.findByPartyId(stockMast.getParty().getId());
        UserData userData = userDao.getUserById(party.getUserHeadData().getId());
        Quality quality = qualityDao.getqualityById(stockMast.getQuality().getId());
        QualityName qualityName = quality.getQualityName();
        ;
        List<PchallanByBatchId> pchallanByBatchIdList = batchDao.getListOfPchallanByBatchId(batchId);

        /*
         * System.out.println(stockMast.getId()); System.out.println(party.getId());
         * System.out.println(quality.getId()); System.out.println(qualityName.getId());
         * System.out.println(userData.getId()); System.out.println(totalMtr);
         * System.out.println(totalPcs); System.out.println(totalWt);
         * System.out.println(totalPcs);
         *
         * can be get and error for userdata
         */
        JobCard jobCard = new JobCard(stockMast, party, userData, quality, qualityName, totalMtr, totalPcs, totalWt);
        if (pchallanByBatchIdList.size() == 1) {
            jobCard.setChalNo(pchallanByBatchIdList.get(0).getPchallanRef());
        } else {
            jobCard.setChalNo(pchallanByBatchIdList.stream().map(PchallanByBatchId::getPchallanRef)
                    .collect(Collectors.joining(",")));
        }
        jobCard.setBatchId(batchId);
        jobCard.setTotalFinishMtr(totalFinish);
        jobCard.setBatchDataList(batchData);
        return jobCard;

    }

    public List<GetAllBatch> getAllBatchForAdditionalSlip(String id) throws Exception {
        // get the user record first
        Long userId = Long.parseLong(id);

        UserData userData = userDao.getUserById(userId);
        Long userHeadId = null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());

        List<GetAllBatch> list = new ArrayList<>();

        // get all batches which are in the queue
        List<JetData> jetDataList = jetService.getAllProductionInTheQueueWithStart();

        for (JetData jetData : jetDataList) {
            // get the production record
            ProductionPlan productionPlan = productionPlanService.getProductionDataById(jetData.getProductionId());
            if (productionPlan == null)
                continue;

            System.out.println(productionPlan.getBatchId());
            GetAllBatch getAllBatch = new GetAllBatch();
            // filter the record
            if (permissions.getViewAll()) {
                userId = null;
                userHeadId = null;

                getAllBatch.setBatchId(productionPlan.getBatchId());// batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId());
            } else if (permissions.getViewGroup()) {
                // check the user is master or not ?
                // admin
                if (userData.getUserHeadId() == 0) {
                    userId = null;
                    userHeadId = null;
                    getAllBatch.setBatchId(productionPlan.getBatchId());// batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId());
                } else if (userData.getUserHeadId() > 0) {
                    // check weather master or operator
                    UserData userHead = userDao.getUserById(userData.getUserHeadId());
                    userId = userData.getId();
                    userHeadId = userHead.getId();
                    getAllBatch.setBatchId(productionPlan.getBatchId());// batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId(),userId,userHeadId);

                }
            } else if (permissions.getView()) {
                userId = userData.getId();
                userHeadId = null;
                getAllBatch.setBatchId(productionPlan.getBatchId());// batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId(),userId,userHeadId);
            }

            if (getAllBatch.getBatchId() != null) {
                DyeingSlipMast dyeingSlipMast = dyeingSlipService.getDyeingSlipByProductionId(productionPlan.getId());
                DyeingSlipData additionalExist = dyeingSlipService.dyeingSlipDataDao
                        .getOnlyAdditionalSlipMastById(dyeingSlipMast.getId());
                if (additionalExist == null) {
                    list.add(getAllBatch);
                }
            }

        }

        /*
         * if(list.isEmpty()) throw new Exception(CommonMessage.Batch_Data_Not_Found);
         */

        return list;
    }

    public List<GetAllBatch> getAllBatchForRedyeingSlip(String id) throws Exception {
        // get the user record first
        Long userId = Long.parseLong(id);

        UserData userData = userDao.getUserById(userId);
        Long userHeadId = null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());

        List<GetAllBatch> list = new ArrayList<>();

        // get all batches which are in the queue
        List<JetData> jetDataList = jetService.getAllProductionSuccessFromJet();

        for (JetData jetData : jetDataList) {
            GetAllBatch getAllBatch = null;
            // get the production record
            ProductionPlan productionPlan = productionPlanService.getProductionDataById(jetData.getProductionId());
            if (productionPlan == null)
                continue;

            // check the batch bill is generated or not
            if (batchDao.isFinishMtrSave(productionPlan.getBatchId()))
                continue;
            // GetAllBatch getAllBatch=null;
            // filter the record
            if (permissions.getViewAll()) {
                userId = null;
                userHeadId = null;
                getAllBatch = null;// batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId());
            } else if (permissions.getViewGroup()) {
                // check the user is master or not ?
                // admin
                if (userData.getUserHeadId() == 0) {
                    userId = null;
                    userHeadId = null;
                    getAllBatch = null;// batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId());
                } else if (userData.getUserHeadId() > 0) {
                    // check weather master or operator
                    UserData userHead = userDao.getUserById(userData.getUserHeadId());
                    userId = userData.getId();
                    userHeadId = userHead.getId();
                    getAllBatch = null;// batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId(),userId,userHeadId);

                }
            } else if (permissions.getView()) {
                userId = userData.getId();
                userHeadId = null;
                getAllBatch = null;// batchDao.getBatchForAdditionalSlipByBatchAndStock(productionPlan.getStockId(),productionPlan.getBatchId(),userId,userHeadId);
            }
            if (getAllBatch.getControlId() != null)
                list.add(getAllBatch);

        }

        if (list.isEmpty())
            throw new Exception(ConstantFile.Batch_Data_Not_Found);

        return list;

    }

    public void createMergeBatchList(CreateMergeBatch record) throws Exception {
        // check first the all batch record is exist or not

        List<BatchData> batchDataRecordList = new ArrayList<>();
        for (BatchData batchData : record.getBatchDataList()) {
            BatchData batchExist = batchDao.getBatchDataById(batchData.getId());
            if (batchExist == null)
                throw new Exception(ConstantFile.Shade_Not_Found);

            batchExist.setMergeBatchId(record.getMergeBatchId());
            batchDataRecordList.add(batchExist);
        }

        if (!batchDataRecordList.isEmpty()) {
            batchDao.saveAll(batchDataRecordList);
        }

    }

    public void updateMergeBatchList(CreateMergeBatch record) throws Exception {

        List<BatchData> batchDataRecordList = new ArrayList<>();
        // check the merge batch record is exit or not
        BatchData batchDataExist = batchDao.getMergeBatchExist(record.getMergeBatchId());
        if (batchDataExist == null)
            throw new Exception(ConstantFile.MergeBatch_Not_Found);

        // existing batches record with mergeBatchId
        // List<BatchData> existingBatchRecord
        // =batchDao.getMergeBatchListByMergeBatchId(record.getMergeBatchId());
        List<Long> comingBatchRecordId = new ArrayList<>();
        List<Long> existingBatchRecordId = batchDao.getMergeBatchIdListByMergeBatchId(record.getMergeBatchId());
        for (BatchData batchData : record.getBatchDataList()) {
            BatchData batchExist = batchDao.getBatchDataById(batchData.getId());
            if (batchExist == null)
                throw new Exception(ConstantFile.Batch_Data_Not_Found);

            batchExist.setMergeBatchId(record.getMergeBatchId());

            comingBatchRecordId.add(batchExist.getId());
            batchDataRecordList.add(batchExist);
        }
        batchDao.saveAll(batchDataRecordList);

        // change the status of batches which are existing but not comming from batch
        // record
        existingBatchRecordId.forEach(e -> {
            if (!comingBatchRecordId.contains(e)) {
                batchDao.updateMergeIdByBatchEntryId(e, null);
            }

        });

    }

    public List<MergeBatchResponse> getAllMergeBatchId() {
        // List<GetAllMergeBatchId> list = new ArrayList<>();

        List<MergeBatchResponse> getAllBatchWithPartyAndQualities = new ArrayList<>();
        List<MergeBatchId> record = batchDao.getAllMergeBatchId();
        List<GetBatchWithControlId> batchDataForMergeBatch = null;

        batchDataForMergeBatch = batchDao.findAllMergeBatchWithoutFilter();

        // merge batch filter api
        for (GetBatchWithControlId batch : batchDataForMergeBatch) {
            // get batches based on batch id and stock id by mergebatchId

            List<GetBatchWithControlId> basedOnBatch = batchDao
                    .getBatcheAndStockIdByMergeBatchIdWithoutFilter(batch.getMergeBatchId());
            MergeBatchResponse batchToPartyAndQuality = new MergeBatchResponse();
            for (GetBatchWithControlId batchByMergeBatch : basedOnBatch) {
                // System.out.println("stockId:"+batchByMergeBatch.getControlId());

                Optional<StockMast> stockMast = stockMastDao.findById(batchByMergeBatch.getControlId());
                System.out.println(stockMast.get().getId());
                if (stockMast.get().getQuality() != null && stockMast.get().getParty() != null) {

                    Quality quality = stockMast.get().getQuality();

                    QualityName qualityName = quality.getQualityName();
                    Party party = stockMast.get().getParty();

                    // check that the process and party shade is exist or not
                    // if not then set the detail by null
                    /*
                     * ProductionPlan
                     * productionPlan=productionPlanService.getProductionDataByBatchAndStock(
                     * batchByMergeBatch.getBatchId(),batchByMergeBatch.getControlId());
                     * if(productionPlan==null) { batchToPartyAndQuality.setPartyShadeNo(null);
                     * batchToPartyAndQuality.setProcessName(null); } else { //get the shade and
                     * process Optional<ShadeMast> shadeMast =
                     * shadeService.getShadeMastById(productionPlan.getShadeId()); DyeingProcessMast
                     * dyeingProcessMast =
                     * dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());
                     *
                     * if(dyeingProcessMast==null || shadeMast.isEmpty()) continue;
                     *
                     *
                     * batchToPartyAndQuality.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
                     * batchToPartyAndQuality.setProcessName(dyeingProcessMast.getProcessName()); }
                     */

                    batchToPartyAndQuality
                            .setPartyName(batchToPartyAndQuality.getPartyName() == null ? party.getPartyName()
                                    : batchToPartyAndQuality.getPartyName() + "," + party.getPartyName());
                    batchToPartyAndQuality
                            .setQualityId(batchToPartyAndQuality.getQualityId() == null ? quality.getQualityId()
                                    : batchToPartyAndQuality.getQualityId() + "," + quality.getQualityId());
                    batchToPartyAndQuality
                            .setPartyId(batchToPartyAndQuality.getPartyId() == null ? party.getId().toString()
                                    : batchToPartyAndQuality.getPartyId() + "," + party.getId().toString());
                    batchToPartyAndQuality.setQualityEntryId(
                            batchToPartyAndQuality.getQualityEntryId() == null ? quality.getId().toString()
                                    : batchToPartyAndQuality.getQualityEntryId() + "," + quality.getId());
                    batchToPartyAndQuality.setQualityName(
                            batchToPartyAndQuality.getQualityName() == null ? qualityName.getQualityName()
                                    : batchToPartyAndQuality.getQualityName() + "," + qualityName.getQualityName());

                    // System.out.println("batch id for merge batch:"+batch.getBatchId());
                    batchToPartyAndQuality
                            .setBatchId(batchToPartyAndQuality.getBatchId() == null ? batchByMergeBatch.getBatchId()
                                    : batchToPartyAndQuality.getBatchId() + "," + batchByMergeBatch.getBatchId());

                }
                batchToPartyAndQuality.setTotalMtr(batch.getMTR());
                batchToPartyAndQuality.setTotalWt(batch.getWT());
                batchToPartyAndQuality.setMergeBatchId(batch.getMergeBatchId());

                // set the production plag as well
                BatchData isMergeBatch = batchDao.getIsMergeBatchId(batchToPartyAndQuality.getMergeBatchId());
                if (isMergeBatch != null) {
                    batchToPartyAndQuality.setIsProductionPlanned(isMergeBatch.getIsProductionPlanned());
                }

            }
            batchToPartyAndQuality.setMergeBatchId(batch.getMergeBatchId());

            // add the record
            getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);

        }
        return getAllBatchWithPartyAndQualities;
    }

    public BatchToPartyQualityWithGr getMergeBatchByMergeBatchId(String mergeBatchId) throws Exception {
        BatchToPartyQualityWithGr batchToPartyAndQuality = new BatchToPartyQualityWithGr();
        BatchData mergeBatchExist = batchDao.getMergeBatchExist(mergeBatchId);

        List<BatchData> batchDataList = new ArrayList<>();
        if (mergeBatchExist == null)
            throw new Exception(ConstantFile.MergeBatch_Not_Found);

        List<GetBatchWithControlId> batchDataForMergeBatch = null;

        batchDataForMergeBatch = batchDao.findAllMergeBatchWithoutFilterByMergeBatchId(mergeBatchId);

        // merge batch filter api
        for (GetBatchWithControlId batch : batchDataForMergeBatch) {
            // get batches based on batch id and stock id by mergebatchId

            // List<GetBatchWithControlId> basedOnBatch =
            // batchDao.getBatcheAndStockIdByMergeBatchId(batch.getMergeBatchId());
            /*
             * //BatchToPartyAndQuality batchToPartyAndQuality=new BatchToPartyAndQuality();
             * for(GetBatchWithControlId batchByMergeBatch:basedOnBatch) {
             */
            // System.out.println("stockId:"+batchByMergeBatch.getControlId());

            Optional<StockMast> stockMast = stockMastDao.findById(batch.getControlId());
            if (stockMast.get().getQuality().getId() != null && stockMast.get().getParty().getId() != null) {

                Optional<Quality> quality = qualityDao.findById(stockMast.get().getQuality().getId());

                QualityName qualityName = quality.get().getQualityName();
                Optional<Party> party = partyDao.findById(stockMast.get().getParty().getId());

                // check that the process and party shade is exist or not
                // if not then set the detail by null
                /*
                 * ProductionPlan
                 * productionPlan=productionPlanService.getProductionDataByBatchAndStock(
                 * batchByMergeBatch.getBatchId(),batchByMergeBatch.getControlId());
                 * if(productionPlan==null) { batchToPartyAndQuality.setPartyShadeNo(null);
                 * batchToPartyAndQuality.setProcessName(null); } else { //get the shade and
                 * process Optional<ShadeMast> shadeMast =
                 * shadeService.getShadeMastById(productionPlan.getShadeId()); DyeingProcessMast
                 * dyeingProcessMast =
                 * dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());
                 *
                 * if(dyeingProcessMast==null || shadeMast.isEmpty()) continue;
                 *
                 *
                 * batchToPartyAndQuality.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
                 * batchToPartyAndQuality.setProcessName(dyeingProcessMast.getProcessName()); }
                 */

                batchToPartyAndQuality
                        .setPartyName(batchToPartyAndQuality.getPartyName() == null ? party.get().getPartyName()
                                : batchToPartyAndQuality.getPartyName() + "," + party.get().getPartyName());
                batchToPartyAndQuality
                        .setQualityId(batchToPartyAndQuality.getQualityId() == null ? quality.get().getQualityId()
                                : batchToPartyAndQuality.getQualityId() + "," + quality.get().getQualityId());
                batchToPartyAndQuality
                        .setPartyId(batchToPartyAndQuality.getPartyId() == null ? party.get().getId().toString()
                                : batchToPartyAndQuality.getPartyId() + "," + party.get().getId().toString());
                batchToPartyAndQuality.setQualityEntryId(
                        batchToPartyAndQuality.getQualityEntryId() == null ? quality.get().getId().toString()
                                : batchToPartyAndQuality.getQualityEntryId() + "," + quality.get().getId());
                batchToPartyAndQuality
                        .setQualityName(batchToPartyAndQuality.getQualityName() == null ? qualityName.getQualityName()
                                : batchToPartyAndQuality.getQualityName() + "," + qualityName.getQualityName());

                batchToPartyAndQuality.setBatchId(batchToPartyAndQuality.getBatchId() == null ? batch.getBatchId()
                        : batchToPartyAndQuality.getBatchId() + "," + batch.getBatchId());

            }
            // Double totalMtr =
            // batch.getMTR();//batchDao.getTotalMtrByMergeBatchId(batch.getMergeBatchId());
            // Double totalWt =
            // batch.getWT();//batchDao.getTotalWtByMergeBatchId(batch.getMergeBatchId());
            batchToPartyAndQuality.setTotalMtr(batchDao.getTotalMtrByMergeBatchId(batch.getMergeBatchId()));
            batchToPartyAndQuality.setTotalWt(batchDao.getTotalWtByMergeBatchId(batch.getMergeBatchId()));

            // }
            batchDataList.addAll(batchDao.getBatchByBatchIdWithMergeBatchId(batch.getBatchId(), mergeBatchId));
            batchToPartyAndQuality.setMergeBatchId(batch.getMergeBatchId());
            // batchToPartyAndQuality.setBatchId(batch.getMergeBatchId());
            // add the record
            // getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);

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
        if (mergeBatchIdExist == null)
            throw new Exception("no record found");

        // check that the merge batch is production planned??
        if (mergeBatchIdExist.getIsProductionPlanned() == true)
            throw new Exception("unable to delete because production planned");

        List<BatchData> batchDataList = batchDao.getMergeBatchDataByMergeBatchId(mergeBatchId);

        for (BatchData batchData : batchDataList) {
            batchDao.updateMergeIdByBatchEntryId(batchData.getId(), null);
        }

    }

    public Optional<List<GetAllStockWithPartyNameResponse>> getStockByCreatedOrUserHeadId(Long id) {
        return stockMastDao.getAllStockWithPartyNameByUserHeadIdAndCreatedBy(id, id);
    }

    public Double getAvailableStockValueByPartyIdWithQualityEntryId(Long partyId, Long qualityEntryId)
            throws Exception {
        Double availableStockValue = 0.0;

        if (qualityEntryId == null) {

            List<Quality> qualityList = qualityDao.getQualityListByPartyIdId(partyId);
            for (Quality quality : qualityList) {
                QualityName qualityNameRate = quality.getQualityName();
                if (qualityNameRate != null) {

                    Double totalMtr = batchDao.getTotalMtrByQualityIdWithBillGeneratedFlag(quality.getId(), false);

                    if (totalMtr != null)
                        availableStockValue += qualityNameRate.getRate() * totalMtr;

                }

            }

        } else {
            // quality exist or not
            Quality quality = qualityDao.getqualityById(qualityEntryId);
            if (quality == null)
                throw new Exception(ConstantFile.Quality_Data_Not_Exist);

            QualityName qualityNameRate = quality.getQualityName();
            if (qualityNameRate != null) {

                Double totalMtr = batchDao.getTotalMtrByQualityIdWithBillGeneratedFlag(quality.getId(), false);

                if (totalMtr != null)
                    availableStockValue += qualityNameRate.getRate() * totalMtr;

            }

        }
        return availableStockValue;
    }

    public Double getTotalFinishMtrByBatchEntryIdList(List<Long> batchIdsByQuality) {

        return batchDao.getTotalFinishMtrByBatchEntryIdList(batchIdsByQuality);
    }

    public Long saveReturnStockBatch(BatchReturnBody record) throws Exception {

        // get the latest chl_no no
        Long latestChlNo = batchReturnMastDao.getlatestChlNo();
        latestChlNo = latestChlNo == null ? 1 : ++latestChlNo;

        // stock id, quality id, party id
        // Map<Long,HashMap<Long,Long>> hashMapMap = new HashMap<>();
        List<Long> batchEntryIdToDelete = new ArrayList<>();

        Party party = partyDao.findPartyByStockId(record.getBatchDataList().get(0).getControlId());
        StockMast stockMast = stockMastDao.findByStockId(record.getBatchDataList().get(0).getControlId());
        if (stockMast == null)
            throw new Exception(ConstantFile.StockBatch_Not_Exist);

        List<BatchReturnData> batchReturnDataList = new ArrayList<>();
        for (BatchData e : record.getBatchDataList()) {
            BatchData batchDataExist = batchDao.getBatchDataById(e.getId());
            if (batchDataExist == null)
                throw new Exception(ConstantFile.Batch_Data_Not_Exist);

            if (batchDataExist.getIsProductionPlanned() == true)
                throw new Exception(ConstantFile.Production_Record_Exist);

            batchEntryIdToDelete.add(batchDataExist.getId());

            Quality quality = qualityServiceImp.getQualityByStockId(e.getControlId());
            if (quality != null) {
                Optional<QualityWithQualityNameParty> qualityWithQualityNameParty = qualityDao
                        .findByIdWithQualityNameResponse(quality.getId());
                batchReturnDataList.add(new BatchReturnData(qualityWithQualityNameParty, batchDataExist));
            }

        }
        BatchReturnMast batchReturnMast = new BatchReturnMast(party, record, latestChlNo, stockMast);
        if (record.getDiffDeliveryParty() == true) {
            batchReturnMast.setDiffPartyName(record.getDiffPartyName());
            batchReturnMast.setDiffGst(record.getDiffGst() == null ? null : record.getDiffGst());
            batchReturnMast.setDiffPartyAddress(record.getDiffPartyAddress());
            batchReturnMast.setDiffDeliveryParty(true);
        }

        batchReturnMast.setBatchReturnData(batchReturnDataList);
        batchReturnMastDao.save(batchReturnMast);

        // remove all from batch table
        batchDao.deleteByIdList(batchEntryIdToDelete);
        // save all in return list

        return latestChlNo;

    }

    public List<BatchReturnResponse> getAllReturnBatch() {

        List<BatchReturnResponse> list = new ArrayList<>();

        List<BatchReturnMast> returnBatchDetailList = batchReturnMastDao.getAllBatchReturn();

        returnBatchDetailList.forEach(e -> {

            BatchReturnResponse batchReturnResponse = getReturnBatchByChalNo(e.getChlNo());
            if (batchReturnResponse != null)
                list.add(batchReturnResponse);

        });

        return list;
    }

    public BatchReturnResponse getReturnBatchByChalNo(Long chlNo) {

        BatchReturnResponse batchReturnResponse = null;
        // check that chl no exit or not
        BatchReturnMast batchReturnExist = batchReturnMastDao.getChalNoExist(chlNo);

        if (batchReturnExist != null) {
            List<BatchReturnData> batchReturnList = batchReturnDataDao.getChallanDetailByChlNo(chlNo);

            Long pcs = Long.valueOf(batchReturnList.size());
            Double totalMtr = batchReturnList.stream().filter(p -> p.getId() != null).mapToDouble(p -> p.getMtr())
                    .sum();

            if (batchReturnExist != null)
                batchReturnResponse = new BatchReturnResponse(batchReturnExist, pcs, batchReturnList, totalMtr);
        }

        return batchReturnResponse;
    }

    public List<BatchData> getBatchByBatchIdWithInvoiceNuber(String batchId, String invoiceNumber) {
        return batchDao.getBatchByBatchIdAndInvoiceNumber(batchId, invoiceNumber);
    }

    public Double getTotalMtrByBatchId(String batchId) {
        return batchDao.getTotalMtrByBatchId(batchId);
    }

    public Double getTotalMtrByMergeBatchId(String batchId) {
        return batchDao.getTotalMtrByMergeBatchId(batchId);
    }

    // add pchallan record api'ss service
    public Long addPChallanRef(AddStockBatch addStockBatch) throws Exception {
        Party party = partyDao.findByPartyId(addStockBatch.getPartyId());
        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);
        Optional<Quality> quality = qualityDao.findById(addStockBatch.getQualityId());

        if (!quality.isPresent()) {
            throw new Exception(ConstantFile.Quality_Data_Not_Found);
        }

        StockMast stockMast = new StockMast(addStockBatch, party, quality.get());

        List<BatchData> batchDataList = new ArrayList<>();
        // check that the party exist with same challan ref
        for (BatchData batchData : stockMast.getBatchData()) {
            List<BatchData> batchDataExistWithChallanAndPartyId = batchDao
                    .getBatchDataWithPartyIdAndPchallaneRefExceptBatchEntryId(batchData.getPchallanRef(),
                            stockMast.getParty().getId(), 0l);
            if (batchDataExistWithChallanAndPartyId.size() > 0)
                throw new Exception(ConstantFile.StockBatch_PChallanRef_ExistWithParty);

            if (batchData.getBatchId() != null) {
                throw new Exception(ConstantFile.StockBatch_PChallanRef_Create);
            }
            batchDataList.add(new BatchData(batchData));

        }

        // store pchallan

        stockMast.setBatchData(batchDataList);

        StockMast x = stockMastDao.save(stockMast);
        return x.getId();
    }

    public Long updatePChallanRef(AddStockBatch addStockBatch, String id) throws Exception {

        Party party = partyDao.findByPartyId(addStockBatch.getPartyId());
        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);
        Optional<Quality> quality = qualityDao.findById(addStockBatch.getQualityId());

        if (!quality.isPresent()) {
            throw new Exception(ConstantFile.Quality_Data_Not_Found);
        }

        StockMast stockMast = new StockMast(addStockBatch, party, quality.get());

        List<BatchData> batchDataList = new ArrayList<>();
        for (BatchData batchData : stockMast.getBatchData()) {
            /*
             * List<BatchData> batchDataExistWithChallanAndPartyId =
             * batchDao.getBatchDataWithPartyIdAndPchallaneRefExceptBatchEntryId(batchData.
             * getPchallanRef(),stockMast.getParty().getId(),batchData.getId()==null?0l:
             * batchData.getId()); if(batchDataExistWithChallanAndPartyId!=null) throw new
             * Exception(ConstantFile.StockBatch_PChallanRef_ExistWithParty);
             */
            if (batchData.getBatchId() != null) {
                throw new Exception(ConstantFile.StockBatch_PChallanRef_Create);
            }
            // batchDataList.add(new BatchData(batchData));

            /*
             * if (batchData.getIsProductionPlanned() == true) throw new
             * Exception(ConstantFile.Batch_Dyeing_Already);
             */

        }

        Long batchId = 0l, max = 0l;

        Optional<StockMast> original = stockMastDao.findById(stockMast.getId());
        if (original.isEmpty()) {
            throw new Exception(ConstantFile.StockBatch_Not_Found + stockMast.getId());
        }

        // Validate, if batch is not given to the production planning then throw the
        // exception
        /*
         * if (original.get().getIsProductionPlanned()) { throw new
         * Exception("BatchData is already sent to production, for id:" +
         * stockMast.getId()); }
         */

        // for delete the batch gr if not coming from FE

        // ##Get the data first from the list
        Map<Long, Boolean> batchGr = new HashMap<>();
        List<BatchData> batchData = batchDao.findByControlId(stockMast.getId());
        for (BatchData batch : batchData) {
            batchGr.put(batch.getId(), false);

            if (batch.getIsExtra() == true) {
                batchDataList.add(batch);
            }
            // System.out.println(batch.getId());
        }

        // change the as per the data is coming from FE
        for (BatchData batch : stockMast.getBatchData()) {
            if (batch.getBatchId() != null)
                throw new Exception(ConstantFile.StockBatch_PChallanRef_Update);

            // System.out.println("coming:"+batch.getId());
            if (batchGr.containsKey(batch.getId())) {
                BatchData batchData1 = batchDao.getBatchDataById(batch.getId());
                batchData1.setMtr(batch.getMtr());
                batchData1.setWt(batch.getWt());
                batchData1.setIsProductionPlanned(batch.getIsProductionPlanned());

                batch = new BatchData(batchData1);
                batchGr.replace(batch.getId(), true);
            } else {
                batch = new BatchData(batch);
            }
            /*
             * batchId = Long.parseLong(batch.getBatchId()); if (batchId > max) { max =
             * batchId; }
             */
            batch.setControlId(stockMast.getId());

            // check the challan changes?
            /*
             * BatchData batchDataExistWithPChallan =
             * batchDao.getBatchDataWithPartyIdAndPchallaneRefExceptBatchEntryId(batch.
             * getPchallanRef(),stockMast.getParty().getId(),batch.getId());
             * if(batchDataExistWithPChallan!=null) throw new
             * Exception(ConstantFile.StockBatch_PChallanRef_ExistWithParty);
             */
            batchDataList.add(batch);
        }

        // remove the record jiska flag false ho
        for (Map.Entry<Long, Boolean> entry : batchGr.entrySet()) {
            // System.out.println(entry.getKey()+":"+entry.getValue());
            if (entry.getValue() == false) {
                // remove the batch id and only if the production is not plan
                batchDao.deleteByIdWithProduction(entry.getKey());
            }
        }

        // for data entry user
        UserData userData = userDao.getUserById(Long.parseLong(id));
        if (userData.getIsMaster() == false || stockMast.getUserHeadId() == 0) {
            // fetch the party record to set the usert head
            Party party1 = stockMast.getParty();
            stockMast.setUserHeadId(party1.getUserHeadData().getId());
        }
        // update record
        StockMast x = new StockMast(stockMast);
        x.setBatchData(batchDataList);
        StockMast update = stockMastDao.save(x);
        return update.getId();
    }

    public List<BatchData> getPChallanExistWithParyId(Long partyId, String pchallanRef) {
        return batchDao.getBatchDataWithPartyIdAndPchallaneRefExceptBatchEntryId(pchallanRef, partyId, 0l);
    }

    public FilterResponse<BatchReturnResponse> getAllReturnBatchAllPaginated(GetBYPaginatedAndFiltered requestParam) {

        List<BatchReturnResponse> list = new ArrayList<>();
        List<BatchReturnMast> batchReturnMastList = batchReturnMastDao.getAllBatchReturn();
        List<Filter> parameters = requestParam.getData().getParameters();
        for (int i = 0; i < batchReturnMastList.size(); i++) {
            List<BatchReturnData> batchReturnDataList = new ArrayList<BatchReturnData>();

            for (int k = 0; k < batchReturnMastList.get(i).getBatchReturnData().size(); k++) {
                Boolean condition = requestParam.getData().isAnd;
                if (parameters.size() == 0)
                    condition = true;

                for (int j = 0; j < parameters.size(); j++) {
                    String val2 = null;
                    Class fieldType = null;
                    if (parameters.get(j).getField().get(0).equals("chlNo")) {
                        fieldType = Long.class;
                        val2 = Long.toString(batchReturnMastList.get(i).getChlNo());
                    } else if (parameters.get(j).getField().get(0).equals("partyName")) {
                        fieldType = String.class;
                        val2 = batchReturnMastList.get(i).getPartyName();
                    } else if (parameters.get(j).getField().get(0).equals("broker")) {
                        fieldType = String.class;
                        val2 = batchReturnMastList.get(i).getBroker();
                    } else if (parameters.get(j).getField().get(0).equals("tempoNo")) {
                        fieldType = String.class;
                        val2 = batchReturnMastList.get(i).getTempoNo();
                    } else if (parameters.get(j).getField().get(0).equals("qualityName")) {
                        fieldType = String.class;
                        val2 = batchReturnMastList.get(i).getBatchReturnData().get(k).getQualityName();
                    } else if (parameters.get(j).getField().get(0).equals("mtr")) {
                        fieldType = Double.class;
                        val2 = Double.toString(batchReturnMastList.get(i).getBatchReturnData().get(k).getMtr());
                    } else if (parameters.get(j).getField().get(0).equals("wt")) {
                        fieldType = Double.class;
                        val2 = Double.toString(batchReturnMastList.get(i).getBatchReturnData().get(k).getWt());
                    }
                    if (requestParam.getData().isAnd == true)
                        condition = condition && specificationManager.getOperationResult(parameters.get(j).getValue(),
                                val2, fieldType, parameters.get(j).getOperator());

                    else
                        condition = condition || specificationManager.getOperationResult(parameters.get(j).getValue(),
                                val2, fieldType, parameters.get(j).getOperator());

                }
                if (condition) {
                    batchReturnDataList.add(batchReturnMastList.get(i).getBatchReturnData().get(k));
                }

            }
            if (batchReturnDataList.size() > 0)
                list.add(new BatchReturnResponse(batchReturnMastList.get(i), Long.valueOf(batchReturnDataList.size()),
                        batchReturnDataList, 0d));

        }
        System.out.println(list.size());

        int pageSize = requestParam.getData().getPageSize();
        int pageIndex = requestParam.getData().getPageIndex();
        FilterResponse<BatchReturnResponse> response = new FilterResponse<BatchReturnResponse>(
                list.subList(Integer.min(pageIndex * pageSize, list.size()),
                        Integer.min((pageIndex + 1) * pageSize, list.size())),
                pageIndex, pageSize, list.size());
        return response;

    }

    public List<PendingBatchMast> getBatchReportByFilter(BatchFilterRequest filter) throws Exception {

        List<PendingBatchMast> list;
        Date from = null;
        Date to = null;
        // add one day because of timestamp issue
        Calendar c = Calendar.getInstance();

        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

        if (filter.getFrom()!=null)
            from = datetimeFormatter1.parse(filter.getFrom());
        if (filter.getTo()!=null) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            // c.add(Calendar.DATE, 1);// adding one day in to because of time issue in
            // created date
            to = c.getTime();
        }

        List<StockMast> stockMastList = stockMastDao.filterByBatchFilterRequestWithPendingBatch(from, to,
                filter.getPartyId(), filter.getQualityNameId(), filter.getQualityEntryId(),filter.getUserHeadId());

//        if (stockMastList == null)
//            throw new Exception(ConstantFile.StockBatch_Not_Found);

        // party id and it's pending request
        Map<Long, PendingBatchMast> qualityList = new HashMap<>();

        stockMastList.forEach(e -> {

            if (qualityList.containsKey(e.getQuality().getId())) {
                PendingBatchMast pendingBatchMast = qualityList.get(e.getQuality().getId());
                List<PendingBatchData> pendingBatchDataList = pendingBatchMast.getList();
                List<PendingBatchData> newPendingBatchList = batchDao.getPendingBatchListByStockId(e.getId());
                pendingBatchDataList.addAll(newPendingBatchList);
                Double totalMtr = newPendingBatchList.stream().mapToDouble(q -> q.getTotalBatchMtr()).sum();
                Double totalWt = newPendingBatchList.stream().mapToDouble(q -> q.getTotalBatchWt()).sum();
                pendingBatchMast.addTotalQualityMeter(StockBatchServiceImpl.changeInFormattedDecimal(totalMtr));
                pendingBatchMast.addTotalQualityWt(StockBatchServiceImpl.changeInFormattedDecimal(totalWt));
                qualityList.put(e.getQuality().getId(), pendingBatchMast);

            } else {
                PendingBatchMast pendingBatchMast = new PendingBatchMast(e);
                List<PendingBatchData> newPendingBatchList = batchDao.getPendingBatchListByStockId(e.getId());
                Double totalMtr = newPendingBatchList.stream().mapToDouble(q -> q.getTotalBatchMtr()).sum();
                Double totalWt = newPendingBatchList.stream().mapToDouble(q -> q.getTotalBatchWt()).sum();
                pendingBatchMast.setTotalQualityMeter(StockBatchServiceImpl.changeInFormattedDecimal(totalMtr!=null?totalMtr:0));
                pendingBatchMast.setTotalQualityWt(StockBatchServiceImpl.changeInFormattedDecimal(totalWt!=null?totalWt:0));
                pendingBatchMast.setList(newPendingBatchList);
                qualityList.put(pendingBatchMast.getQualityEntryId(), pendingBatchMast);
            }

        });

        if (qualityList.size() > 0) {
            list = new ArrayList<PendingBatchMast>(qualityList.values());
            Collections.sort(list, new Comparator<PendingBatchMast>() {

                @Override
                public int compare(PendingBatchMast o1, PendingBatchMast o2) {
                    return o1.getPartyId().intValue() - o2.getPartyId().intValue();
                }
            });
        }
        else
            list = new ArrayList<>();
        return list;

    }
    public List<PendingBatchDataForExcel> getBatchReportForExcelByFilter(BatchFilterRequest filter) throws Exception {

        List<PendingBatchDataForExcel> list=new ArrayList<>();
        Date from = null;
        Date to = null;
        // add one day because of timestamp issue
        Calendar c = Calendar.getInstance();

        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

        if (filter.getFrom()!=null)
            from = datetimeFormatter1.parse(filter.getFrom());
        if (filter.getTo()!=null) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            // c.add(Calendar.DATE, 1);// adding one day in to because of time issue in
            // created date
            to = c.getTime();
        }

//        list = batchDao.filterForExcelByBatchFilterRequestWithPendingBatch(from, to,
//                filter.getPartyId(), filter.getQualityNameId(), filter.getQualityEntryId(),filter.getUserHeadId());
//

        List<StockMast> stockMastList = stockMastDao.filterByBatchFilterRequestWithPendingBatch(from, to,
                filter.getPartyId(), filter.getQualityNameId(), filter.getQualityEntryId(),filter.getUserHeadId());


        // party id and it's pending request
        Map<Long, PendingBatchDataForExcel> partyList = new HashMap<>();

        stockMastList.forEach(e -> {

            List<PendingBatchDataForExcel> newPendingBatchList = batchDao.getPendingBatchListForExcelByStockId(e.getId());
            if(!newPendingBatchList.isEmpty())
            {
                list.addAll(newPendingBatchList);
            }
        });


        return list;

    }
}
