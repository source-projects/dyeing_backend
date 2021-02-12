package com.main.glory.servicesImpl;

import com.main.glory.Dao.designation.DesignationDao;
import com.main.glory.model.designation.Designation;
import com.main.glory.services.DesignationServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("designationServiceImpl")
public class DesignationServiceImpl implements DesignationServiceInterface
{

    @Autowired
    DesignationDao designationDao;

    public int createDesignation(Designation designationData) {

        if(designationData!=null)
        {
            designationDao.saveAndFlush(designationData);
            return 1;
        }

        return 0;
    }

    public Optional<Designation> getDesignationById(Long id) throws Exception{
        if(id!=null)
        {
            Optional<Designation> designation = designationDao.findById(id);
            if(designation.isEmpty())
                throw new Exception("no data found");
            return designation;
        }

        return null;
    }

    public List<Designation> getDesignation() throws Exception {

        List<Designation> designationList=designationDao.findAllExceptAdmin();
        if (designationList.isEmpty())
            throw new Exception("no data found");

        return designationList;
    }

    public Boolean deleteDesignationById(Long id) throws Exception {

        try {
            Designation designationExist = designationDao.getDesignationById(id);
            if (designationExist == null)
                throw new Exception("no designation found");

            designationDao.deleteDesignationById(id);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
