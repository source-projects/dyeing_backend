package com.main.glory.services;
import java.util.List;

import com.main.glory.model.Party;

public interface PartyServiceInterface {
	public int saveParty(Party party);
	public List<Party> getAllPartyDetails();
	public boolean editPartyDetails(Party pary) throws Exception;
	public boolean deletePartyById(Long id);
	public Party getPartyDetailById(Long id);
	public String getPartyNameByPartyId(Long partyId);
}
	
