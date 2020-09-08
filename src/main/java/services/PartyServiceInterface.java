package services;
import java.util.List;

import model.Party;

public interface PartyServiceInterface {
	public int saveParty(Party party);
	public List<Party> getAllPartyDetails();
	public boolean editPartyDetails(Party pary) throws Exception;
	public boolean deletePartyById(Long id);
}
	
