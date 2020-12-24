package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.request.GetDispatchCompleteDetail;
import com.main.glory.model.dispatch.response.GetAllDispatch;
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

    @GetMapping("/dispatch/getBatchByParty/{partyId}")
    public GeneralResponse<List<GetBatchWithControlId>> getBatchByParty(@PathVariable(name="partyId") Long partyId) throws Exception{
        try{
            if(partyId!=null) {
                List<GetBatchWithControlId> x =dispatchMastService.getBatchByParty(partyId);
                return new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
                return new GeneralResponse<>(null,"party id can't be null", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

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




}
