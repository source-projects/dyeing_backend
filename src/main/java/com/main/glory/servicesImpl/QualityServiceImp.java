package com.main.glory.servicesImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.filters.Filter;
import com.main.glory.filters.FilterResponse;
import com.main.glory.filters.QueryOperator;
import com.main.glory.filters.SpecificationManager;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.basic.PartyQuality;
import com.main.glory.model.basic.QualityData;
import com.main.glory.model.basic.QualityParty;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.response.QualityWithRateAndTotalMtr;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.party.PartyWithMasterName;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.program.Program;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.QualityWithPartyName;
import com.main.glory.model.quality.request.AddQualityName;
import com.main.glory.model.quality.request.GetQualityReport;
import com.main.glory.model.quality.response.GetAllQualtiy;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.quality.response.QualityReport;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.supplier.responce.SupplierResponse;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import com.main.glory.services.FilterService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.model.quality.AddQuality;
import com.main.glory.model.quality.Quality;

@Service("qualityServiceImp")
public class QualityServiceImp  {

    @Autowired
    SupplierServiceImpl supplierService;

    ConstantFile constantFile;

    @Autowired
    SpecificationManager<Quality> specificationManager;
	@Autowired
    FilterService<Quality,QualityDao> filterService;


    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @Autowired
    DispatchMastImpl dispatchMastService;

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

    
    public int saveQuality(AddQuality addQuality,String id) throws Exception {
        
        UserData createdBy=userDao.getUserById(Long.parseLong(id));
        UserData userHeadData=userDao.getUserById(createdBy.getUserHeadId());
        
        Party party=partyDao.findByPartyId(addQuality.getPartyId());
        addQuality.setCreatedDate(new Date(System.currentTimeMillis()));
        addQuality.setUpdatedDate(new Date(System.currentTimeMillis()));
        Quality qualityDto = new Quality(addQuality, userHeadData, createdBy, createdBy,party);


        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        //Quality quality = new Quality(qualityDto);

        System.out.println("header:"+id);

        //for data entry user
        UserData user = userDao.getUserById(Long.parseLong(id));
        System.out.println(":"+user.getId());
        if(user.getIsMaster()==false || qualityDto.getUserHeadData().getId()==0)
        {
            qualityDto.setUserHeadData(party.getUserHeadData());
        }

        //elese remin the master
        /*String quality1 = qualityDao.isQualityNameExist(qualityDto.getQualityId());
        if (quality1 != null)
            throw new Exception("Quality id is already exist");*/

        Quality qualityExistWithId = qualityDao.getQualityIdIsExistExceptId(qualityDto.getQualityId(),qualityDto.getParty().getId(),qualityDto.getId()==null?0: qualityDto.getId());
        if(qualityExistWithId!=null)
            throw new Exception(ConstantFile.Quality_Data_Exist_With_QualityId);

        if(qualityDto.getUnit().equals("weight"))
            qualityDto.setWtPer100m(1.0);

        if(qualityDto.getHsn()!=null)
            qualityDto.setHsn("998821");
        Quality x = qualityDao.save(qualityDto);

        return 1;
    }

    public FilterResponse<GetQualityResponse> getAllQualityPaginated(GetBYPaginatedAndFiltered requestParam, String id) throws Exception {
        List<Quality> qualityListobject = null;
        List<GetQualityResponse> quality = new ArrayList<>();

        String getBy=requestParam.getGetBy();
		Pageable pageable=filterService.getPageable(requestParam.getData());
        List<Filter> filtersParam=requestParam.getData().getParameters();
        HashMap<String,List<String>> subModelCase=new HashMap<String,List<String>>();
        subModelCase.put("partyCode",new ArrayList<String>(Arrays.asList("party","partyCode")));
        subModelCase.put("partyName", new ArrayList<String>(Arrays.asList("party","partyName")));
        subModelCase.put("partyId", new ArrayList<String>(Arrays.asList("party","id")));
        subModelCase.put("userHeadId",new ArrayList<String>(Arrays.asList("userHeadData","id")));
        subModelCase.put("createdBy",new ArrayList<String>(Arrays.asList("userCreatedByData","id")));
        subModelCase.put("userHeadName",new ArrayList<String>(Arrays.asList("userHeadData","userName")));
        subModelCase.put("createdByName",new ArrayList<String>(Arrays.asList("userCreatedByData","userName")));
        
		Page queryResponse=null;

        Specification<Quality> filterSpec=specificationManager.getSpecificationFromFilters(filtersParam, requestParam.getData().isAnd,subModelCase);


        if (id == null || getBy.equals("all")) {
            List<Filter> filters=new ArrayList<Filter>();
            Specification<Quality> spec=specificationManager.getSpecificationFromFilters(filters, true,subModelCase);
            spec=spec.and(filterSpec);
			queryResponse = qualityDao.findAll(spec, pageable);


        } else if (getBy.equals("group")) {
            UserData userData = userDao.findUserById(Long.parseLong(id));

            if(userData.getUserHeadId()==0)
            {
                //for admin
                
                List<Filter> filters=new ArrayList<Filter>();
                Specification<Quality> spec=specificationManager.getSpecificationFromFilters(filters, true,subModelCase);
                spec=spec.and(filterSpec);
                queryResponse = qualityDao.findAll(spec, pageable);
    
            }
            else if(userData.getUserHeadId().equals(userData.getId())) {
                //master user
                List<Filter> filters=new ArrayList<Filter>();
                filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")),QueryOperator.EQUALS,id));
				filters.add(new Filter(new ArrayList<String>(Arrays.asList("userHeadId")),QueryOperator.EQUALS,id));
                Specification<Quality> spec=specificationManager.getSpecificationFromFilters(filters, true,subModelCase);
                spec=spec.and(filterSpec);
    
                queryResponse = qualityDao.findAll(spec, pageable);    

            }
            else
            {
                List<Filter> filters=new ArrayList<Filter>();
				filters.add(new Filter(new ArrayList<String>(Arrays.asList("userHeadId")),QueryOperator.EQUALS,id));
                Specification<Quality> spec=specificationManager.getSpecificationFromFilters(filters, true,subModelCase);
                spec=spec.and(filterSpec);
                queryResponse = qualityDao.findAll(spec, pageable);

            }



        } else if (getBy.equals("own")) {
            UserData userData = userDao.findUserById(Long.parseLong(id));
            List<Filter> filters=new ArrayList<Filter>();
            
            if(userData.getUserHeadId()!=0)
            filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")),QueryOperator.EQUALS,id));

            Specification<Quality> spec=specificationManager.getSpecificationFromFilters(filters, true,subModelCase);
            spec=spec.and(filterSpec);

            queryResponse = qualityDao.findAll(spec, pageable);    

        }
        qualityListobject=queryResponse.getContent();
        for(Quality q :qualityListobject)
        {QualityWithPartyName data;
            if(q.getParty()==null)
            data=new QualityWithPartyName(q,null,null);
            else
            data=new QualityWithPartyName(q,q.getParty().getPartyName(), q.getParty().getPartyCode());
            
            Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(data.getQualityNameId());
            if(qualityName.isEmpty())
                continue;
            data.setQualityName(qualityName.get().getQualityName());
            quality.add(new GetQualityResponse(data));
        }

       /* if (quality.isEmpty())
            throw new Exception(CommonMessage.Quality_Data_Not_Added);*/
        FilterResponse<GetQualityResponse> response=new FilterResponse<GetQualityResponse>(quality,queryResponse.getNumber(),queryResponse.getNumberOfElements() ,(int)queryResponse.getTotalElements());

        return response;
    }



    
    public List<GetQualityResponse> getAllQuality(Long id, String getBy) throws Exception {
        List<QualityWithPartyName> qualityListobject = null;
        List<GetQualityResponse> quality = new ArrayList<>();
        if (id == null) {
            qualityListobject = qualityDao.findAllWithPartyName();


           /* for(QualityWithPartyName data :qualityListobject)
            {

                Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(data.getQualityNameId());
                if(qualityName.isEmpty())
                    continue;
                data.setQualityName(qualityName.get().getQualityName());

                quality.add(new GetQualityResponse(data));
                //System.out.println("rate:"+data.getRate() );
            }*/

        } else if (getBy.equals("group")) {
            UserData userData = userDao.findUserById(id);

            if(userData.getUserHeadId()==0)
            {
                //for admin
                qualityListobject = qualityDao.findAllWithPartyName();

            }
            else if(userData.getUserHeadId().equals(userData.getId())) {
                //master user
                qualityListobject = qualityDao.findAllWithPartyByCreatedAndHeadId(id,id);

            }
            else
            {
                UserData userOperator = userDao.getUserById(id);
                qualityListobject = qualityDao.findQualityByUserHeadId(userOperator.getUserHeadId());

            }



        } else if (getBy.equals("own")) {
            qualityListobject = qualityDao.findAllWithPartyNameByCreatedBy(id);

        }
        for(QualityWithPartyName data :qualityListobject)
        {
            Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(data.getQualityNameId());
            if(qualityName.isEmpty())
                continue;
            data.setQualityName(qualityName.get().getQualityName());
            quality.add(new GetQualityResponse(data));
        }

       /* if (quality.isEmpty())
            throw new Exception(CommonMessage.Quality_Data_Not_Added);*/
        return quality;
    }


    
    public boolean updateQuality(AddQuality addQuality,String id) throws Exception {
        UserData userHeadData=userDao.getUserById(addQuality.getUserHeadId());
        UserData createdBy=userDao.getUserById(addQuality.getCreatedBy());
        UserData updatedBy=userDao.getUserById(Long.parseLong(id));
        addQuality.setUpdatedDate(new Date(System.currentTimeMillis()));        
        Party party=partyDao.findByPartyId(addQuality.getPartyId());
        Quality qualityDto = new Quality(addQuality, userHeadData, createdBy, updatedBy,party);
        var qualityData = qualityDao.findById(addQuality.getId());
        if (!qualityData.isPresent())
            return false;
        else {
            Optional<QualityName> qualityNameExist = qualityNameDao.getQualityNameDetailById(qualityDto.getQualityNameId());
            if(qualityNameExist.isEmpty())
                throw new Exception("no quality name found");


            //check the quality except the the given id
            Quality qualityExistWithId = qualityDao.getQualityIdIsExistExceptId(qualityDto.getQualityId(),qualityDto.getParty().getId(),qualityDto.getId());
            if(qualityExistWithId!=null)
                throw new Exception(ConstantFile.Quality_Data_Exist_With_QualityId);

            /*qualityData.get().setPartyId(qualityDto.getPartyId());
            qualityData.get().setQualityId(qualityDto.getQualityId());
            qualityData.get().setQualityNameId(qualityDto.getQualityNameId());
            qualityData.get().setQualityName(qualityDto.getQualityName());
            qualityData.get().setQualityType(qualityDto.getQualityType());
            qualityData.get().setUnit(qualityDto.getUnit());
            qualityData.get().setWtPer100m(qualityDto.getWtPer100m());
            qualityData.get().setMtrPerKg(qualityDto.getMtrPerKg());
            qualityData.get().setRemark(qualityDto.getRemark());
            qualityData.get().setUpdatedBy(qualityDto.getUpdatedBy());
            qualityData.get().setQualityDate(qualityDto.getQualityDate());
            qualityData.get().setRate(qualityDto.getRate());
            qualityData.get().setBillingUnit(qualityDto.getBillingUnit());*/

            qualityDao.save(qualityDto);
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

    //get latest complete response
    public GetQualityResponse getQualityByID(Long id) throws Exception {
        Optional<Quality> quality = qualityDao.findById(id);
        if (!quality.isPresent())
            return null;
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        GetQualityResponse quality1 = new GetQualityResponse(quality.get());
        Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality1.getQualityNameId());
        if(qualityName.isEmpty())
            return null;
        quality1.setQualityName(qualityName.get().getQualityName());
        Party party = partyDao.findByPartyId(quality1.getPartyId());
        quality1.setPartyName(party.getPartyName());
        quality1.setPartyCode(party.getPartyCode());

        if(quality.get().getProcessId()!=null)
        {
            DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(quality.get().getProcessId());
            if(dyeingProcessMast!=null)
                quality1.setProcecssName(dyeingProcessMast.getProcessName());

        }

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
            throw new Exception(constantFile.Quality_Data_Not_Found);


        QualityParty qualityParties = new QualityParty(qualityLists.get());

        Optional<Party> party = partyDao.findById(qualityLists.get().getParty().getId());

        qualityParties.setPartyName(party.get().getPartyName());

        if (qualityParties == null)
            throw new Exception("no data faund");

        return qualityParties;


    }

    public PartyQuality getAllPartyWithQuality(Long partyId) throws Exception {

        Optional<List<Quality>> qualityList = qualityDao.findByPartyId(partyId);

        Party partName = partyDao.findByPartyId(partyId);
        if (partName==null)
            throw new Exception(constantFile.Party_Not_Found + partyId);
       /* if (!qualityList.isPresent()) {
            throw new Exception("Add Quality data for partyId:" + partyId);
        }*/
        PartyQuality partyQualityData = new PartyQuality();

        List<QualityData> qualityDataList = new ArrayList<>();
        if(qualityList.isPresent()) {
            if (qualityList.isPresent()) {
                for (Quality quality : qualityList.get()) {

                    if (qualityList.get().isEmpty())
                        continue;

                    Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality.getQualityNameId());
                    if (qualityName.isEmpty())
                        continue;

                    QualityData qualityData = new QualityData(quality, qualityName.get(), partName);
                    if(quality.getProcessId()!=null)
                    {
                        DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(quality.getProcessId());
                        if(dyeingProcessMast!=null) {
                            qualityData.setProcessId(quality.getProcessId());
                            qualityData.setProcessName(dyeingProcessMast.getProcessName());
                        }
                    }
                    qualityDataList.add(qualityData);


                }
            }
        }
        partyQualityData.setQualityDataList(qualityDataList);
        partyQualityData.setPartyId(partyId);


        partyQualityData.setPartyName(partName.getPartyName());


        if (partyQualityData == null)
            throw new Exception(constantFile.Quality_Data_Not_Found);

        return partyQualityData;


    }

    public List<GetAllQualtiy> getAllQualityData() throws Exception {
        List<Quality> qualities = qualityDao.getAllQuality();
        List<GetAllQualtiy> getAllQualtiyList = new ArrayList<>();
        for (Quality quality : qualities) {
            Optional<Party> partyName = partyDao.findById(quality.getParty().getId());
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
            throw new Exception(constantFile.Quality_Data_Not_Found_ByMaster + userHeadId);
        }
        List<PartyWithMasterName> partyList = partyDao.findByUserHeadId(userHeadId);
        if (partyList.isEmpty()) {
            throw new Exception(constantFile.Party_Found_ByMaster + userHeadId);
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
                    if(quality1.getProcessId()!=null)
                    {
                        DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(quality1.getProcessId());
                        if(dyeingProcessMast!=null)
                        {
                            qualityData.setProcessId(dyeingProcessMast.getId());
                            qualityData.setProcessName(dyeingProcessMast.getProcessName());
                        }
                    }
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
            if (userData.getUserHeadId() == 0) {
                userId = null;
                userHeadId = null;
                qualities = qualityDao.getAllQuality();
            } else if (userData.getUserHeadId() > 0) {
                //check weather master or operator
                UserData userHead = userDao.getUserById(userData.getUserHeadId());
                userId = userData.getId();
                userHeadId = userHead.getId();
                qualities = qualityDao.getAllQualityWithIdAndUserHeadId(userId, userHeadId);

            }
        }
        else if (permissions.getView()) {
            userId = userData.getId();
            userHeadId=null;
            qualities = qualityDao.getAllQualityCreatedBy(userId);
        }



        List<GetAllQualtiy> getAllQualtiyList = new ArrayList<>();
        for (Quality quality : qualities) {
            Optional<Party> partyName = partyDao.findById(quality.getParty().getId());
            if (!partyName.isPresent())
                continue;

            Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(quality.getQualityNameId());
            if(qualityName.isEmpty())
                continue;

            GetAllQualtiy getAllQualtiy = new GetAllQualtiy(quality);
            getAllQualtiy.setPartyName(partyName.get().getPartyName());
            getAllQualtiy.setQualityName(qualityName.get().getQualityName());
            getAllQualtiy.setQualityNameId(qualityName.get().getId());

            //check that the dyeing process is exit or not
            if(quality.getProcessId()!=null)
            {
                DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(quality.getProcessId());
                if(dyeingProcessMast!=null)
                {
                    getAllQualtiy.setProcessId(dyeingProcessMast.getId());
                    getAllQualtiy.setProcessName(dyeingProcessMast.getProcessName());
                }
            }
            getAllQualtiyList.add(getAllQualtiy);


        }
        /*if (getAllQualtiyList.isEmpty())
            throw new Exception("no data found");*/
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

    public List<AddQualityName> getAllQualityNameData() {

        List<AddQualityName> list =new ArrayList<>();
        Optional<List<QualityName>> qualityNameList = qualityNameDao.getAllQualityName();
        System.out.println("qualityNameDao.getAllQualityName");
        if(!qualityNameList.isEmpty()) {
            for (QualityName qualityName : qualityNameList.get()) {
                System.out.println("supplierService.getSupplierByQualityNameId "+Long.toString(qualityName.getId()));
                List<SupplierResponse> supplierList = supplierService.getSupplierByQualityNameId(qualityName.getId());
                list.add(new AddQualityName(qualityName,supplierList));

            }
        }

        return list;

    }

    public Optional<QualityName> getQualityNameDataById(Long id) {
        Optional<QualityName> qualityName = qualityNameDao.getQualityNameDetailById(id);
        return qualityName;
    }

    public Quality getQualityByEntryId(Long qualityId) {
        return qualityDao.getqualityById(qualityId);
    }

    public Optional<Quality> getQualityEntryByIDAndPartyId(Long qualityId, Long partyId) {
        return qualityDao.findByPartyIdAndQualityId(qualityId,partyId);
    }

    public List<Quality> getQualityByCreatedByAndUserHeadId(Long id) {
        return qualityDao.getAllQualityWithIdAndUserHeadId(id,id);
    }

    public Quality getQualityByStockId(Long controlId) {
        return qualityDao.getQualityByStockId(controlId);
    }

    public List<QualityReport> getQualityReport(GetQualityReport record) throws Exception {
        List<QualityReport> qualityReportList=new ArrayList<>();

        if(record.getFromDate()==null || record.getToDate()==null)
            throw new Exception(ConstantFile.Null_Record_Passed);

        if(record.getQualityEntryId()==null)
        {
            List<DispatchMast> dispatchMastList = dispatchMastService.getDispatchByDateFilter(record.getFromDate(),record.getToDate());
            for(DispatchMast dispatchMast:dispatchMastList)
            {
                List<QualityWithRateAndTotalMtr> qualityWithRateAndTotalMtrList = dispatchMastService.getAllQualityByInvoiceNo(Long.parseLong(dispatchMast.getDispatchData().getInvoiceNo()));
                for(QualityWithRateAndTotalMtr qualityWithRateAndTotalMtr:qualityWithRateAndTotalMtrList)
                {
                    List<Long> batchIdsByQuality = dispatchMastService.getAllBatchEntryIdByQualityAndInvoice(qualityWithRateAndTotalMtr.getQualityEntryId(),Long.parseLong(dispatchMast.getDispatchData().getInvoiceNo()));

                    if(!batchIdsByQuality.isEmpty())
                    {
                        Double totalMtr = stockBatchService.getTotalFinishMtrByBatchEntryIdList(batchIdsByQuality);
                        qualityReportList.add(new QualityReport(qualityWithRateAndTotalMtr,totalMtr));
                    }
                }
            }


        }
        else
        {
            //check that the quality is exist or not
            Quality qualityExist = qualityDao.getqualityById(record.getQualityEntryId());
            if(qualityExist==null)
                throw new Exception(ConstantFile.Quality_Data_Not_Exist);


            List<DispatchMast> dispatchMastList = dispatchMastService.getDispatchByDateFilter(record.getFromDate(),record.getToDate());
            for(DispatchMast dispatchMast:dispatchMastList)
            {
                List<QualityWithRateAndTotalMtr> qualityWithRateAndTotalMtrList = dispatchMastService.getAllQualityByInvoiceNo(Long.parseLong(dispatchMast.getDispatchData().getInvoiceNo()));
                for(QualityWithRateAndTotalMtr qualityWithRateAndTotalMtr:qualityWithRateAndTotalMtrList)
                {
                    if(qualityWithRateAndTotalMtr.getQualityEntryId().equals(record.getQualityEntryId())) {
                        List<Long> batchIdsByQuality = dispatchMastService.getAllBatchEntryIdByQualityAndInvoice(qualityWithRateAndTotalMtr.getQualityEntryId(), Long.parseLong(dispatchMast.getDispatchData().getInvoiceNo()));
                        if (!batchIdsByQuality.isEmpty()) {
                            Double totalMtr = stockBatchService.getTotalFinishMtrByBatchEntryIdList(batchIdsByQuality);
                            qualityReportList.add(new QualityReport(qualityWithRateAndTotalMtr, totalMtr));
                        }
                    }
                }
            }
        }
        return qualityReportList;
    }

    public Quality getQualityIdIsExistExceptId(String qualityId, Long partyId,Long id) {
        return qualityDao.getQualityIdIsExistExceptId(qualityId,partyId,id);
    }
}
