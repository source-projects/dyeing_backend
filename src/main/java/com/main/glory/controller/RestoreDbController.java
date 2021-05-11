package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.RestoreDbImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RestoreDbController extends ControllerConfig {

    @Autowired
    RestoreDbImpl restoreDb;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    @GetMapping("/db/restore/{date}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> restoreDb(@PathVariable(name = "date") String date) {
        GeneralResponse<Boolean,Object> result;
        try{


            Boolean flag = restoreDb.restoreDb(date);
            if(flag)
            {
                result = new GeneralResponse<>(true, "Db restore Successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            else result =  new GeneralResponse<>(false, "Db restore not Successfully", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            logService.saveLog(result,request,debugAll);

        } catch (Exception e) {
            e.printStackTrace();
            result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }


        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/db/backup")
    public ResponseEntity<GeneralResponse<Boolean,Object>> backupDb() {
        GeneralResponse<Boolean,Object> result;
        try{


            Boolean flag = restoreDb.backupDb();
            if(flag)
            {
                result=  new GeneralResponse<>(true, "Db restore Successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            else result= new GeneralResponse<>(false, "Db restore not Successfully", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            logService.saveLog(result,request,debugAll);



        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

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
