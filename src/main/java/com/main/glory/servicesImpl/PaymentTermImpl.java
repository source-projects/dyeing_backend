package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.Dao.paymentTerm.AdvancePaymentDao;
import com.main.glory.Dao.paymentTerm.PaymentDataDao;
import com.main.glory.Dao.paymentTerm.PaymentMastDao;
import com.main.glory.Dao.paymentTerm.PaymentTypeDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.filters.Filter;
import com.main.glory.filters.FilterResponse;
import com.main.glory.filters.SpecificationManager;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.paymentTerm.PaymentMast;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.paymentTerm.AdvancePayment;
import com.main.glory.model.paymentTerm.response.GetAllPayment;
import com.main.glory.model.paymentTerm.PaymentData;
import com.main.glory.model.paymentTerm.PaymentType;
import com.main.glory.model.paymentTerm.request.*;
import com.main.glory.model.user.UserData;

import com.main.glory.services.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("paymentServiceImp")
public class PaymentTermImpl {


    @Autowired
    SpecificationManager<PaymentMast> specificationManager;

    @Autowired
    FilterService<PaymentMast, PaymentMastDao> filterService;

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

    @Autowired
    UserDao userDao;

    @Autowired
    PartyDao partyDao;

    public Boolean savePayment(AddPaymentMast paymentMast, String userId) throws Exception {

        UserData userData = userDao.getUserById(Long.parseLong(userId));

        if (userData == null)
            throw new Exception(ConstantFile.User_Not_Exist);

        if (paymentMast.getInvoices().size() <= 0)
            throw new Exception(ConstantFile.Select_Invoice_First);

        if (paymentMast.getAmtToPay() <= 0)
            throw new Exception(ConstantFile.Payment_Greater_Than_zero);

        //paymentMastDao.save(paymentMast);
        if (!paymentMast.getAmtToPay().equals(paymentMast.getAmtPaid()))
            throw new Exception(ConstantFile.Enter_Right_Amount);

        Party party = partyDao.findByPartyId(paymentMast.getPartyId());
        if(party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);

        UserData createdBy = userData;

        if (userData.getIsMaster() == false || userData.getUserHeadId().equals(0)) {
            createdBy = party.getCreatedBy();
        }

        PaymentMast paymentMastToSave = new PaymentMast(paymentMast, party, createdBy, createdBy);

        PaymentMast savePaymentMast = paymentMastDao.save(paymentMastToSave);

        List<PaymentData> paymentDataList = new ArrayList<>();
        for (PaymentData paymentData : paymentMast.getPaymentData()) {
            PaymentType typeExist = paymentTypeDao.getPaymentTypeById(paymentData.getPayTypeId());
            if (typeExist == null)
                throw new Exception("no payment type found");

            paymentData.setControlId(paymentMastToSave.getId());
            paymentDataList.add(paymentData);
        }

        paymentDataDao.saveAll(paymentDataList);


        //change the status of invoice and assign the paymentBunchId to the
        for (PendingInvoice pendingInvoice : paymentMast.getInvoices()) {
            DispatchMast dispatchMast = dispatchMastDao.getDataByInvoiceNumber(pendingInvoice.getInvoiceNo());
            if (dispatchMast != null) {
                dispatchMast.setPaymentBunchId(paymentMastToSave.getId());
                dispatchMastDao.save(dispatchMast);

            }
        }


        //update the advance payment if it is coming
        if (paymentMast.getAdvancePayList().size() > 0) {
            for (AdvancePaymentIdList record : paymentMast.getAdvancePayList()) {
                //check the record is exist or not
                Optional<AdvancePayment> addPaymentMast = advancePaymentDao.findById(record.getId());
                if (!addPaymentMast.isPresent())
                    throw new Exception(ConstantFile.Advance_Payment_Not_Found);

            }
            for (AdvancePaymentIdList record : paymentMast.getAdvancePayList()) {
                //check the record is exist or not
                Optional<AdvancePayment> addPaymentMast = advancePaymentDao.findById(record.getId());
                addPaymentMast.get().setPaymentBunchId(savePaymentMast.getId());
                advancePaymentDao.save(addPaymentMast.get());

            }
        }

        return true;

    }

    public List<GetPendingDispatch> getPendingBillByPartyId(Long partyId) throws Exception {
        //List<GetPendingDispatch> list=new ArrayList<>();

        List<GetPendingDispatch> dispatchMastList = dispatchMastService.getPendingDispatchResponseByPartyId(partyId);

//        for(DispatchMast dispatchMast:dispatchMastList)
//        {
//
//            Double amt=0.0;
//
//            PartyDataByInvoiceNumber data = dispatchMastService.checkInvoiceDataIsAvailable(dispatchMast.getPostfix());
//            if(data==null)
//                continue;
//            for(QualityBillByInvoiceNumber p:data.getQualityList())
//            {
//                amt+=p.getAmt();
//            }
//            GetPendingDispatch getPendingDispatch=new GetPendingDispatch(dispatchMast);
//            getPendingDispatch.setAmt(amt);
//            getPendingDispatch.setDate(dispatchMast.getCreatedDate().toString());
//            getPendingDispatch.setInvoicNo(dispatchMast.getPostfix());
//            list.add(getPendingDispatch);
//
//        }

      /*  if(list.isEmpty())
            throw new Exception("no pending invoice found for party:"+partyId);*/
        return dispatchMastList;
    }

    public Boolean addAdvancePayment(List<AdvancePayment> list) throws Exception {

        for (AdvancePayment paymentMast : list) {
            Party partyExist = partyServiceImp.getPartyById(paymentMast.getPartyId());
            if (partyExist == null)
                throw new Exception("no data found for party:" + paymentMast.getPartyId());


            if (paymentMast.getAmt() <= 0)
                throw new Exception(ConstantFile.Payment_Greater_Than_zero);
        }
        advancePaymentDao.saveAll(list);
        return true;
    }

    public List<GetAdvancePayment> getAdvancePayment(Long partyId) throws Exception {
        List<GetAdvancePayment> list = new ArrayList<>();

        //check the party is exist or not

        Party partyExist = partyServiceImp.getPartyById(partyId);
        if (partyExist == null)
            throw new Exception("no party found for id:" + partyId);


        List<AdvancePayment> advancePaymentList = advancePaymentDao.findAdvancePaymentByPartyId(partyId);
//        if (advancePaymentList.isEmpty())
//            throw new Exception("no advance payment found for party:"+partyId);

        for (AdvancePayment advancePayment : advancePaymentList) {
            PaymentType paymentType = paymentTypeDao.getPaymentTypeById(advancePayment.getPayTypeId());
            if (paymentType == null)
                continue;
            list.add(new GetAdvancePayment(advancePayment, partyExist, paymentType));
        }


        return list;

    }

    public AddPaymentMast getPaymentDetailById(Long paymentBunchId) throws Exception {

        Optional<PaymentMast> paymentMastExist = paymentMastDao.findByPayId(paymentBunchId);
        if (paymentMastExist.isEmpty())
            throw new Exception(ConstantFile.Payment_Not_Exist);

        AddPaymentMast addPaymentMast = new AddPaymentMast(paymentMastExist.get());

        //list of invoice which are connected with this paymentBunchId
        List<PendingInvoice> invoiceList = dispatchMastDao.getInvoiceListByPaymentBunchId(paymentBunchId);
        addPaymentMast.setInvoices(invoiceList);


        //getAdvance Payment related to this payment bunch id
        List<AdvancePaymentIdList> advancePaymentIdList = advancePaymentDao.getAdvancePaymentByPaymentBunchId(paymentBunchId);
        addPaymentMast.setAdvancePayList(advancePaymentIdList);

        return addPaymentMast;
    }

    public Boolean savePaymentType(String type) throws Exception {

        PaymentType paymentTypeExist = paymentTypeDao.getPaymentTypeByName(type);
        if (paymentTypeExist != null)
            throw new Exception(ConstantFile.ProductionType_Exist);
        PaymentType paymentType = new PaymentType(type);
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
        if (list.isEmpty())
            throw new Exception(ConstantFile.Payment_Not_Found);
        return list;
    }

    public List<PaymentMast> getAllPaymentByPartyId(Long id) {
        List<PaymentMast> list = paymentMastDao.findByPartyId(id);
        return list;
    }

    public List<GetAllBank> getAllBankName() {
        List<String> getAllBanks = paymentDataDao.getAllBankOfPaymentData();
        List<GetAllBank> getAllBankList = new ArrayList<>();
        getAllBanks.forEach(e -> {
            getAllBankList.add(new GetAllBank(e));
        });
        return getAllBankList;
    }

    private List<GetAllBank> getAllBanksResponse(List<String> getAllBanks) {
        List<GetAllBank> getAllBankList = new ArrayList<>();

        List<String> getAllBankName = new ArrayList<>();
        getAllBanks.forEach(e -> {
            if (!getAllBankName.contains(e)) {
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

        List<String> getAllBanks = advancePaymentDao.getAllBankOfAdvancePaymentData();
        List<GetAllBank> getAllBankList = new ArrayList<>();
        getAllBanks.forEach(e -> {
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

    public FilterResponse<GetAllPayment> getAllPaymentWithPartyNameWithPagination(GetBYPaginatedAndFiltered requestParam) {
        List<GetAllPayment> list = new ArrayList<>();
        Pageable pageable = filterService.getPageable(requestParam.getData());
        List<Filter> filtersParam = requestParam.getData().getParameters();
        HashMap<String, List<String>> subModelCase = new HashMap<String, List<String>>();
        Page queryResponse = null;
        Specification<PaymentMast> filterSpec = specificationManager.getSpecificationFromFilters(filtersParam,
                requestParam.getData().isAnd, subModelCase);
        subModelCase.put("partyName", new ArrayList<String>(Arrays.asList("party", "partyName")));
        queryResponse = paymentMastDao.findAll(filterSpec, pageable);

        List<PaymentMast> paymentMastList = queryResponse.getContent();
        paymentMastList.forEach(e -> {
            list.add(new GetAllPayment(e));
        });
        FilterResponse<GetAllPayment> response = new FilterResponse<GetAllPayment>(list,
                queryResponse.getNumber(), queryResponse.getNumberOfElements(), (int) queryResponse.getTotalElements());
        return response;

    }

    public Boolean deletePaymentById(Long id) {

        Optional<PaymentMast> paymentMast = paymentMastDao.findById(id);

        if (!paymentMast.isPresent())
            return false;

        paymentMastDao.deleteById(id);
        paymentDataDao.deleteByControlId(id);


        //update invoice because payment has been removed
        List<Long> dispatchMastIdList = dispatchMastDao.getDispatchMastIdsByPaymentBunchId(id);

        if (dispatchMastIdList.size() > 0) {
            //update and set null to paymentBunchId column
            dispatchMastDao.updateDispatchMastPaymentBunchIdByMastIdsAndPaymentBunchId(dispatchMastIdList, null);
        }


        return true;
    }

    public Boolean updatePayment(AddPaymentMast addPaymentMast, String id) throws Exception {
        //coming user
        UserData userData = userDao.getUserById(Long.parseLong(id));
        UserData createdBy = userDao.getUserById(addPaymentMast.getCreatedBy());
        UserData updatedBy = userData;
        Party party = partyDao.findByPartyId(addPaymentMast.getPartyId());

        if(party == null)
            throw new Exception(ConstantFile.Party_Not_Exist);

        if(userData==null || createdBy==null)
            throw new Exception(ConstantFile.User_Not_Exist);

        if(userData.getUserHeadId()==0 || userData.getIsMaster()==false)
        {
            updatedBy = party.getCreatedBy();
        }


        //check weather the data is exist or not
        PaymentMast paymentMastExist = paymentMastDao.getPaymentMastById(addPaymentMast.getId());
        if(paymentMastExist == null)
            return false;


        //check existing invoices
        List<String> existingInvoiceNo = dispatchMastDao.getInvoiceNoListByPaymentBunchId(addPaymentMast.getId());

        //hashMapfor existing invoiceno
        //true means keep the record
        //false means remove the record
        Map<String,Boolean> existingInvoiceNoMap = new HashMap<>();
        //intialize with false
        existingInvoiceNo.forEach(e->{
                existingInvoiceNoMap.put(e,false);
        });


        for (PendingInvoice invoice : addPaymentMast.getInvoices()) {
            //check coming invoice number is exist or not

            DispatchMast dispatchMastExist = dispatchMastDao.getDispatchMastByInvoiceNo(invoice.getInvoiceNo());

            if(dispatchMastExist == null)
                throw new Exception(ConstantFile.Dispatch_Not_Exist);

            //make true for every invoice no, else default is false
            existingInvoiceNoMap.put(invoice.getInvoiceNo(),true);
        }


        List<Long> listOfAdvancePaymentId = advancePaymentDao.getAdvancePaymentIdListByPaymentBunchId(addPaymentMast.getId());
        //check for advance payment
        Map<Long,Boolean> existingAdvancePaymentHashMap = new HashMap<>();
        listOfAdvancePaymentId.forEach(e-> {
            //store default as false
            existingAdvancePaymentHashMap.put(e,false);
        });


        //check coming advance payment is exist or not and update hash mapp as well
        //true mean keep the record or create it, false mean remove the record

        for (AdvancePaymentIdList advancePaymentIdList : addPaymentMast.getAdvancePayList()) {
            Optional<AdvancePayment> advancePaymentExist = advancePaymentDao.findById(advancePaymentIdList.getId());

            if(advancePaymentExist.isEmpty())
                throw new Exception(ConstantFile.Advance_Payment_Not_Exist);

            existingAdvancePaymentHashMap.put(advancePaymentExist.get().getId(),true);

        }


        //update advance payment respect to flags
        for (Map.Entry<Long, Boolean> entry : existingAdvancePaymentHashMap.entrySet()) {
            if(entry.getValue() == true)
            {
                advancePaymentDao.updateAdvancePaymentBunchIdById(entry.getKey(),addPaymentMast.getId());
            }
            else if(entry.getValue() == false){
                advancePaymentDao.updateAdvancePaymentBunchIdById(entry.getKey(),null);
            }
        }
        //update invoice number respect to given hashmap
        for (Map.Entry<String, Boolean> entry : existingInvoiceNoMap.entrySet()) {
            if(entry.getValue() == true)
            {
                dispatchMastDao.updateDispatchMastPaymentBunchIdByInvoiceNumberWithPaymentBunchId(entry.getKey(),addPaymentMast.getId());
            }
            else if(entry.getValue() == false){
                dispatchMastDao.updateDispatchMastPaymentBunchIdByInvoiceNumberWithPaymentBunchId(entry.getKey(),null);
            }
        }


        paymentMastExist = new PaymentMast(addPaymentMast,party,createdBy,updatedBy);
        paymentMastExist.setPaymentData(addPaymentMast.getPaymentData());

        paymentMastDao.save(paymentMastExist);

        //remove the payment data jiska control id null hai
        paymentDataDao.deletePaymentDataWhoControlIdIsNull();
        return true;
    }
}
