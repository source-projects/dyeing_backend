package com.main.glory.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.model.quality.request.UpdateQualityRequest;
import com.main.glory.model.quality.response.GetAllQualtiy;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.QualityProcessImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    //@Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    @PostMapping(value = "/quality")
    public ResponseEntity<GeneralResponse<Boolean,Object>> saveQuality(@RequestBody AddQualityRequest quality,@RequestHeader Map<String, String> headers) {
        GeneralResponse<Boolean,Object> result;
        try{

            Optional<Party> party = partyDao.findById(quality.getPartyId());
            if(party.isEmpty()){
                throw new Exception("No party present with id:"+quality.getPartyId());
            }

            int flag = qualityServiceImp.saveQuality(quality,headers.get("id"));
            if (flag == 1)
                result= new GeneralResponse<>(null, "Quality Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED,quality);
            else
                result= new GeneralResponse<>(null, "Please Enter Valid Data", false, System.currentTimeMillis(), HttpStatus.OK,quality);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,quality);
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
                        result =  new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                    else
                        result = new GeneralResponse<>(x, "No data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                    break;
                case "group":
                    x = qualityServiceImp.getAllQuality(id, getBy);
                    if(!x.isEmpty())
                        result= new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                    else
                        result= new GeneralResponse<>(x, "No data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                    break;
                case "all":
                    x = qualityServiceImp.getAllQuality(null, null);
                    if(!x.isEmpty())
                        result= new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                    else
                        result= new GeneralResponse<>(x, "No quality added yet", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

                    break;
                default:
                    result= new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

                    logService.saveLog(result,request,debugAll);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            result= new GeneralResponse<>(null, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value = "/quality")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateQualityById(@RequestBody UpdateQualityRequest quality) throws Exception {
        GeneralResponse<Boolean,Object> result;
        try {
            Optional<Party> party = partyDao.findById(quality.getPartyId());
            if(party.isEmpty()){
                throw new Exception("No party present with id:"+quality.getPartyId());
            }
            if (quality.getId() != null) {
                boolean flag = qualityServiceImp.updateQuality(quality);
                if (flag) {
                    result= new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                } else {
                    result = new GeneralResponse<>(false, "No such id found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                }
            }
            else {
                result = new GeneralResponse<>(false, "Null quality Object", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);
        }catch(Exception e){
            e.printStackTrace();
            result =new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
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
                    result = new GeneralResponse<>(null, "No quality data found ", false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
                } else
                    result = new GeneralResponse<>(qualityData, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            } else
                result = new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            logService.saveLog(result,request,true);

        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/quality/qualityName/get/all")
    public ResponseEntity<GeneralResponse<List<QualityName>,Object>> getAllQualityNameData() {
        GeneralResponse<List<QualityName>,Object> result;

        try {
            var qualityData = qualityServiceImp.getAllQualityNameData();
            if (qualityData.isEmpty()) {
                result = new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            } else
                result = new GeneralResponse<>(qualityData.get(), "Fetch Success", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/quality/qualityName/get/{id}")
    public ResponseEntity<GeneralResponse<QualityName,Object>> getAllQualityNameByQualityNameId(@PathVariable(name = "id")Long id) throws Exception {
        GeneralResponse<QualityName,Object> result;

        try {
            if (id == null)
                throw new Exception("null id passed");

            var qualityData = qualityServiceImp.getQualityNameDataById(id);
            if (qualityData.isEmpty()) {
                result = new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            } else
                result = new GeneralResponse<>(qualityData.get(), "Fetch Success", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
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
                result =  new GeneralResponse<>(null, "No quality found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else {

                result = new GeneralResponse<>(qualityData, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);
        }catch (Exception e)
        {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
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
                    result = new GeneralResponse<>(true, "found successfully", true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
                } else
                    result = new GeneralResponse<>(false, "quality id not found", false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
                logService.saveLog(result, request, debugAll);
            } else
                result = new GeneralResponse<>(false, "Null id passed", false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
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
                    result =  new GeneralResponse<>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                } else {
                    result= new GeneralResponse<>(false, "no such id found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                }
            }
            else {
                result = new GeneralResponse<>(false, "Null party object", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }

            logService.saveLog(result, request, debugAll);

        }catch (Exception e )
        {
            e.printStackTrace();
            result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

}
