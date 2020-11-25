package com.main.glory.servicesImpl;

import com.main.glory.Dao.qualityProcess.QualityProcessDataDao;
import com.main.glory.Dao.qualityProcess.QualityProcessMastDao;
import com.main.glory.Dao.qualityProcess.QualityTypeDataDao;
import com.main.glory.model.qualityProcess.QualityProcessData;
import com.main.glory.model.qualityProcess.QualityProcessMast;
import com.main.glory.model.qualityProcess.QualityTypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

	@Transactional
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

	@Transactional
	public List<QualityProcessMast> qualityProcessMasts(String getBy, Long id){
		List<QualityProcessMast> q = null;
		if(id == null)
			q = qualityProcessMastDao.findAll();
		else if(getBy.equals("own")){
			q = qualityProcessMastDao.findAllByCreatedBy(id);
		}
		else if(getBy.equals("group")){
			q = qualityProcessMastDao.findAllByUserHeadId(id);
		}
		return q;
	}

	@Transactional
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

	@Transactional
	public void update(QualityProcessMast qualityProcessMast) throws Exception {
		Optional<QualityProcessMast> qualityProcessMast1 = qualityProcessMastDao.findById(qualityProcessMast.getId());
		if(!qualityProcessMast1.isPresent()){
			throw new Exception("No such process data with id:"+ qualityProcessMast1.get());
		} else {
			qualityProcessMastDao.save(qualityProcessMast);
			qualityProcessDataDao.saveAll(qualityProcessMast.getQualityProcessData());
			qualityProcessMast.getQualityProcessData().forEach(e -> {
				qualityTypeDataDao.saveAll(e.getQualityTypeData());
			});
		}
	}


}
