package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.filters.FilterResponse;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;


import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.paymentTerm.PaymentMast;
import com.main.glory.model.paymentTerm.AdvancePayment;
import com.main.glory.model.paymentTerm.response.GetAllPayment;
import com.main.glory.model.paymentTerm.PaymentType;
import com.main.glory.model.paymentTerm.request.AddPaymentMast;
import com.main.glory.model.paymentTerm.request.GetAdvancePayment;
import com.main.glory.model.paymentTerm.request.GetAllBank;
import com.main.glory.model.paymentTerm.request.GetPendingDispatch;
import com.main.glory.servicesImpl.DispatchMastImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.PaymentTermImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentTermController extends ControllerConfig {

    @Autowired
    PaymentTermImpl paymentTermService;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll;


    @Autowired
    DispatchMastImpl dispatchMastService;

    @PostMapping(value="/paymentTerm/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> savePayment(@RequestBody AddPaymentMast paymentMast,@RequestHeader Map<String, String> headers) {
        GeneralResponse<Boolean,Object> result;
        try {
            Boolean flag = paymentTermService.savePayment(paymentMast,headers.get("id"));
            if (flag) {
                result = new GeneralResponse<>(true, ConstantFile.Payment_Added, true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            } else {
                result = new GeneralResponse<>(null, ConstantFile.Payment_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,paymentMast);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //update payment
    @PutMapping(value="/paymentTerm/update")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updatePaymentMast(@RequestBody AddPaymentMast paymentMast,@RequestHeader Map<String, String> headers) {
        GeneralResponse<Boolean,Object> result;
        try {
            String id = headers.get("id");
            if(paymentMast == null || id == null)
                throw new Exception(ConstantFile.Null_Record_Passed);

            Boolean flag = paymentTermService.updatePayment(paymentMast,id);
            if (flag) {
                result = new GeneralResponse<>(true, ConstantFile.Payment_Updated, true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            } else {
                result = new GeneralResponse<>(null, ConstantFile.Payment_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,paymentMast);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get the payment detail by paymentBunchId
    @GetMapping(value="/paymentTerm/getPaymentDetailById")
    public ResponseEntity<GeneralResponse<AddPaymentMast,Object>> getPaymentDetailById(@RequestParam(name = "paymentBunchId") Long paymentBunchId)
    {
        GeneralResponse<AddPaymentMast,Object> result;
        try {
            if(paymentBunchId==null)
                throw new Exception(ConstantFile.Null_Record_Passed);

            AddPaymentMast list = paymentTermService.getPaymentDetailById(paymentBunchId);
            if(list!=null)
                result= new GeneralResponse<>(list, ConstantFile.Payment_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result = new GeneralResponse<>(null, ConstantFile.Payment_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode())) ;
    }

    @DeleteMapping(value="/paymentTerm/delete")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deletePaymentMastById(@RequestParam("id")Long id) {
        GeneralResponse<Boolean,Object> result;
        try {
            if(id==null)
                throw new Exception(ConstantFile.Null_Record_Passed);

            Boolean flag = paymentTermService.deletePaymentById(id);
            if (flag) {
                result = new GeneralResponse<>(true, ConstantFile.Payment_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result = new GeneralResponse<>(null, ConstantFile.Payment_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
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
            result = new GeneralResponse<>(true, ConstantFile.Payment_Added, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    //add payment type api
    @GetMapping(value="/paymentTerm/getAllPaymentType")
    public ResponseEntity<GeneralResponse<List<PaymentType>,Object>> getAllPaymentType() {
        GeneralResponse<List<PaymentType>,Object> result;
        try {
            List<PaymentType> flag = paymentTermService.getAllPaymentType();

            if(flag.isEmpty())
                result= new GeneralResponse<>(flag, ConstantFile.Payment_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
            result= new GeneralResponse<>(flag, ConstantFile.Payment_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
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
                result= new GeneralResponse<>(true, ConstantFile.Payment_Added, true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            } else {
                result= new GeneralResponse<>(null, ConstantFile.Payment_Not_Added, true, System.currentTimeMillis(), HttpStatus.OK,paymentMast);
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,paymentMast);
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
                throw new Exception(ConstantFile.Null_Record_Passed);

            List<GetPendingDispatch> list = paymentTermService.getPendingBillByPartyId(partyId);
            if(!list.isEmpty())
                result = new GeneralResponse<>(list, ConstantFile.Payment_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result= new GeneralResponse<>(list, ConstantFile.Payment_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
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
                throw new Exception(ConstantFile.Null_Record_Passed);

            List<GetAdvancePayment> list = paymentTermService.getAdvancePayment(partyId);
            if(!list.isEmpty())
                result= new GeneralResponse<>(list, ConstantFile.Payment_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result= new GeneralResponse<>(list, ConstantFile.Payment_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //get the bank list distinct
    @GetMapping(value="/paymentTerm/getAllBank")
    public ResponseEntity<GeneralResponse<List<GetAllBank>,Object>> getAllBankName()
    {
        GeneralResponse<List<GetAllBank>,Object> result;
        try {


            List<GetAllBank> list = paymentTermService.getAllBankName();
            if(!list.isEmpty())
                result= new GeneralResponse<>(list, ConstantFile.Payment_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result= new GeneralResponse<>(null, ConstantFile.Payment_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get the bank list distinct
    @GetMapping(value="/paymentTerm/advancePayment/getAllBank")
    public ResponseEntity<GeneralResponse<List<GetAllBank>,Object>> getAllAdvancePaymentBank()
    {
        GeneralResponse<List<GetAllBank>,Object> result;
        try {


            List<GetAllBank> list = paymentTermService.getAllAdvanceBankName();
            if(!list.isEmpty())
                result= new GeneralResponse<>(list, ConstantFile.Bank_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result= new GeneralResponse<>(null, ConstantFile.Bank_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
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
                throw new Exception(ConstantFile.Null_Record_Passed);

            List<PaymentMast> list = paymentTermService.getAllPaymentMast(partyId);
            if(!list.isEmpty())
                result = new GeneralResponse<>(list, ConstantFile.Payment_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result= new GeneralResponse<>(null, ConstantFile.Payment_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping(value="/paymentTerm/getAllPayment")
    public ResponseEntity<GeneralResponse<List<GetAllPayment>,Object>> getAllPayment()
    {
        GeneralResponse<List<GetAllPayment>,Object> result;
        try {

            List<GetAllPayment> list = paymentTermService.getAllPaymentWithPartyName();
            if(list!=null)
                result= new GeneralResponse<>(list, ConstantFile.Payment_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result = new GeneralResponse<>(null, ConstantFile.Payment_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode())) ;
    }

    @PostMapping(value="/paymentTerm/getAllPayment/allPaginated")
    public ResponseEntity<GeneralResponse<FilterResponse<GetAllPayment>,Object>> getAllPaymentWithPagination(@RequestBody GetBYPaginatedAndFiltered requestParam)
    {
        GeneralResponse<FilterResponse<GetAllPayment>,Object> result;
        try {

            FilterResponse<GetAllPayment> list = paymentTermService.getAllPaymentWithPartyNameWithPagination(requestParam);
            if(list!=null && !list.getData().isEmpty())
                result= new GeneralResponse<>(list, ConstantFile.Payment_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result = new GeneralResponse<>(null, ConstantFile.Payment_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode())) ;
    }



//    @PostMapping(value="/paymentTerm/monthWisePendingReport")
//    public ResponseEntity<GeneralResponse<List<MonthlyDispatchPendingReport>,Object>> getMonthWiseReportPendingDispatch(@RequestBody DispatchFilter filter) throws Exception{
//        GeneralResponse<List<MonthlyDispatchPendingReport>, Object> result;
//        try{
//            List<MonthlyDispatchPendingReport> list = dispatchMastService.getMonthWiseReportPendingDispatch(filter);
//            if(!list.isEmpty())
//                result= new GeneralResponse<>(list, ConstantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
//            else
//                result = new GeneralResponse<>(null, ConstantFile.Dispatch_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
//            logService.saveLog(result,request,debugAll);
//        } catch (Exception e){
//            e.printStackTrace();
//            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,filter);
//            logService.saveLog(result,request,true
//            );
//        }
//        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
//    }



}
