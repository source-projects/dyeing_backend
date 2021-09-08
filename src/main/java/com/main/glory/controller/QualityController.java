package com.main.glory.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.filters.FilterResponse;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.request.AddQualityName;
import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.model.quality.request.GetQualityReport;
import com.main.glory.model.quality.request.UpdateQualityRequest;
import com.main.glory.model.quality.response.GetAllQualtiy;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.quality.response.QualityReport;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.main.glory.servicesImpl.QualityServiceImp;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class QualityController extends ControllerConfig {

    @Autowired
    private QualityServiceImp qualityServiceImp;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private PartyDao partyDao;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll;

    @PostMapping(value = "/quality")
    public ResponseEntity<GeneralResponse<Boolean,Object>> saveQuality(@RequestBody Quality quality,@RequestHeader Map<String, String> headers) {
        GeneralResponse<Boolean,Object> result;
        try{

            Optional<Party> party = partyDao.findById(quality.getParty().getId());
            if(party.isEmpty()){
                throw new Exception(ConstantFile.Party_Not_Exist);
            }

            int flag = qualityServiceImp.saveQuality(quality,headers.get("id"));
            if (flag == 1)
                result= new GeneralResponse<>(null, ConstantFile.Quality_Data_Added, true, System.currentTimeMillis(), HttpStatus.CREATED,quality);
            else
                result= new GeneralResponse<>(null, ConstantFile.Quality_Data_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,quality);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,quality);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/quality/all/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List<GetQualityResponse>,Object>> getQualityList(@PathVariable(value = "getBy") String getBy, @PathVariable(value = "id") Long id) {
        GeneralResponse<List<GetQualityResponse>,Object> result;
        try {
            List<GetQualityResponse> x;
            switch (getBy){
                case "own":
                    x = qualityServiceImp.getAllQuality(id, getBy);
                    if(!x.isEmpty())
                        result =  new GeneralResponse<>(x, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    else
                        result = new GeneralResponse<>(x, ConstantFile.Quality_Data_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    break;
                case "group":
                    x = qualityServiceImp.getAllQuality(id, getBy);
                    if(!x.isEmpty())
                        result= new GeneralResponse<>(x, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    else
                        result= new GeneralResponse<>(x, ConstantFile.Quality_Data_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    break;
                case "all":
                    x = qualityServiceImp.getAllQuality(null, null);
                    if(!x.isEmpty())
                        result= new GeneralResponse<>(x, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    else
                        result= new GeneralResponse<>(x, ConstantFile.Quality_Data_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

                    break;
                default:
                    result= new GeneralResponse<>(null, ConstantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());


            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        logService.saveLog(result,request,debugAll);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping(value = "/quality/allPaginated")
    public ResponseEntity<GeneralResponse<FilterResponse<GetQualityResponse>,Object>> getQualityListPaginated(@RequestBody GetBYPaginatedAndFiltered requestParam,@RequestHeader Map<String,String> header) {
        GeneralResponse<FilterResponse<GetQualityResponse>,Object> result;
        String id=header.get("id");
        if(id=="")id=null;

        try {
            FilterResponse<GetQualityResponse> x;
            switch (requestParam.getGetBy()){
                case "own":
                    x = qualityServiceImp.getAllQualityPaginated(requestParam, id);
                    if(!x.getData().isEmpty())
                        result =  new GeneralResponse<>(x, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    else
                        result = new GeneralResponse<>(x, ConstantFile.Quality_Data_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    break;
                case "group":
                    x = qualityServiceImp.getAllQualityPaginated(requestParam, id);
                    if(!x.getData().isEmpty())
                        result= new GeneralResponse<>(x, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    else
                        result= new GeneralResponse<>(x, ConstantFile.Quality_Data_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    break;
                case "all":
                    x = qualityServiceImp.getAllQualityPaginated(null,null);
                    if(!x.getData().isEmpty())
                        result= new GeneralResponse<>(x, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                    else
                        result= new GeneralResponse<>(x, ConstantFile.Quality_Data_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

                    break;
                default:
                    result= new GeneralResponse<>(null, ConstantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());


            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        logService.saveLog(result,request,debugAll);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }





    @PutMapping(value = "/quality")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateQualityById(@RequestBody Quality quality) throws Exception {
        GeneralResponse<Boolean,Object> result;
        try {
            Optional<Party> party = partyDao.findById(quality.getParty().getId());
            if(party.isEmpty()){
                throw new Exception(ConstantFile.Party_Not_Exist+quality.getParty().getId());
            }
            if (quality.getId() != null) {
                boolean flag = qualityServiceImp.updateQuality(quality);
                if (flag) {
                    result= new GeneralResponse<>(true, ConstantFile.Quality_Data_Updated, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                } else {
                    result = new GeneralResponse<>(false, ConstantFile.Quality_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                }
            }
            else {
                result = new GeneralResponse<>(false, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        }catch(Exception e){
            e.printStackTrace();
            result =new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/quality/{id}")
    public ResponseEntity<GeneralResponse<GetQualityResponse,Object>> getQualityDataById(@PathVariable(value = "id") Long id) {
        GeneralResponse<GetQualityResponse,Object> result;
        try {


            if (id != null) {
                var qualityData = qualityServiceImp.getQualityByID(id);
                if (qualityData == null) {
                    result = new GeneralResponse<>(null, ConstantFile.Quality_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
                } else
                    result = new GeneralResponse<>(qualityData, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
            } else
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);

        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/quality/qualityName/get/all")
    public ResponseEntity<GeneralResponse<List<AddQualityName>,Object>> getAllQualityNameData() {
        GeneralResponse<List<AddQualityName>,Object> result;

        try {
            var qualityData = qualityServiceImp.getAllQualityNameData();
            if (qualityData.isEmpty()) {
                result = new GeneralResponse<>(null, ConstantFile.Quality_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else
                result = new GeneralResponse<>(qualityData, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            logService.saveLog(result,request,debugAll);
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/quality/qualityName/get/{id}")
    public ResponseEntity<GeneralResponse<QualityName,Object>> getAllQualityNameByQualityNameId(@PathVariable(name = "id")Long id) throws Exception {
        GeneralResponse<QualityName,Object> result;

        try {
            if (id == null)
                throw new Exception(ConstantFile.Null_Record_Passed);

            var qualityData = qualityServiceImp.getQualityNameDataById(id);
            if (qualityData.isEmpty()) {
                result = new GeneralResponse<>(null, ConstantFile.Quality_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else
                result = new GeneralResponse<>(qualityData.get(), ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/quality/allQuality")
    public ResponseEntity<GeneralResponse<List<GetAllQualtiy>,Object>> getAllQualityData(@RequestHeader Map<String, String> headers) throws Exception{

        GeneralResponse<List<GetAllQualtiy>,Object> result;
        try {

            List<GetAllQualtiy> qualityData = qualityServiceImp.getAllQualityDataWithHeaderId(headers.get("id"));
            if (qualityData == null) {
                result =  new GeneralResponse<>(null, ConstantFile.Quality_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            else {

                result = new GeneralResponse<>(qualityData, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        }catch (Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



//    @DeleteMapping(value = "/delete-quality/{id}")
//    public GeneralResponse<Boolean> deletePartyDetailsByID(@PathVariable(value = "id") Long id) {
//        if (id != null) {
//            boolean flag = qualityServiceImp.deleteQualityById(id);
//            if (flag) {
//                return new GeneralResponse<Boolean>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
//            }
//            return new GeneralResponse<Boolean>(false, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.OK);
//        }
//        return new GeneralResponse<Boolean>(false, "Null id passed", false, System.currentTimeMillis(), HttpStatus.OK);
//    }

    @GetMapping(value = "/is_quality_exist/{quality_id}/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> IsQualityAlreadyExist(@PathVariable("quality_id") String quality_id,@PathVariable(name="id")Long id) {

        GeneralResponse<Boolean, Object> result;
        try {
            if (quality_id != null) {
                Boolean flag = qualityServiceImp.getQualityIsExist(quality_id, id);
                if (flag) {
                    result = new GeneralResponse<>(true, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
                } else
                    result = new GeneralResponse<>(false, ConstantFile.Quality_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
                logService.saveLog(result, request, debugAll);
            } else
                result = new GeneralResponse<>(false, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result, request, true);
        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @DeleteMapping(value="/quality/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteQualityDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
        GeneralResponse<Boolean,Object> result;
        try {
            if (id != null) {
                boolean flag = qualityServiceImp.deleteQualityById(id);
                if (flag) {
                    result =  new GeneralResponse<>(true, ConstantFile.Quality_Data_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                } else {
                    result= new GeneralResponse<>(false, ConstantFile.Quality_Data_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                }
            }
            else {
                result = new GeneralResponse<>(false, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }

            logService.saveLog(result, request, debugAll);

        }catch (Exception e )
        {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //report of quality
    @PostMapping(value="/quality/all/report")
    public ResponseEntity<GeneralResponse<List<QualityReport>,Object>> getPartyReportById(@RequestBody GetQualityReport record) throws Exception {
        GeneralResponse<List<QualityReport>,Object> result;
        try {
            if (record!=null) {
                List<QualityReport> data = qualityServiceImp.getQualityReport(record);
                if (!data.isEmpty()) {
                    result =  new GeneralResponse<>(data, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK,record);
                } else {
                    result= new GeneralResponse<>(null, ConstantFile.Quality_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,record);
                }
            }
            else {
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,record);
            }
            logService.saveLog(result, request, debugAll);
        }catch (Exception e )
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,record);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping(value = "/quality/qualityIdExist")
    public ResponseEntity<GeneralResponse<Boolean,Object>> IsQualityIdAlreadyExist(@RequestParam(name = "qualityId") String qualityId,@RequestParam(name="partyId")Long partyId,@RequestParam(name="id")Long id) {

        GeneralResponse<Boolean, Object> result;
        try {
            if (partyId != null) {
                Quality flag = qualityServiceImp.getQualityIdIsExistExceptId(qualityId, partyId,id);
                if (flag!=null) {
                    result = new GeneralResponse<>(true, ConstantFile.Quality_Data_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
                } else
                    result = new GeneralResponse<>(false, ConstantFile.Quality_Data_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
                logService.saveLog(result, request, debugAll);
            } else
                result = new GeneralResponse<>(false, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result, request, true);
        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

}
