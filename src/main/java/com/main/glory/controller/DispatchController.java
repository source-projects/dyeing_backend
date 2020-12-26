package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.dispatch.request.CreateDispatch;
import com.main.glory.model.dispatch.request.UpdateInvoice;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.servicesImpl.DispatchMastImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DispatchController extends ControllerConfig {


    @Autowired
    DispatchMastImpl dispatchMastService;

    @PostMapping("/dispatch")
    public GeneralResponse<Boolean> createDispatch(@RequestBody List<CreateDispatch> dispatchMast) throws Exception{
        try{
            Boolean flag = dispatchMastService.saveDispatch(dispatchMast);
            if(flag==true)
                return new GeneralResponse<>(true,"Invoice data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<>(false,"Invoice not created", false, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    //Get batches by the party id
    @GetMapping("/dispatch/getBatchByParty/{partyId}")
    public GeneralResponse<List<BatchWithTotalMTRandFinishMTR>> getBatchByParty(@PathVariable(name="partyId") Long partyId) throws Exception{
        try{
            if(partyId!=null) {
                List<BatchWithTotalMTRandFinishMTR> x =dispatchMastService.getBatchByParty(partyId);
                return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
                return new GeneralResponse<>(null,"party id can't be null", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    //Get dispatch by invoice number
    @GetMapping("/getDispatch/byInvoiceNumber/{invoiceNo}")
    public GeneralResponse<List<GetBatchByInvoice>> getDispatchByInvoiceNumber(@PathVariable(name="invoiceNo") String invoiceNo) throws Exception{
        try{
            if(invoiceNo!=null) {
                List<GetBatchByInvoice> x =dispatchMastService.getDispatchByInvoiceNumber(invoiceNo);
                return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
                return new GeneralResponse<>(null,"party id can't be null", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/dispatch/getAll")
    public GeneralResponse<List<GetAllDispatch>> getAllDispatch() throws Exception{
        try{

            List<GetAllDispatch> x =dispatchMastService.getAllDisptach();
            return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateDispatch/")
    public GeneralResponse<Boolean> updateDispatch(@RequestBody UpdateInvoice updateInvoice) throws Exception{
        try{

            Boolean flag = dispatchMastService.updateDispatch(updateInvoice);
            if(flag==true)
            return new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            else
                return new GeneralResponse<>(false, "data not updated", true, System.currentTimeMillis(), HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
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
            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
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
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
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
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
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
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dispatch/getAll")
    public GeneralResponse<List<GetAllDispatch>> getAllDispatch() throws Exception{
        try{

            List<GetAllDispatch> x =dispatchMastService.getAllDisptach();
            return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
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
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


*/

}
