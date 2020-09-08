package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import model.Quality;
import servicesImpl.QualityServiceImp;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT})
@RestController
@RequestMapping("/api")
public class QualityController {

	@Autowired 
	private QualityServiceImp qualityServiceImp;
	
	@PostMapping(value="/add-quality")
	public String saveQuality(@NotNull @RequestBody Quality quality)
	{
		System.out.println(quality);
		qualityServiceImp.saveQuality(quality);
		return "inserted";
	}
	
	@GetMapping(value="get-quality-list")
	public List<Quality>getQualityList()
	{
		return qualityServiceImp.getAllQuality();
	}
	

	@PutMapping(value="/updateQuality")
	public boolean  updateQuality(@RequestBody Quality quality)
	{
		return true;
	}
	
}
