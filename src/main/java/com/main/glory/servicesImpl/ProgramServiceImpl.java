package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.ProgramDao;
import com.main.glory.Dao.ProgramRecordDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.party.Party;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.response.StockQualityWise;
import com.main.glory.model.program.Program;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse;
import com.main.glory.model.program.ProgramRecord;
import com.main.glory.model.program.request.AddProgramWithProgramRecord;
import com.main.glory.model.program.request.ShadeIdwithPartyShadeNo;
import com.main.glory.model.program.request.UpdateProgramRecord;
import com.main.glory.model.program.request.UpdateProgramWithProgramRecord;
import com.main.glory.model.program.response.GetAllProgram;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.UserData;
import com.main.glory.services.ProgramServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("programServiceImpl")
public class ProgramServiceImpl implements ProgramServiceInterface {

    @Autowired
    UserDao userDao;
    @Autowired
    private ProgramDao programDao;

    @Autowired
    private ProgramRecordDao programRecordDao;

    @Autowired
    private BatchImpl batchService;

    @Autowired
    private StockBatchServiceImpl stockBatchService;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private PartyDao partyDao;


    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BatchDao batchDao;

    @Autowired
    ShadeServiceImpl shadeService;


   @Transactional
    public void saveProgram(AddProgramWithProgramRecord program) throws Exception {

            modelMapper.getConfiguration().setAmbiguityIgnored(true);

            Program programData = modelMapper.map(program, Program.class);
            //System.out.println(programData);
            Long quality = programData.getQualityEntryId();
            if(!qualityDao.findById(quality).isPresent())
            {
                throw new Exception("No such quality found with id:"+quality);
            }

            Long partyId = programData.getPartyId();

            if(!partyDao.findById(partyId).isPresent())
            {
                throw new Exception("No suh party is available with:"+partyId);
            }

            programDao.save(programData);




    }





    @Transactional
    public List<GetAllProgram> getAllProgram(String getBy, Long id) throws Exception {

        List<Program> programList = null;
        List<GetAllProgram> getAllProgramList = new ArrayList<>();
        if(id == null){
            programList = programDao.getAllProgramList();
            for (Program e : programList) {
                GetAllProgram programData;
                if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
                {
                    Optional<Quality> quality = qualityDao.findById(e.getQualityEntryId());
                    Optional<Party> party = partyDao.findById(e.getPartyId());
                    programData=new GetAllProgram(e,party.get(),quality.get());
                    getAllProgramList.add(programData);
                }
            }
        }
        else if(getBy.equals("own")){
            programList = programDao.findByCreatedBy(id);
            for (Program e : programList) {
                GetAllProgram programData;

                if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
                {
                    Optional<Quality> quality = qualityDao.findById(e.getQualityEntryId());
                    Optional<Party> party = partyDao.findById(e.getPartyId());
                    programData=new GetAllProgram(e,party.get(),quality.get());
                    getAllProgramList.add(programData);
                }
            }
        }
        else if(getBy.equals("group")){
            UserData userData = userDao.findUserById(id);
            System.out.println("user head:"+userData.getId());
            if(userData.getUserHeadId().equals(0l)) {
                //master
                programList = programDao.findByUserHeadId(id);
                for (Program e : programList) {
                    GetAllProgram programData;
                    if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
                    {
                        Optional<Quality> quality = qualityDao.findById(e.getQualityEntryId());
                        Optional<Party> party = partyDao.findById(e.getPartyId());
                        programData=new GetAllProgram(e,party.get(),quality.get());
                        getAllProgramList.add(programData);
                    }
                }
            }
            else
            {
                //operator
                programList = programDao.findByUserHeadIdAndCreatedId(userData.getId(),userData.getUserHeadId());
                for (Program e : programList) {
                    GetAllProgram programData;
                    if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
                    {
                        Optional<Quality> quality = qualityDao.findById(e.getQualityEntryId());
                        Optional<Party> party = partyDao.findById(e.getPartyId());
                        programData=new GetAllProgram(e,party.get(),quality.get());
                        getAllProgramList.add(programData);
                    }
                }

            }


        }



        if(getAllProgramList.isEmpty())
            throw new Exception("no data found");
        return getAllProgramList;
    }

    @Override
    public Program getProgramById(Long id) throws Exception {
        if (id != null) {
             Optional<Program> findProgram = programDao.findById(id);
            if (!findProgram.isPresent()) {
                System.out.println("No Record Found");
                throw new Exception("Data not found");
            } else
                return findProgram.get();
        }
        return null;
    }

    @Override
    public boolean deleteProgramById(Long id) throws Exception {
        if (id != null) {
            var findProgram = programDao.findById(id);
            if (findProgram.isPresent()) {
                programDao.deleteById(id);
                return true;
            } else {
                System.out.println("Record not Found");
                return false;
            }
        }
            return false;
    }


    public void updateProgramByID(UpdateProgramWithProgramRecord bm) throws Exception {


        if (bm.getId() != null) {
            Optional<Program> findProgram = programDao.findById(bm.getId());
            if (findProgram.isPresent()) {
                Optional<Quality> qid = qualityDao.findById(bm.getQualityEntryId());
                Optional<Party> partyId = partyDao.findById(bm.getPartyId());

                if (!qid.isPresent())
                    throw new Exception("No such Quality is available with id:"+qid.get().getId());

                if(!partyId.isPresent())
                    throw new Exception("No such Party is available with id:"+partyId.get().getId());


                modelMapper.getConfiguration().setAmbiguityIgnored(true);

                Program programData = modelMapper.map(bm, Program.class);
                List<ProgramRecord> programRecordList = new ArrayList<>();

                //to set the list of child table in update purpose
                for(ProgramRecord up:bm.getProgramRecords())
                {
                    //map the child data one by one bind with list
                    ProgramRecord programRecordData = modelMapper.map(up, ProgramRecord.class);
                    programRecordData.setProgramControlId(bm.getId());
                    programRecordList.add(programRecordData);
                }


                //set the updated child list and bind with parent table object
                programData.setProgramRecords(programRecordList);

                programDao.saveAndFlush(programData);
                //programRecordDao.saveAndFlush(programRecordData);


                }
            else
                throw new Exception("No such Program available with id:"+bm.getId());

            }
            else
                throw new Exception("id can't be null");
        }

    public List<GetAllBatchResponse> getAllBatchByQuality(Long qualityEntryId) throws Exception{
        Optional<Quality> quality = qualityDao.findById(qualityEntryId);
            if (quality.isPresent())
            {

                //System.out.print(quality.get().getId());
                List<GetAllBatchResponse> allBatch=new ArrayList<>();
                List<StockMast> stock= stockBatchService.getAllStockBatch(quality.get().getId());
                //System.out.println("stockc:"+stock.toString());
                if(stock.isEmpty())
                    throw new Exception("Batch is not created for quality:"+qualityEntryId);
                stock.forEach(e->{
                  //  System.out.println("e:"+e.getId());
                    List<GetAllBatchResponse> batchDataList = batchDao.findAllQTYControlId(e.getId());
                    //System.out.println("BatchData:"+batchDataList.toString());
                    allBatch.addAll(batchDataList);
                });

                //System.out.print(allBatch);
                if(allBatch.isEmpty())
                    throw new Exception("no data found");
                return allBatch;

            } else
                throw new Exception("Quality not found");


   }



    public List<ShadeIdwithPartyShadeNo> getShadeDetail() throws Exception {

        List<ShadeMast> shadeMastList= shadeService.getAllShadeMast();
        if(shadeMastList.isEmpty() || shadeMastList == null)
            throw new Exception("no party shade found");

        List<ShadeIdwithPartyShadeNo> shadeIdwithPartyShadeNoList = new ArrayList<>();

        for (ShadeMast e : shadeMastList) {

            if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
            {
                Optional<Quality> quality = qualityDao.findById(e.getQualityEntryId());
                Optional<Party> party=partyDao.findById(e.getPartyId());
                if(!quality.isPresent() || !party.isPresent()){
                    continue;
                }
                if(e.getId()==null)
                    continue;

                ShadeIdwithPartyShadeNo data=new ShadeIdwithPartyShadeNo();
                data.setId(e.getId());
                data.setQualityId(quality.get().getQualityId());
                data.setPartyId(e.getPartyId());
                data.setPartyShadeNo(e.getPartyShadeNo());
                data.setColorTone(e.getColorTone());
                shadeIdwithPartyShadeNoList.add(data);

            }

        }


        if(shadeIdwithPartyShadeNoList.isEmpty())
            throw new Exception("no party shade found");

        return shadeIdwithPartyShadeNoList;
    }

    public List<StockQualityWise> getAllStockByQuality(Long id) throws Exception {


        List<StockQualityWise> stockQualityWiseList=new ArrayList<>();
        List<StockMast> stockMastList = stockBatchService.findByQualityId(id);

        Optional<Quality> quality = qualityDao.findById(id);

        if(!quality.isPresent())
            throw new Exception("No such Quality is present with id :"+id);

        if(stockMastList.isEmpty())
            throw new Exception("Stock is not created for the quality id:"+id);

        for(StockMast stockMast : stockMastList)
        {
            double qty=0;

            List<BatchData> batchDataList =  batchDao.findByControlId(stockMast.getId());


            for(BatchData batchData:batchDataList)
            {
                if(batchData.getIsProductionPlanned()!=true)
                qty+=batchData.getWt();
            }
            StockQualityWise stockQualityWise = new StockQualityWise();
            stockQualityWise.setQty(qty);
            stockQualityWise.setStockId(stockMast.getId());
            stockQualityWiseList.add(stockQualityWise);
            qty=0;

        }
        return stockQualityWiseList;
    }
}

