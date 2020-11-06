package com.main.glory.servicesImpl;

import com.main.glory.Dao.ProgramDao;
import com.main.glory.model.Program;
import com.main.glory.services.ProgramServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("programServiceImpl")
public class ProgramServiceImpl implements ProgramServiceInterface {

    @Autowired
    private ProgramDao programDao;


    @Override
//    @Transactional
    public boolean saveProgram(Program program) throws Exception {
        try {
            if (program!= null) {
                programDao.saveAndFlush(program);
                return true;
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
}
