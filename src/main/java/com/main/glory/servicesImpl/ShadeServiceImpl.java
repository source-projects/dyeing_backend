package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.ShadeDataDao;
import com.main.glory.Dao.ShadeMastDao;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.AddShadeMast;
import com.main.glory.model.shade.requestmodals.GetAllShade;
import com.main.glory.model.user.UserData;
import com.main.glory.services.ShadeServicesInterface;
import org.modelmapper.ModelMapper;
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
	SupplierServiceImpl supplierService;

	@Autowired
	QualityDao qualityDao;

	@Autowired
	PartyDao partyDao;


	@Autowired
	ModelMapper modelMapper;


	@Transactional

	public void saveShade(AddShadeMast shadeMast) throws Exception{

		Optional<Quality> quality=qualityDao.findByQualityIdAndQualityName(shadeMast.getQualityId(),shadeMast.getQualityName());
		if(!quality.isPresent())
		{
			throw new Exception("Quality Not Found with QualityId:"+shadeMast.getQualityId()+" and QualityName:"+shadeMast.getQualityName());
		}
		//System.out.println("QUQUQUQUQUQU:"+quality.get().toString());
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		System.out.println("Shade Mast;"+shadeMast.toString());
		ShadeMast shadeData =  modelMapper.map(shadeMast, ShadeMast.class);
		System.out.println("\nShadeData"+shadeData.toString());
		shadeData.setQualityId(quality.get());
		shadeData.setCuttingId(0l);
		shadeData.setUserHeadId(0l);


/*
		shadeData.setQualityId(quality);
		shadeData.setCreatedDate(dt);
*/

		Date dt = new Date(System.currentTimeMillis());

		ShadeMast x = shadeMastDao.save(shadeData);
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

	public List<GetAllShade> getAllShadesInfo() {
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		List<ShadeMast> shadeMastList = shadeMastDao.findAll();
		List<GetAllShade> getAllShadesList = new ArrayList<>();
		for (ShadeMast e : shadeMastList) {

			GetAllShade getShade =  modelMapper.map(e, GetAllShade.class);
			getShade.setPartyName(partyDao.getPartyNameByPartyId(getShade.getPartyId()));
			Optional<Quality> qualityName=qualityDao.findByQualityId(getShade.getQualityId());
			if(qualityName.isPresent()) {
				getShade.setQualityName(qualityName.get().getQualityName());
			}
			getAllShadesList.add(getShade);
		}
		return getAllShadesList;

	}
}
