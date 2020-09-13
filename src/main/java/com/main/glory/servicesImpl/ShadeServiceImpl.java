package com.main.glory.servicesImpl;

import com.main.glory.Dao.ShadeDataDao;
import com.main.glory.Dao.ShadeMastDao;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.services.ShadeServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("ShadeServiceImpl")
public class ShadeServiceImpl implements ShadeServicesInterface {

	@Autowired
	ShadeMastDao shadeMastDao;

	@Autowired
	ShadeDataDao shadeDataDao;

	public void saveShade(ShadeMast shadeMast){
		Date dt = new Date(System.currentTimeMillis());
		shadeMast.setCreatedDate(dt);
		shadeMast.setIsActive(true);
		ShadeMast x = shadeMastDao.save(shadeMast);
		shadeMast.getShadeDataList().forEach(e -> {
			e.setIsActive(true);
			e.setState("original");
			e.setControlId(x.getId());
			e.setCreatedDate(dt);
		});
		shadeDataDao.saveAll(shadeMast.getShadeDataList());
	}
}
