package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.purchaseOrder.PurchaseOrder;
import com.main.glory.model.purchaseOrder.ResponsePurchase;
import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.servicesImpl.PurchaseOrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PurchaseOrderController extends ControllerConfig {

    @Autowired
    PurchaseOrderImpl purchaseOrderService;


    @PostMapping("/purchaseOrder/")
    public GeneralResponse<Boolean> addPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){
        try{
            Boolean flag = purchaseOrderService.addPurchaseOrder(purchaseOrder);
            if(flag) {
                return new GeneralResponse<Boolean>(true, "Order added successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                return new GeneralResponse<Boolean>(false, "Invalid Data Sent", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/purchaseOrder/all")
    public GeneralResponse<List<ResponsePurchase>> getAllPurchaseOrder(){
        try{
            List<ResponsePurchase> purchaseOrderList = purchaseOrderService.getAllPurchaseOrder();
            if(purchaseOrderList.isEmpty())
            {
                return new GeneralResponse<>(null, "Order not added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            return new GeneralResponse<>(purchaseOrderList, "Order fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/purchaseOrder/update/")
    public GeneralResponse<Boolean> updatePurchaseOrder(@RequestBody PurchaseOrder purchaseOrder){
        try{
            purchaseOrderService.updatePurchaseOrder(purchaseOrder);
            return new GeneralResponse<>(true, "Order updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        } catch (Exception e) {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
    }



}
