package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.MergeSplitBatch;
import com.main.glory.model.StockDataBatchData.request.WTByStockAndBatch;
import com.main.glory.model.StockDataBatchData.response.*;
import com.main.glory.servicesImpl.BatchImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StockBatchController extends ControllerConfig {

    @Autowired
    private BatchImpl batchService;

    @Autowired
    private StockBatchServiceImpl stockBatchService;


    @PostMapping("/stockBatch")
    public GeneralResponse<Boolean> createBatch(@RequestBody StockMast stockMast) throws Exception {
        try {
            Boolean flag = stockBatchService.saveStockBatch(stockMast);
            if (flag == true)
                return new GeneralResponse<>(true, "Stock batch created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<>(false, "Stock batch not created because quality in not availble", false, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/stockBatch/getWtByStockIdAndBatchId/{batchId}/{stockId}")
    public GeneralResponse<WTByStockAndBatch> getWtByStockIdAndBatchId(@PathVariable(name = "batchId")String batchId,@PathVariable(name = "stockId")Long stockId) throws Exception {
        try {
            WTByStockAndBatch qty= stockBatchService.getWtByStockAndBatchId(stockId,batchId);

                return new GeneralResponse<>(qty, "Stock batch qty fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stockBatch/batch/ByQualityAndParty/{qualityId}/{partyId}")
    public GeneralResponse<List<GetAllBatch>> getBatchById(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId) {
        try {
            if (qualityId != null && partyId != null) {
                List<GetAllBatch> batchData = stockBatchService.getBatchByPartyAndQuality(qualityId, partyId);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            } else {
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stockBatch/all/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List<GetAllStockWithPartyNameResponse>>> getAllStockBatch(@PathVariable(value = "getBy") String getBy, @PathVariable(value = "id") Long id) throws Exception {

        GeneralResponse<List<GetAllStockWithPartyNameResponse>> result;

        try {
            List<GetAllStockWithPartyNameResponse> stockMast = null;
            switch (getBy) {
                case "own":
                    stockMast = stockBatchService.getAllStockBatch(getBy, id);
                    if (stockMast == null) {
                        result= new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);
                    } else {
                        result= new GeneralResponse<>(stockMast, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    }
                    break;

                case "group":
                    stockMast = stockBatchService.getAllStockBatch(getBy, id);
                    if (stockMast == null) {
                        result= new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);
                    } else {
                        result= new GeneralResponse<>(stockMast, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    }
                    break;

                case "all":
                    stockMast = stockBatchService.getAllStockBatch(null, null);
                    if (stockMast == null) {
                        result= new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.OK);
                    } else {
                        result= new GeneralResponse<>(stockMast, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    }
                    break;
                default:
                    result= new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/stockBatch/{id}")
    public GeneralResponse<StockMast> getStockMastById(@PathVariable(value = "id") Long id) {
        try {
            if (id != null) {
                Optional<StockMast> stockMast = stockBatchService.getStockBatchById(id);
                if (stockMast.isPresent()) {
                    return new GeneralResponse<StockMast>(stockMast.get(), "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                } else {
                    return new GeneralResponse<StockMast>(null, "no data found for id: " + id, false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                }
            } else {
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stockBatch/getAllBatchWithoutFilter")
    public GeneralResponse<List<GetAllBatch>> getAllBatchWithoutFilter() {
        GeneralResponse<List<GetAllBatch>> result;
        try {

            List<GetAllBatch> stockMast = stockBatchService.getAllBatchWithoutFilter();
            if (!stockMast.isEmpty()) {
                result= new GeneralResponse<>(stockMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result= new GeneralResponse<>(null, "no data found  "  , false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;

    }

    @GetMapping("/stockBatch/batch/{controlId}/{batchId}")
    public GeneralResponse<List<BatchData>> getBatchById(@PathVariable(value = "batchId") String batchId, @PathVariable(value = "controlId") Long controlId) {
        try {
            if (batchId != null) {
                List<BatchData> batchData = stockBatchService.getBatchById(batchId, controlId);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            } else {
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stockBatch/batch/ByQualityAndPartyWithoutProductionPlan/{qualityId}/{partyId}")
    public GeneralResponse<List<GetAllBatch>> ByQualityAndPartyWithoutProducctionPlan(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId) {
        try {
            if (qualityId != null && partyId != null) {
                List<GetAllBatch> batchData = stockBatchService.byQualityAndPartyWithoutProductionPlan(qualityId, partyId);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            } else {
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stockBatch/batch/ByQualityAndPartyWithProductionPlan/{qualityId}/{partyId}")
    public GeneralResponse<List<GetAllBatch>> ByQualityAndPartyWithProducctionPlan(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId) {
        try {
            if (qualityId != null && partyId != null) {
                List<GetAllBatch> batchData = stockBatchService.byQualityAndPartyWithProductionPlan(qualityId, partyId);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            } else {
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    //get complete stock and batch based on party id and qualityid
    @GetMapping("/stockBatch/stockBatchDataList/ByQualityAndParty/{qualityId}/{partyId}")
    public GeneralResponse<List<StockMast>> getStockBatchListById(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId) {
        try {
            if (qualityId != null && partyId != null) {
                List<StockMast> batchData = stockBatchService.getStockBatchListById(qualityId, partyId);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            } else {
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stockBatch/batch/all")
    public GeneralResponse<List<BatchToPartyAndQuality>> getAllBatch() {
        try {

            List<BatchToPartyAndQuality> batchData = stockBatchService.getAllBatchDetail();

            return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    //batch by quality
    @GetMapping("/stockBatch/batch/byQualityId/{id}")
    public GeneralResponse<List<BatchData>> getAllBatchByQualityId(@PathVariable(name = "id") Long qualityId) {
        try {

            List<BatchData> batchData = stockBatchService.getAllBatchByQualityId(qualityId);

            return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stockBatch/BatchToPartyAndQuality/{controlId}/{batchId}")
    public GeneralResponse<BatchToPartyAndQuality> getPartyAndQualityByBatch(@PathVariable(name = "controlId") Long controlId, @PathVariable(name = "batchId") String batchId) {
        try {

            BatchToPartyAndQuality batchData = stockBatchService.getPartyAndQualityByBatch(controlId, batchId);

            if(batchData!=null)
            return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<>(batchData, "no data found", false, System.currentTimeMillis(), HttpStatus.OK);




        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stockBatch/IsBatchAvailableWithId/{controlId}/{batchId}")
    public GeneralResponse<Boolean> IsBatchAvailable(@PathVariable(name = "controlId") Long controlId, @PathVariable(name = "batchId") String batchId) {
        try {

            Boolean batchDataFlag = stockBatchService.IsBatchAvailable(controlId, batchId);

            if (batchDataFlag == true) {
                return new GeneralResponse<>(true, "Batch Id is available", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<>(false, "Batch Id is already exist", false, System.currentTimeMillis(), HttpStatus.FOUND);

            }


        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/stockBatch/batchWithoutExtra/ByQualityAndParty/{qualityId}/{partyId}")
    public GeneralResponse<List<GetAllBatch>> getBatchWithoutProductionPlanById(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId) {
        try {
            if (qualityId != null && partyId != null) {
                List<GetAllBatch> batchData = stockBatchService.getBatchWithoutProductionPlanByPartyAndQuality(qualityId, partyId);

                return new GeneralResponse<>(batchData, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

            } else {
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping("/stockBatch")
    public GeneralResponse<Boolean> updateStockBatch(@RequestBody StockMast stockMast) {
        try {
            stockBatchService.updateBatch(stockMast);
            return new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/stockBatch/MergeBatch")
    public GeneralResponse<Boolean> updateBatchMerge(@RequestBody List<MergeSplitBatch> batchData1) {
        try {
            stockBatchService.updateBatchForMerge(batchData1);
            return new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/stockBatch/SplitBatch")
    public GeneralResponse<Boolean> updateBatchSplit(@RequestBody List<MergeSplitBatch> batchData1) {
        try {
            stockBatchService.updateBatchSplit(batchData1);
            return new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/stockBatch/{id}")
    public GeneralResponse<Boolean> deleteStockBatch(@PathVariable(value = "id") Long id) {
        try {
            stockBatchService.deleteStockBatch(id);
            return new GeneralResponse<>(true, "stockBatch deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/batchGr/delete/{id}")
    public GeneralResponse<Boolean> deleteBatchGr(@PathVariable(value = "id") Long id) {
        try {
            stockBatchService.deleteBatchGr(id);
            return new GeneralResponse<>(true, "Batch gr deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/stockBatch/delete/{controlId}/{batchId}")
    public GeneralResponse<Boolean> deleteBatchByControlIdAndBatchID(@PathVariable(name = "controlId") Long controlId, @PathVariable(name = "batchId") String batchId) {
        try {
            Boolean flag = stockBatchService.deleteStockBatchWithControlAndBatchID(controlId, batchId);

            if (flag == true)
                return new GeneralResponse<>(true, "Batch deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<>(false, "Batch not deleted", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/stockBatch/getAllBatchForFinishMtr")
    public GeneralResponse<List<GetAllBatchWithProduction>> getAllBatchWithoutBillGenerated() {
        GeneralResponse<List<GetAllBatchWithProduction>> response;

        try {
            List<GetAllBatchWithProduction> flag = stockBatchService.getAllBatchWithoutBillGenerated();

            if (!flag.isEmpty())
                response= new GeneralResponse<>(flag, "Batch fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                response= new GeneralResponse<>(flag, "Batch not found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            response= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return response;

    }

}
