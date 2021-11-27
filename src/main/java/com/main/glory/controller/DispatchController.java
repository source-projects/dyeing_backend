package com.main.glory.controller;

import com.main.glory.model.dispatch.response.*;

import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.filters.FilterResponse;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.dispatch.DispatchFilter;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.bill.GetBill;
import com.main.glory.model.dispatch.request.*;
import com.main.glory.model.dispatch.response.report.ConsolidatedBillDataForExcel;
import com.main.glory.model.dispatch.response.report.ConsolidatedBillMast;
import com.main.glory.model.machine.request.PaginatedData;
import com.main.glory.services.FilterService;
import com.main.glory.servicesImpl.DispatchMastImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
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
    Boolean debugAll;

    @Autowired
    DispatchMastDao dispatchMastDao;

    @Autowired
    DispatchMastImpl dispatchMastService;

    @Autowired
    FilterService<DispatchMast, DispatchMastDao> filterService;

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
    public ResponseEntity<GeneralResponse<List<GetConsolidatedBill>,Object>> getDispatchConsolidateBillByFilter(@RequestBody DispatchFilter filter) throws Exception{
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
    public ResponseEntity<GeneralResponse<List<GetBill>,Object>> getDispatchBillByFilter(@RequestBody DispatchFilter filter) throws Exception{
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
    public ResponseEntity<GeneralResponse<List<GetAllDispatch>, Object>> getAllDispatch(@RequestParam(name = "signByParty") String signByParty) throws Exception{

        GeneralResponse<List<GetAllDispatch>,Object> result;
        try{

            List<GetAllDispatch> x =dispatchMastService.getAllDisptach(signByParty);
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

    @PostMapping("/dispatch/allPaginated")
    public ResponseEntity<GeneralResponse<FilterResponse<GetAllDispatch>, Object>> getAllDispatchPaginated(@RequestBody GetBYPaginatedAndFiltered requestParam) throws Exception{
        GeneralResponse<FilterResponse<GetAllDispatch>,Object> result;

        try{

            FilterResponse<GetAllDispatch> x =dispatchMastService.getAllDisptach(requestParam);
            if(!x.getData().isEmpty())
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
    public ResponseEntity<GeneralResponse<Long,Object>> updateDispatch(@RequestBody CreateDispatch updateInvoice) throws Exception{
        GeneralResponse<Long,Object> result;
        try{

            Long invoiceNo = dispatchMastService.updateDispatch(updateInvoice);

            result= new GeneralResponse<>(invoiceNo, constantFile.Dispatch_Updated, true, System.currentTimeMillis(), HttpStatus.OK,updateInvoice);

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

    //for printing the invoice
    @GetMapping("/dispatch/getPartyWithQualityDispatchBy/{id}")
    public ResponseEntity<GeneralResponse<PartyDataByInvoiceNumber,Object>> getPartyWithQualityDispatchBy(@PathVariable(name="id") String id) throws Exception{
        GeneralResponse<PartyDataByInvoiceNumber,Object> result;
        try{
            //id = invoiceNo
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

    //for receipt/preview of invoice before saving the record
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
    @PostMapping("/dispatch/report/forConslidateReportBill")
    public ResponseEntity<GeneralResponse<List<ConsolidatedBillMast>,Object>> getReportDispatchConsolidateBillByFilter(@RequestBody DispatchFilter filter) throws Exception{
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
    @PostMapping("/dispatch/report/forConslidateExcelBill")
    public ResponseEntity<GeneralResponse<List<ConsolidatedBillDataForExcel>,Object>> getReportDispatchConsolidateExcelBillByFilterNew(@RequestBody DispatchFilter filter) throws Exception{
        GeneralResponse<List<ConsolidatedBillDataForExcel>, Object> result;
        try{
            List<ConsolidatedBillDataForExcel> list = dispatchMastService.getConsolidateDispatchBillByFilterNew(filter);
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

    @PostMapping("/dispatch/monthWiseReport")
    public ResponseEntity<GeneralResponse<List<MonthlyDispatchReport>,Object>> getMonthWiseReportDispatch(@RequestBody DispatchFilter filter) throws Exception{
        GeneralResponse<List<MonthlyDispatchReport>, Object> result;
        try{    
            List<MonthlyDispatchReport> list = dispatchMastService.getMonthWiseReportDispatch(filter);
            if(list !=null || !list.isEmpty())
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

    //sign dispatch flag
    @PostMapping("/dispatch/signByParty")
    public ResponseEntity<GeneralResponse<Boolean,Object>> signDispatchByParty(@RequestBody List<UpdateSignDispatch> updateSignDispatchList) throws Exception{
        GeneralResponse<Boolean,Object> result;
        try{
            //System.out.println(request.getHeaderNames());
            dispatchMastService.signDispatchByParty(updateSignDispatchList);
            result= new GeneralResponse<>(true, constantFile.Dispatch_Updated, true, System.currentTimeMillis(), HttpStatus.OK,updateSignDispatchList);

            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result =  new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,updateSignDispatchList);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //get complete bill detail by filter for export by excel
    /*@PostMapping("/dispatch/report/forCompleteInvoiceDetailForExportData")
    public ResponseEntity<GeneralResponse<List<ConsolidatedBillMast>,Object>> forCompleteInvoiceDetailForExportData(@RequestBody Filter filter) throws Exception{
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
    }*/




    //create dispatchBy pchallan api's
    @GetMapping("/dispatch/getPChallanByParty/{partyId}")
    public ResponseEntity<GeneralResponse<List<BatchWithTotalMTRandFinishMTR>,Object>> getPChallanByParty(@PathVariable(name="partyId") Long partyId) throws Exception{
        GeneralResponse<List<BatchWithTotalMTRandFinishMTR>,Object> result;
        try{
            if(partyId!=null) {
                List<BatchWithTotalMTRandFinishMTR> x =dispatchMastService.getPChallanByParty(partyId);
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

    @PostMapping("/dispatch/add")
    public ResponseEntity<GeneralResponse<Long,Object>> createDispatchForPchallan(@RequestBody CreateDispatch dispatchMast) throws Exception{
        GeneralResponse<Long,Object> result;
        try{
            System.out.println(request.getHeaderNames());
            Long flag = dispatchMastService.createDispatchForPchallan(dispatchMast,Long.parseLong(request.getHeader("id")));
            result= new GeneralResponse<>(flag, constantFile.Dispatch_Added, true, System.currentTimeMillis(), HttpStatus.OK,dispatchMast);

            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result =  new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,dispatchMast);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/dispatch/update")
    public ResponseEntity<GeneralResponse<Long,Object>> updateDispatchForPchallan(@RequestBody CreateDispatch updateInvoice) throws Exception{
        GeneralResponse<Long,Object> result;
        try{

            Long invoiceNo = dispatchMastService.updateDispatchWithPChallan(updateInvoice,Long.parseLong(request.getHeader("id")));

            result= new GeneralResponse<>(invoiceNo, constantFile.Dispatch_Updated, true, System.currentTimeMillis(), HttpStatus.OK,updateInvoice);

            logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,updateInvoice);
            logService.saveLog(result,request,true);
        }
        return  new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    //for print invoice
    @GetMapping("/dispatch/getPChallanPartyWithQualityDispatchBy/{id}")
    public ResponseEntity<GeneralResponse<PartyDataByInvoiceNumber,Object>> getPChallanPartyWithQualityDispatchBy(@PathVariable(name="id") String id) throws Exception{
        GeneralResponse<PartyDataByInvoiceNumber,Object> result;
        try{
            //id = invoiceNo
            if(id!=null) {
                PartyDataByInvoiceNumber x =dispatchMastService.getPChallanPartyWithQualityDispatchBy(id);

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


    //preview repsonse of FE api by party with pchallan detail
    @PostMapping("/dispatch/get/receipt/getPartyWithQualityDispatchByPChallanAndStock/")
    public ResponseEntity<GeneralResponse<PartyDataByInvoiceNumber,Object>> getPartyWithQualityDispatchByPChallanAndStock(@RequestBody CreateDispatch createDispatch) throws Exception{
        GeneralResponse<PartyDataByInvoiceNumber,Object> result;
        try{
            if(createDispatch!=null) {
                PartyDataByInvoiceNumber x =dispatchMastService.getPartyWithQualityDispatchByPChallanAndStockId(createDispatch);

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


    //get dipatch to update dispatch by invoice number
    @GetMapping("/dispatch/getDispatchWithPChallan/byInvoiceNumber/{invoiceNo}")
    public ResponseEntity<GeneralResponse<PartyWithBatchByInvoice,Object>> getDispatchForUpdateByInvoiceNumber(@PathVariable(name="invoiceNo") String invoiceNo) throws Exception{
        GeneralResponse<PartyWithBatchByInvoice,Object> result;
        try{
            if(invoiceNo!=null) {
                PartyWithBatchByInvoice x =dispatchMastService.getDispatchForUpdatePChallanByInvoiceNumber(invoiceNo);
                if(x!=null)
                    result = new GeneralResponse<>(x, constantFile.Dispatch_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                else
                    result = new GeneralResponse<>(x, constantFile.Dispatch_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
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

    @PostMapping("/dispatch/getDispatchMastData")
    public ResponseEntity<GeneralResponse<FilterResponse<DispatchMast>, Object>> getDispatchData(@RequestBody PaginatedData data){
        GeneralResponse<FilterResponse<DispatchMast>,Object> result;
        try{
System.out.println("entered dispatch/getDispatchMastData");
            FilterResponse<DispatchMast> x = filterService.getpaginatedSortedFilteredData(data);
            if(!x.getData().isEmpty())
            result = new GeneralResponse<FilterResponse<DispatchMast>, Object>(x, constantFile.Dispatch_Mast_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            else
                result = new GeneralResponse<FilterResponse<DispatchMast>, Object>(x, constantFile.Dispatch_Mast_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            // logService.saveLog(result,request,debugAll);
        } catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
}
 
@PostMapping("/getOperationsForGivenDataType")
public ResponseEntity<GeneralResponse<List<String>, Object>> getOpeartionList(@RequestParam(name="dataType") String dataType) throws Exception{
    GeneralResponse<List<String>,Object> result;
    List<String> x=new ArrayList<String>();
    try{
        switch(dataType){
            case "String":
            x=Arrays.asList(new String[]{"EQUALS", "LIKE","NOT_EQUALS","START_WITH","END_WITH"});
            break;
            
            case "Number":
            x=Arrays.asList(new String[]{"GREATER_THAN", "LESS_THAN","EQUALS","NOT_EQUALS"});
            break;
            
            case "Date":
            x=Arrays.asList(new String[]{"IN_RANGE", "NOT_EQUALS","EQUALS","GREATER_THAN", "LESS_THAN"});
            break;
            
        }

        
        if(!x.isEmpty())
        result = new GeneralResponse<List<String>, Object>(x, constantFile.Dispatch_Mast_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
        else
            result = new GeneralResponse<List<String>, Object>(x, constantFile.Dispatch_Mast_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

        // logService.saveLog(result,request,debugAll);
    } catch (Exception e){
        e.printStackTrace();
        result = new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
        logService.saveLog(result,request,true);
    }
    return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
}

}
