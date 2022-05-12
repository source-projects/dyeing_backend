package com.main.glory.servicesImpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.filters.Filter;
import com.main.glory.filters.FilterResponse;
import com.main.glory.filters.QueryOperator;
import com.main.glory.filters.SpecificationManager;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.BatchDetail;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.document.request.GetDocumentModel;
import com.main.glory.model.party.Party;
import com.main.glory.model.party.PartyWithMasterName;
import com.main.glory.model.party.request.*;
import com.main.glory.model.paymentTerm.PaymentMast;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.response.QualityWithDetail;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import com.main.glory.services.FilterService;
import com.main.glory.services.PartyServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Service("partyServiceImp")
public class PartyServiceImp implements PartyServiceInterface {

    @Autowired
    DocumentImpl documentService;

    @Autowired
    QualityNameDao qualityNameDao;

    @Autowired
    DispatchMastImpl dispatchMast;

    @Autowired
    private PartyDao partyDao;

    @Autowired
    ProductionPlanImpl productionPlanService;

    @Autowired
    QualityServiceImp qualityServiceImp;

    @Autowired
    PaymentTermImpl paymentTermService;
    @Autowired
    ShadeServiceImpl shadeService;
    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    SpecificationManager<Party> specificationManager;

    @Autowired
    FilterService<Party, PartyDao> filterService;

    @Autowired
    UserDao userDao;

    @Autowired
    ModelMapper modelMapper;

    public void saveParty(AddParty party, String id) throws Exception {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        if (party != null) {

            UserData createdBy = userDao.getUserById(Long.parseLong(id));
            UserData userHeadData = userDao.getUserById(party.getUserHeadId());
            if(userHeadData==null)
            throw new Exception(ConstantFile.Party_Not_Found_ByMaster);

            party.setCreatedDate(new Date(System.currentTimeMillis()));
            party.setUpdatedDate(new Date(System.currentTimeMillis()));
            Party partyData = new Party(party, userHeadData, createdBy, createdBy);

            if (party.getGSTIN() == null || party.getGSTIN().isEmpty()) {

                if (party.getPartyCode().length() < 2 || party.getPartyCode().length() > 5)
                    throw new Exception(ConstantFile.Party_Code_Less);

                Party partyExistWithName = partyDao.getPartyByName(party.getPartyName());
                if (partyExistWithName != null)
                    throw new Exception(ConstantFile.Party_Exist);

                partyDao.save(partyData);
                return;
            } else {

                if (party.getPartyCode().length() < 2 || party.getPartyCode().length() > 5)
                    throw new Exception(ConstantFile.Party_Code_Less);

                Party gstAvailable = partyDao.findByGSTIN(party.getGSTIN());
                /* Party partyCodeAvailable = partyDao.findByPartyCode(party.getPartyCode()); */

                if (gstAvailable != null)
                    throw new Exception("GST No." + party.getGSTIN() + " is already exist");

                /*
                 * if (partyCodeAvailable != null) throw new
                 * Exception(ConstantFile.Party_Code_Exist + party.getPartyCode());
                 */
                // check the partyname exist

                Party partyExistWithName = partyDao.getPartyByName(party.getPartyName());
                if (partyExistWithName != null)
                    throw new Exception("party name is already exist");

                partyDao.save(partyData);

            }
        }
    }

    @Override
    public List<PartyWithMasterName> getAllPartyDetails(Long id, String getBy) throws Exception {
        List<PartyWithMasterName> partyDetailsList = null;
        if (id == null) {
            partyDetailsList = partyDao.getAllParty();
        } else if (getBy.equals("group")) {
            UserData userData = userDao.findUserById(id);

            if (userData.getUserHeadId() == 0) {
                // fr admin
                partyDetailsList = partyDao.getAllParty();
            } else if (userData.getUserHeadId().equals(userData.getId())) {
                // master user
                partyDetailsList = partyDao.findByCreatedByAndUserHeadId(id, id);
            } else {
                UserData opratorUsr = userDao.getUserById(id);
                partyDetailsList = partyDao.findByUserHeadId(opratorUsr.getUserHeadId());
            }

        } else if (getBy.equals("own")) {
            partyDetailsList = partyDao.findByCreatedBy(id);
        }

        return partyDetailsList;
    }

    public FilterResponse<PartyWithMasterName> getAllPartyDetailsPaginated(GetBYPaginatedAndFiltered requestParam,
            String id) throws Exception {
        List<PartyWithMasterName> partyDetailsList = null;
        String getBy = requestParam.getGetBy();
        Pageable pageable = filterService.getPageable(requestParam.getData());
        List<Filter> filtersParam = requestParam.getData().getParameters();
        HashMap<String, List<String>> subModelCase = new HashMap<String, List<String>>();
        subModelCase.put("userHeadId", new ArrayList<String>(Arrays.asList("userHeadData", "id")));
        subModelCase.put("createdBy", new ArrayList<String>(Arrays.asList("createdBy", "id")));
        subModelCase.put("userHeadName", new ArrayList<String>(Arrays.asList("userHeadData", "userName")));
        subModelCase.put("createdByName", new ArrayList<String>(Arrays.asList("createdBy", "userName")));
        Page queryResponse = null;
        Specification<Party> filterSpec = specificationManager.getSpecificationFromFilters(filtersParam,
                requestParam.getData().isAnd, subModelCase);

        if (id == null || getBy.equals("all")) {

            queryResponse = partyDao.findAll(filterSpec, pageable);

        } else if (getBy.equals("group")) {
            UserData userData = userDao.findUserById(Long.parseLong(id));

            if (userData.getUserHeadId() == 0) {
                // fr admin
                queryResponse = partyDao.findAll(filterSpec, pageable);

            } else if (userData.getUserHeadId().equals(userData.getId())) {
                // master user
                List<Filter> filters = new ArrayList<Filter>();
                filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")), QueryOperator.EQUALS, id));
                filters.add(new Filter(new ArrayList<String>(Arrays.asList("userHeadId")), QueryOperator.EQUALS, id));
                Specification<Party> spec = specificationManager.getSpecificationFromFilters(filters, false,
                        subModelCase);
                spec = spec.and(filterSpec);
                queryResponse = partyDao.findAll(spec, pageable);

            } else {
                UserData opratorUsr = userDao.getUserById(Long.parseLong(id));
                List<Filter> filters = new ArrayList<Filter>();
                filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")), QueryOperator.EQUALS, id));
                Specification<Party> spec = specificationManager.getSpecificationFromFilters(filters, true,
                        subModelCase);
                spec = spec.and(filterSpec);
                queryResponse = partyDao.findAll(spec, pageable);

                partyDetailsList = partyDao.findByUserHeadId(opratorUsr.getUserHeadId());
            }

        } else if (getBy.equals("own")) {
            UserData userData = userDao.findUserById(Long.parseLong(id));

            if (userData.getUserHeadId() == 0) {
                queryResponse = partyDao.findAll(filterSpec, pageable);
            } else {
                List<Filter> filters = new ArrayList<Filter>();

                filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")), QueryOperator.EQUALS, id));

                Specification<Party> spec = specificationManager.getSpecificationFromFilters(filters, true,
                        subModelCase);
                spec = spec.and(filterSpec);

                queryResponse = partyDao.findAll(spec, pageable);

            }
        }

        partyDetailsList = queryResponse.getContent();
        FilterResponse<PartyWithMasterName> response = new FilterResponse<PartyWithMasterName>(partyDetailsList,
                queryResponse.getNumber(), queryResponse.getNumberOfElements(), (int) queryResponse.getTotalElements());
        return response;

    }

    @Override
    public PartyWithUserHeadName getPartyDetailById(Long id) throws Exception {
        PartyWithUserHeadName partyData = partyDao.findPartyWithUserHeadById(id);
        if (partyData == null)
            throw new Exception(ConstantFile.Party_Not_Found);
        else
            return partyData;
    }

    public boolean editPartyDetails(AddParty addParty) throws Exception {

        UserData userHeadData = userDao.getUserById(addParty.getUserHeadId());
        UserData createdBy = userDao.getUserById(addParty.getCreatedBy());
        UserData updatedBy = userDao.getUserById(addParty.getUpdatedBy());
        addParty.setUpdatedDate(new Date(System.currentTimeMillis()));
        Party party = new Party(addParty, userHeadData, createdBy, updatedBy);
        party.setId(addParty.getId());

        var partyIndex = partyDao.findById(party.getId());
        //Party party1 = partyDao.findByPartyCodeExceptId(party.getPartyCode(), party.getId());

        if (!partyIndex.isPresent())
            throw new Exception("Party dat  a not found for id:" + party.getId());

        Party party1 =partyIndex.get();
        party.setCreatedBy(partyIndex.get().getCreatedBy());
        /*
         * if (party1!=null) throw new Exception("Party code should be unique");
         */

        // party code length exception
        if (party.getPartyCode().length() < 2 || party.getPartyCode().length() > 5)
            throw new Exception(ConstantFile.Party_Code_Less);

        if (party.getGSTIN() == null || party.getGSTIN().isEmpty()) {
            partyDao.saveAndFlush(party);
            return true;
        }

        party1 = partyDao.findByGSTIN(party.getGSTIN());

        if (party1 != null && party1.getId() != party.getId())
            throw new Exception("GST is already availble");


//        Double totalPendingAmt = paymentTermService.getTotalPendingAmtByPartyId(party.getId());
//        if(party.getCreditLimit() < totalPendingAmt)
//        throw new Exception(ConstantFile.CreditLimit_Can_Not_Be_Less_than_Pending_Amt);
        if (!partyIndex.isPresent())
            return false;
        else
            partyDao.save(party);
        return true;
    }

    @Override
    public boolean deletePartyById(Long id) throws Exception {

        Party partyIndex = partyDao.findByPartyId(id);
        if (partyIndex == null)
            return false;
        else {
            // check the record in sub table that are avialble or not
            List<DispatchMast> dispatchMastList = dispatchMast.getDispatchByPartyId(id);
            if (!dispatchMastList.isEmpty())
                throw new Exception(ConstantFile.Dispatch_Exit);

            List<Quality> qualityList = qualityServiceImp.getqualityListByPartyId(id);
            if (!qualityList.isEmpty())
                throw new Exception(ConstantFile.Quality_Data_Exist);

            List<PaymentMast> paymentMastList = paymentTermService.getAllPaymentByPartyId(id);
            if (!paymentMastList.isEmpty())
                throw new Exception(ConstantFile.Payment_Exist);

            List<ShadeMast> shadeMastList = shadeService.getShadeByPartyId(id);
            if (!shadeMastList.isEmpty())
                throw new Exception(ConstantFile.Shade_Exist);

            List<ProductionPlan> productionPlans = productionPlanService.getAllProductinByPartyId(id);
            if (!productionPlans.isEmpty())
                throw new Exception(ConstantFile.Production_Record_Exist);

            List<StockMast> stockMastList = stockBatchService.getAllStockByPartyId(id);
            if (!stockMastList.isEmpty())
                throw new Exception(ConstantFile.StockBatch_Exist);

            partyDao.deleteById(id);

        }
        return true;
    }

    @Override
    public String getPartyNameByPartyId(Long partyId) {
        String partyName = partyDao.getPartyNameByPartyId(partyId);
        return partyName;
    }

    public List<PartyWithName> getAllPartyNameWithId() {
        try {
            List<Party> partyAll = partyDao.getAllPartyDetail();

            List<PartyWithName> partyWithNameList = new ArrayList<>();
            if (!partyAll.isEmpty()) {
                partyAll.forEach(e -> {
                    PartyWithName partyWithName = new PartyWithName(e);
                    // System.out.println(partyWithName.getId());
                    partyWithNameList.add(partyWithName);
                });
            }
            return partyWithNameList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PartyWithPartyCode> getAllPartyNameWithPartyCode() throws Exception {
        List<PartyWithPartyCode> partyWithPartyCodesList = new ArrayList<>();
        List<Party> partyList = partyDao.getAllPartyDetail();
        for (Party party : partyList) {
            if (party.getPartyCode() != null) {
                PartyWithPartyCode partyWithPartyCode = new PartyWithPartyCode(party);
                partyWithPartyCodesList.add(partyWithPartyCode);

            }
        }
        if (partyWithPartyCodesList.isEmpty())
            throw new Exception("Data not added yet");

        return partyWithPartyCodesList;
    }

    public Boolean partyCodeExistOrNot(String partyCode, Long id) {

        // if the id is null then it is insert request and check the name in entire
        // records
        // else it is update request
        if (id == null) {
            Party party = partyDao.findByPartyCode(partyCode);

            if (party == null)
                return true;

            else
                return false;
        } else {
            Party party = partyDao.findByPartyCodeExceptId(partyCode, id);

            if (party == null)
                return true;

            else
                return false;
        }

    }

    // send pdf for mail
    public void sendPdfForParty(GetDocumentModel documentModel) throws Exception {
        String fileName = "party.pdf";
        File f = new File(fileName);
        f.createNewFile();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));// file is created, where the project folder is
        document.open();

        // Add the data
        PdfPTable table = new PdfPTable(5);
        PdfPCell partyName = new PdfPCell(new Phrase("Party Name"));
        table.addCell(partyName);
        PdfPCell partyAddress = new PdfPCell(new Phrase("Address"));
        table.addCell(partyAddress);
        PdfPCell partyContact = new PdfPCell(new Phrase("Contact no"));
        table.addCell(partyContact);
        PdfPCell partyCity = new PdfPCell(new Phrase("City"));
        table.addCell(partyCity);
        PdfPCell partyState = new PdfPCell(new Phrase("State"));
        table.addCell(partyState);
        table.setHeaderRows(1);

        List<Party> party = partyDao.getAllPartyDetail();

        if (documentModel.getToRow() > party.size())
            throw new Exception("Party size is :" + party.size());

        for (int i = documentModel.getFromRow().intValue() - 1; i < (documentModel.getToRow()); i++) {

            // System.out.println(party.get(i).getPartyName());
            table.addCell(party.get(i).getPartyName());
            table.addCell(party.get(i).getPartyAddress1());
            table.addCell(party.get(i).getContactNo());
            table.addCell(party.get(i).getCity());
            table.addCell(party.get(i).getCity());

        }

        document.add(table);
        document.close();

        // ______Document created successfully

        // Send mail
        documentService.sendMail(documentModel, fileName, f);

    }

    public Party getPartyById(Long id) {
        Party party = partyDao.findByPartyId(id);
        return party;

    }

    public List<PartyWithName> getAllPartyNameWithHeaderId(String id) {
        try {

            Long userId = Long.parseLong(id);

            UserData userData = userDao.getUserById(userId);
            Long userHeadId = null;

            UserPermission userPermission = userData.getUserPermissionData();

            List<Party> partyAll = null;
            Permissions permissions = new Permissions(userPermission.getPa().intValue());
            if (permissions.getViewAll()) {
                userId = null;
                userHeadId = null;
                partyAll = partyDao.getAllPartyDetail();
            } else if (permissions.getViewGroup()) {
                // check the user is master or not ?
                // admin
                if (userData.getUserHeadId() == 0) {
                    userId = null;
                    userHeadId = null;
                    partyAll = partyDao.getAllPartyDetail();
                } else if (userData.getUserHeadId() > 0) {
                    // for master or operator
                    UserData userHead = userDao.getUserById(userData.getUserHeadId());
                    userId = userData.getId();
                    userHeadId = userHead.getId();
                    partyAll = partyDao.getAllPartyByCreatedAndHead(userId, userHeadId);

                }

            } else if (permissions.getView()) {
                userId = userData.getId();
                userHeadId = null;

                partyAll = partyDao.getAllPartyByCreatedBy(userId);
            }

            List<PartyWithName> partyWithNameList = new ArrayList<>();
            if (!partyAll.isEmpty()) {
                partyAll.forEach(e -> {
                    Double pendingAmt = paymentTermService.getTotalPendingAmtByPartyId(e.getId());
                    PartyWithName partyWithName = new PartyWithName(e, pendingAmt);
                    // System.out.println(partyWithName.getId());
                    partyWithNameList.add(partyWithName);
                });
            }
            return partyWithNameList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean isPartyNameIsExist(String name, Long id) {
        // if id is null then it is update request
        // if id is not null then it is add request

        if (id == null) {
            Party party = partyDao.getPartyByName(name);
            if (party == null) {
                return true;
            } else
                return false;

        } else {

            // check the name except the given id
            Party party = partyDao.getPartyByNameExceptId(name, id);
            if (party == null)
                return true;
            else
                return false;

        }

    }

    public PartyReport getPartyReportById(Long id, Long qualityId) throws Exception {
        Party party = partyDao.findByPartyId(id);
        if (party == null)
            throw new Exception(ConstantFile.Party_Not_Found);

        List<QualityWithDetail> qualityWithDetailList = new ArrayList<>();

        PartyReport partyReport = new PartyReport(party);

        List<BatchDetail> batchDetailList = new ArrayList<>();

        // if quality id is not coming then get all quality for the given party
        if (qualityId == null) {
            List<Quality> qualityList = qualityServiceImp.getqualityListByPartyId(party.getId());
            for (Quality quality : qualityList) {
                List<BatchDetail> list = stockBatchService.getBatchDetailForReport(party.getId(), quality.getId());
                if (list == null || list.isEmpty())
                    continue;
                for (BatchDetail batchDetail : list) {
                    batchDetailList.add(batchDetail);
                }

               QualityName qualityName=quality.getQualityName();
                if (qualityName!=null)
                    qualityWithDetailList.add(new QualityWithDetail(qualityName, stockBatchService
                            .getAvailableStockValueByPartyIdWithQualityEntryId(party.getId(), qualityId)));

            }
            /*
             * if(batchDetailList.isEmpty()) throw new Exception("no batch found");
             */

            partyReport.setBatchDetailList(batchDetailList);
        } else {
            // specific quality record

            Quality qualityExist = qualityServiceImp.getQualityByEntryId(qualityId);
            if (qualityExist == null)
                throw new Exception(ConstantFile.Quality_Data_Not_Found);

            List<BatchDetail> list = stockBatchService.getBatchDetailForReport(party.getId(), qualityId);
            /*
             * if(list.isEmpty()) throw new Exception("no record found");
             */
            for (BatchDetail batchDetail : list) {
                batchDetailList.add(batchDetail);
            }
            partyReport.setBatchDetailList(batchDetailList);

            QualityName qualityName = qualityExist.getQualityName();
            if (qualityName!=null)
                qualityWithDetailList.add(new QualityWithDetail(qualityName,
                        stockBatchService.getAvailableStockValueByPartyIdWithQualityEntryId(party.getId(), qualityId)));

        }

        // get the more information for analysis
        Double pendingAmt = paymentTermService.getTotalPendingAmtByPartyId(party.getId());
        // last unpaid dispatch
        DispatchMast dispatchMast = paymentTermService.getLastUnpaidDispatchByPartyId(party.getId());
        // get the available stock value by quality name rate * stock batch mtr
        Double stockAvailable = stockBatchService.getAvailableStockValueByPartyIdWithQualityEntryId(party.getId(),
                null);

        // set the values
        partyReport.setLastDispatch(dispatchMast);
        partyReport.setPendingAmt(pendingAmt == null ? 0 : pendingAmt);
        partyReport.setAvailableStockValue(stockAvailable == null ? 0 : stockAvailable);
        partyReport.setQualityWithDetailList(qualityWithDetailList);
        return partyReport;
    }

    public List<Party> getPartyByCreatedAndUserHeadId(Long id) {
        return partyDao.getAllPartyByCreatedAndHead(id, id);
    }

    public Party getPartyByStockId(Long controlId) {
        return partyDao.findPartyByStockId(controlId);
    }

    @Override
    public boolean editPartyDetails(Party pary) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }
}
