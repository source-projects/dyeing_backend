package com.main.glory.servicesImpl;

import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.Dao.paymentTerm.AdvancePaymentDao;
import com.main.glory.Dao.paymentTerm.PaymentDataDao;
import com.main.glory.Dao.paymentTerm.PaymentMastDao;
import com.main.glory.Dao.paymentTerm.PaymentTypeDao;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.paymentTerm.PaymentMast;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.request.PartyDataByInvoiceNumber;
import com.main.glory.model.dispatch.request.QualityBillByInvoiceNumber;
import com.main.glory.model.party.Party;
import com.main.glory.model.paymentTerm.AdvancePayment;
import com.main.glory.model.paymentTerm.GetAllPayment;
import com.main.glory.model.paymentTerm.PaymentData;
import com.main.glory.model.paymentTerm.PaymentType;
import com.main.glory.model.paymentTerm.request.*;
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
    PaymentTypeDao paymentTypeDao;
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
            PaymentType typeExist=paymentTypeDao.getPaymentTypeById(paymentData.getPayTypeId());
            if(typeExist==null)
                throw new Exception("no payment type found");

            paymentData.setControlId(paymentMastToSave.getId());
            paymentDataList.add(paymentData);
        }

        paymentDataDao.saveAll(paymentDataList);


        //change the status of invoice and assign the paymentBunchId to the
        for(PendingInvoice pendingInvoice:paymentMast.getInvoices())
        {
            DispatchMast dispatchMast = dispatchMastDao.getDataByInvoiceNumber(pendingInvoice.getInvoiceNo());
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

            PartyDataByInvoiceNumber data = dispatchMastService.checkInvoiceDataIsAvailable(dispatchMast.getPostfix());
            if(data==null)
                continue;
            for(QualityBillByInvoiceNumber p:data.getQualityList())
            {
                amt+=p.getAmt();
            }
            GetPendingDispatch getPendingDispatch=new GetPendingDispatch(dispatchMast);
            getPendingDispatch.setAmt(amt);
            getPendingDispatch.setDate(dispatchMast.getCreatedDate().toString());
            getPendingDispatch.setInvoicNo(dispatchMast.getPostfix());
            list.add(getPendingDispatch);

        }

      /*  if(list.isEmpty())
            throw new Exception("no pending invoice found for party:"+partyId);*/
        return list;
    }

    public Boolean addAdvancePayment(List<AdvancePayment> list) throws Exception {

        for (AdvancePayment paymentMast : list)
        {
        Party partyExist = partyServiceImp.getPartyDetailById(paymentMast.getPartyId());
        if(partyExist==null)
            throw new Exception("no data found for party:"+paymentMast.getPartyId());


        advancePaymentDao.save(paymentMast);

        }
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
            PaymentType paymentType = paymentTypeDao.getPaymentTypeById(advancePayment.getPayTypeId());
            if(paymentType==null)
                continue;
            list.add(new GetAdvancePayment(advancePayment,partyExist,paymentType));
        }


        return list;

    }

    public PaymentMast getPaymentDetailById(Long paymentBunchId) throws Exception {

        PaymentMast paymentMastExist=paymentMastDao.findByPaymentBunchId(paymentBunchId);
        if(paymentMastExist==null)
            throw new Exception("no data found for bunch id:"+paymentBunchId);


        return  paymentMastExist;
    }

    public Boolean savePaymentType(String type) throws Exception {

        PaymentType paymentTypeExist=paymentTypeDao.getPaymentTypeByName(type);
        if(paymentTypeExist!=null)
            throw new Exception(ConstantFile.ProductionType_Exist);
        PaymentType paymentType=new PaymentType(type);
        paymentTypeDao.save(paymentType);
        return true;
    }

    public List<PaymentType> getAllPaymentType() throws Exception {
        List<PaymentType> paymentTypeList = paymentTypeDao.getAllPaymentType();
       /* if(paymentTypeList.isEmpty())
            throw new Exception(CommonMessage.Payment_Not_Found);
        else*/
            return paymentTypeList;
    }

    public List<PaymentMast> getAllPaymentMast(Long partyId) throws Exception {
        List<PaymentMast> list = paymentMastDao.findByPartyId(partyId);
        if(list.isEmpty())
            throw new Exception(ConstantFile.Payment_Not_Found);
        return list;
    }

    public List<PaymentMast> getAllPaymentByPartyId(Long id) {
        List<PaymentMast> list = paymentMastDao.findByPartyId(id);
        return list;
    }

    public List<GetAllBank> getAllBankName() {
        List<String> getAllBanks =  paymentDataDao.getAllBankOfPaymentData();
        List<GetAllBank> getAllBankList = new ArrayList<>();
        getAllBanks.forEach(e->{
            getAllBankList.add(new GetAllBank(e));
        });
        return getAllBankList;
    }

    private List<GetAllBank> getAllBanksResponse(List<String> getAllBanks) {
        List<GetAllBank> getAllBankList = new ArrayList<>();

        List<String> getAllBankName=new ArrayList<>();
        getAllBanks.forEach(e->{
            if(!getAllBankName.contains(e))
            {
                getAllBankName.add(e);
                getAllBankList.add(new GetAllBank(e));
            }
        });
        return getAllBankList;
    }

    public List<GetAllPayment> getAllPaymentWithPartyName() {
        return paymentMastDao.getAllPaymentWithPartyName();
    }

    public List<GetAllBank> getAllAdvanceBankName() {

        List<String> getAllBanks =  advancePaymentDao.getAllBankOfAdvancePaymentData();
        List<GetAllBank> getAllBankList = new ArrayList<>();
        getAllBanks.forEach(e->{
            getAllBankList.add(new GetAllBank(e));
        });

        return getAllBankList;
    }

    public Double getTotalPendingAmtByPartyId(Long partyId) {
        return dispatchMastDao.getTotalPendingAmtByPartyId(partyId);
    }

    public DispatchMast getLastUnpaidDispatchByPartyId(Long id) {
        return dispatchMastDao.getLastPendingDispatchByPartyId(id);
    }
}
