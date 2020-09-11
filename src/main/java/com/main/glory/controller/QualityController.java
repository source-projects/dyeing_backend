package com.main.glory.controller;

import java.util.List;

import com.main.glory.Dao.QualityDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.services.QualityServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sun.istack.NotNull;

import com.main.glory.model.Quality;
import com.main.glory.servicesImpl.QualityServiceImp;

@RestController
@RequestMapping("/api")
public class QualityController  extends ControllerConfig {

	@Autowired 
	private QualityServiceImp qualityServiceImp;

	@Autowired
	private QualityDao qualityDao;
	
	@PostMapping(value="/add-quality")
	public boolean saveQuality(@NotNull @RequestBody Quality quality)
	{
		int flag=qualityServiceImp.saveQuality(quality);
		if(flag==1)
			return  true;
		else
		return false;
	}

	@GetMapping(value="/get-quality-list")
	public List<Quality>getQualityList()
	{
		return qualityServiceImp.getAllQuality();
	}

	@PutMapping(value="/update_quality_By_ID")
	public boolean  updateQualityById(@RequestBody Quality quality) throws Exception {
		if(quality.getId()!=null)
		{
			boolean flag=qualityServiceImp.updateQuality(quality);
			return true;
		}
		return false;
	}

	@GetMapping(value="/getqualitydataById/{id}")
	public Quality getQualityDataById(@PathVariable(value = "id") Long id)
	{
		if(id!=null)
		{
			return qualityServiceImp.getQualityByID(id);
		}
		return null;
	}
	@DeleteMapping(value="/delete-quality/{id}")
	public boolean deletePartyDetailsByID(@PathVariable(value = "id") Long id)
	{
		if(id!=null)
		{
			boolean flag=qualityServiceImp.deleteQualityById(id);
			if(flag)
			{
				return true;
			}
		}
		return false;
	}

	@GetMapping(value="/isExistQualityId/{quality_id}")
	public boolean IsQualityAlreadyExist(@PathVariable("quality_id") String quality_id)
	{
		String flag = qualityDao.isQualityNameExist(quality_id);
		if(flag!=null)
		{
			return true;
		}
		return false;
	}

}
