package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.filters.FilterResponse;
import com.main.glory.filters.StockDataBatchData.StockMastFilter;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.BatchReturn;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.*;
import com.main.glory.model.StockDataBatchData.response.*;
import com.main.glory.services.AllStockDateWiseData;
import com.main.glory.services.DataConversion;
import com.main.glory.services.DataFilterService;
import com.main.glory.servicesImpl.BatchImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StockBatchController extends ControllerConfig {

    @Autowired
    private BatchImpl batchService;

    @Autowired
    private StockBatchServiceImpl stockBatchService;

    @Autowired
    AllStockDateWiseData allStockDateWiseData;
    
    @Autowired
    DataFilterService dataFilterService;
     

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll;

    @GetMapping("/stockBatch/isBatchExists/{name}/{id}")
    public ResponseEntity<GeneralResponse<Boolean, Object>> isBatchIdUnique(@PathVariable(value = "name") String name, @PathVariable(value = "id") Long id) {
        GeneralResponse<Boolean, Object> result;
        try {
            Boolean isPresent = batchService.isBatchIdExists(name, id);
            if (isPresent)
                result = new GeneralResponse<>(isPresent, ConstantFile.Batch_Id_Found + isPresent, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            else {
                result = new GeneralResponse<>(isPresent, ConstantFile.Batch_Id_Not_Found + isPresent, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @PostMapping("/stockBatch")
    public ResponseEntity<GeneralResponse<Long, Object>> createBatch(@RequestBody AddStockBatch stockMast, @RequestHeader Map<String, String> headers) throws Exception {
        GeneralResponse<Long, Object> result = null;
        try {
            Long flag = stockBatchService.saveStockBatch(stockMast, headers.get("id"));
            if (flag != null)
                result = new GeneralResponse<>(flag, ConstantFile.StockBatch_Added, true, System.currentTimeMillis(), HttpStatus.OK, stockMast);

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, stockMast);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/stockBatch/getWtByStockIdAndBatchId/{batchId}/{stockId}")
    public ResponseEntity<GeneralResponse<WTByStockAndBatch, Object>> getWtByStockIdAndBatchId(@PathVariable(name = "batchId") String batchId, @PathVariable(name = "stockId") Long stockId) throws Exception {
        GeneralResponse<WTByStockAndBatch, Object> result;
        try {
            WTByStockAndBatch qty = stockBatchService.getWtByStockAndBatchId(stockId, batchId);

            result = new GeneralResponse<>(qty, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/stockBatch/batch/ByQualityAndParty/{qualityId}/{partyId}")
    public ResponseEntity<GeneralResponse<List<GetAllBatch>, Object>> getBatchById(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId) {
        GeneralResponse<List<GetAllBatch>, Object> result;
        try {
            if (qualityId != null && partyId != null) {
                List<GetAllBatch> batchData = stockBatchService.getBatchByPartyAndQuality(qualityId, partyId);

                result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            } else {
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/stockBatch/all/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List<GetAllStockWithPartyNameResponse>, Object>> getAllStockBatch(@PathVariable(value = "getBy") String getBy, @PathVariable(value = "id") Long id) throws Exception {

        GeneralResponse<List<GetAllStockWithPartyNameResponse>, Object> result;
        try {
            List<GetAllStockWithPartyNameResponse> stockMast = null;
            switch (getBy) {
                case "own":
                    stockMast = stockBatchService.getAllStockBatch(getBy, id);
                    if (stockMast == null) {
                        result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    } else {
                        result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    }
                    break;

                case "group":
                    stockMast = stockBatchService.getAllStockBatch(getBy, id);
                    if (stockMast == null) {
                        result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    } else {
                        result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    }
                    break;

                case "all":
                    stockMast = stockBatchService.getAllStockBatch(null, null);
                    if (stockMast == null) {
                        result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    } else {
                        result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    }
                    break;
                default:
                    result = new GeneralResponse<>(null, ConstantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());


            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        logService.saveLog(result, request, debugAll);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping(value="/stockBatch/allpaginated")
    public ResponseEntity<GeneralResponse<FilterResponse<GetAllStockWithPartyNameResponse>, Object>> getAllStockBatchPaginatedAndFiltered(@RequestBody GetBYPaginatedAndFiltered requestParam,@RequestHeader Map<String,String> header) throws Exception {
        System.out.println("stockBatch/all entered");

        GeneralResponse<FilterResponse<GetAllStockWithPartyNameResponse>, Object> result;
        try {
            FilterResponse<GetAllStockWithPartyNameResponse> stockMast = null;
            String id=header.get("id");
            if(id=="")id=null;
            
             
            switch (requestParam.getGetBy()) {
                case "own":
                    stockMast = stockBatchService.getAllStockBatchPaginatedAndFiltered(requestParam, id);
                    if (stockMast == null) {
                        result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    } else {
                        result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    }
                    break;

                case "group":
                    stockMast = stockBatchService.getAllStockBatchPaginatedAndFiltered(requestParam,id);
                    if (stockMast == null) {
                        result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    } else {
                        result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    }
                    break;

                case "all":
                    stockMast = stockBatchService.getAllStockBatchPaginatedAndFiltered(requestParam, null);
                    if (stockMast == null) {
                        result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    } else {
                        result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    }
                    break;
                default:
                    result = new GeneralResponse<>(null, ConstantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());


            }
        } catch (Exception e) {
            e.printStackTrace();

            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        logService.saveLog(result, request, debugAll);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }




    @GetMapping("/stockBatch/{id}")
    public ResponseEntity<GeneralResponse<StockMast, Object>> getStockMastById(@PathVariable(value = "id") Long id) {
        GeneralResponse<StockMast, Object> result;
        try {
            if (id != null) {
                StockMast stockMast = stockBatchService.getStockBatchById(id);
                if (stockMast != null) {
                    result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                } else {
                    result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                }
            } else {
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/stockBatch/getAllBatchWithoutFilter")
    public ResponseEntity<GeneralResponse<List<GetAllBatch>, Object>> getAllBatchWithoutFilter() {
        GeneralResponse<List<GetAllBatch>, Object> result;
        try {

            List<GetAllBatch> stockMast = stockBatchService.getAllBatchWithoutFilter();
            if (!stockMast.isEmpty()) {
                result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            } else {
                result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/stockBatch/batch/{controlId}/{batchId}")
    public ResponseEntity<GeneralResponse<List<BatchData>, Object>> getBatchById(@PathVariable(value = "batchId") String batchId, @PathVariable(value = "controlId") Long controlId) {
        GeneralResponse<List<BatchData>, Object> result;
        try {
            if (batchId != null) {
                List<BatchData> batchData = stockBatchService.getBatchById(batchId, controlId);

                result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            } else {
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/stockBatch/batch/ByQualityAndPartyWithoutProductionPlan/{qualityId}/{partyId}")
    public ResponseEntity<GeneralResponse<List<GetAllBatch>, Object>> ByQualityAndPartyWithoutProductionPlan(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId, @RequestHeader Map<String, String> headers) {

        GeneralResponse<List<GetAllBatch>, Object> result;
        try {
            if (qualityId != null && partyId != null) {
                List<GetAllBatch> batchData = stockBatchService.byQualityAndPartyWithoutProductionPlan(qualityId, partyId, headers.get("id"));

                if (batchData.isEmpty())
                    result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                else
                    result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            } else {
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/stockBatch/batch/ByQualityAndPartyWithProductionPlan/{qualityId}/{partyId}")
    public ResponseEntity<GeneralResponse<List<GetAllBatch>, Object>> ByQualityAndPartyWithProducctionPlan(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId) {
        GeneralResponse<List<GetAllBatch>, Object> result;
        try {
            if (qualityId != null && partyId != null) {
                List<GetAllBatch> batchData = stockBatchService.byQualityAndPartyWithProductionPlan(qualityId, partyId);

                result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            } else {
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    //get complete stock and batch based on party id and qualityid
    @GetMapping("/stockBatch/stockBatchDataList/ByQualityAndParty/{qualityId}/{partyId}")
    public ResponseEntity<GeneralResponse<List<StockMast>, Object>> getStockBatchListById(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId) {

        GeneralResponse<List<StockMast>, Object> result;
        try {
            if (qualityId != null && partyId != null) {
                List<StockMast> batchData = stockBatchService.getStockBatchListById(qualityId, partyId);

                result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            } else {
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/stockBatch/batch/all")
    public ResponseEntity<GeneralResponse<List<BatchToPartyAndQuality>, Object>> getAllBatch(@RequestHeader Map<String, String> headers) {
        GeneralResponse<List<BatchToPartyAndQuality>, Object> result;
        try {

            List<BatchToPartyAndQuality> batchData = stockBatchService.getAllBatchDetail(headers.get("id"));

            if (batchData.isEmpty())
                result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            else
                result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(result, request, debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    //batch by quality
    @GetMapping("/stockBatch/batch/byQualityId/{id}")
    public ResponseEntity<GeneralResponse<List<BatchData>, Object>> getAllBatchByQualityId(@PathVariable(name = "id") Long qualityId) {
        GeneralResponse<List<BatchData>, Object> result;
        try {

            List<BatchData> batchData = stockBatchService.getAllBatchByQualityId(qualityId);

            result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(result, request, debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/stockBatch/BatchToPartyAndQuality/{controlId}/{batchId}")
    public ResponseEntity<GeneralResponse<BatchToPartyAndQuality, Object>> getPartyAndQualityByBatch(@PathVariable(name = "controlId") Long controlId, @PathVariable(name = "batchId") String batchId) {
        GeneralResponse<BatchToPartyAndQuality, Object> result;
        try {

            BatchToPartyAndQuality batchData = stockBatchService.getPartyAndQualityByBatch(controlId, batchId);

            if (batchData != null)
                result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            else
                result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(result, request, debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/stockBatch/IsBatchAvailableWithId/{controlId}/{batchId}")
    public ResponseEntity<GeneralResponse<Boolean, Object>> IsBatchAvailable(@PathVariable(name = "controlId") Long controlId, @PathVariable(name = "batchId") String batchId) {
        GeneralResponse<Boolean, Object> result;
        try {

            Boolean batchDataFlag = stockBatchService.IsBatchAvailable(controlId, batchId);

            if (batchDataFlag == true) {
                result = new GeneralResponse<>(true, ConstantFile.Batch_Id_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            } else {
                result = new GeneralResponse<>(false, ConstantFile.Batch_Id_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            }
            logService.saveLog(result, request, debugAll);


        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }


    @GetMapping("/stockBatch/batchWithoutExtra/ByQualityAndParty/{qualityId}/{partyId}")
    public ResponseEntity<GeneralResponse<List<GetAllBatch>, Object>> getBatchWithoutProductionPlanById(@PathVariable(value = "qualityId") Long qualityId, @PathVariable(value = "partyId") Long partyId) {
        GeneralResponse<List<GetAllBatch>, Object> result;
        try {
            if (qualityId != null && partyId != null) {
                List<GetAllBatch> batchData = stockBatchService.getBatchWithoutProductionPlanByPartyAndQuality(qualityId, partyId);

                result = new GeneralResponse<>(batchData, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            } else {
                throw new Exception(ConstantFile.Null_Record_Passed);
                //result =new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }


    @PutMapping("/stockBatch")
    public ResponseEntity<GeneralResponse<Long, Object>> updateStockBatch(@RequestBody StockMast stockMast, @RequestHeader Map<String, String> headers) {
        GeneralResponse<Long, Object> result;
        try {
            stockBatchService.updateBatch(stockMast, headers.get("id"));
            result = new GeneralResponse<>(stockMast.getId(), ConstantFile.StockBatch_Updated, true, System.currentTimeMillis(), HttpStatus.OK, stockMast);
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, stockMast);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/stockBatch/MergeBatch")
    public ResponseEntity<GeneralResponse<Boolean, Object>> updateBatchMerge(@RequestBody List<MergeSplitBatch> batchData1) {
        GeneralResponse<Boolean, Object> result;
        try {
            stockBatchService.updateBatchForMerge(batchData1);
            result = new GeneralResponse<>(true, ConstantFile.StockBatch_Updated, true, System.currentTimeMillis(), HttpStatus.OK, batchData1);
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, batchData1);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/stockBatch/SplitBatch")
    public ResponseEntity<GeneralResponse<Boolean, Object>> updateBatchSplit(@RequestBody List<MergeSplitBatch> batchData1) {
        GeneralResponse<Boolean, Object> result;
        try {
            stockBatchService.updateBatchSplit(batchData1);
            result = new GeneralResponse<>(true, ConstantFile.StockBatch_Updated, true, System.currentTimeMillis(), HttpStatus.OK, batchData1);
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, batchData1);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @DeleteMapping("/stockBatch/{id}")
    public ResponseEntity<GeneralResponse<Boolean, Object>> deleteStockBatch(@PathVariable(value = "id") Long id) {
        GeneralResponse<Boolean, Object> result;
        try {
            stockBatchService.deleteStockBatch(id);
            result = new GeneralResponse<>(true, ConstantFile.StockBatch_Deleted, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("/batchGr/delete/{id}")
    public ResponseEntity<GeneralResponse<Boolean, Object>> deleteBatchGr(@PathVariable(value = "id") Long id) {
        GeneralResponse<Boolean, Object> result;
        try {
            stockBatchService.deleteBatchGr(id);
            result = new GeneralResponse<>(true, ConstantFile.Batch_Data_Deleted, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("/stockBatch/delete/{controlId}/{batchId}")
    public ResponseEntity<GeneralResponse<Boolean, Object>> deleteBatchByControlIdAndBatchID(@PathVariable(name = "controlId") Long controlId, @PathVariable(name = "batchId") String batchId) {
        GeneralResponse<Boolean, Object> result;
        try {
            Boolean flag = stockBatchService.deleteStockBatchWithControlAndBatchID(controlId, batchId);

            if (flag == true)
                result = new GeneralResponse<>(true, ConstantFile.Batch_Data_Deleted, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            else
                result = new GeneralResponse<>(false, ConstantFile.Batch_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/stockBatch/getAllBatchForFinishMtr")
    public ResponseEntity<GeneralResponse<List<GetAllBatchWithProduction>, Object>> getAllBatchWithoutBillGenerated(@RequestHeader Map<String, String> headers) {
        GeneralResponse<List<GetAllBatchWithProduction>, Object> response;

        try {
            List<GetAllBatchWithProduction> flag = stockBatchService.getAllBatchWithoutBillGenerated(headers.get("id"));

            if (!flag.isEmpty())
                response = new GeneralResponse<>(flag, ConstantFile.Batch_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            else
                response = new GeneralResponse<>(flag, ConstantFile.Batch_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(response, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            response = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(response, request, true);
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

    }


    //gt the job card detail by batch and stock Id
    @GetMapping("/stockBatch/get/getJobCardBy")
    public ResponseEntity<GeneralResponse<JobCard, Object>> getJobCardByStockIdAndBatchId(@RequestParam(name = "batchId") String batchId) throws Exception {
        GeneralResponse<JobCard, Object> result;
        try {
            if (batchId.isEmpty())
                throw new Exception(ConstantFile.Null_Record_Passed);

            JobCard qty = stockBatchService.getJobCardByStockIdAndBatchId(batchId);

            result = new GeneralResponse<>(qty, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    //get all batch for additional slip
    @GetMapping("/stockBatch/batch/forAdditionalSlip")
    public ResponseEntity<GeneralResponse<List<GetAllBatch>, Object>> getBatchForAdditionalSlip(@RequestHeader Map<String, String> headers) {
        GeneralResponse<List<GetAllBatch>, Object> result;
        try {

            List<GetAllBatch> batchData = stockBatchService.getAllBatchForAdditionalSlip(headers.get("id"));

            if (batchData.isEmpty())
                result = new GeneralResponse<>(batchData, ConstantFile.Batch_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            else
                result = new GeneralResponse<>(batchData, ConstantFile.Batch_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, debugAll);


        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }


    //get all batch for additional slip
    @GetMapping("/stockBatch/batch/forRedyeingSlip")
    public ResponseEntity<GeneralResponse<List<GetAllBatch>, Object>> getBatchForReDyeingByPartyAndQualityId(@RequestHeader Map<String, String> headers) {
        GeneralResponse<List<GetAllBatch>, Object> result;
        try {

            List<GetAllBatch> batchData = stockBatchService.getAllBatchForRedyeingSlip(headers.get("id"));

            result = new GeneralResponse<>(batchData, ConstantFile.Batch_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(result, request, debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @PostMapping("/stockBatch/add/returnBatch")
    public ResponseEntity<GeneralResponse<Long, Object>> createReturnBatch(@RequestBody BatchReturnBody record) throws Exception {
        GeneralResponse<Long, Object> result = null;
        try {
            Long id = stockBatchService.saveReturnStockBatch(record);
            result = new GeneralResponse<>(id, ConstantFile.ReturnStockBatch_Added, true, System.currentTimeMillis(), HttpStatus.OK, record);

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, record);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/stockBatch/all/returnBatch")
    public ResponseEntity<GeneralResponse<List<BatchReturnResponse>, Object>> getReturnBatch() throws Exception {
        GeneralResponse<List<BatchReturnResponse>, Object> result = null;
        try {
            List<BatchReturnResponse> list = stockBatchService.getAllReturnBatch();

            if (!list.isEmpty()) {
                result = new GeneralResponse<>(list, ConstantFile.ReturnStockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            } else
                result = new GeneralResponse<>(list, ConstantFile.ReturnStockBatch_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/stockBatch/all/returnBatch/allPaginated")
    public ResponseEntity<GeneralResponse<FilterResponse<BatchReturnResponse>, Object>> getReturnBatchAllPaginated(@RequestBody GetBYPaginatedAndFiltered requestParam) throws Exception {
        GeneralResponse<FilterResponse<BatchReturnResponse>, Object> result = null;
        try {
            FilterResponse<BatchReturnResponse> list = stockBatchService.getAllReturnBatchAllPaginated(requestParam);

            if (!list.getData().isEmpty()) {
                result = new GeneralResponse<>(list, ConstantFile.ReturnStockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            } else
                result = new GeneralResponse<>(list, ConstantFile.ReturnStockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }



    @GetMapping("/stockBatch/get/returnBatch")
    public ResponseEntity<GeneralResponse<BatchReturnResponse, Object>> getReturnBatchByChalNo(@RequestParam(name = "chlNo") Long chlNo) throws Exception {
        GeneralResponse<BatchReturnResponse, Object> result = null;
        try {
            BatchReturnResponse list = stockBatchService.getReturnBatchByChalNo(chlNo);

            if (list != null) {
                result = new GeneralResponse<>(list, ConstantFile.ReturnStockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            } else
                result = new GeneralResponse<>(list, ConstantFile.ReturnStockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    //available stock batch
    @GetMapping("/stockBatch/pending/all/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List<GetAllStockWithPartyNameResponse>, Object>> getAllAvailableStockBatch(@PathVariable(value = "getBy") String getBy, @PathVariable(value = "id") Long id) throws Exception {

        GeneralResponse<List<GetAllStockWithPartyNameResponse>, Object> result;

        try {
            List<GetAllStockWithPartyNameResponse> stockMast = null;
            switch (getBy) {
                case "own":
                    stockMast = stockBatchService.getAllAvailableStockBatch(getBy, id);
                    if (stockMast == null) {
                        result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    } else {
                        result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    }
                    break;

                case "group":
                    stockMast = stockBatchService.getAllAvailableStockBatch(getBy, id);
                    if (stockMast == null) {
                        result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    } else {
                        result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    }
                    break;

                case "all":
                    stockMast = stockBatchService.getAllAvailableStockBatch(null, null);
                    if (stockMast == null) {
                        result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    } else {
                        result = new GeneralResponse<>(stockMast, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                    }
                    break;
                default:
                    result = new GeneralResponse<>(null, ConstantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());


            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        logService.saveLog(result, request, debugAll);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    //pending batch
    @GetMapping("/stockBatch/pending/report")
    public ResponseEntity<GeneralResponse<List<PendingBatchMast>, Object>> getAllPendingBatchReport() throws Exception {

        GeneralResponse<List<PendingBatchMast>, Object> result;

        try {
            List<PendingBatchMast> list = stockBatchService.getAllPendingBatchReport();
            if (list == null) {
                result = new GeneralResponse<>(null, ConstantFile.StockBatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            } else {
                result = new GeneralResponse<>(list, ConstantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }


        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        logService.saveLog(result, request, debugAll);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    //pchallan api's
    @PostMapping("/stockBatch/add/pchallan")
    public ResponseEntity<GeneralResponse<Long, Object>> addPchallanStockMast(@RequestBody AddStockBatch addStockBatch) throws Exception {
        GeneralResponse<Long, Object> result = null;
        try {
            Long id = stockBatchService.addPChallanRef(addStockBatch);
            result = new GeneralResponse<>(id, ConstantFile.StockBatch_PChallanRef_Added, true, System.currentTimeMillis(), HttpStatus.OK, addStockBatch);

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, addStockBatch);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @PutMapping("/stockBatch/update/pchallan")
    public ResponseEntity<GeneralResponse<Long, Object>> updatePchallanStockMast(@RequestBody AddStockBatch addStockBatch) throws Exception {
        GeneralResponse<Long, Object> result = null;
        try {
            Long id = stockBatchService.updatePChallanRef( addStockBatch, request.getHeader("id"));
            result = new GeneralResponse<>(id, ConstantFile.StockBatch_PChallanRef_Added, true, System.currentTimeMillis(), HttpStatus.OK, addStockBatch);


            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, addStockBatch);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/stockBatch/exist/pchallan")
    public ResponseEntity<GeneralResponse<Boolean, Object>> existPchallanStockMast(@RequestParam(name = "partyId") Long partyId, @RequestParam(name = "pchallanRef") String pchallanRef) throws Exception {
        GeneralResponse<Boolean, Object> result = null;
        try {
            List<BatchData> batchDataList = stockBatchService.getPChallanExistWithParyId(partyId, pchallanRef);
            if (batchDataList.size() > 0) {
                result = new GeneralResponse<>(true, ConstantFile.StockBatch_PChallanRef_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            } else {
                result = new GeneralResponse<>(false, ConstantFile.StockBatch_PChallanRef_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/stockBatch/getmtrsum/filterbydate")
    public ResponseEntity<GeneralResponse<List<AllStockDateWiseDataUnderParty>, Object>> getMtrSumFilterByDate(String from,String to) throws Exception {
        GeneralResponse<List<AllStockDateWiseDataUnderParty>, Object> response;
        try {
            List<AllStockDateWiseDataUnderParty> flag = allStockDateWiseData.getAllStockDateWiseData(from,to);

            if (!flag.isEmpty())
                response = new GeneralResponse<>(flag, ConstantFile.Batch_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            else
                response = new GeneralResponse<>(flag, ConstantFile.Batch_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(response, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            response = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(response, request, true);
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("/stockMast/filter")
    public ResponseEntity<GeneralResponse<List<StockMast>, Object>> getFilteredStockMast(@RequestParam("pageSize") int pageSize,@RequestParam("pageNumber") int pagePumber ,@RequestBody StockMastFilter filter) throws Exception {
        GeneralResponse<List<StockMast>, Object> response;
        try {
            List<StockMast> flag = dataFilterService.getFilteredStockMast(filter,pageSize,pagePumber);

            if (!flag.isEmpty())
                response = new GeneralResponse<>(flag, ConstantFile.Batch_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            else
                response = new GeneralResponse<>(flag, ConstantFile.Batch_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(response, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            response = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(response, request, true);
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }


}

