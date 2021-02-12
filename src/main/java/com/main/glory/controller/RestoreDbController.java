package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.servicesImpl.RestoreDbImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RestoreDbController extends ControllerConfig {

    @Autowired
    RestoreDbImpl restoreDb;

    @GetMapping("/db/restore/{name}")
    public GeneralResponse<Boolean> restoreDb(@PathVariable(name = "name") String name) {
        try{


            Boolean flag = restoreDb.restoreDb(name);
            if(flag)
            {
                return new GeneralResponse<>(true, "Db restore Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else return new GeneralResponse<>(false, "Db restore not Successfully", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);


        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/db/backup")
    public GeneralResponse<Boolean> backupDb() {
        try{


            Boolean flag = restoreDb.backupDb();
            if(flag)
            {
                return new GeneralResponse<>(true, "Db restore Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else return new GeneralResponse<>(false, "Db restore not Successfully", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);


        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }
    //clear the db except the designation
    /*@GetMapping("/db/clearDb")
    public GeneralResponse<Boolean> clearDb() {
        try{


            Boolean flag = restoreDb.clearDb();
            if(flag)
            {
                return new GeneralResponse<>(true, "Db clear Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else return new GeneralResponse<>(false, "Db not cleared", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);


        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }*/

}
