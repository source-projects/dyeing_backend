package com.main.glory.controller;

import java.util.List;

import com.main.glory.config.ControllerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import com.main.glory.model.Party;
import com.main.glory.servicesImpl.PartyServiceImp;

@RestController
@RequestMapping("/api")
public class PartyController  extends ControllerConfig {

	@Autowired 
	private PartyServiceImp partyServiceImp;
	
	@PostMapping(value="/party")
	public String saveQuality(@NotNull @RequestBody Party party)
	{
		if(party==null)
		{
			return "Nullable Object";
		}
		int flag=partyServiceImp.saveParty(party);
		if(flag!=1)
		{
			return "Something went wrong";
		}else
			return "Succefully Inserted";
	}
	
	@GetMapping(value="/get-party-list")
	public List<Party>getQualityList()
	{
		return partyServiceImp.getAllPartyDetails();
	}
	

	@PutMapping(value="/update-party")
	public boolean  updateQuality(@RequestBody Party party) throws Exception
	{
		if(party!=null)
		{
			 boolean flag=partyServiceImp.editPartyDetails(party);
			 if(flag)
			 {
				 return true; 
			 }
			 
		}
		return false;
	}
	
	@DeleteMapping(value="/delete-party/{id}")
	public boolean deletePartyDetailsByID(@PathVariable(value = "id") Long id)
	{
		if(id!=null)
		{
			boolean flag=partyServiceImp.deletePartyById(id);
			if(flag)
			{
			 return true;
			}
		}
		return false;
	}
}