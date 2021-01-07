package com.main.glory.servicesImpl;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.Dao.SupplierRateDao;
import com.main.glory.Dao.qualityProcess.ChemicalDao;
import com.main.glory.Dao.qualityProcess.QualityProcessDataDao;
import com.main.glory.Dao.qualityProcess.QualityProcessMastDao;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.request.UpdateQualityRequest;
import com.main.glory.model.qualityProcess.Chemical;
import com.main.glory.model.qualityProcess.QualityProcessData;
import com.main.glory.model.qualityProcess.QualityProcessMast;
import com.main.glory.model.qualityProcess.request.UpdateRequestQualityProcess;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class QualityProcessImpl {

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

	@Transactional
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

	@Transactional
	public List<QualityProcessMast> qualityProcessMasts(String getBy, Long id) throws Exception{
		List<QualityProcessMast> q = null;
		if(id == null)
			q = qualityProcessMastDao.getAllQualityProcess();
		else if(getBy.equals("own")){
			q = qualityProcessMastDao.findAllByCreatedBy(id);
		}
		else if(getBy.equals("group")){
			q = qualityProcessMastDao.findAllByUserHeadId(id);
		}

		if(q.isEmpty())
			throw new Exception("No data added yet");
		return q;
	}

	@Transactional
	public QualityProcessMast findById(Long id) throws Exception{
		Optional<QualityProcessMast> qualityProcessMast = qualityProcessMastDao.findById(id);
		if(!qualityProcessMast.isPresent()) {
			throw new Exception("no data found");
		}

		List<QualityProcessData> qualityProcessData = qualityProcessDataDao.findByControlId(qualityProcessMast.get().getId());

		qualityProcessMast.get().setSteps(qualityProcessData);

		return qualityProcessMast.get();
	}

	@Transactional
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
}
