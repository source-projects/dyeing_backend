package servicesImpl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Dao.FabDataDao;
import Dao.FabricsDao;
import model.Fabric;
import model.FabricInRecord;
import services.FabricsServicesInterface;

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
