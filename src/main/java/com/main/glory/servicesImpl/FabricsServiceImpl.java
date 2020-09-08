package com.main.glory.servicesImpl;

import java.util.List;

import javax.transaction.Transactional;

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
	@Transactional
	public int saveFabrics(Fabric fabrics) throws Exception {
		try {
			if (fabrics!=null)
			{
				
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
	public boolean editFabricsDetails(Fabric fabs) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFabricsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
