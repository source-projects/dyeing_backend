package com.main.glory.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.batch.BatchGrDetail;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.servicesImpl.BatchServiceImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BatchController extends ControllerConfig {

    @Autowired
    LogServiceImpl logService;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private BatchServiceImpl batchService;

   /* @PostMapping("/batch")
    public ResponseEntity<GeneralResponse<Boolean,Object>> createBatch(@RequestBody BatchMast batchMast) throws Exception{
        try{
            batchService.createBatch(batchMast);
            return new GeneralResponse<>(true,"batch created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e){

            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/batch/all")
    public GeneralResponse<List<BatchMast>> getAll() throws Exception{
        try{
            List<BatchMast> batchMasts = batchService.getBatchList();
            return new GeneralResponse<>(batchMasts,"batch created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e){

            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value="/batch/{id}")
    public GeneralResponse<BatchMast> getBatchById(@PathVariable(value = "id") Long id)
    {
        if(id!=null)
        {
            var batchObj=batchService.getBatchMastById(id);
            if(batchObj!=null)
            {
                return new GeneralResponse<BatchMast>(batchObj, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
            }
            return new GeneralResponse<BatchMast>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
        return new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/batch")
    public GeneralResponse<Boolean> updateBatch(@RequestBody BatchMast batchMast) {
        try {
            batchService.updateBatch(batchMast);
            return new GeneralResponse<>(true, "batch updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);

        }
    }

    @DeleteMapping("/batch/{id}")
    public GeneralResponse<Boolean> deleteBatch(@PathVariable("id") Long id){
        try{
            System.out.println("deleting batch with id:"+id);
            batchService.deleteBatch(id);
            return new GeneralResponse<>(true,"batch deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }
*/
}
