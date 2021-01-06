package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.model.quality.Quality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("basicServiceImpl")
public class BasicServiceImpl {


    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private PartyDao partyDao;


    public void getPartyByQualityId(Long qualityEntryId)
    {
        Optional<Quality> quality = qualityDao.findById(qualityEntryId);

        if(quality.isPresent())
        {



        }

    }


}
