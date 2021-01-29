package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;

import com.main.glory.model.PaymentMast;
import com.main.glory.model.paymentTerm.AdvancePayment;
import com.main.glory.model.paymentTerm.PaymentType;
import com.main.glory.model.paymentTerm.request.AddPaymentMast;
import com.main.glory.model.paymentTerm.request.GetAdvancePayment;
import com.main.glory.model.paymentTerm.request.GetPendingDispatch;
import com.main.glory.servicesImpl.PaymentTermImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentTermController extends ControllerConfig {

    @Autowired
    PaymentTermImpl paymentTermService;


    @PostMapping(value="/paymentTerm/")
    public GeneralResponse<Boolean> savePayment(@RequestBody AddPaymentMast paymentMast) {
        GeneralResponse<Boolean> result;
        try {
            Boolean flag = paymentTermService.savePayment(paymentMast);
            if (flag) {
                result = new GeneralResponse<Boolean>(true, "Payment Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            } else {
                result = new GeneralResponse<Boolean>(null, "Data Not added", true, System.currentTimeMillis(), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    //add payment type api
    @PostMapping(value="/paymentTerm/addPaymentType/{type}")
    public GeneralResponse<Boolean> savePaymentType(@PathVariable(name = "type") String type) {
        GeneralResponse<Boolean> result;
        try {
            Boolean flag = paymentTermService.savePaymentType(type);
            result = new GeneralResponse<Boolean>(true, "Payment Type Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }
    //add payment type api
    @GetMapping(value="/paymentTerm/getAllPaymentType")
    public GeneralResponse<List<PaymentType>> getAllPaymentType() {
        try {
            List<PaymentType> flag = paymentTermService.getAllPayemntType();
            return new GeneralResponse<>(flag, "Payment Type Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value="/paymentTerm/addAdvancePayment")
    public GeneralResponse<Boolean> addAdvancePayment(@RequestBody List<AdvancePayment> paymentMast) {
        try {
            Boolean flag = paymentTermService.addAdvancePayment(paymentMast);
            if (flag) {
                return new GeneralResponse<Boolean>(true, "Advance Payment Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            } else {
                return new GeneralResponse<Boolean>(null, "Data Not added", true, System.currentTimeMillis(), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }

    }


    //get the pending bill list
    @GetMapping(value="/paymentTerm/getPendingBillByPartyId/{partyId}")
    public GeneralResponse<List<GetPendingDispatch>> getPendingBillByPartyId(@PathVariable(name = "partyId") Long partyId)
    {
        try {
            if(partyId==null)
                throw new Exception("id can't be null");

            List<GetPendingDispatch> list = paymentTermService.getPendingBillByPartyId(partyId);
            if(!list.isEmpty())
                return new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            else return new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    //get the advance payment list
    @GetMapping(value="/paymentTerm/getAdvancePayment/{partyId}")
    public GeneralResponse<List<GetAdvancePayment>> getAdvancePayment(@PathVariable(name = "partyId") Long partyId)
    {
        try {
            if(partyId==null)
                throw new Exception("id can't be null");

            List<GetAdvancePayment> list = paymentTermService.getAdvancePayment(partyId);
            if(!list.isEmpty())
                return new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            else return new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    //get the payment detail by paymentBunchId
    @GetMapping(value="/paymentTerm/getPaymentDetailById/{paymentBunchId}")
    public GeneralResponse<PaymentMast> getPaymentDetailById(@PathVariable(name = "paymentBunchId") Long paymentBunchId)
    {
        try {
            if(paymentBunchId==null)
                throw new Exception("id can't be null");

            PaymentMast list = paymentTermService.getPaymentDetailById(paymentBunchId);
            if(list!=null)
                return new GeneralResponse<>(list, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
            else return new GeneralResponse<>(null, "no data found", false, System.currentTimeMillis(), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }




}
