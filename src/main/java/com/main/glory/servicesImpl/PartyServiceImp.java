package com.main.glory.servicesImpl;

import java.util.List;
import java.util.Optional;

import com.main.glory.model.party.request.AddParty;
import com.main.glory.model.quality.Quality;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.glory.Dao.PartyDao;
import com.main.glory.model.party.Party;
import com.main.glory.services.PartyServiceInterface;

@Service("partyServiceImp")
public class PartyServiceImp implements PartyServiceInterface{

	@Autowired
	private PartyDao partyDao;

	@Autowired
	ModelMapper modelMapper;

	public int saveParty(AddParty party) {
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		if(party!=null)
		{
			Party partyData = new Party(party);
			partyDao.save(partyData);
			return 1;
		}else
			return 0;
	}

	@Override
	public List<Party> getAllPartyDetails(Long id,String getBy) {
		List<Party> partyDetailsList = null;
		if(id == null){
			partyDetailsList=partyDao.findAll();
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
		var partyData=partyDao.findById(id);
		if(partyData.isEmpty())
			return null;
		else
			return partyData.get();
	}

	@Override
	public boolean editPartyDetails(Party party) throws Exception {
  		var partyIndex= partyDao.findById(party.getId());
		if(!partyIndex.isPresent())
		return false;
		else
		partyDao.save(party);
		  return true;	
	}

	@Override
	public boolean deletePartyById(Long id) {

  		var partyIndex= partyDao.findById(id);
		if(!partyIndex.isPresent())
		return false;
		else
		partyDao.deleteById(id);
		 return true;	
	}

	@Override
	public String getPartyNameByPartyId(Long partyId) {
		String partyName=partyDao.getPartyNameByPartyId(partyId);
		return partyName;
	}
}
