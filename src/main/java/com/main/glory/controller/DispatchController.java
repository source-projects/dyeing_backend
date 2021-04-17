package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.dispatch.Filter;
import com.main.glory.model.dispatch.bill.GetBill;
import com.main.glory.model.dispatch.request.*;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.dispatch.response.GetConsolidatedBill;
import com.main.glory.servicesImpl.DispatchMastImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.spel.ast.OperatorBetween;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class DispatchController extends ControllerConfig {


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
            Long flag = dispatchMastService.saveDispatch(dispatchMast);
            result= new GeneralResponse<>(flag,"Invoice data added successfully", true, System.currentTimeMillis(), HttpStatus.OK,dispatchMast);

            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result =  new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,dispatchMast);
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
                result= new GeneralResponse<>(list,"Invoice data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,filter);
            else
                result = new GeneralResponse<>(null,"Invoice not created", false, System.currentTimeMillis(), HttpStatus.OK,filter);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,filter);
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
                result =  new GeneralResponse<>(list,"Invoice data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,filter);
            else
                result =  new GeneralResponse<>(null,"Invoice not created", false, System.currentTimeMillis(), HttpStatus.OK,filter);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,filter);
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
                result= new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else
                result= new GeneralResponse<>(null,"party id can't be null", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
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
                result = new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else
                result = new GeneralResponse<>(null,"party id can't be null", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/dispatch/getAll")
    public ResponseEntity<GeneralResponse<List<GetAllDispatch>, Object>> getAllDispatch() throws Exception{

        GeneralResponse<List<GetAllDispatch>,Object> result;
        try{

            List<GetAllDispatch> x =dispatchMastService.getAllDisptach();
            result = new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/dispatch/updateDispatch/")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateDispatch(@RequestBody UpdateInvoice updateInvoice) throws Exception{
        GeneralResponse<Boolean,Object> result;
        try{

            Boolean flag = dispatchMastService.updateDispatch(updateInvoice);
            if(flag==true)
            result= new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK,updateInvoice);
            else
                result= new GeneralResponse<>(false, "data not updated", true, System.currentTimeMillis(), HttpStatus.OK,updateInvoice);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,updateInvoice);
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
                result = new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            else
                result = new GeneralResponse<>(false, "data not updated", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
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

                result= new GeneralResponse<>(x, "Invoice fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else
                result= new GeneralResponse<>(null,"Invoice id can't be null", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);

        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
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

                result= new GeneralResponse<>(x, "Invoice receipt successfully", true, System.currentTimeMillis(), HttpStatus.OK,createDispatch);
            }
            else
                result= new GeneralResponse<>(null,"Invoice record can't be null", false, System.currentTimeMillis(), HttpStatus.OK,createDispatch);
            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result= new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,createDispatch);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }




}
