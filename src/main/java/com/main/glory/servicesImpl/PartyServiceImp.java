package com.main.glory.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.glory.Dao.PartyDao;
import com.main.glory.model.Party;
import com.main.glory.services.PartyServiceInterface;

import javax.servlet.http.Part;

@Service("partyServiceImp")
public class PartyServiceImp implements PartyServiceInterface{

	@Autowired
	private PartyDao partyDao;

	@Override
	public int saveParty(Party party) {
		
		if(party!=null)
		{
			partyDao.save(party);
			return 1;
		}else
			return 0;
	}

	@Override
	public List<Party> getAllPartyDetails() {
	  List<Party> partyDetailsList=partyDao.findAll();
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
}
