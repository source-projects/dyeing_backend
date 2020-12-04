package com.main.glory.servicesImpl;

import com.main.glory.Dao.jobcard.JobDataDao;
import com.main.glory.Dao.jobcard.JobMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.jobcard.JobData;
import com.main.glory.model.jobcard.JobMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("jobMastImpl")
public class JobMastImpl {

    @Autowired
    JobMastDao jobMastDao;

    @Autowired
    JobDataDao jobDataDao;


    public Optional<JobMast> getJobMastById(Long id) {
        Optional<JobMast> jobMast = jobMastDao.findById(id);
        if(jobMast.isPresent())
        {
            return jobMast;
        }
        return null;
    }

    public Boolean createJobCard(StockMast x) {

        JobMast jobMast = new JobMast(x);
        List<JobData> jobDataList=new ArrayList<>();

        for(BatchData batchData: x.getBatchData())
        {
            JobData jobData=new JobData(batchData);
            jobDataList.add(jobData);
        }
        jobMast.setJobData(jobDataList);
        jobMastDao.save(jobMast);
        return true;

    }
}
