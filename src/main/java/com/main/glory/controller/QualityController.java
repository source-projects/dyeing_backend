package com.main.glory.controller;

import java.util.List;
import com.main.glory.Dao.QualityDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.Party;
import com.main.glory.services.QualityServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.sun.istack.NotNull;
import com.main.glory.model.Quality;
import com.main.glory.servicesImpl.QualityServiceImp;

@RestController
@RequestMapping("/api")
public class QualityController extends ControllerConfig {

    @Autowired
    private QualityServiceImp qualityServiceImp;

    @Autowired
    private QualityDao qualityDao;

    @PostMapping(value = "/quality")
    public GeneralResponse<Boolean> saveQuality(@RequestBody Quality quality) {
        int flag = qualityServiceImp.saveQuality(quality);
        if (flag == 1)
            return new GeneralResponse<Boolean>(null, "Quality Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
        else
            return new GeneralResponse<Boolean>(null, "Please Enter Valid Data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/quality/all")
    public GeneralResponse<List<Quality>> getQualityList() {
        try {
            var x = qualityServiceImp.getAllQuality();
            return new GeneralResponse<List<Quality>>(x, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new GeneralResponse<List<Quality>>(null, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/quality")
    public GeneralResponse<Boolean> updateQualityById(@RequestBody Quality quality) throws Exception {
        if (quality.getId() != null) {
            boolean flag = qualityServiceImp.updateQuality(quality);
            if (flag) {
                return new GeneralResponse<Boolean>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            return new GeneralResponse<Boolean>(false, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new GeneralResponse<Boolean>(false, "Null quality Object", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/quality/{id}")
    public GeneralResponse<List<Quality>> getQualityDataById(@PathVariable(value = "id") Long id) {
        try {
            if (id != null) {
                var x = qualityServiceImp.getQualityByID(id);
                return new GeneralResponse<List<Quality>>(x, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                return new GeneralResponse<Boolean>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            return new GeneralResponse<Boolean>(false, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new GeneralResponse<Boolean>(false, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }

}
