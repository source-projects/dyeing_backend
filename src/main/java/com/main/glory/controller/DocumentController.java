package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.document.request.GetDocumentModel;
import com.main.glory.servicesImpl.DocumentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")

public class DocumentController extends ControllerConfig {


    @Autowired
    HttpServletRequest request;

    @Autowired
    DocumentImpl documentService;

    @PostMapping("/document/")
    public ResponseEntity<GeneralResponse<Boolean,String>> GetDocument(@RequestBody GetDocumentModel documentModel){
        GeneralResponse<Boolean,String> result;

        try{
            documentService.getDocument(documentModel);

            result = new GeneralResponse<>(true, "PDF send successfully", true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());

        }catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }
}
