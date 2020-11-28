package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.ShadeDataDao;
import com.main.glory.Dao.ShadeMastDao;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.party.Party;
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
		//modelMapper.getConfiguration().setAmbiguityIgnored(true);
		System.out.println("Shade Mast;"+shadeMast.toString());
		//ShadeMast shadeData =  modelMapper.map(shadeMast, ShadeMast.class);
		ShadeMast shadeData =  new ShadeMast(shadeMast);
		System.out.println("\nShadeData"+shadeData.toString());
		shadeData.setQualityEntryId(quality.get().getId());
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

	public List<GetAllShade> getAllShadesInfo(String getBy, Long id) {
		List<ShadeMast> shadeMastList = null;
		List<GetAllShade> getAllShadesList = new ArrayList<>();
		if(id == null){
			shadeMastList = shadeMastDao.findAll();
			for (ShadeMast e : shadeMastList) {
				GetAllShade getShade =  new GetAllShade();
				Optional<Party> party = partyDao.findById(e.getPartyId());
				Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());
				if(!qualityName.isPresent())
					continue;
				if(!party.isPresent())
					continue;
				getShade.setPartyName(party.get().getPartyName());
				getShade.setQualityName(qualityName.get().getQualityName());
				getShade.setId(e.getId());
				getShade.setColorTone(e.getColorTone());
				getShade.setProcessId(e.getProcessId());
				getShade.setProcessName(e.getProcessName());
				getShade.setPartyShadeNo(e.getPartyShadeNo());
				getShade.setQualityId(qualityName.get().getQualityId());
				getShade.setPartyId(party.get().getId());
				getShade.setUserHeadId(e.getUserHeadId());
				getShade.setCreatedBy(e.getCreatedBy());
				getShade.setQualityEntryId(qualityName.get().getId());

				getAllShadesList.add(getShade);
			}
		}
		else if(getBy.equals("own")){
			shadeMastList = shadeMastDao.findAllByCreatedBy(id);
			for (ShadeMast e : shadeMastList) {
				GetAllShade getShade =  new GetAllShade();
				Optional<Party> party = partyDao.findById(e.getPartyId());
				Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());
				if(!qualityName.isPresent())
					continue;
				if(!party.isPresent())
					continue;
				getShade.setPartyName(party.get().getPartyName());
				getShade.setQualityName(qualityName.get().getQualityName());
				getShade.setId(e.getId());
				getShade.setColorTone(e.getColorTone());
				getShade.setProcessId(e.getProcessId());
				getShade.setProcessName(e.getProcessName());
				getShade.setUserHeadId(e.getUserHeadId());
				getShade.setCreatedBy(e.getCreatedBy());
				getShade.setPartyShadeNo(e.getPartyShadeNo());
				getShade.setQualityId(qualityName.get().getQualityId());
				getShade.setPartyId(party.get().getId());
				getShade.setQualityEntryId(qualityName.get().getId());

				getAllShadesList.add(getShade);
			}
		}
		else if(getBy.equals("group")){
			shadeMastList = shadeMastDao.findAllByUserHeadId(id);
			for (ShadeMast e : shadeMastList) {
				GetAllShade getShade =  new GetAllShade();
				Optional<Party> party = partyDao.findById(e.getPartyId());
				Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());
				if(!qualityName.isPresent())
					continue;
				if(!party.isPresent())
					continue;
				getShade.setPartyName(party.get().getPartyName());
				getShade.setQualityName(qualityName.get().getQualityName());
				getShade.setId(e.getId());
				getShade.setColorTone(e.getColorTone());
				getShade.setProcessId(e.getProcessId());
				getShade.setProcessName(e.getProcessName());
				getShade.setPartyShadeNo(e.getPartyShadeNo());
				getShade.setUserHeadId(e.getUserHeadId());
				getShade.setCreatedBy(e.getCreatedBy());
				getShade.setQualityId(qualityName.get().getQualityId());
				getShade.setPartyId(party.get().getId());
				getShade.setQualityEntryId(qualityName.get().getId());

				getAllShadesList.add(getShade);
			}
		}

		return getAllShadesList;
	}
}
