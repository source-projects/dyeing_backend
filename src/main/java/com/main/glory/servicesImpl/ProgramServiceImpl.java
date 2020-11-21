package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.ProgramDao;
import com.main.glory.Dao.ProgramRecordDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.model.Party;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.response.StockQualityWise;
import com.main.glory.model.program.Program;
import com.main.glory.model.ProgramRecord;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse;
import com.main.glory.model.program.request.AddProgramWithProgramRecord;
import com.main.glory.model.program.request.ShadeIdwithPartyShadeNo;
import com.main.glory.model.program.response.GetAllProgram;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.UserData;
import com.main.glory.services.ProgramServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("programServiceImpl")
public class ProgramServiceImpl implements ProgramServiceInterface {

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


//    @Transactional
    public boolean saveProgram(AddProgramWithProgramRecord program) throws Exception {
        try {

            /*var pdata = programDao.findById(program.getId());

            //System.out.println("programData:"+pdata.get().getId());
            if (pdata.isPresent()) {

                var record =program.getProgram_record();

                boolean isPartyShadeAvailable=false;
                //if the program is same but the record is diffrent
                ProgramRecord programRecord = new ProgramRecord();


                programRecord.setColour_tone(record.get(0).getColour_tone());
                programRecord.setProgramControlId(record.get(0).getProgramControlId());
                programRecord.setQuantity(record.get(0).getQuantity());
                programRecord.setRemark(record.get(0).getRemark());
                List<ShadeMast> shadeMast=shadeService.getAllShadeMast();
                for (ShadeMast e : shadeMast) {

                    if(e.getPartyShadeNo() == record.get(0).getParty_shade_no())
                    {
                        isPartyShadeAvailable=true;
                        break;
                    }
                    else
                    {
                        isPartyShadeAvailable=false;
                    }
                }


                if(isPartyShadeAvailable==true)
                {
                    programRecord.setParty_shade_no(record.get(0).getParty_shade_no());
                }
                else
                    throw new Exception("Party shade no is not Availble for partyShadeno:"+record.get(0).getParty_shade_no());

                programRecord.setShade_no(record.get(0).getShade_no());
                programRecord.setId(null);

                System.out.println("RecordData:"+programRecord.getColour_tone());

                programRecordDao.saveAndFlush(programRecord);
                return true;


            } else {
            //program.setId(0l);
                Optional<Quality> dataQuality = qualityDao.findById(program.getQuality_entry_id());
                if (dataQuality.isPresent()) {

                    Optional<Party> party = partyDao.findById(program.getParty_id());
                    if (party.isPresent()) {
                       // program.getProgram_record().get(0).setId(0l);
                       // program.getProgram_record().get(0).setProgramControlId(program.getId());
                       // program.setProgram_record(program.getProgram_record());
                        programDao.saveAndFlush(program);
                        return true;
                    } else {
                        throw new Exception("party not found");
                    }

                }
                else
                {
                    throw new Exception("Quality not found");
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }


        return false;*/
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            Program programData = modelMapper.map(program, Program.class);
            System.out.println(programData);
            programDao.save(programData);
            return true;
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


            return false;
    }


    @Override
    public boolean saveProgram(Program program) throws Exception {
        return false;
    }

    @Transactional
    public List<GetAllProgram> getAllProgram() throws Exception {

        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        List<Program> programList = programDao.findAll();
        List<GetAllProgram> getAllProgramList = new ArrayList<>();
        for (Program e : programList) {

            GetAllProgram programData =  new GetAllProgram();

            //modelMapper.map(e, GetAllProgram.class);
            Optional<Quality> quality = qualityDao.findById(e.getQualityEntryId());
            Optional<Party> party = partyDao.findById(e.getPartyId());
            //programData.setQuality_id(qualityDao.findById(e.getQuality_entry_id()));
            if(!quality.isPresent())
            {
                throw new Exception("Quality data is not Found:");
            }
            if(!party.isPresent())
            {
                throw new Exception("Party Data not found");
            }

            programData.setParty_id(party.get().getId());
            programData.setProgram_given_by(e.getProgramGivenBy());
            programData.setPriority(e.getPriority());
            programData.setRemark(e.getRemark());
            programData.setQuality_entry_id(e.getQualityEntryId());
            programData.setQuality_id(quality.get().getQualityId());
            programData.setQualityName(quality.get().getQualityName());
            programData.setPartName(party.get().getPartyName());

            getAllProgramList.add(programData);
        }

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

    @Override
    public boolean updateProgramByID(Program bm) throws Exception {

        try {
            if (bm.getId() != null) {
                var findProgram = programDao.findById(bm.getId());
                if (findProgram.isPresent()) {
                    Optional<Quality> qid = qualityDao.findById(bm.getQualityEntryId());
                    Optional<Quality> qualityId = qualityDao.findByQualityId(qid.get().getQualityId());
                    //System.out.println(qualityId.get().getQualityId());
                    if (qid.isPresent() && qualityId.isPresent()) {
                        bm.setQualityEntryId(qid.get().getId());
                        bm.setQualityId(qid.get().getQualityId());
                        // System.out.println("qid:"+qid.get().getId());
                        //System.out.println("qid:"+qid.get().getQualityId());
                        programDao.saveAndFlush(bm);
                        return true;
                    } else
                        return false;
                } else {
                    System.out.println("Record not Found");
                    return false;
                }


            }
        }catch(Exception e)
        {
            return false;
        }
        return false;
    }

    public List<GetAllBatchResponse> getAllBatchByQuality(String qualityId) {
        Optional<Quality> quality = qualityDao.findByQualityId(qualityId);
        try {
            if (quality.isPresent())
            {
                //System.out.print(quality.get().getId());
                List<GetAllBatchResponse> allBatch=new ArrayList<>();
                List<StockMast> stock= stockBatchService.getAllStockBatch(quality.get().getId());
                //System.out.println("stockc:"+stock.toString());
                stock.forEach(e->{
                  //  System.out.println("e:"+e.getId());
                    List<GetAllBatchResponse> batchDataList = batchDao.findAllQTYControlId(e.getId());
                    //System.out.println("BatchData:"+batchDataList.toString());
                    allBatch.addAll(batchDataList);
                });

                //System.out.print(allBatch);
                return allBatch;

            } else
                throw new Exception("Quality not found");
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }
        return null;
    }

    public List<ShadeIdwithPartyShadeNo> getShadeDetail() {

        List<ShadeMast> shadeMastList= shadeService.getAllShadeMast();
        List<ShadeIdwithPartyShadeNo> shadeIdwithPartyShadeNoList = new ArrayList<>();

        for (ShadeMast e : shadeMastList) {
            ShadeIdwithPartyShadeNo data=new ShadeIdwithPartyShadeNo();
            data.setId(e.getId());
            data.setPartyShadeNo(e.getPartyShadeNo());
            data.setColorTone(e.getColorTone());
            shadeIdwithPartyShadeNoList.add(data);
        }


        return shadeIdwithPartyShadeNoList;
    }

    public List<StockQualityWise> getAllStockByQuality(Long id) throws Exception {


        List<StockQualityWise> stockQualityWiseList=new ArrayList<>();
        List<StockMast> stockMastList = stockBatchService.findByQualityId(id);
        for(StockMast stockMast : stockMastList)
        {
            double qty=0;

            List<BatchData> batchDataList =  batchDao.findByControlId(stockMast.getId());
            for(BatchData batchData:batchDataList)
            {
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

