package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.response.GetAllBatch;
import com.main.glory.model.document.request.GetDocumentModel;
import com.main.glory.servicesImpl.DocumentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Document extends ControllerConfig {

    @Autowired
    DocumentImpl documentService;

    /*@GetMapping("/party/")
    public ResponseEntity<GeneralResponse<Boolean>> GetBatchByMaster(@RequestBody GetDocumentModel documentModel){
        GeneralResponse<Boolean> result;
        try{
            documentService.getParty(documentModel);

            result = new GeneralResponse<>(true, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }*/


}
