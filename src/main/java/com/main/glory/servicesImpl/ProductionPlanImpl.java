package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.productionPlan.ProductionPlanDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.filters.Filter;
import com.main.glory.filters.FilterResponse;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.StockDataBatchData.response.GetBatchDetailByProduction;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetMast;
import com.main.glory.model.jet.request.AddJetData;
import com.main.glory.model.jet.responce.GetJetData;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.request.AddDirectBatchToJet;
import com.main.glory.model.productionPlan.request.AddProductionWithJet;
import com.main.glory.model.productionPlan.request.GetAllProduction;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("productionPlanServiceImpl")
public class ProductionPlanImpl {

    @Autowired
    SupplierServiceImpl supplierService;

    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @Autowired
    DyeingSlipServiceImpl dyeingSlipService;
    @Autowired
    ProductionPlanDao productionPlanDao;
    @Autowired
    JetServiceImpl jetService;

    @Autowired
    UserDao userDao;
    @Autowired
    QualityServiceImp qualityServiceImp;

    @Autowired
    PartyServiceImp partyServiceImp;

    @Autowired
    ShadeServiceImpl shadeService;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    BatchImpl batchService;

    @Autowired
    BatchDao batchDao;

    public Long saveProductionPlan(ProductionPlan productionPlan) throws Exception {

        // Optional<Quality> qualityIsExist=
        // qualityServiceImp.getQualityByIDAndPartyId(productionPlan.getQualityEntryId(),productionPlan.getPartyId());

        Optional<ShadeMast> shadeMastExist = shadeService.getShadeMastById(productionPlan.getShadeId());

        if (shadeMastExist.isPresent()) {
            // check already batch and stock is exist or not
            ProductionPlan productionPlanExist = productionPlanDao.getProductionByBatchId(productionPlan.getBatchId());
            if (productionPlanExist != null)
                throw new Exception(ConstantFile.Production_Record_Exist);

            List<BatchData> batchDataList = batchService.getBatchByBatchIdId(productionPlan.getBatchId());
            if (batchDataList.isEmpty())
                throw new Exception(ConstantFile.Batch_Data_Not_Found);

            // ProductionPlan shadeAndStockIsExist =
            // productionPlan.findByStockIdAndShadeId(productionPlan.)

            for (BatchData batchData : batchDataList) {
                if (batchData.getIsProductionPlanned() == false)
                    batchData.setIsProductionPlanned(true);
                batchDao.save(batchData);
            }

            productionPlanDao.save(productionPlan);
            return productionPlan.getId();

        } else
            throw new Exception("unable to insert the record");

    }

    public ProductionPlan getProductionData(Long id) throws Exception {
        ProductionPlan productionPlan = productionPlanDao.getByProductionId(id);

        if (productionPlan == null)
            throw new Exception(ConstantFile.Production_Not_Found);

        return productionPlan;

    }

    public Boolean deleteById(Long id) throws Exception {
        ProductionPlan productionPlan = productionPlanDao.getByProductionId(id);
        if (productionPlan == null)
            return false;

        if (productionPlan.getStatus() == true)
            throw new Exception(ConstantFile.Jet_Exist_With_Production);

        // change the status of batch if all the condition are
        List<BatchData> batchDataList = batchDao.getBatchByBatchId(productionPlan.getBatchId());
        for (BatchData batchData : batchDataList) {
            batchDao.updateProductionPlanned(batchData.getId(), false);
        }
        productionPlanDao.deleteProductionById(id);
        return true;

    }

    public List<GetAllProductionWithShadeData> getAllProductionData(String id) throws Exception {

        // get the user record first
        Long userId = Long.parseLong(id);

        UserData userData = userDao.getUserById(userId);
        Long userHeadId = null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());

        List<String> batchId = new ArrayList<>();

        List<GetAllProductionWithShadeData> list = new ArrayList<>();
        // filter the record
        if (permissions.getViewAll()) {
            userId = null;
            userHeadId = null;

            batchId = productionPlanDao.getAllBatchWithoutMergeBatch();
            // List<GetAllProductionWithShadeData> finalList = list;

            for (String e : batchId) {
                GetAllProductionWithShadeData record = getProductionWithColorToneByBatchId(e);
                if (record != null)
                    list.add(record);

            }

            // get batches of merge batch
            batchId = productionPlanDao.getAllProductionBasedOnMergeBatchId();
            for (String e : batchId) {

                GetAllProductionWithShadeData record = getProductionWithColorToneByBatchId(e);
                if (record != null)
                    list.add(record);

            }

        } else if (permissions.getViewGroup()) {
            // check the user is master or not ?
            // admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                batchId = productionPlanDao.getAllBatchWithoutMergeBatch();
                // List<GetAllProductionWithShadeData> finalList = list;

                for (String e : batchId) {
                    Long stockId = batchDao.getControlIdByBatchId(e);
                    StockMast stockMast = stockBatchService.getStockByStockId(stockId);
                    if (stockMast != null) {
                        GetAllProductionWithShadeData data = productionPlanDao.getProductionWithColorToneByBatchId(e,
                                stockMast.getParty().getId(), stockMast.getQuality().getId());
                        if (data != null)
                            list.add(data);
                    }

                }
            } else if (userData.getUserHeadId() > 0) {
                // check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                // list =
                // productionPlanDao.getAllProductionWithColorTone(userId,userHeadId);//new
                // ArrayList<>();

            }
        } else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId = null;
            // list =
            // productionPlanDao.getAllProductionWithColorTone(userId,userHeadId);//new
            // ArrayList<>();
        }

        /*
         * if(list.isEmpty()) throw new Exception("no data found");
         */

        if (!list.isEmpty()) {
            list.forEach(e -> {
                e.setTotalMtr(stockBatchService.changeInFormattedDecimal(e.getTotalMtr()));
                e.setTotalWt(stockBatchService.changeInFormattedDecimal(e.getTotalWt()));
            });
        }

        return list;

    }

    public FilterResponse<GetAllProductionWithShadeData> getAllProductionDataPaginated(String id,
            GetBYPaginatedAndFiltered requestParam) throws Exception {

        // get the user record first
        Long userId = Long.parseLong(id);

        UserData userData = userDao.getUserById(userId);
        Long userHeadId = null;

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getSb().intValue());

        List<String> batchId = new ArrayList<>();

        String partyId = null;
        String qualityEntryId = null;
        String batch_id = null;

        // batchData.addAll(batchDataForMergeBatch);
        for (int i = 0; i < requestParam.getData().getParameters().size(); i++) {
            Filter filter = requestParam.getData().getParameters().get(i);
            String field = filter.getField().get(0);
            String value = filter.getValue();

            if (field.equals("partyId"))
                partyId = value;

            if (field.equals("qualityEntryId"))
                qualityEntryId = value;

            if (field.equals("batchId"))
                batch_id = value;

        }

        List<GetAllProductionWithShadeData> list = new ArrayList<>();
        // filter the record
        if (permissions.getViewAll()) {
            userId = null;
            userHeadId = null;
            batchId = productionPlanDao.getAllBatchWithoutMergeBatch();
            // List<GetAllProductionWithShadeData> finalList = list;
            for (String e : batchId) {

                if (batch_id != null)
                    if ((!e.contains(batch_id)))
                        continue;

                GetAllProductionWithShadeData record = getProductionWithColorToneByBatchId(e);

                if (partyId != null)
                    if (!partyId.equals(record.getPartyId()))
                        continue;

                if (qualityEntryId != null)
                    if (!qualityEntryId.equals(record.getQualityEntryId()))
                        continue;

                if (record != null)
                    list.add(record);

            }
            // get batches of merge batch
            batchId = productionPlanDao.getAllProductionBasedOnMergeBatchId();
            for (String e : batchId) {
                if (batch_id != null)
                    if ((!e.contains(batch_id)))
                        continue;

                GetAllProductionWithShadeData record = getProductionWithColorToneByBatchId(e);
                if (partyId != null)
                    if (!partyId.equals(record.getPartyId()))
                        continue;

                if (qualityEntryId != null)
                    if (!qualityEntryId.equals(record.getQualityEntryId()))
                        continue;

                if (record != null)
                    list.add(record);

            }

        } else if (permissions.getViewGroup()) {
            // check the user is master or not ?
            // admin
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                batchId = productionPlanDao.getAllBatchWithoutMergeBatch();
                // List<GetAllProductionWithShadeData> finalList = list;

                for (String e : batchId) {
                    if (batch_id != null)
                        if ((!e.contains(batch_id)))
                            continue;

                    Long stockId = batchDao.getControlIdByBatchId(e);
                    StockMast stockMast = stockBatchService.getStockByStockId(stockId);
                    if (stockMast != null) {
                        GetAllProductionWithShadeData data = productionPlanDao.getProductionWithColorToneByBatchId(e,
                                stockMast.getParty().getId(), stockMast.getQuality().getId());
                        if (partyId != null)
                            if (!partyId.equals(data.getPartyId()))
                                continue;

                        if (qualityEntryId != null)
                            if (!qualityEntryId.equals(data.getQualityId()))
                                continue;

                        if (data != null)
                            list.add(data);
                    }

                }
            } else if (userData.getUserHeadId() > 0) {
                // check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                // list =
                // productionPlanDao.getAllProductionWithColorTone(userId,userHeadId);//new
                // ArrayList<>();

            }
        } else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId = null;
            // list =
            // productionPlanDao.getAllProductionWithColorTone(userId,userHeadId);//new
            // ArrayList<>();
        }

        if (!list.isEmpty()) {
            list.forEach(e -> {
                e.setTotalMtr(stockBatchService.changeInFormattedDecimal(e.getTotalMtr()));
                e.setTotalWt(stockBatchService.changeInFormattedDecimal(e.getTotalWt()));
            });
        }

        int pageSize = requestParam.getData().getPageSize();
        int pageIndex = requestParam.getData().getPageIndex();
        FilterResponse<GetAllProductionWithShadeData> response = new FilterResponse<GetAllProductionWithShadeData>(
                list.subList(Integer.min(pageIndex * pageSize, list.size()),
                        Integer.min((pageIndex + 1) * pageSize, list.size())),
                pageIndex, pageSize, list.size());
        return response;

    }

    public GetAllProductionWithShadeData getProductionWithColorToneByBatchId(String e) throws Exception {

        GetAllProductionWithShadeData record = new GetAllProductionWithShadeData();
        List<GetBatchWithControlId> batchDataForMergeBatch = null;
        GetAllProductionWithShadeData data;
        // check the batch id is merge or simple
        ProductionPlan productionPlanExist = productionPlanDao.getProductionByBatchId(e);
        if (productionPlanExist != null) {

            if (productionPlanExist.getIsMergeBatchId() == true) {
                // if true then process for multiple batch
                record.setId(productionPlanDao.getProductionIdByBatchId(e));
                record.setBatchId(e);
                batchDataForMergeBatch = batchDao.getAllBatchByMergeBatchId(e);
                for (GetBatchWithControlId batchRespone : batchDataForMergeBatch) {
                    // Long stockId = batchDao.getControlIdByBatchId(e);

                    System.out.println(productionPlanExist.getId() + "batch-" + batchRespone.getBatchId() + "-"
                            + batchRespone.getControlId());

                    StockMast stockMast = stockBatchService.getStockByStockId(batchRespone.getControlId());
                    Party party = partyServiceImp.getPartyById(stockMast.getParty().getId());
                    Quality quality = qualityServiceImp.getQualityByEntryId(stockMast.getQuality().getId());
                    QualityName qualityName = quality.getQualityName();

                    record.setPartyId(record.getPartyId() == null ? party.getId().toString()
                            : record.getPartyId() + "," + party.getId().toString());
                    record.setQualityEntryId(record.getQualityEntryId() == null ? quality.getId().toString()
                            : record.getQualityEntryId() + "," + quality.getId().toString());
                    record.setPartyName(record.getPartyName() == null ? party.getPartyName()
                            : record.getPartyName() + "," + party.getPartyName());
                    record.setQualityId(record.getQualityId() == null ? quality.getQualityId()
                            : record.getQualityId() + "," + quality.getQualityId());
                    record.setQualityName(record.getQualityName() == null ? qualityName.getQualityName()
                            : record.getQualityName() + "," + qualityName.getQualityName());
                }
                record.setIsMergeBatchId(true);

                // if it is direct then store diffrent record
                if (productionPlanExist.getIsDirect() == false) {
                    DyeingProcessMast dyeingProcessMast = dyeingProcessService
                            .getDyeingProcessById(productionPlanDao.getDyeingProcessByBatchId(e));
                    record.setShadeId(productionPlanDao.getShadeIdByBatchId(e));
                    record.setProcessName(dyeingProcessMast.getProcessName());
                    record.setPartyShadeNo(productionPlanDao.getPartyShadenoByBatchId(e));
                    record.setColorTone(productionPlanDao.getColorToneByBatchId(e));
                }

                record.setTotalMtr(stockBatchService.changeInFormattedDecimal(batchDao.getTotalMtrByMergeBatchId(e)));
                record.setTotalWt(stockBatchService.changeInFormattedDecimal(batchDao.getTotalWtByMergeBatchId(e)));

            } else {
                // else process for single batch
                Long stockId = batchDao.getControlIdByBatchId(e);
                StockMast stockMast = stockBatchService.getStockByStockId(stockId);
                if (stockMast != null) {
                    record = productionPlanDao.getProductionWithColorToneByBatchId(e, stockMast.getParty().getId(),
                            stockMast.getQuality().getId());
                    record.setTotalWt(stockBatchService.changeInFormattedDecimal(record.getTotalWt()));
                    record.setTotalMtr(stockBatchService.changeInFormattedDecimal(record.getTotalMtr()));
                    record.setBatchId(e);
                    record.setPartyId(stockMast.getParty().getId().toString());
                    record.setQualityEntryId(stockMast.getQuality().getId().toString());

                }

            }
        }

        if (record != null)
            return record;
        else
            return null;

    }

    public void updateProductionPlan(ProductionPlan productionPlan, String id) throws Exception {

        Optional<ProductionPlan> productionPlanExist = productionPlanDao.findById(productionPlan.getId());
        if (productionPlanExist.isEmpty())
            throw new Exception(ConstantFile.Production_Not_Found);

        productionPlanDao.save(productionPlan);

    }

    public List<GetAllProductionWithShadeData> getAllProductionDataWithAndWithoutPlan() throws Exception {
        Optional<List<GetAllProductionWithShadeData>> productionWithShadeData = productionPlanDao.getAllProduction();
        if (productionWithShadeData.isEmpty())
            throw new Exception("no data found");

        return productionWithShadeData.get();

    }

    // list of production who are not added yet into the jet
    public List<ProductionPlan> getAllProductionListByPartyAndQuality(Long partyId, Long qualityEntryId) {
        List<ProductionPlan> list = null;// productionPlanDao.getProductionByPartyAndQuality(partyId,qualityEntryId);
        return list;

    }

    public ProductionPlan getProductionDataByBatchAndStock(String batchId, Long controlId) {
        ProductionPlan productionPlan = null;// productionPlanDao.getProductionByBatchAndStockId(batchId,controlId);
        return productionPlan;
    }

    public GetBatchDetailByProduction getBatchDetailByProductionAndBatchId(Long productionId, String batchId)
            throws Exception {

        ProductionPlan productionExist = productionPlanDao.getProductionByBatchAndProduction(batchId, productionId);
        GetBatchDetailByProduction data = null;
        /*
         * if(productionExist==null) throw new Exception("no data found");
         * 
         * StockMast
         * stockMast=stockBatchService.getStockByStockId(productionExist.getStockId());
         * Double totalWt =
         * batchDao.getAllBatchQtyByBatchIdAndStockId(productionExist.getBatchId(),
         * productionExist.getStockId()); Party
         * party=partyServiceImp.getPartyDetailById(stockMast.getParty().getId());
         * if(productionExist.getShadeId()==null) { data=new
         * GetBatchDetailByProduction(party,totalWt,stockMast,batchId); } else {
         * Optional<ShadeMast> shadeMast =
         * shadeService.getShadeMastById(productionExist.getShadeId()); data=new
         * GetBatchDetailByProduction(party,totalWt,shadeMast,stockMast,batchId); }
         */

        return data;

    }

    public List<GetAllProduction> getAllProductionWithoutFilter() throws Exception {
        List<GetAllProduction> dataList = new ArrayList<>();
        Optional<List<GetAllProductionWithShadeData>> list = productionPlanDao.getAllProduction();

        /*
         * if(list.isEmpty()) throw new Exception("no data found");
         * 
         * for(GetAllProductionWithShadeData g:list.get()) { Party party =
         * partyServiceImp.getPartyDetailById(g.getPartyId()); GetQualityResponse
         * quality=qualityServiceImp.getQualityByID(g.getQualityEntryId());
         * 
         * if(party==null) continue; if (quality==null) continue;
         * 
         * dataList.add(new GetAllProduction(g,party,quality)); }
         */

        return dataList;
    }

    public List<ProductionPlan> getProductionByShadeId(Long shadeId) {
        return productionPlanDao.getAllProductionByShadeId(shadeId);
    }

    public List<ProductionPlan> getProductionByQualityId(Long id) {
        List<ProductionPlan> list = null;// productionPlanDao.getAllProductionByQualityId(id);
        return list;
    }

    public List<ProductionPlan> getAllProductinByPartyId(Long id) {
        return null;// productionPlanDao.getAllProuctionByPartyId(id);
    }

    public List<ProductionPlan> getProductionByStockId(Long id) {
        List<ProductionPlan> productionPlans = null;// productionPlanDao.getProductionByStockId(id);
        return productionPlans;
    }

    public Long saveProductionPlanWithJet(AddProductionWithJet productionPlan) throws Exception {
        // check first the batch id is coming batch or merge batch id
        BatchData isMergeBatchId = batchDao.getIsMergeBatchId(productionPlan.getBatchId());// if data is not null then
                                                                                           // it is merge batch id
        BatchData isBatchId = batchDao.getIsBatchId(productionPlan.getBatchId());
        Double wt = 0.0;
        // if production id is 0 then create the record with shade or jet

        if (productionPlan.getProductionId() == null || productionPlan.getProductionId() == 0) {
            // create the record

            // perform all the record based on merge batchID
            // first of all we have to store the infor of production any how
            // then process for the jet if the jet id is not null
            ProductionPlan productionPlanExistWithMergeBatchId = productionPlanDao
                    .getProductionByBatchId(productionPlan.getBatchId());
            if (productionPlanExistWithMergeBatchId != null)
                throw new Exception(ConstantFile.Production_Record_Exist);

            // update the status of batches as well
            List<BatchData> batchDataList;
            if (isMergeBatchId != null) {
                batchDataList = batchService.getBatchByMergeBatchId(productionPlan.getBatchId());
            } else {
                batchDataList = batchDao.getBatchByBatchId(productionPlan.getBatchId());
            }
            if (batchDataList.isEmpty())
                throw new Exception(ConstantFile.Batch_Data_Not_Found);

            // ProductionPlan shadeAndStockIsExist =
            // productionPlan.findByStockIdAndShadeId(productionPlan.)

            productionPlanExistWithMergeBatchId = new ProductionPlan(productionPlan);
            if (isMergeBatchId != null) {
                wt = batchDao.getTotalWtByMergeBatchId(productionPlanExistWithMergeBatchId.getBatchId());
                productionPlanExistWithMergeBatchId.setIsMergeBatchId(true);
            } else {
                wt = batchDao.getTotalWtByBatchId(productionPlanExistWithMergeBatchId.getBatchId());
                productionPlanExistWithMergeBatchId.setIsMergeBatchId(false);
            }
            productionPlanExistWithMergeBatchId.setIsDirect(false);

            // check the wt of batch for jet
            if (productionPlan.getJetId() != null && productionPlan.getJetId() != 0l) {
                JetMast jetMast = jetService.getJetMastById(productionPlan.getJetId());
                if (jetMast == null)
                    throw new Exception(ConstantFile.Jet_Not_Exist_With_Name);

                if (jetMast.getCapacity() < wt)
                    throw new Exception(ConstantFile.Jet_Capacity_Exceed);
            }

            // change the status og batches
            for (BatchData batchData : batchDataList) {
                if (batchData.getIsProductionPlanned() == false)
                    batchData.setIsProductionPlanned(true);
                batchDao.save(batchData);
            }

            ProductionPlan productionPlanExistWithBatchId = productionPlanDao
                    .getProductionByBatchId(productionPlan.getBatchId());
            if (productionPlanExistWithBatchId != null)
                throw new Exception(ConstantFile.Production_With_Jet);
            ProductionPlan x = productionPlanDao.save(productionPlanExistWithMergeBatchId);

            // now check that the jet id is null or not
            if (productionPlan.getJetId() == 0 || productionPlan.getJetId() == null) {
                return x.getId();
            } else {
                // we have to add the same data with jet as well
                List<AddJetData> jetDataList = new ArrayList<>();
                AddJetData addJetData = new AddJetData();
                addJetData.setProductionId(x.getId());
                addJetData.setControlId(productionPlan.getJetId());
                addJetData.setSequence(12l);
                jetDataList.add(addJetData);

                jetService.saveJetData(jetDataList);
                return x.getId();
            }

        } else {
            // update the record of only shade , if jet is not assign and check the status
            // as well

            // check the production is already added in jet or not
            ProductionPlan productionPlanExist = productionPlanDao.getByProductionId(productionPlan.getProductionId());
            if (productionPlanExist.getStatus() == true)
                throw new Exception(ConstantFile.Production_With_Jet);

            // change the status of production
            Optional<ShadeMast> shadeMastExist = shadeService.getShadeMastById(productionPlan.getShadeId());
            if (shadeMastExist.isEmpty())
                throw new Exception(ConstantFile.Shade_Not_Found);

            productionPlanExist = new ProductionPlan(productionPlan);

            if (isMergeBatchId != null) {
                wt = batchDao.getTotalWtByMergeBatchId(productionPlanExist.getBatchId());
                productionPlanExist.setIsMergeBatchId(true);
            } else {
                wt = batchDao.getTotalWtByBatchId(productionPlanExist.getBatchId());
                productionPlanExist.setIsMergeBatchId(false);
            }
            productionPlanExist.setIsDirect(false);

            // before saving check the jet capacity
            // check the wt of batch for jet
            if (productionPlan.getJetId() != null || productionPlan.getJetId() != 0) {
                JetMast jetMast = jetService.getJetMastById(productionPlan.getJetId());
                if (jetMast == null)
                    throw new Exception(ConstantFile.Jet_Not_Exist_With_Name);

                if (jetMast.getCapacity() < wt)
                    throw new Exception(ConstantFile.Jet_Capacity_Exceed);
            }

            ProductionPlan x = productionPlanDao.save(productionPlanExist);

            // now check that the jet id is null or not
            if (productionPlan.getJetId() == 0 || productionPlan.getJetId() == null) {
                return x.getId();
            } else {
                // we have to add the same data with jet as well
                List<AddJetData> jetDataList = new ArrayList<>();
                AddJetData addJetData = new AddJetData();
                addJetData.setProductionId(x.getId());
                addJetData.setControlId(productionPlan.getJetId());
                addJetData.setSequence(12l);
                jetDataList.add(addJetData);

                jetService.saveJetData(jetDataList);
                return x.getId();
            }

        }

    }

    public Long saveDirectDyeingSlip(AddDirectBatchToJet record) throws Exception {
        // store direct dyeing slip with jet and with shade or else shade colour

        /*
         * 
         * 1.check the record exist or not 2.create the production 3.add the record to
         * the jet 4.create the dyeing slip for the batch 5.change the batch status
         * 
         */

        // check first the batch id is coming batch or merge batch id
        BatchData isMergeBatchId = batchDao.getIsMergeBatchId(record.getBatchId());// if data is not null then it is
                                                                                   // merge batch id
        BatchData isBatchId = batchDao.getIsBatchId(record.getBatchId());
        Double totalBatchCapacity = 0.0;

        Long jetSequence = 0l;

        Double totalBatchWt = 0.0;// stockBatchService.getWtByControlAndBatchId(record.getStockId(),record.getBatchId());
        // process type ::directDyeing

        // check that the production is already exist or not

        ProductionPlan productionPlanExistWithBatch = productionPlanDao.getProductionByBatchId(record.getBatchId());
        if (productionPlanExistWithBatch != null)
            throw new Exception(ConstantFile.Production_Record_Exist);

        ProductionPlan productionPlan = new ProductionPlan(record);

        if (isMergeBatchId != null) {
            totalBatchWt = batchDao.getTotalWtByMergeBatchId(record.getBatchId());
            productionPlan.setIsMergeBatchId(true);
        } else {
            totalBatchWt = batchDao.getTotalWtByBatchId(record.getBatchId());
            productionPlan.setIsMergeBatchId(false);
        }

        /*
         * if(productionPlan.getShadeId()==null &&
         * productionPlan.getColorName().isEmpty()) throw new
         * Exception("please enter shade or color name");
         */

        // check the production is exist with batch and stock or not

        ProductionPlan productionPlanExist = productionPlanDao.getProductionByBatchId(record.getBatchId());
        if (productionPlanExist != null)
            throw new Exception("batch and stock is already exist");

        // jet capacity check and store the jet record
        JetMast jetMast = jetService.getJetMastById(record.getJetId());
        List<GetJetData> jetDataList = jetService.getJetRecordData(record.getJetId());

        for (GetJetData getJetData : jetDataList) {
            ProductionPlan jetWithProduction = productionPlanDao.getByProductionId(getJetData.getProductionId());
            // totalBatchCapacity+=stockBatchService.getWtByControlAndBatchId(jetWithProduction.getStockId(),jetWithProduction.getBatchId());

            if (jetSequence < getJetData.getSequence()) {
                jetSequence = getJetData.getSequence();
            }
        }

        if (totalBatchWt > jetMast.getCapacity())
            throw new Exception(ConstantFile.Jet_Capacity_Exceed);

        // don't save the shade id with produciton because it is direct dyeing shade id
        // is just fore refrecnce
        productionPlan.setShadeId(null);
        ProductionPlan x = productionPlanDao.save(productionPlan);

        JetData jetData = new JetData(x, jetSequence + 1, jetMast);
        jetService.saveJetRecord(jetData);

        // create the dyeing slip for the given dyeing record only

        DyeingSlipMast dyeingSlipMast = new DyeingSlipMast(record, x);
        List<DyeingSlipData> dyeingSlipDataList = new ArrayList<>();
        // dyeing slip record
        dyeingSlipDataList.add(new DyeingSlipData(record.getDyeingSlipData()));

        dyeingSlipMast.setDyeingSlipDataList(dyeingSlipDataList);
        dyeingSlipService.addDirectDyeingSlip(dyeingSlipMast);

        // List<DyeingSlipItemData> dyeingSlipItemDataList =new ArrayList<>();

        // if shade id is not null then get the shade item as well
        /*
         * if(record.getShadeId()!=null) { Optional<ShadeMast> shadeMast =
         * shadeService.getShadeMastById(record.getShadeId()); for (ShadeData shadeData
         * : shadeMast.get().getShadeDataList()) { Optional<Supplier> supplier =
         * supplierService.getSupplier(shadeData.getSupplierId()); SupplierRate
         * supplierRate =
         * supplierService.getSupplierRateByItemId(shadeData.getSupplierItemId());
         * dyeingSlipItemDataList.add(new DyeingSlipItemData(shadeData, supplierRate,
         * supplier.get(), totalBatchWt)); } }
         */

        /*
         * dyeingSlipItemDataList.addAll(dyeingSlipData.getDyeingSlipItemData());
         * dyeingSlipData.setDyeingSlipItemData(dyeingSlipItemDataList);
         */

        // change the batch status

        // update the status of batches as well
        List<BatchData> batchDataList = null;

        if (isMergeBatchId != null) {
            batchDataList = batchService.getBatchByMergeBatchId(x.getBatchId());

        } else {
            batchDataList = batchService.getBatchByBatchIdId(x.getBatchId());
        }

        // ProductionPlan shadeAndStockIsExist =
        // productionPlan.findByStockIdAndShadeId(productionPlan.)

        for (BatchData batchData : batchDataList) {
            if (batchData.getIsProductionPlanned() == false)
                batchData.setIsProductionPlanned(true);
            batchDao.save(batchData);
        }

        return x.getId();

    }

    public ProductionPlan getProductionDataById(Long productionId) {
        return productionPlanDao.getByProductionId(productionId);
    }

    public Party getPartyDetailByProductionId(Long productionId) {
        return null;// productionPlanDao.getPartyByProductionId(productionId);
    }

    public Quality getQualityByProductionId(Long productionId) {
        return null;// productionPlanDao.getQualityByProductionId(productionId);
    }

    public QualityName getQualityNameByProductionId(Long productionId) {
        return null;// productionPlanDao.getQualityNameByProductionId(productionId);
    }

    public ProductionPlan getById(Long productionId) {
        return productionPlanDao.getById(productionId);
    }

    public ProductionPlan getProductionByBatchId(String batchId) {
        return productionPlanDao.getProductionByBatchId(batchId);
    }

    public void deleteProductionByBatchId(String batchId) throws Exception {

        ProductionPlan productionPlanExist = productionPlanDao.getProductionByBatchId(batchId);

        if (productionPlanExist == null)
            throw new Exception(ConstantFile.Production_Not_Found);

        if (productionPlanExist.getStatus() == true)
            throw new Exception(ConstantFile.Production_With_Jet);

        productionPlanDao.deleteProductionById(productionPlanExist.getId());
        List<BatchData> batchDataList = null;

        // change the flag of batchData list
        if (productionPlanExist.getIsMergeBatchId()) {
            batchDataList = batchDao.getBatchByMergeBatchId(productionPlanExist.getBatchId());

        } else {
            batchDataList = batchDao.getBatchByBatchId(productionPlanExist.getBatchId());
        }

        if (batchDataList.isEmpty())
            throw new Exception(ConstantFile.Batch_Data_Not_Exist);

        batchDataList.forEach(e -> {
            batchDao.updateProductionPlanned(e.getId(), false);
        });

    }

    /*
     * public List<BatchData> getAllBatch(Long partyId, Long qualityEntryId, String
     * batchId) {
     * 
     * }
     */
}
