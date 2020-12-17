package com.main.glory.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.ShadeMastDao;
import com.main.glory.model.party.request.AddParty;
import com.main.glory.model.party.request.PartyWithName;
import com.main.glory.model.party.request.PartyWithPartyCode;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import com.main.glory.Dao.PartyDao;
import com.main.glory.model.party.Party;
import com.main.glory.services.PartyServiceInterface;

@Service("partyServiceImp")
public class PartyServiceImp implements PartyServiceInterface{

	@Autowired
	QualityServiceImp qualityServiceImp;

	@Autowired
	private PartyDao partyDao;

	@Autowired
	ShadeMastDao shadeMastDao;

	@Autowired
	QualityDao qualityDao;

	@Autowired
	ModelMapper modelMapper;

	public void saveParty(AddParty party) throws Exception {
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		if(party!=null)
		{
			Party partyData = new Party(party);

			if(party.getGSTIN().isEmpty())
			{
				partyDao.save(partyData);
				return;
			}


			if(party.getPartyCode().length()>4)
				throw new Exception("Party code should not be greater than 4 digit");

			Optional<Party> gstAvailable = partyDao.findByGSTIN(party.getGSTIN());
			Optional<Party> partyCodeAvailable = partyDao.findByPartyCode(party.getPartyCode());

			if(gstAvailable.isPresent())
				throw new Exception("GST No."+party.getGSTIN()+" is already exist");

			if(partyCodeAvailable.isPresent())
				throw new Exception("Party is availble with code:"+party.getPartyCode());

			partyDao.save(partyData);

		}
	}

	@Override
	public List<Party> getAllPartyDetails(Long id,String getBy) {
		List<Party> partyDetailsList = null;
		if(id == null){
			partyDetailsList=partyDao.findAllParty();
		}
		else if(getBy.equals("group")){
			partyDetailsList=partyDao.findByUserHeadId(id);
		}
		else if(getBy.equals("own")){
			partyDetailsList=partyDao.findByCreatedBy(id);
		}
		return partyDetailsList;
	}

	@Override
	public Party getPartyDetailById(Long id) {
		var partyData=partyDao.findByPartyId(id);
		if(partyData.isEmpty())
			return null;
		else
			return partyData.get();
	}

	@Override
	public boolean editPartyDetails(Party party) throws Exception {
  		var partyIndex= partyDao.findByPartyId(party.getId());
  		Optional<Party> party1 = partyDao.findByPartyCode(party.getPartyCode());
  		if(party1.isPresent())
  			throw new Exception("Party code should be unique");
  		party1=partyDao.findByGSTIN(party.getGSTIN());
  		if(!party1.isEmpty())
  			throw new Exception("GST No."+party1.get().getGSTIN()+" is already available");

  		if(party.getGSTIN().isEmpty())
		{
			partyDao.save(party);
			return true;
		}


  		party1=partyDao.findByGSTIN(party.getGSTIN());

  		if(party1.isPresent())
  			throw new Exception("GST is already availble");

		if(!partyIndex.isPresent())
		return false;
		else
		partyDao.save(party);
		  return true;	
	}

	@Override
	public boolean deletePartyById(Long id) throws Exception {

  		var partyIndex= partyDao.findByPartyId(id);
		if(!partyIndex.isPresent())
		return false;
		else
		{
			partyIndex.get().setPartyIsDeleted(true);

			//soft delete of quality
			Optional<List<Quality>> qualityList =qualityServiceImp.getAllQualityByPartyId(partyIndex.get().getId());
			if(qualityList.isPresent()) {
				for (Quality quality : qualityList.get()) {
					quality.setQualityIsDeleted(true);

				}
			}

			//soft delete for shade
			Optional<List<ShadeMast>> shadeMastList=shadeMastDao.findByPartyId(id);
			if(shadeMastList.isPresent())
			{
				for(ShadeMast shadeMast:shadeMastList.get())
				{
					shadeMast.setShadeIsDeleted(true);
				}

			}


			partyDao.save(partyIndex.get());
			return true;
		}


	}

	@Override
	public String getPartyNameByPartyId(Long partyId) {
		String partyName=partyDao.getPartyNameByPartyId(partyId);
		return partyName;
	}

    public List<PartyWithName> getAllPartyNameWithId() {
		try {
			List<Party> partyAll = partyDao.findAllParty();

			List<PartyWithName> partyWithNameList = new ArrayList<>();
			if(!partyAll.isEmpty()) {
				partyAll.forEach(e ->
				{
					PartyWithName partyWithName = new PartyWithName(e.getId(), e.getPartyName());
					System.out.println(partyWithName.getId());
					partyWithNameList.add(partyWithName);
				});
			}
			return partyWithNameList;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
    }

    public Boolean partyCodeExistOrNot(String partyCode) {

		Optional<Party> party = partyDao.findByPartyCode(partyCode);

		if(!party.isPresent())
			return true;

		return false;
    }
}
