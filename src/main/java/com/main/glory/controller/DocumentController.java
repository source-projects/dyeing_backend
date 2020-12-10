package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.document.request.GetDocumentModel;
import com.main.glory.servicesImpl.DocumentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class DocumentController extends ControllerConfig {
    @Autowired
    DocumentImpl documentService;

    @GetMapping("/partyDocument/")
    public GeneralResponse<Boolean> GetBatchByMaster(@RequestBody GetDocumentModel documentModel){
        try{
            documentService.getParty(documentModel);

            return new GeneralResponse<>(true, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);

        }catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
