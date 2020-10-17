package com.main.glory.servicesImpl;

import com.main.glory.Dao.ProgramDao;
import com.main.glory.Dao.batch.BatchDataDao;
import com.main.glory.Dao.batch.BatchMastDao;
import com.main.glory.model.Program;
import com.main.glory.model.ProgramRecord;
import com.main.glory.model.batch.BatchData;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.services.ProgramServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service("programServiceImpl")
public class ProgramServiceImpl implements ProgramServiceInterface {

    @Autowired
    private ProgramDao programDao;

    @Autowired
    private BatchMastDao batchMastDao;

    @Autowired
    private BatchDataDao batchDataDao;


    @Override
    @Transactional
    public boolean saveProgram(Program program) throws Exception {

        if (program!= null) {

            if(program.getProgramRecords() == null){
                throw new Exception("Program record can not be null");
            }

            for (ProgramRecord programRecord : program.getProgramRecords()) {
                Optional<BatchMast> batchMast = batchMastDao.findById(programRecord.getBatchId());

                if (batchMast.isEmpty()) {
                    throw new Exception("No batch present with id:" + programRecord.getId());
                }

                if (batchMast.get().getIsProductionPlaned()){
                    throw new Exception("Production already planned for batchId:"+programRecord.getId());
                }

                batchMast.get().setIsProductionPlaned(true);

            }

            programDao.saveAndFlush(program);
            return true;
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
            programDao.deleteById(id);
            return true;
        } else
            return false;
    }

    @Override
    public boolean updateProgramByID(Program bm) throws Exception {
        if (bm.getId() != null) {
            var findProgram = programDao.findById(bm.getId());
            if (findProgram.get().getId() != null) {
                programDao.saveAndFlush(bm);
                return true;
            }
            System.out.println("Record not Found");
        }
        return false;
    }
}
