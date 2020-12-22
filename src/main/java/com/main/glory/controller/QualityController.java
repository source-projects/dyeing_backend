package com.main.glory.controller;

import java.util.List;
import java.util.Optional;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.model.quality.request.UpdateQualityRequest;
import com.main.glory.model.quality.response.GetAllQualtiy;
import com.main.glory.model.quality.response.GetQualityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public GeneralResponse<Boolean> saveQuality(@RequestBody AddQualityRequest quality) {
        try{

            Optional<Party> party = partyDao.findById(quality.getPartyId());
            if(party.isEmpty()){
                throw new Exception("No party present with id:"+quality.getPartyId());
            }

            int flag = qualityServiceImp.saveQuality(quality);
            if (flag == 1)
                return new GeneralResponse<Boolean>(null, "Quality Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            else
                return new GeneralResponse<Boolean>(null, "Please Enter Valid Data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/quality/all/{getBy}/{id}")
    public GeneralResponse<List<GetQualityResponse>> getQualityList(@PathVariable(value = "getBy") String getBy, @PathVariable(value = "id") Long id) {
        try {
            List<GetQualityResponse> x;
            switch (getBy){
                case "own":
                    x = qualityServiceImp.getAllQuality(id, getBy);
                    if(!x.isEmpty())
                        return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    else
                        return new GeneralResponse<>(x, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

                case "group":
                    x = qualityServiceImp.getAllQuality(id, getBy);
                    if(!x.isEmpty())
                        return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    else
                        return new GeneralResponse<>(x, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

                case "all":
                    x = qualityServiceImp.getAllQuality(null, null);
                    if(!x.isEmpty())
                        return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
                    else
                        return new GeneralResponse<>(x, "No quality added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

                default:
                    return new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new GeneralResponse<>(null, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/quality")
    public GeneralResponse<Boolean> updateQualityById(@RequestBody UpdateQualityRequest quality) throws Exception {
        try {
            Optional<Party> party = partyDao.findById(quality.getPartyId());
            if(party.isEmpty()){
                throw new Exception("No party present with id:"+quality.getPartyId());
            }
            if (quality.getId() != null) {
                boolean flag = qualityServiceImp.updateQuality(quality);
                if (flag) {
                    return new GeneralResponse<Boolean>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                } else {
                    return new GeneralResponse<Boolean>(false, "No such id found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                }
            }
            return new GeneralResponse<Boolean>(false, "Null quality Object", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/quality/{id}")
    public GeneralResponse<GetQualityResponse> getQualityDataById(@PathVariable(value = "id") Long id) {
        if (id != null) {
            var qualityData = qualityServiceImp.getQualityByID(id);
            if (qualityData == null) {
                return new GeneralResponse<>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            } else
                return new GeneralResponse<>(qualityData, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
        } else
            return new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }
    @GetMapping(value = "/quality/allQuality")
    public GeneralResponse<List<GetAllQualtiy>> getAllQualityData() throws Exception{

        try {


            List<GetAllQualtiy> qualityData = qualityServiceImp.getAllQualityData();
            if (qualityData == null) {
                return new GeneralResponse<>(null, "No quality found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }

            return new GeneralResponse<>(qualityData, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
        }catch (Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }

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

    @GetMapping(value = "/is_quality_exist/{quality_id}")
    public GeneralResponse<Boolean> IsQualityAlreadyExist(@PathVariable("quality_id") String quality_id) {

        if (quality_id != null) {
            String flag = qualityDao.isQualityNameExist(quality_id);
            if (flag!=null) {
                return new GeneralResponse<Boolean>(true, "found successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            return new GeneralResponse<Boolean>(false, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new GeneralResponse<Boolean>(false, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }

}
