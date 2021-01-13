package com.main.glory.servicesImpl;

import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.Dao.paymentTerm.AdvancePaymentDao;
import com.main.glory.Dao.paymentTerm.PaymentDataDao;
import com.main.glory.Dao.paymentTerm.PaymentMastDao;
import com.main.glory.model.PaymentMast;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.request.PartyDataByInvoiceNumber;
import com.main.glory.model.dispatch.request.PartyWithBatchByInvoice;
import com.main.glory.model.dispatch.request.QualityBillByInvoiceNumber;
import com.main.glory.model.party.Party;
import com.main.glory.model.paymentTerm.AdvancePayment;
import com.main.glory.model.paymentTerm.PaymentData;
import com.main.glory.model.paymentTerm.request.AddPaymentMast;
import com.main.glory.model.paymentTerm.request.GetAdvancePayment;
import com.main.glory.model.paymentTerm.request.GetPendingDispatch;
import com.main.glory.model.paymentTerm.request.PendingInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("paymentServiceImp")
public class PaymentTermImpl {

    @Autowired
    PaymentMastDao paymentMastDao;

    @Autowired
    PaymentDataDao paymentDataDao;

    @Autowired
    DispatchMastDao dispatchMastDao;

    @Autowired
    DispatchMastImpl dispatchMastService;

    @Autowired
    DispatchDataDao dispatchDataDao;

    @Autowired
    PartyServiceImp partyServiceImp;

    @Autowired
    AdvancePaymentDao advancePaymentDao;

    public Boolean savePayment(AddPaymentMast paymentMast) throws Exception {

        //paymentMastDao.save(paymentMast);
        if(!paymentMast.getAmtToPay().equals(paymentMast.getAmtPaid()))
            throw new Exception("please enter right amount");


        PaymentMast paymentMastToSave=new PaymentMast(paymentMast);

        paymentMastDao.save(paymentMastToSave);

        List<PaymentData> paymentDataList=new ArrayList<>();
        for(PaymentData paymentData:paymentMast.getPaymentData())
        {
            paymentData.setControlId(paymentMastToSave.getId());
            paymentDataList.add(paymentData);
        }

        paymentDataDao.saveAll(paymentDataList);


        //change the status of invoice and assign the bunchid to the
        for(PendingInvoice pendingInvoice:paymentMast.getInvoices())
        {
            DispatchMast dispatchMast = dispatchMastDao.getDataByInvoiceNumber(Long.parseLong(pendingInvoice.getInvoiceNo().substring(3)));
            if(dispatchMast!=null)
            {
                dispatchMast.setPaymentBunchId(paymentMastToSave.getId());
                dispatchMastDao.save(dispatchMast);
            }
        }


        return true;

    }

    public List<GetPendingDispatch> getPendingBillByPartyId(Long partyId) throws Exception {
        List<GetPendingDispatch> list=new ArrayList<>();

        List<DispatchMast> dispatchMastList = dispatchMastService.getPendingDispatchByPartyId(partyId);



        for(DispatchMast dispatchMast:dispatchMastList)
        {
            Double amt=0.0;

            PartyDataByInvoiceNumber data = dispatchMastService.getPartyWithQualityDispatchBy(dispatchMast.getPrefix()+dispatchMast.getPostfix());
            for(QualityBillByInvoiceNumber p:data.getQualityList())
            {
                amt+=p.getAmt();
            }
            GetPendingDispatch getPendingDispatch=new GetPendingDispatch();
            getPendingDispatch.setAmt(amt);
            getPendingDispatch.setDate(dispatchMast.getCreatedDate().toString());
            getPendingDispatch.setInvoicNo(dispatchMast.getPrefix()+dispatchMast.getPostfix());
            list.add(getPendingDispatch);

        }

        if(list.isEmpty())
            throw new Exception("no pending invoice found for party:"+partyId);
        return list;
    }

    public Boolean addAdvancePayment(AdvancePayment paymentMast) throws Exception {

        Party partyExist = partyServiceImp.getPartyDetailById(paymentMast.getPartyId());
        if(partyExist==null)
            throw new Exception("no data found for party:"+paymentMast.getPartyId());


        advancePaymentDao.save(paymentMast);
        return true;
    }

    public List<GetAdvancePayment> getAdvancePayment(Long partyId) throws Exception {
        List<GetAdvancePayment> list=new ArrayList<>();

        //check the party is exist or not

        Party partyExist=partyServiceImp.getPartyDetailById(partyId);
        if(partyExist==null)
            throw new Exception("no party found for id:"+partyId);



        List<AdvancePayment> advancePaymentList = advancePaymentDao.findAdvancePaymentByPartyId(partyId);
        if (advancePaymentList.isEmpty())
            throw new Exception("no advance payment found for party:"+partyId);

        for(AdvancePayment advancePayment:advancePaymentList)
        {
            list.add(new GetAdvancePayment(advancePayment,partyExist));
        }


        return list;

    }
}
