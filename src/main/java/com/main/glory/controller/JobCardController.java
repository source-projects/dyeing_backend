package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.jobcard.JobMast;
import com.main.glory.servicesImpl.JobDataImpl;
import com.main.glory.servicesImpl.JobMastImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class JobCardController extends ControllerConfig {

    @Autowired
    JobMastImpl jobMast;

    @Autowired
    JobDataImpl jobData;

    @GetMapping("/jobCard/{id}")
    public GeneralResponse<JobMast> getJobCardId(@PathVariable(value = "id") Long id){
        try{
            if(id!=null){
                Optional<JobMast> jobMastData = jobMast.getJobMastById(id);
                if(jobMastData.isPresent()){
                    return new GeneralResponse<JobMast>(jobMastData.get(), "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                }else{
                    return new GeneralResponse<JobMast>(null, "no data found for id: "+id, false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
                }
            }
            else{
                return new GeneralResponse<>(null, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/jobCard")
    public GeneralResponse<Boolean> updateJobCard(@RequestBody JobMast jobMastData) {
        try {
            jobMast.updateJobCard(jobMastData);
            return new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

}
