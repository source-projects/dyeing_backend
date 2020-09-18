package com.main.glory.servicesImpl;

import com.main.glory.Dao.qualityProcess.QualityProcessDataDao;
import com.main.glory.Dao.qualityProcess.QualityProcessMastDao;
import com.main.glory.Dao.qualityProcess.QualityTypeDataDao;
import com.main.glory.model.qualityProcess.QualityProcessData;
import com.main.glory.model.qualityProcess.QualityProcessMast;
import com.main.glory.model.qualityProcess.QualityTypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QualityProcessImpl {

	@Autowired
	QualityProcessMastDao qualityProcessMastDao;

	@Autowired
	QualityProcessDataDao qualityProcessDataDao;

	@Autowired
	QualityTypeDataDao qualityTypeDataDao;

	public void saveQualityProcess(QualityProcessMast qualityProcessMast){
		QualityProcessMast m = qualityProcessMastDao.save(qualityProcessMast);
		qualityProcessMast.getQualityProcessData().forEach(e -> {
			e.setControlId(m.getId());
			QualityProcessData d = qualityProcessDataDao.save(e);
			e.getQualityTypeData().forEach(ee -> {
				ee.setControlId(d.getId());
				qualityTypeDataDao.save(ee);
			});
		});
	}

	public List<QualityProcessMast> qualityProcessMasts(){
		return qualityProcessMastDao.findAll();
	}

	public QualityProcessMast findById(Long id){
		Optional<QualityProcessMast> qualityProcessMast = qualityProcessMastDao.findById(id);
		if(!qualityProcessMast.isPresent()) {
			return null;
		}

		List<QualityProcessData> qualityProcessData = qualityProcessDataDao.findByControlId(qualityProcessMast.get().getId());

		qualityProcessData.forEach(e -> {
			List<QualityTypeData> qualityTypeData = qualityTypeDataDao.findByControlId(e.getId());
			e.setQualityTypeData(qualityTypeData);
		});

		qualityProcessMast.get().setQualityProcessData(qualityProcessData);

		return qualityProcessMast.get();
	}


}
