package com.main.glory.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.model.Quality;
import com.main.glory.services.QualityServiceInterface;

@Service("qualityServiceImp")
public class QualityServiceImp implements QualityServiceInterface{

	@Autowired
	private QualityDao qualityDao;
	
	@Autowired
	private PartyDao partyDao;
	
	@Override
	public int saveQuality(Quality quality) {
		 
		qualityDao.save(quality);
		return 1;
	}

	@Override
	public List<Quality> getAllQuality() {
		List<Quality> qualityLisobject=qualityDao.findAll();
//		qualityLisobject.forEach(c->
//		{
//			if(c.getParty_id()!=null)
//			{
//				var partyIndex=  partyDao.findById(c.getParty_id());
//				
//				if(partyIndex!=null && c.getParty_name()==null)
//				{ 
//					if(partyIndex.get().getId()==c.getId())
//					{
//						c.setParty_name(partyIndex.get().getParty_name());
//					}
//					 
//				}
//			}
//		});
		return qualityLisobject;
	}

	@Override
	public boolean updateQuality(Quality quality) throws Exception {
		var partyIndex= qualityDao.findById(quality.getId());
		if(!partyIndex.isPresent())
		return false;
		else
			qualityDao.save(quality);
		  return true;	
	}

	@Override
	public boolean deleteQualityById(Long id) {
		var partyIndex= qualityDao.findById(id);
		if(!partyIndex.isPresent())
		return false;
		else
			qualityDao.deleteById(id);
		 return true;	
	}

}
