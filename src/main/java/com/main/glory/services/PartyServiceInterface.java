package com.main.glory.services;
import java.util.List;

import com.main.glory.model.party.Party;
import com.main.glory.model.party.PartyWithMasterName;

public interface PartyServiceInterface {

	public List<PartyWithMasterName> getAllPartyDetails(Long id, String getBy) throws Exception;
	public boolean editPartyDetails(Party pary) throws Exception;
	public boolean deletePartyById(Long id) throws Exception;
	public Party getPartyDetailById(Long id) throws Exception;
	public String getPartyNameByPartyId(Long partyId);
}
	
