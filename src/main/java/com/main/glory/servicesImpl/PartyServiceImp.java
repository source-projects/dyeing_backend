package com.main.glory.servicesImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.PaymentMast;
import com.main.glory.model.SendEmail;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.document.request.GetDocumentModel;
import com.main.glory.model.document.request.ToEmailList;
import com.main.glory.model.party.request.AddParty;
import com.main.glory.model.party.request.PartyWithName;
import com.main.glory.model.party.request.PartyWithPartyCode;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import com.main.glory.Dao.PartyDao;
import com.main.glory.model.party.Party;
import com.main.glory.services.PartyServiceInterface;

import javax.mail.Part;

@Service("partyServiceImp")
public class PartyServiceImp implements PartyServiceInterface {

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
    UserDao userDao;

    @Autowired
    ModelMapper modelMapper;

    public void saveParty(AddParty party) throws Exception {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        if (party != null) {
            Party partyData = new Party(party);

            if (party.getGSTIN().isEmpty()) {

                if (party.getPartyCode().length() > 4)
                    throw new Exception("Party code should not be greater than 4 digit");

               // Party gstAvailable = partyDao.findByGSTIN(party.getGSTIN());
                Party partyCodeAvailable = partyDao.findByPartyCode(party.getPartyCode());

               /* if (gstAvailable!=null)
                    throw new Exception("GST No." + party.getGSTIN() + " is already exist");*/

                if (partyCodeAvailable!=null)
                    throw new Exception("Party is availble with code:" + party.getPartyCode());

                //check the partyname exist
/*
                Party partyExistWithName = partyDao.getPartyByName(party.getPartyName());
                if(partyExistWithName!=null)
                    throw new Exception("party name is already exist");*/

                partyDao.save(partyData);
                return;
            }
            else {

                if (party.getPartyCode().length() > 4)
                    throw new Exception("Party code should not be greater than 4 digit");

                Party gstAvailable = partyDao.findByGSTIN(party.getGSTIN());
                Party partyCodeAvailable = partyDao.findByPartyCode(party.getPartyCode());

                if (gstAvailable != null)
                    throw new Exception("GST No." + party.getGSTIN() + " is already exist");

                if (partyCodeAvailable != null)
                    throw new Exception("Party is availble with code:" + party.getPartyCode());

                //check the partyname exist

               /* Party partyExistWithName = partyDao.getPartyByName(party.getPartyName());
                if (partyExistWithName == null)
                    throw new Exception("party name is already exist");*/

                partyDao.save(partyData);

            }
        }
    }

    @Override
    public List<Party> getAllPartyDetails(Long id, String getBy) throws Exception {
        List<Party> partyDetailsList = null;
        if (id == null) {
            partyDetailsList = partyDao.getAllParty();
        } else if (getBy.equals("group")) {
            UserData userData = userDao.findUserById(id);

            if(userData.getUserHeadId()==0) {
                //master user
                partyDetailsList = partyDao.findByCreatedByAndUserHeadId(id,id);
            }
            else {
                partyDetailsList = partyDao.findByCreatedByAndUserHeadId(id,userData.getUserHeadId());
            }



        } else if (getBy.equals("own")) {
            partyDetailsList = partyDao.findByCreatedBy(id);
        }
        if (partyDetailsList.isEmpty())
            throw new Exception("no data found");
        return partyDetailsList;
    }

    @Override
    public Party getPartyDetailById(Long id) throws Exception {
        var partyData = partyDao.findById(id);
        if (partyData.isEmpty())
            throw new Exception("no data found");
        else
            return partyData.get();
    }

    @Override
    public boolean editPartyDetails(Party party) throws Exception {
        var partyIndex = partyDao.findById(party.getId());
        Party party1 = partyDao.findByPartyCode(party.getPartyCode());
        if (!partyIndex.isPresent())
            throw new Exception("Party data not found for id:" + party.getId());

        if (party1!=null && party1.getId() != party.getId())
            throw new Exception("Party code should be unique");

        if (party.getGSTIN().isEmpty()) {
            partyDao.saveAndFlush(party);
            return true;
        }


        party1 = partyDao.findByGSTIN(party.getGSTIN());

        if (party1!=null && party1.getId() != party.getId())
            throw new Exception("GST is already availble");

        if (!partyIndex.isPresent())
            return false;
        else
            partyDao.save(party);
        return true;
    }

    @Override
    public boolean deletePartyById(Long id) throws Exception {

        Party partyIndex = partyDao.findByPartyId(id);
        if (partyIndex==null)
            return false;
        else {
            //check the record in sub table that are avialble or not
            List<DispatchMast> dispatchMastList = dispatchMast.getDispatchByPartyId(id);
            if(!dispatchMastList.isEmpty())
                throw new Exception("delete the invoice data first");

            List<Quality> qualityList = qualityServiceImp.getqualityListByPartyId(id);
            if(!qualityList.isEmpty())
                throw new Exception("delete the quality data first");

            List<PaymentMast> paymentMastList = paymentTermService.getAllPaymentByPartyId(id);
            if(!paymentMastList.isEmpty())
                throw new Exception("delete the payment record first");

            List<ShadeMast> shadeMastList = shadeService.getShadeByPartyId(id);
            if(!shadeMastList.isEmpty())
                throw new Exception("delete the shade record first");

            List<ProductionPlan> productionPlans =productionPlanService.getAllProductinByPartyId(id);
            if(!productionPlans.isEmpty())
                throw new Exception("delete the production record first");

            List<StockMast> stockMastList = stockBatchService.getAllStockByPartyId(id);
            if(!stockMastList.isEmpty())
                throw new Exception("delete the stock record first");


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
            List<Party> partyAll = partyDao.getAllParty();

            List<PartyWithName> partyWithNameList = new ArrayList<>();
            if (!partyAll.isEmpty()) {
                partyAll.forEach(e ->
                {
                    PartyWithName partyWithName = new PartyWithName(e.getId(), e.getPartyName(),e.getPartyCode());
                    //System.out.println(partyWithName.getId());
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
        List<Party> partyList = partyDao.getAllParty();
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

    public Boolean partyCodeExistOrNot(String partyCode) {

        Party party = partyDao.findByPartyCode(partyCode);

        if (party==null)
            return true;

        return false;
    }

    //send pdf for mail
    public void sendPdfForParty(GetDocumentModel documentModel) throws Exception {
        String fileName = "party.pdf";
        File f = new File(fileName);
        f.createNewFile();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));//file is created, where the project filder is
        document.open();


        //Add the data
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

        List<Party> party = partyDao.getAllParty();

        if (documentModel.getToRow() > party.size())
            throw new Exception("Party size is :" + party.size());

        for (int i = documentModel.getFromRow().intValue() - 1; i < (documentModel.getToRow()); i++) {

            //System.out.println(party.get(i).getPartyName());
            table.addCell(party.get(i).getPartyName());
            table.addCell(party.get(i).getPartyAddress1());
            table.addCell(party.get(i).getContactNo());
            table.addCell(party.get(i).getCity());
            table.addCell(party.get(i).getCity());

        }

        document.add(table);
        document.close();


        //______Document created successfully


        //Send mail

        for (ToEmailList toEmailList : documentModel.getToEmailList()) {
            System.out.println("To:" + toEmailList.getToEmail());
            SendEmail email = new SendEmail(toEmailList.getToEmail(), fileName, documentModel.getSubjectEmail(), documentModel.getSendText());
            email.sendMail();

        }

    }

    public Party getPartyById(Long id) {
        Party party = partyDao.findByPartyId(id);
        return party;

    }

    public List<PartyWithName> getAllPartyNameWithHeaderId(String id) {
        try {

            Long userId = Long.parseLong(id);

            UserData userData = userDao.getUserById(userId);
            Long userHeadId=null;

            UserPermission userPermission = userData.getUserPermissionData();

            Permissions permissions = new Permissions(userPermission.getPa().intValue());
            if (permissions.getViewAll())
            {
                userId=null;
                userHeadId=null;
            }
            else if (permissions.getViewGroup()) {
                //check the user is master or not ?
                //admin
                if(userData.getUserHeadId() == 0)
                {
                    userId=null;
                    userHeadId=null;
                }
                else if(userData.getUserHeadId() > 0)
                {
                    //check weather master or operator
                    UserData userHead = userDao.getUserById(userData.getUserHeadId());

                    if(userHead.getUserHeadId()==0)
                    {
                        //for master
                        userId=userData.getId();
                        userHeadId=userData.getId();

                    }
                    else {
                        //for operator
                        userId=userData.getId();
                        userHeadId=userData.getUserHeadId();
                    }
                }

            }
            else if (permissions.getView()) {
                userId = userData.getId();
                userHeadId=null;
            }




            List<Party> partyAll = partyDao.getAllPartiesByUserId(userId,userHeadId);

            List<PartyWithName> partyWithNameList = new ArrayList<>();
            if (!partyAll.isEmpty()) {
                partyAll.forEach(e ->
                {
                    PartyWithName partyWithName = new PartyWithName(e.getId(), e.getPartyName(),e.getPartyCode());
                    //System.out.println(partyWithName.getId());
                    partyWithNameList.add(partyWithName);
                });
            }
            return partyWithNameList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
