package servicesImpl;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import Dao.PartyDao;
import model.Party;
import services.PartyServiceInterface;

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
