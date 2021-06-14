package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTRWithPendingBill;
import com.main.glory.model.dispatch.Filter;
import com.main.glory.model.dispatch.bill.GetBill;
import com.main.glory.model.dispatch.request.*;
import com.main.glory.model.dispatch.response.ConsolidatedBillMast;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import com.main.glory.model.dispatch.response.GetConsolidatedBill;
import com.main.glory.servicesImpl.DispatchMastImpl;
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
public class DispatchController extends ControllerConfig {


    ConstantFile constantFile;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll=true;

    @Autowired
    DispatchMastImpl dispatchMastService;

    @PostMapping("/dispatch/")
    public ResponseEntity<GeneralResponse<Long,Object>> createDispatch(@RequestBody CreateDispatch dispatchMast) throws Exception{
        GeneralResponse<Long,Object> result;
        try{
            System.out.println(request.getHeaderNames());
            Long flag = dispatchMastService.saveDispatch(dispatchMast,Long.parseLong(request.getHeader("id")));
            result= new GeneralResponse<>(flag, constantFile.Dispatch_Added, true, System.currentTimeMillis(), HttpStatus.OK,dispatchMast);

            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result =  new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,dispatchMast);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/dispatch/filter/forConslidateBill")
    public ResponseEntity<GeneralResponse<List<GetConsolidatedBill>,Object>> getDispatchConsolidateBillByFilter(@RequestBody Filter filter) throws Exception{
        GeneralResponse<List<GetConsolidatedBill>, Object> result;
        try{
            List<GetConsolidatedBill> list = dispatchMastService.getDispatchByFilter(filter);
            if(!list.isEmpty())
                result= new GeneralResponse<>(list, constantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
            else
                result = new GeneralResponse<>(null, constantFile.Dispatch_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,filter);
            logService.saveLog(result,request,true
            );
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @PostMapping("/dispatch/filter/getBill")
    public ResponseEntity<GeneralResponse<List<GetBill>,Object>> getDispatchBillByFilter(@RequestBody Filter filter) throws Exception{
        GeneralResponse<List<GetBill>,Object> result;
        try{
            List<GetBill> list = dispatchMastService.getDispatchBillByFilter(filter);
            if(!list.isEmpty())
                result =  new GeneralResponse<>(list, constantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
            else
                result =  new GeneralResponse<>(list, constantFile.Dispatch_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,filter);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    //Get batches by the party id
    @GetMapping("/dispatch/getBatchByParty/{partyId}")
    public ResponseEntity<GeneralResponse<List<BatchWithTotalMTRandFinishMTR>,Object>> getBatchByParty(@PathVariable(name="partyId") Long partyId) throws Exception{
        GeneralResponse<List<BatchWithTotalMTRandFinishMTR>,Object> result;
        try{
            if(partyId!=null) {
                List<BatchWithTotalMTRandFinishMTR> x =dispatchMastService.getBatchByParty(partyId);
                if(x.isEmpty())
                    result= new GeneralResponse<>(x, constantFile.StockBatch_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                else
                result= new GeneralResponse<>(x, constantFile.StockBatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            else
                result= new GeneralResponse<>(null, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //Get dispatch by invoice number
    @GetMapping("/dispatch/getDispatch/byInvoiceNumber/{invoiceNo}")
    public ResponseEntity<GeneralResponse<PartyWithBatchByInvoice,Object>> getDispatchByInvoiceNumber(@PathVariable(name="invoiceNo") String invoiceNo) throws Exception{
        GeneralResponse<PartyWithBatchByInvoice,Object> result;
        try{
            if(invoiceNo!=null) {
                PartyWithBatchByInvoice x =dispatchMastService.getDispatchByInvoiceNumber(invoiceNo);
                if(x!=null)
                result = new GeneralResponse<>(x, constantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                else
                result = new GeneralResponse<>(x, constantFile.Dispatch_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            else
                result = new GeneralResponse<>(null, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/dispatch/getAll")
    public ResponseEntity<GeneralResponse<List<GetAllDispatch>, Object>> getAllDispatch() throws Exception{

        GeneralResponse<List<GetAllDispatch>,Object> result;
        try{

            List<GetAllDispatch> x =dispatchMastService.getAllDisptach();
            if(!x.isEmpty())
            result = new GeneralResponse<>(x, constantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result = new GeneralResponse<>(x, constantFile.Dispatch_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/dispatch/updateDispatch/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateDispatch(@RequestBody CreateDispatch updateInvoice) throws Exception{
        GeneralResponse<Boolean,Object> result;
        try{

            dispatchMastService.updateDispatch(updateInvoice);

            result= new GeneralResponse<>(true, constantFile.Dispatch_Updated, true, System.currentTimeMillis(), HttpStatus.OK,updateInvoice);

            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,updateInvoice);
            logService.saveLog(result,request,true);
        }
        return  new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @PutMapping("/dispatch/updateDispatchStatus/{invoiceNo}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateDispatchStatus(@PathVariable(name="invoiceNo") String invoiceNo) throws Exception{
        GeneralResponse<Boolean,Object> result;
        try{

            Boolean flag = dispatchMastService.updateDispatchStatus(invoiceNo);
            if(flag==true)
                result = new GeneralResponse<>(true, constantFile.Dispatch_Updated, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result = new GeneralResponse<>(false, "data not updated", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode())) ;
    }

    @GetMapping("/dispatch/getPartyWithQualityDispatchBy/{id}")
    public ResponseEntity<GeneralResponse<PartyDataByInvoiceNumber,Object>> getPartyWithQualityDispatchBy(@PathVariable(name="id") String id) throws Exception{
        GeneralResponse<PartyDataByInvoiceNumber,Object> result;
        try{
            if(id!=null) {
                PartyDataByInvoiceNumber x =dispatchMastService.getPartyWithQualityDispatchBy(id);

                result= new GeneralResponse<>(x, constantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            else
                result= new GeneralResponse<>(null,ConstantFile.Null_Record_Passed, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);

        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
            //logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


/*
    @PostMapping("/dispatch")
    public GeneralResponse<Boolean> createDispatch(@RequestBody DispatchMast dispatchMast) throws Exception{
        try{
            Boolean flag = dispatchMastService.saveDispatch(dispatchMast);
            if(flag==true)
                return new GeneralResponse<>(true,"Invoice created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<>(false,"Invoice not created", false, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }

    @GetMapping("/getCompleteDispatchBy/{id}")
    public GeneralResponse<GetDispatchCompleteDetail> getCompleteDipatchBy(@PathVariable(name="id") Long id) throws Exception{
        try{
            if(id!=null) {
                GetDispatchCompleteDetail x =dispatchMastService.getDispatchCompleteDetail(id);
                return new GeneralResponse<>(x, "Invoice fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
                return new GeneralResponse<>(null,"Invoice id can't be null", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }

    @GetMapping("/dispatch/{id}")
    public GeneralResponse<DispatchMast> getDispatch(@PathVariable(name="id") Long id) throws Exception{
        try{
            if(id!=null) {
                DispatchMast x =dispatchMastService.getInvoiceById(id);
                return new GeneralResponse<>(x, "Invoice fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
                return new GeneralResponse<>(null,"Invoice id can't be null", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }
*/

/*
    @GetMapping("/dispatch/getFinishMtrList/{batchId}/{controlId}")
    public GeneralResponse<List<BatchData>> getFinishMtrList(@PathVariable(name="batchId") String batchId,@PathVariable(name="controlId") Long controlId) throws Exception{
        try{

                List<BatchData> x =dispatchMastService.getFinishMtrListBasedOnBatchIdAndControlId(batchId,controlId);
                return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }

    @GetMapping("/dispatch/getAll")
    public GeneralResponse<List<GetAllDispatch>> getAllDispatch() throws Exception{
        try{

            List<GetAllDispatch> x =dispatchMastService.getAllDisptach();
            return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }

    @PutMapping("/dispatch/updateDispatch")
    public GeneralResponse<Boolean> updateDispatchData(@RequestBody DispatchMast dispatchMastToUpdate){
        try{

            Boolean x =dispatchMastService.updateDispatchData(dispatchMastToUpdate);
            if(x==true)
            return new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
            return new GeneralResponse<>(false, "Data can't updated", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }


*/





    //********************* New Invoice module

    @PostMapping("/dispatch/get/receipt/getPartyWithQualityDispatchByBatchesAndStock/")
    public ResponseEntity<GeneralResponse<PartyDataByInvoiceNumber,Object>> getPartyWithQualityDispatchBy(@RequestBody CreateDispatch createDispatch) throws Exception{
        GeneralResponse<PartyDataByInvoiceNumber,Object> result;
        try{
            if(createDispatch!=null) {
                PartyDataByInvoiceNumber x =dispatchMastService.getPartyWithQualityDispatchByBatchesAndStockId(createDispatch);

                result= new GeneralResponse<>(x, constantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,createDispatch);
            }
            else
                result= new GeneralResponse<>(null, constantFile.Dispatch_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,createDispatch);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,createDispatch);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get password to create invoie
    @GetMapping("/dispatch/get/passwordExist")
    public ResponseEntity<GeneralResponse<Boolean,Object>> getPasswordToCreateInvoice(@RequestParam(name = "password")String password) throws Exception{
        GeneralResponse<Boolean,Object> result;
        try{
            Boolean flag = dispatchMastService.getPasswordToCreateInvoice(password);
            if(flag==true)
            {
            //result= new GeneralResponse<>(flag, constantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                result= new GeneralResponse<>(flag, constantFile.Dispatch_Password_Correct, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            else
            {
                result= new GeneralResponse<>(flag, constantFile.Dispatch_Password_Wrong, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //delete functionality to take one step back
    @DeleteMapping("/dispatch/delete")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteDispatch(@RequestParam(name = "invoiceNo") Long invoiceNo) throws Exception{
        GeneralResponse<Boolean,Object> result;
        try{
            dispatchMastService.deleteDispatchByInvoiceNo(invoiceNo);
            result= new GeneralResponse<>(true, constantFile.Dispatch_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result =  new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //report by date with consildate bill response
    @PostMapping("/dispatch/report/forConslidateBill")
    public ResponseEntity<GeneralResponse<List<ConsolidatedBillMast>,Object>> getReportDispatchConsolidateBillByFilter(@RequestBody Filter filter) throws Exception{
        GeneralResponse<List<ConsolidatedBillMast>, Object> result;
        try{
            List<ConsolidatedBillMast> list = dispatchMastService.getConsolidateDispatchBillByFilter(filter);
            if(!list.isEmpty())
                result= new GeneralResponse<>(list, constantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
            else
                result = new GeneralResponse<>(null, constantFile.Dispatch_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,filter);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,filter);
            logService.saveLog(result,request,true
            );
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }




}
