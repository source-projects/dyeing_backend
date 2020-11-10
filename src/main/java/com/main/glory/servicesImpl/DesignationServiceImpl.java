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

    public Optional<Designation> getDesignationById(Long id) {
        if(id!=null)
        {
            Optional<Designation> designation = designationDao.findById(id);
            return designation;
        }

        return null;
    }

    public List<Designation> getDesignation() {
        return designationDao.findAll();
    }
}
