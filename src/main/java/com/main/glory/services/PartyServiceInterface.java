package com.main.glory.services;
import java.util.List;
import java.util.Optional;

import com.main.glory.model.party.Party;

public interface PartyServiceInterface {

	public List<Party> getAllPartyDetails(Long id, String getBy);
	public boolean editPartyDetails(Party pary) throws Exception;
	public boolean deletePartyById(Long id);
	public Party getPartyDetailById(Long id);
	public String getPartyNameByPartyId(Long partyId);
}
	
