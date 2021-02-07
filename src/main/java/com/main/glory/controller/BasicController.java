package com.main.glory.controller;

import com.main.glory.Dao.PartyDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetAllBatch;
import com.main.glory.model.basic.PartyQuality;
import com.main.glory.model.basic.QualityParty;
import com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality;
import com.main.glory.servicesImpl.BasicServiceImpl;
import com.main.glory.servicesImpl.QualityServiceImp;
import com.main.glory.servicesImpl.ShadeServiceImpl;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BasicController extends ControllerConfig {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    private QualityServiceImp qualityServiceImp;

    @Autowired
    private PartyDao partyDao;

    @Autowired
    private BasicServiceImpl basicService;

    @Autowired
    ShadeServiceImpl shadeService;

    @GetMapping("/party/ByQuality/{id}")
    public GeneralResponse<QualityParty> ByQuality(@PathVariable(value = "id") Long id) {
        GeneralResponse<QualityParty> result;
        try {

            // String quality="sndkjabn";
            QualityParty qualityParties = qualityServiceImp.getAllQualityWithParty(id);
            if (qualityParties != null) {
                result = new GeneralResponse<>(qualityParties, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            } else {
                result = new GeneralResponse<>(null, "No data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;

    }

    @GetMapping("/Quality/ByParty/{id}")
    public GeneralResponse<PartyQuality> ByParty(@PathVariable(value = "id") Long partyId) {
        GeneralResponse<PartyQuality> result;
        try {

            // String quality="sndkjabn";
            PartyQuality partyQualities = qualityServiceImp.getAllPartyWithQuality(partyId);
            if (partyQualities != null) {
                result= new GeneralResponse<>(partyQualities, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            } else {
                result= new GeneralResponse<>(null, "No data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @GetMapping("/QualityAndParty/ByMaster/{userHeadId}")
    public GeneralResponse<List<PartyQuality>> ByMaster(@PathVariable(value = "userHeadId") Long userHeadId) {
        try {


            List<PartyQuality> partyQualities = qualityServiceImp.getAllPartyWithQualityByMaster(userHeadId);
            if (partyQualities != null) {
                return new GeneralResponse<>(partyQualities, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            } else {
                return new GeneralResponse<>(null, "No shade data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/batch/ByMaster/{userHeadId}")
    public GeneralResponse<List<GetAllBatch>> GetBatchByMaster(@PathVariable(value = "userHeadId") Long userHeadId) {
        try {
            List<GetAllBatch> batchDataList = stockBatchService.getAllBatchByMaster(userHeadId);
            if (batchDataList != null) {
                return new GeneralResponse<>(batchDataList, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            } else {
                return new GeneralResponse<>(null, "No shade data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/shade/ByPartyIdAndQualityId/{partyId}/{qualityId}")
    public GeneralResponse<List<GetShadeByPartyAndQuality>> getShadeByPartyAndQuality(@PathVariable(value = "partyId") Long partyId, @PathVariable(value = "qualityId") Long qualityId) {
        try {
            List<GetShadeByPartyAndQuality> shadeListByPartyAndQualities = shadeService.getAllShadeByPartyAndQuality(partyId, qualityId);
            if (shadeListByPartyAndQualities != null) {
                return new GeneralResponse<>(shadeListByPartyAndQualities, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            } else {
                return new GeneralResponse<>(null, "No shade data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    //batch by party id
    @GetMapping("/stockBatch/batchListByPartyWithoutProductionPlan/{partyId}")
    public GeneralResponse<List<GetAllBatch>> getBatchListByPartyWithoutProductionPlan(@PathVariable(value = "partyId") Long partyId) {
        try {
            if (partyId != null) {
                List<GetAllBatch> stockMast = stockBatchService.getBatchListByPartyWithoutProductionPlan(partyId);
                if (stockMast != null) {
                    return new GeneralResponse<>(stockMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                } else {
                    return new GeneralResponse<>(null, "no data found for party: " + partyId, false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                }
            } else {
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    //batch by qualityId id
    @GetMapping("/stockBatch/batchListByQualityWithoutProductionPlan/{qualityId}")
    public GeneralResponse<List<GetAllBatch>> getBatchListByQualityWithoutProductionPlan(@PathVariable(value = "qualityId") Long qualityId) {
        try {
            if (qualityId != null) {
                List<GetAllBatch> stockMast = stockBatchService.getBatchListByQualityWithoutProductionPlan(qualityId);
                if (stockMast != null) {
                    return new GeneralResponse<>(stockMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                } else {
                    return new GeneralResponse<>(null, "no data found for quality id: " + qualityId, false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                }
            } else {
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    //get the list of stock who's all bathc are not plannned
    @GetMapping("/stockBatch/getAllStockWithoutPlan")
    public GeneralResponse<List<StockMast>> getAllStockWithoutPlan() {
        try {

            List<StockMast> stockMast = stockBatchService.getAllStockWithoutPlan();
            if (stockMast != null) {
                return new GeneralResponse<>(stockMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<>(null, "no data found  "  , false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }


}


