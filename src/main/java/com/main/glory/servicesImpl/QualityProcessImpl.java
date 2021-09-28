package com.main.glory.servicesImpl;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.Dao.SupplierRateDao;
import com.main.glory.Dao.qualityProcess.ChemicalDao;
import com.main.glory.Dao.qualityProcess.QualityProcessDataDao;
import com.main.glory.Dao.qualityProcess.QualityProcessMastDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.color.ColorBox;
import com.main.glory.model.color.ColorData;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.request.UpdateQualityRequest;
import com.main.glory.model.qualityProcess.Chemical;
import com.main.glory.model.qualityProcess.QualityProcessData;
import com.main.glory.model.qualityProcess.QualityProcessMast;
import com.main.glory.model.qualityProcess.request.UpdateRequestQualityProcess;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.user.UserData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AutoPopulatingList;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.CheckedInputStream;

@Service
public class QualityProcessImpl {

	@Autowired
	UserDao userDao;
	@Autowired
	ColorServiceImpl colorService;

	@Autowired
	QualityProcessMastDao qualityProcessMastDao;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	SupplierDao supplierDao;

	@Autowired
	SupplierRateDao supplierRateDao;

	@Autowired
	QualityProcessDataDao qualityProcessDataDao;

	@Autowired
	ChemicalDao chemicalDao;


	public void saveQualityProcess(QualityProcessMast qualityProcessMast) throws Exception{
		QualityProcessMast m = qualityProcessMastDao.save(qualityProcessMast);
		for (QualityProcessData e : m.getSteps()) {
			e.setControlId(m.getId());
			if (e.getIsDosingControl()) {
				for(Chemical chemical:e.getDosingChemical())
				{
					Optional<Chemical> c = chemicalDao.findById(chemical.getId());
					if (!c.isPresent()) {
						chemicalDao.deleteById(chemical.getId());
						qualityProcessDataDao.deleteById(e.getId());
						qualityProcessMastDao.deleteById(m.getId());
						throw new Exception("Null chemical data passed");
					}
					if (chemical.getSupplierId() == null) {
						chemicalDao.deleteById(chemical.getId());
						qualityProcessDataDao.deleteById(e.getId());
						qualityProcessMastDao.deleteById(m.getId());
						throw new Exception("Null supplier id passed");
					} else {
						Optional<Supplier> s = supplierDao.findById(chemical.getSupplierId());
						if (s.isPresent())
							chemical.setQualityProcessControlId(e.getId());
						else{
							chemicalDao.deleteById(chemical.getId());
							qualityProcessDataDao.deleteById(e.getId());
							qualityProcessMastDao.deleteById(m.getId());
							throw new Exception("No supplier found with id:" + chemical.getSupplierId());
						}

					}

				}

			}
		}
		qualityProcessDataDao.saveAll(qualityProcessMast.getSteps());
	}


	public List<QualityProcessMast> qualityProcessMasts(String getBy, Long id) throws Exception{
		List<QualityProcessMast> q = null;
		if(id == null)
			q = qualityProcessMastDao.getAllQualityProcess();
		else if(getBy.equals("own")){
			q = qualityProcessMastDao.findAllByCreatedBy(id);
		}
		else if(getBy.equals("group")){
			UserData userData = userDao.findUserById(id);

			if(userData.getUserHeadId()==0) {
				//master user
				q = qualityProcessMastDao.findAllUserHeadAndCreatedBy(id,id);
			}
			else
			{
				q = qualityProcessMastDao.findAllUserHeadAndCreatedBy(id,userData.getUserHeadId());
			}
		}

		if(q.isEmpty())
			throw new Exception("No data added yet");
		return q;
	}


	public QualityProcessMast findById(Long id) throws Exception{
		Optional<QualityProcessMast> qualityProcessMast = qualityProcessMastDao.findById(id);
		if(!qualityProcessMast.isPresent()) {
			throw new Exception("no data found for process"+id);
		}

		List<QualityProcessData> qualityProcessData = qualityProcessDataDao.findByControlId(qualityProcessMast.get().getId());

		qualityProcessMast.get().setSteps(qualityProcessData);

		return qualityProcessMast.get();
	}


	public void update(UpdateRequestQualityProcess qualityProcessMast) throws Exception {
		Optional<QualityProcessMast> q = qualityProcessMastDao.findById(qualityProcessMast.getId());
		if(!q.isPresent()){
			throw new Exception("No process data found with id: "+qualityProcessMast.getId());
		}
		for(QualityProcessData e: q.get().getSteps()){
			if(e.getIsDosingControl()){

				for(Chemical chemical:e.getDosingChemical())
				{
					Optional<Supplier> s = supplierDao.findById(chemical.getSupplierId());
					if(!s.isPresent())
						throw new Exception("No supplier found with id: "+chemical.getSupplierId());

				}

			}
		}
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		QualityProcessMast qualityProcess = modelMapper.map(qualityProcessMast, QualityProcessMast.class);
		qualityProcessMastDao.save(qualityProcess);
		}

		public Boolean deleteQualityProcess(Long id){
			qualityProcessMastDao.deleteById(id);
			return true;
		}

	public void saveQualityProcessMast(QualityProcessMast qualityProcessMast) throws Exception {
		int x=0;
		if(qualityProcessMast.getSteps().get(x).getDosingChemical()!=null)
		{
			for(Chemical qualityChemicalData:qualityProcessMast.getSteps().get(x).getDosingChemical())
			{
				Optional<Supplier> supplierExist=supplierDao.findById(qualityChemicalData.getSupplierId());
				Optional<SupplierRate> supplierItemExist  = supplierRateDao.findByIdAndSupplierId(qualityChemicalData.getItemId(),qualityChemicalData.getSupplierId());


				if(supplierExist.isEmpty() || supplierItemExist.isEmpty())
					throw new Exception("Supplier item is not available");


				x++;
			}
		}

		qualityProcessMastDao.save(qualityProcessMast);
	}

    public List<QualityProcessData> getQualityProcessDataDoseTypeChemical(Long id) throws Exception {

		Optional<QualityProcessMast> qualityProcessMast=qualityProcessMastDao.findById(id);

		if(qualityProcessMast.isEmpty())
			throw new Exception("no quality process found for id:"+id);


		List<QualityProcessData> chemicalProcessList=qualityProcessDataDao.findByControlIdAndDoseTypeChemical(qualityProcessMast.get().getId(),"chemical");

		if(chemicalProcessList.isEmpty())
			throw new Exception("no quality data found");

		return chemicalProcessList;


    }
}
