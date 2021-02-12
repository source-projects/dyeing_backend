package com.main.glory.servicesImpl;

import com.main.glory.Dao.*;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.APC;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.AddShadeMast;
import com.main.glory.model.shade.requestmodals.GetAPC;
import com.main.glory.model.shade.requestmodals.GetAllShade;
import com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality;
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
	APCDao acpDao;
	
	@Autowired
	UserDao userDao;

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
	DyeingProcessServiceImpl dyeingProcessService;

	@Autowired
	ModelMapper modelMapper;


	@Transactional

	public void saveShade(AddShadeMast shadeMast) throws Exception{

		if (shadeMast.getPending()==true)
		{
			Optional<Quality> quality=qualityDao.findByQualityIdAndQualityName(shadeMast.getQualityId(),shadeMast.getQualityName());
			if(!quality.isPresent())
			{
				throw new Exception("Quality Not Found with QualityId:"+shadeMast.getQualityId()+" and QualityName:"+shadeMast.getQualityName());
			}

			//check the dyeing process for the shade is available or not

			DyeingProcessMast processMastExist = dyeingProcessService.getDyeingProcessById(shadeMast.getProcessId());

			ShadeMast shadeData =  new ShadeMast(shadeMast);

			shadeData.setQualityEntryId(quality.get().getId());

			Date dt = new Date(System.currentTimeMillis());


			//check the ACP number

			APC numberExist =acpDao.getAcpNumberExist(Long.parseLong(shadeMast.getApcNo().substring(3)));

			if(numberExist!=null)
				throw new Exception("APC number is already available");

			APC apc=new APC(Long.parseLong(shadeMast.getApcNo().substring(3)));
			acpDao.save(apc);
			shadeMastDao.save(shadeData);





		}



	}


	@Override
	public List<ShadeMast> getAllShadeMast() throws Exception{
		List<ShadeMast> shadeMastList = shadeMastDao.getAllShadeMast();
		if(shadeMastList.isEmpty())
			throw new Exception("no party shade found");
		else{
			return shadeMastList;
		}
	}

	@Override
	public Optional<ShadeMast> getShadeMastById(Long id) throws Exception {
		Optional<ShadeMast> shadeMastList = shadeMastDao.findById(id);

		if(shadeMastList.isPresent() && shadeMastList.get().getPartyId()!=null && shadeMastList.get().getQualityEntryId()!=null)
			return shadeMastList;
		else{
			throw new Exception("shade data not found");
		}
	}

	@Override
	public Boolean updateShade(ShadeMast shadeMast) {
		ShadeMast shadeIndex = shadeMastDao.getShadeMastById(shadeMast.getId());
		if(shadeIndex==null)
			return false;
		else{
			try{
				//System.out.println(shadeMast);
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

	public List<GetAllShade> getAllShadesInfo(String getBy, Long id) throws Exception {
		List<ShadeMast> shadeMastList = null;
		List<GetAllShade> getAllShadesList = new ArrayList<>();
		if(id == null){
			shadeMastList = shadeMastDao.getAllShadeMast();
			for (ShadeMast e : shadeMastList) {

				if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
				{
					Optional<Party> party = partyDao.findById(e.getPartyId());
					Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());

					if(!qualityName.isPresent())
						continue;
					if(!party.isPresent())
						continue;

					getAllShadesList.add(new GetAllShade(e,party,qualityName));

				}

			}
		}
		else if(getBy.equals("own")){
			shadeMastList = shadeMastDao.findAllByCreatedBy(id);
			for (ShadeMast e : shadeMastList) {

				if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
				{
					Optional<Party> party = partyDao.findById(e.getPartyId());
					Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());
					if(!qualityName.isPresent())
						continue;
					if(!party.isPresent())
						continue;

					getAllShadesList.add(new GetAllShade(e,party,qualityName));

				}
			}
		}
		else if(getBy.equals("group")){
			UserData userData = userDao.findUserById(id);

			if(userData.getUserHeadId()==0) {
				//master user
				shadeMastList = shadeMastDao.findAllByCreatedByAndHeadId(id,id);
				for (ShadeMast e : shadeMastList) {

					if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
					{
						Optional<Party> party = partyDao.findById(e.getPartyId());
						Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());
						if(!qualityName.isPresent())
							continue;
						if(!party.isPresent())
							continue;

						getAllShadesList.add(new GetAllShade(e,party,qualityName));


					}
				}
			}
			else
			{
				shadeMastList = shadeMastDao.findAllByCreatedByAndHeadId(id,userData.getUserHeadId());
				for (ShadeMast e : shadeMastList) {

					if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
					{
						Optional<Party> party = partyDao.findById(e.getPartyId());
						Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());
						if(!qualityName.isPresent())
							continue;
						if(!party.isPresent())
							continue;
						getAllShadesList.add(new GetAllShade(e,party,qualityName));


					}
				}

			}


		}

		if(getAllShadesList.isEmpty())
			throw new Exception("no data found");
		return getAllShadesList;
	}

    public List<GetShadeByPartyAndQuality> getShadesByQualityAndPartyId(Long qualityId, Long partyId) throws Exception{
		List<GetShadeByPartyAndQuality> shadeByPartyAndQualities = shadeMastDao.findByQualityEntryIdAndPartyId(qualityId,partyId);
		if(shadeByPartyAndQualities.isEmpty())
			throw new Exception("shade data not found");

		return shadeByPartyAndQualities;

    }

	public ShadeMast getShadesByShadeAndQualityAndPartyId(Long shadeId, Long qualityEntryId, Long partyId) throws Exception{
		Optional<ShadeMast> shadeMast = shadeMastDao.findById(shadeId);
		if(shadeMast.isEmpty())
			throw new Exception("Shade not found for quality or party");

		return shadeMast.get();
	}

    public List<GetShadeByPartyAndQuality> getAllShadeByPartyAndQuality(Long partyId, Long qualityId) throws Exception{

		Optional<Quality> quality=qualityDao.findById(qualityId);
		if(quality.isEmpty())
			throw new Exception("no quality is found");

		List<GetShadeByPartyAndQuality> list=new ArrayList<>();

		List<GetShadeByPartyAndQuality> shadeByPartyAndQualities = shadeMastDao.findByQualityEntryIdAndPartyId(qualityId,partyId);
		if(shadeByPartyAndQualities.isEmpty())
			throw new Exception("shade data not found");

		return shadeByPartyAndQualities;


	}

	public GetAPC getAPCNumber() {
		
		Long number = acpDao.getAcpNumber();
		if(number==null)
		{
			APC acp =new APC(1l);
			APC x = acpDao.save(acp);
			GetAPC getAcp = new GetAPC(x);
			return getAcp;

		}
		else
		{
			GetAPC x=new GetAPC(++number);
			return x;
		}


	}

	public Boolean isAPCExist(String number) {

		Long data = Long.parseLong(number.substring(3));

		APC flag = acpDao.getAcpNumberExist(data);
		if(flag==null)
			return true;
		else
			return false;
	}
}
