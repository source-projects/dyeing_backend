package com.main.glory.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.basic.PartyQuality;
import com.main.glory.model.basic.QualityData;
import com.main.glory.model.basic.QualityParty;
import com.main.glory.model.party.Party;
import com.main.glory.model.party.PartyWithMasterName;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.program.Program;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.QualityWithPartyName;
import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.model.quality.request.UpdateQualityRequest;
import com.main.glory.model.quality.response.GetAllQualtiy;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.model.quality.Quality;
import com.main.glory.services.QualityServiceInterface;

@Service("qualityServiceImp")
public class QualityServiceImp  {

    @Autowired
    ProgramServiceImpl programService;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    QualityNameDao qualityNameDao;

    @Autowired
    ShadeServiceImpl shadeService;

    @Autowired
    ProductionPlanImpl productionPlan;


    @Autowired
    UserDao userDao;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private PartyDao partyDao;

    @Autowired
    ModelMapper modelMapper;

    
    public int saveQuality(AddQualityRequest qualityDto,String id) throws Exception {

        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        Quality quality = new Quality(qualityDto);

        System.out.println("header:"+id);

        //for data entry user
        UserData user = userDao.getUserById(Long.parseLong(id));
        System.out.println(":"+user.getId());
        if(user.getDataEntry()==true)
        {
            Party party = partyDao.findByPartyId(qualityDto.getPartyId());
            quality.setUserHeadId(party.getUserHeadId());
        }

        //elese remin the master
        String quality1 = qualityDao.isQualityNameExist(qualityDto.getQualityId());
        if (quality1 != null)
            throw new Exception("Quality id is already exist");

        if(quality.getUnit().equals("weight"))
            quality.setWtPer100m(1.0);

        qualityDao.save(quality);
        return 1;
    }


    
    public List<GetQualityResponse> getAllQuality(Long id, String getBy) throws Exception {
        List<QualityWithPartyName> qualityListobject = null;
        List<GetQualityResponse> quality = new ArrayList<>();
        if (id == null) {
            qualityListobject = qualityDao.findAllWithPartyName();


            for(QualityWithPartyName data :qualityListobject)
            {

                Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(data.getQualityNameId());
                if(qualityName.isEmpty())
                    continue;
                data.setQualityName(qualityName.get().getQualityName());

                quality.add(new GetQualityResponse(data));
                //System.out.println("rate:"+data.getRate() );
            }

        } else if (getBy.equals("group")) {
            UserData userData = userDao.findUserById(id);

            if(userData.getUserHeadId().equals(userData.getId())) {
                //master user
                qualityListobject = qualityDao.findAllWithPartyByCreatedAndHeadId(id,id);
                for(QualityWithPartyName data :qualityListobject)
                {
                    Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(data.getQualityNameId());
                    if(qualityName.isEmpty())
                        continue;
                    data.setQualityName(qualityName.get().getQualityName());
                    quality.add(new GetQualityResponse(data));
                }
            }
            else
            {
                UserData userOperator = userDao.getUserById(id);
                qualityListobject = qualityDao.findQualityByUserHeadId(userOperator.getUserHeadId());
                for(QualityWithPartyName data :qualityListobject)
                {
                    Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(data.getQualityNameId());
                    if(qualityName.isEmpty())
                        continue;
                    data.setQualityName(qualityName.get().getQualityName());
                    quality.add(new GetQualityResponse(data));
                }
            }



        } else if (getBy.equals("own")) {
            qualityListobject = qualityDao.findAllWithPartyNameByCreatedBy(id);
            for(QualityWithPartyName data :qualityListobject)
            {
                Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(data.getQualityNameId());
                if(qualityName.isEmpty())
                    continue;
                data.setQualityName(qualityName.get().getQualityName());
                quality.add(new GetQualityResponse(data));
            }
        }

        if (quality.isEmpty())
            throw new Exception("quality not added yet");
        return quality;
    }


    
    public boolean updateQuality(UpdateQualityRequest qualityDto) throws Exception {
        var qualityData = qualityDao.findById(qualityDto.getId());
        if (!qualityData.isPresent())
            return false;
        else {
            Optional<QualityName> qualityNameExist = qualityNameDao.getQualityNameDetailById(qualityDto.getQualityNameId());
            if(qualityNameExist.isEmpty())
                throw new Exception("no quality name found");

            qualityData.get().setPartyId(qualityDto.getPartyId());
            qualityData.get().setQualityId(qualityDto.getQualityId());
            qualityData.get().setQualityNameId(qualityDto.getQualityNameId());
            qualityData.get().setQualityName(qualityDto.getQualityName());
            qualityData.get().setQualityType(qualityDto.getQualityType());
            qualityData.get().setUnit(qualityDto.getUnit());
            qualityData.get().setWtPer100m(qualityDto.getWtPer100m());
            qualityData.get().setRemark(qualityDto.getRemark());
            qualityData.get().setUpdatedBy(qualityDto.getUpdatedBy());
            qualityData.get().setQualityDate(qualityDto.getQualityDate());
            qualityData.get().setRate(qualityDto.getRate());
            qualityDao.save(qualityData.get());
            return true;
        }
    }

    
    public boolean deleteQualityById(Long id) throws Exception {
        Quality qualityExist = qualityDao.getqualityById(id);

        if(qualityExist==null)
            throw new Exception("no quality found");

        List<StockMast> stockMastList = stockBatchService.getStockByQualityEntryId(id);
        if(!stockMastList.isEmpty())
            throw new Exception("delete the stock record first");

        List<ProductionPlan> productionPlans = productionPlan.getProductionByQualityId(id);
        if(!productionPlans.isEmpty())
            throw new Exception("delete the production data first");

        List<Program> programList =programService.getAllProgramByQualityId(id);
        if(!programList.isEmpty())
            throw new Exception("delete the program record first");

        List<ShadeMast> shadeMastList = shadeService.getAllShadeByQualityId(id);
        if(!shadeMastList.isEmpty())
            throw new Exception("delete the shade record first");

        qualityDao.deleteByQualtyId(id);
        return true;
    }

    
    public GetQualityResponse getQualityByID(Long id) {
        Optional<Quality> quality = qualityDao.findById(id);
        if (!quality.isPresent())
            return null;
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        GetQualityResponse quality1 = new GetQualityResponse(quality.get());
        Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality1.getQualityNameId());
        if(qualityName.isEmpty())
            return null;
        quality1.setQualityName(qualityName.get().getQualityName());
        String party = partyDao.getPartyNameByPartyId(quality1.getPartyId());
        quality1.setPartyName(party);

        return quality1;
    }

    
    public String isQualityAlreadyExist(String qualityId) {
        String quakit = qualityDao.isQualityNameExist(qualityId);
        return quakit;
    }

    
    public String getPartyNameByPartyId(Long partyId) {
        String partyName = partyDao.getPartyNameByPartyId(partyId);
        return partyName;
    }


    public QualityParty getAllQualityWithParty(Long id) throws Exception {

        Optional<Quality> qualityLists = qualityDao.findById(id);


        if (!qualityLists.isPresent())
            throw new Exception("No quality found");


        QualityParty qualityParties = new QualityParty(qualityLists.get());

        Optional<Party> party = partyDao.findById(qualityLists.get().getPartyId());

        qualityParties.setPartyName(party.get().getPartyName());

        if (qualityParties == null)
            throw new Exception("no data faund");

        return qualityParties;


    }

    public PartyQuality getAllPartyWithQuality(Long partyId) throws Exception {

        Optional<List<Quality>> qualityList = qualityDao.findByPartyId(partyId);

        Optional<Party> partName = partyDao.findById(partyId);
        if (!partName.isPresent())
            throw new Exception("No such Party id available with id:" + partyId);
        if (!qualityList.isPresent()) {
            throw new Exception("Add Quality data for partyId:" + partyId);
        }
        PartyQuality partyQualityData = new PartyQuality();

        List<QualityData> qualityDataList = new ArrayList<>();
        for (Quality quality : qualityList.get()) {

            if (qualityList.get().isEmpty())
                continue;

            Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality.getQualityNameId());
            if(qualityName.isEmpty())
                continue;

            qualityDataList.add(new QualityData(quality,qualityName.get()));


        }
        partyQualityData.setQualityDataList(qualityDataList);
        partyQualityData.setPartyId(partyId);


        partyQualityData.setPartyName(partName.get().getPartyName());


        if (partyQualityData == null)
            throw new Exception("no data faund");

        return partyQualityData;


    }

    public List<GetAllQualtiy> getAllQualityData() throws Exception {
        List<Quality> qualities = qualityDao.getAllQuality();
        List<GetAllQualtiy> getAllQualtiyList = new ArrayList<>();
        for (Quality quality : qualities) {
            Optional<Party> partyName = partyDao.findById(quality.getPartyId());
            if (!partyName.isPresent())
                continue;

            GetAllQualtiy getAllQualtiy = new GetAllQualtiy(quality);
            getAllQualtiy.setPartyName(partyName.get().getPartyName());
            getAllQualtiyList.add(getAllQualtiy);
        }
        if (getAllQualtiyList.isEmpty())
            throw new Exception("no data found");
        return getAllQualtiyList;
    }

    public List<PartyQuality> getAllPartyWithQualityByMaster(Long userHeadId) throws Exception {

        Optional<List<Quality>> QualityList = qualityDao.findByUserHeadId(userHeadId);

        if (!QualityList.isPresent()) {
            throw new Exception("No quality found for master:" + userHeadId);
        }
        List<PartyWithMasterName> partyList = partyDao.findByUserHeadId(userHeadId);
        if (partyList.isEmpty()) {
            throw new Exception("No party found for master:" + userHeadId);
        }

        List<PartyQuality> partyQualityList = new ArrayList<>();
        for (Party party : partyList) {
            Optional<List<Quality>> quality = qualityDao.findByPartyId(party.getId());
            if (quality.isPresent()) {
                PartyQuality partyQuality = new PartyQuality();
                partyQuality.setPartyId(party.getId());
                partyQuality.setPartyName(party.getPartyName());
                List<QualityData> qualityDataList = new ArrayList<>();
                for (Quality quality1 : quality.get()) {
                    Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality1.getQualityNameId());
                    if(qualityName.isEmpty())
                        continue;

                    QualityData qualityData = new QualityData(quality1,qualityName.get());
                    qualityDataList.add(qualityData);

                }
                partyQuality.setQualityDataList(qualityDataList);
                partyQualityList.add(partyQuality);
            }

        }

        if (partyQualityList.isEmpty())
            throw new Exception("no data found");

        return partyQualityList;
    }

    public Optional<Quality> getQualityByIDAndPartyId(Long qualityEntryId, Long partyId) throws Exception {
        Optional<Quality> quality = qualityDao.findByPartyIdAndQualityId(qualityEntryId, partyId);

        if (!quality.isPresent())
            throw new Exception("Quality data not found for party");

        return quality;

    }

    public List<Quality> getqualityListByPartyId(Long id) {
        List<Quality> list = qualityDao.getQualityListByPartyIdId(id);
        return list;
    }

    public List<GetAllQualtiy> getAllQualityDataWithHeaderId(String id) throws Exception {
        Long userId = Long.parseLong(id);


        UserData userData = userDao.getUserById(userId);
        Long userHeadId=null;

        UserPermission userPermission = userData.getUserPermissionData();

        List<Quality> qualities = null;
        Permissions permissions = new Permissions(userPermission.getPa().intValue());
        if (permissions.getViewAll())
        {
            userId=null;
            userHeadId=null;
            qualities = qualityDao.getAllQuality();
        }
        else if (permissions.getViewGroup()) {
            //check the user is master or not ?
            //admin
            if(userData.getUserHeadId() == 0)
            {
                userId=null;
                userHeadId=null;
                qualities=qualityDao.getAllQuality();
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
                    qualities = qualityDao.getAllQualityWithIdAndUserHeadId(userId,userHeadId);

                }
                else {
                    //for operator
                    userId=userData.getId();
                    userHeadId=userData.getUserHeadId();
                    qualities = qualityDao.getAllQualityWithIdAndUserHeadId(userId,userHeadId);
                }
            }

        }
        else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId=null;
            qualities = qualityDao.getAllQualityCreatedBy(userId);
        }



        List<GetAllQualtiy> getAllQualtiyList = new ArrayList<>();
        for (Quality quality : qualities) {
            Optional<Party> partyName = partyDao.findById(quality.getPartyId());
            if (!partyName.isPresent())
                continue;

            Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality.getQualityNameId());
            if(qualityName.isEmpty())
                continue;

            GetAllQualtiy getAllQualtiy = new GetAllQualtiy(quality);
            getAllQualtiy.setPartyName(partyName.get().getPartyName());
            getAllQualtiy.setQualityName(qualityName.get().getQualityName());
            getAllQualtiy.setQualityNameId(qualityName.get().getId());
            getAllQualtiyList.add(getAllQualtiy);
        }
        if (getAllQualtiyList.isEmpty())
            throw new Exception("no data found");
        return getAllQualtiyList;
    }

    public Boolean getQualityIsExist(String quality_id, Long id) {
        //id is null then check with entire record else except that record id

        if(id==null)
        {
            Quality quality = qualityDao.getQualityById(quality_id);
            if(quality==null)
                return false;
            else
                return true;
        }
        else {

            Optional<Quality> quality = qualityDao.getQualityByIdExceptId(quality_id,id);
            if(quality.isEmpty())
                return false;
            else
                return true;
        }
    }


    public Optional<List<Quality>> getQualityByQualityNameId(Long id) {
        return qualityDao.getAllQualityByQualityNameId(id);
    }

    public Optional<List<QualityName>> getAllQualityNameData() {

        return qualityNameDao.getAllQualityName();

    }

    public Optional<QualityName> getQualityNameDataById(Long id) {
        Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(id);
        return qualityName;
    }

    public Quality getQualityByEntryId(Long qualityId) {
        return qualityDao.getqualityById(qualityId);
    }
}
