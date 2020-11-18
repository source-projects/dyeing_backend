package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.ProgramDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.model.Party;
import com.main.glory.model.Program;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse;
import com.main.glory.model.quality.Quality;
import com.main.glory.services.ProgramServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BatchImpl batchService;

    @Autowired
    private StockBatchServiceImpl stockBatchService;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private PartyDao partyDao;


    @Autowired
    BatchDao batchDao;

    @Override
//    @Transactional
    public boolean saveProgram(Program program) throws Exception {
        try {

            Optional<Quality> dataQuality = qualityDao.findByQualityId(program.getQuality_id());
            if (dataQuality.isPresent()) {

                Optional<Party> party = partyDao.findById(program.getParty_id());
                if(party.isPresent()) {
                    programDao.saveAndFlush(program);
                    return true;
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional
    public List<Program> getAllProgram() throws Exception {
        return programDao.findAll();
    }

    @Override
    public Program getProgramById(Long id) throws Exception {
        if (id != null) {
            var findProgram = programDao.findById(id);
            if (findProgram.isEmpty()) {
                System.out.println("No Record Found");
                return null;
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

        if (bm.getId() != null) {
            var findProgram = programDao.findById(bm.getId());
            if (findProgram.isPresent()) {
                programDao.saveAndFlush(bm);
                return true;
            }
            else
            {
                System.out.println("Record not Found");
                return false;
            }


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
}
