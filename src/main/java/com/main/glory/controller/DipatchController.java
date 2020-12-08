package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.servicesImpl.DispatchMastImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DipatchController extends ControllerConfig {


    @Autowired
    DispatchMastImpl dispatchMastService;

    @PostMapping("/dipatch")
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
//
//    @GetMapping("/dipatch/{id}")
//    public GeneralResponse<Boolean> getDispatch(@PathVariable(name="id") Long id) throws Exception{
//        try{
//            if(id!=null) {
//                dispatchMastService.getInvoiceById(id);
//                return new GeneralResponse<>(true, "Invoice created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
//            }
//            else
//                return new GeneralResponse<>(false,"Invoice not created", false, System.currentTimeMillis(), HttpStatus.OK);
//        } catch (Exception e){
//            e.printStackTrace();
//            return new GeneralResponse<>(false,e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
//        }
//    }

}
