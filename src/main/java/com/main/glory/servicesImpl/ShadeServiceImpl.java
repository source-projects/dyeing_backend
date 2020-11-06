package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.ShadeDataDao;
import com.main.glory.Dao.ShadeMastDao;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.UpdateShadeMastRequest;
import com.main.glory.model.shade.responsemodals.ShadeMastWithDetails;
import com.main.glory.services.ShadeServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("ShadeServiceImpl")
public class ShadeServiceImpl implements ShadeServicesInterface {

	@Autowired
	ShadeMastDao shadeMastDao;

	@Autowired
	ShadeDataDao shadeDataDao;

	@Autowired
	QualityDao qualityDao;

	@Autowired
	PartyDao partyDao;

	@Transactional
	public void saveShade(ShadeMast shadeMast){
		Date dt = new Date(System.currentTimeMillis());
		shadeMast.setCreatedDate(dt);
		shadeMast.setIsActive(true);
		ShadeMast x = shadeMastDao.save(shadeMast);
		shadeMast.getShadeDataList().forEach(e -> {
			e.setControlId(x.getId());
			e.setCreatedDate(dt);
		});
		shadeDataDao.saveAll(shadeMast.getShadeDataList());
	}

	@Override
	public List<ShadeMast> getAllShadeMast() {
		List<ShadeMast> shadeMastList = shadeMastDao.findAll();
		shadeMastList.forEach(shadeMast -> {
			List<ShadeData> sd = shadeDataDao.findByControlId(shadeMast.getId());
			//shadeMast.getShadeDataList().
		});
		if(shadeMastList.isEmpty())
			return null;
		else{
			return shadeMastList;
		}
	}


}
