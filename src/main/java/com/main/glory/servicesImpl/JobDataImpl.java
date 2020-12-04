package com.main.glory.servicesImpl;

import com.main.glory.Dao.jobcard.JobDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("jobDataImpl")
public class JobDataImpl {

    @Autowired
    JobDataDao jobDataDao;
}
