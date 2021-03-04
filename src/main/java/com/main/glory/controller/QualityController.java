package com.main.glory.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.model.quality.request.UpdateQualityRequest;
import com.main.glory.model.quality.response.GetAllQualtiy;
import com.main.glory.model.quality.response.GetQualityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.main.glory.servicesImpl.QualityServiceImp;

@RestController
@RequestMapping("/api")
public class QualityController extends ControllerConfig {

    @Autowired
    private QualityServiceImp qualityServiceImp;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private PartyDao partyDao;

    @PostMapping(value = "/quality")
    public ResponseEntity<GeneralResponse<Boolean>> saveQuality(@RequestBody AddQualityRequest quality) {
        GeneralResponse<Boolean> result;
        try{

            Optional<Party> party = partyDao.findById(quality.getPartyId());
            if(party.isEmpty()){
                throw new Exception("No party present with id:"+quality.getPartyId());
            }

            int flag = qualityServiceImp.saveQuality(quality);
            if (flag == 1)
                result= new GeneralResponse<Boolean>(null, "Quality Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            else
                result= new GeneralResponse<Boolean>(null, "Please Enter Valid Data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/quality/all/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List<GetQualityResponse>>> getQualityList(@PathVariable(value = "getBy") String getBy, @PathVariable(value = "id") Long id) {
        GeneralResponse<List<GetQualityResponse>> result;
        try {
            List<GetQualityResponse> x;
            switch (getBy){
                case "own":
                    x = qualityServiceImp.getAllQuality(id, getBy);
                    if(!x.isEmpty())
                        result =  new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    else
                        result = new GeneralResponse<>(x, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);
                    break;
                case "group":
                    x = qualityServiceImp.getAllQuality(id, getBy);
                    if(!x.isEmpty())
                        result= new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    else
                        result= new GeneralResponse<>(x, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);
                    break;
                case "all":
                    x = qualityServiceImp.getAllQuality(null, null);
                    if(!x.isEmpty())
                        result= new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    else
                        result= new GeneralResponse<>(x, "No quality added yet", false, System.currentTimeMillis(), HttpStatus.OK);

                    break;
                default:
                    result= new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result= new GeneralResponse<>(null, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping(value = "/quality")
    public ResponseEntity<GeneralResponse<Boolean>> updateQualityById(@RequestBody UpdateQualityRequest quality) throws Exception {
        GeneralResponse<Boolean> result;
        try {
            Optional<Party> party = partyDao.findById(quality.getPartyId());
            if(party.isEmpty()){
                throw new Exception("No party present with id:"+quality.getPartyId());
            }
            if (quality.getId() != null) {
                boolean flag = qualityServiceImp.updateQuality(quality);
                if (flag) {
                    result= new GeneralResponse<Boolean>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                } else {
                    result = new GeneralResponse<Boolean>(false, "No such id found", false, System.currentTimeMillis(), HttpStatus.OK);
                }
            }
            else {
                result = new GeneralResponse<Boolean>(false, "Null quality Object", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            result =new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value = "/quality/{id}")
    public ResponseEntity<GeneralResponse<GetQualityResponse>> getQualityDataById(@PathVariable(value = "id") Long id) {
        GeneralResponse<GetQualityResponse> result;
        if (id != null) {
            var qualityData = qualityServiceImp.getQualityByID(id);
            if (qualityData == null) {
                result =  new GeneralResponse<>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.OK);
            } else
                result =  new GeneralResponse<>(qualityData, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.OK);
        } else
            result= new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping(value = "/quality/allQuality")
    public ResponseEntity<GeneralResponse<List<GetAllQualtiy>>> getAllQualityData(@RequestHeader Map<String, String> headers) throws Exception{

        GeneralResponse<List<GetAllQualtiy>> result;
        try {

            List<GetAllQualtiy> qualityData = qualityServiceImp.getAllQualityDataWithHeaderId(headers.get("id"));
            if (qualityData == null) {
                result =  new GeneralResponse<>(null, "No quality found", false, System.currentTimeMillis(), HttpStatus.OK);
            }
            else {

                result = new GeneralResponse<>(qualityData, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch (Exception e)
        {
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
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
//            return new GeneralResponse<Boolean>(false, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return new GeneralResponse<Boolean>(false, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
//    }

    @GetMapping(value = "/is_quality_exist/{quality_id}/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> IsQualityAlreadyExist(@PathVariable("quality_id") String quality_id,@PathVariable(name="id")Long id) {

        GeneralResponse<Boolean> result;
        if (quality_id != null) {
            Boolean flag = qualityServiceImp.getQualityIsExist(quality_id,id);
            if (flag) {
                result = new GeneralResponse<Boolean>(true, "found successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            result = new GeneralResponse<Boolean>(false, "quality id not found", false, System.currentTimeMillis(), HttpStatus.OK);
        }
        else
        result = new GeneralResponse<Boolean>(false, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @DeleteMapping(value="/quality/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteQualityDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
        GeneralResponse<Boolean> result;
        try {
            if (id != null) {
                boolean flag = qualityServiceImp.deleteQualityById(id);
                if (flag) {
                    result =  new GeneralResponse<Boolean>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                } else {
                    result= new GeneralResponse<Boolean>(false, "no such id found", false, System.currentTimeMillis(), HttpStatus.OK);
                }
            }
            else {
                result = new GeneralResponse<Boolean>(false, "Null party object", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e )
        {
            result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

}
