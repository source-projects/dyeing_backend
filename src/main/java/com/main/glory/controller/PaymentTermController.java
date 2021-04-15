package com.main.glory.controller;

import com.google.api.Http;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;

import com.main.glory.model.PaymentMast;
import com.main.glory.model.paymentTerm.AdvancePayment;
import com.main.glory.model.paymentTerm.PaymentType;
import com.main.glory.model.paymentTerm.request.AddPaymentMast;
import com.main.glory.model.paymentTerm.request.GetAdvancePayment;
import com.main.glory.model.paymentTerm.request.GetPendingDispatch;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.PaymentTermImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentTermController extends ControllerConfig {

    @Autowired
    PaymentTermImpl paymentTermService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;
    //@Value("${spring.application.debugAll}")
    Boolean debugAll=true;


    @PostMapping(value="/paymentTerm/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> savePayment(@RequestBody AddPaymentMast paymentMast) {
        GeneralResponse<Boolean,Object> result;
        try {
            Boolean flag = paymentTermService.savePayment(paymentMast);
            if (flag) {
                result = new GeneralResponse<>(true, "Payment Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            } else {
                result = new GeneralResponse<>(null, "Data Not added", true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //add payment type api
    @PostMapping(value="/paymentTerm/addPaymentType/{type}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> savePaymentType(@PathVariable(name = "type") String type) {
        GeneralResponse<Boolean,Object> result;
        try {
            Boolean flag = paymentTermService.savePaymentType(type);
            result = new GeneralResponse<>(true, "Payment Type Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    //add payment type api
    @GetMapping(value="/paymentTerm/getAllPaymentType")
    public ResponseEntity<GeneralResponse<List<PaymentType>,Object>> getAllPaymentType() {
        GeneralResponse<List<PaymentType>,Object> result;
        try {
            List<PaymentType> flag = paymentTermService.getAllPayementType();
            result= new GeneralResponse<>(flag, "Payment Type Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @PostMapping(value="/paymentTerm/addAdvancePayment")
    public ResponseEntity<GeneralResponse<Boolean,Object>> addAdvancePayment(@RequestBody List<AdvancePayment> paymentMast) {
        GeneralResponse<Boolean,Object> result;
        try {
            Boolean flag = paymentTermService.addAdvancePayment(paymentMast);
            if (flag) {
                result= new GeneralResponse<>(true, "Advance Payment Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            } else {
                result= new GeneralResponse<>(null, "Data Not added", true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }


    //get the pending bill list
    @GetMapping(value="/paymentTerm/getPendingBillByPartyId/{partyId}")
    public ResponseEntity<GeneralResponse<List<GetPendingDispatch>,Object>> getPendingBillByPartyId(@PathVariable(name = "partyId") Long partyId)
    {
        GeneralResponse<List<GetPendingDispatch>,Object> result;
        try {
            if(partyId==null)
                throw new Exception("id can't be null");

            List<GetPendingDispatch> list = paymentTermService.getPendingBillByPartyId(partyId);
            if(!list.isEmpty())
                result = new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result= new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get the advance payment list
    @GetMapping(value="/paymentTerm/getAdvancePayment/{partyId}")
    public ResponseEntity<GeneralResponse<List<GetAdvancePayment>,Object>> getAdvancePayment(@PathVariable(name = "partyId") Long partyId)
    {
        GeneralResponse<List<GetAdvancePayment>,Object> result;
        try {
            if(partyId==null)
                throw new Exception("id can't be null");

            List<GetAdvancePayment> list = paymentTermService.getAdvancePayment(partyId);
            if(!list.isEmpty())
                result= new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result= new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get payment mast list by party
    @GetMapping(value="/paymentTerm/getPaymentBunchListByPartyId/{partyId}")
    public ResponseEntity<GeneralResponse<List<PaymentMast>,Object>> getPaymentMastByPartyId(@PathVariable(name = "partyId") Long partyId)
    {
        GeneralResponse<List<PaymentMast>,Object> result;
        try {
            if(partyId==null)
                throw new Exception("id can't be null");

            List<PaymentMast> list = paymentTermService.getAllPaymentMast(partyId);
            if(!list.isEmpty())
                result = new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result= new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    //get the payment detail by paymentBunchId
    @GetMapping(value="/paymentTerm/getPaymentDetailById/{paymentBunchId}")
    public ResponseEntity<GeneralResponse<PaymentMast,Object>> getPaymentDetailById(@PathVariable(name = "paymentBunchId") Long paymentBunchId)
    {
        GeneralResponse<PaymentMast,Object> result;
        try {
            if(paymentBunchId==null)
                throw new Exception("id can't be null");

            PaymentMast list = paymentTermService.getPaymentDetailById(paymentBunchId);
            if(list!=null)
                result= new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
           result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode())) ;
    }




}
