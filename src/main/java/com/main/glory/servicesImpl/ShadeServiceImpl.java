package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.ShadeDataDao;
import com.main.glory.Dao.ShadeMastDao;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.services.ShadeServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
		if(shadeMastList.isEmpty())
			return null;
		else{
			return shadeMastList;
		}
	}

	@Override
	public Optional<ShadeMast> getShadeMastById(Long id) {
		Optional<ShadeMast> shadeMastList = shadeMastDao.findById(id);
		if(shadeMastList.isPresent())
			return shadeMastList;
		else{
			return null;
		}
	}

	@Override
	public Boolean updateShade(ShadeMast shadeMast) {
		var shadeIndex = shadeMastDao.findById(shadeMast.getId());
		if(!shadeIndex.isPresent())
			return false;
		else{
			try{
				System.out.println(shadeMast);
				shadeMastDao.save(shadeMast);
				//shadeDataDao.saveAll(shadeMast.getShadeDataList());
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}

		}
		return true;
	}

	@Transactional
	public boolean deleteShadeById(Long id) throws Exception{
		Optional<ShadeMast> shadeMast = shadeMastDao.findById(id);
		// check if this is present in the database
		if(shadeMast.isEmpty()){
			throw new Exception("shade data does not exist with id:"+id);
		}
		shadeMastDao.deleteById(id);

		return true;
	}

}
