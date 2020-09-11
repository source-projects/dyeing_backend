package com.main.glory.servicesImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.main.glory.FabInMasterLookUp.MasterLookUpWithRecord;
import com.main.glory.model.FabricInRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.glory.Dao.FabricsDao;
import com.main.glory.model.Fabric;
import com.main.glory.services.FabricsServicesInterface;

@Service("fabricsServiceImpl")
public class FabricsServiceImpl implements FabricsServicesInterface {

	@Autowired
	private FabricsDao fabricsDao;

	@Override
//	@Transactional
	public int saveFabrics(Fabric fabrics) throws Exception {
		try {
			if (fabrics != null) {
				fabricsDao.saveAndFlush(fabrics);
				return 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Fabric> getAllFabricsDetails() {
		// TODO Auto-generated method stub
		return fabricsDao.findAll();
	}

	@Override
	public boolean updateFabricsDetails(Fabric fabs) throws Exception {
		var partyIndex= fabricsDao.findById(fabs.getId());
		if(!partyIndex.isPresent())
			return false;
		else
			fabricsDao.save(fabs);
		return true;
	}


	@Override
	public boolean deleteFabricsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<MasterLookUpWithRecord> getFabStockMasterListRecord() {
		List<MasterLookUpWithRecord> listMaster = fabricsDao.getFabStockMasterRecordList();
		return listMaster;
	}

	@Override
	public Fabric getFabRecordById(Long id) {
		var getData=fabricsDao.findById(id);
		if(getData.isEmpty())
			return null;
		else {
			return getData.get();
		}
	}
}


