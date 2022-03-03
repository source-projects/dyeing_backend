package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.servicesImpl.DocumentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
