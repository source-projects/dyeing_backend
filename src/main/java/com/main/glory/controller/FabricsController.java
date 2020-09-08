package com.main.glory.controller;

import java.util.List;

import com.main.glory.config.ControllerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.glory.model.Fabric;
import com.main.glory.servicesImpl.FabricsServiceImpl;

@RestController
@RequestMapping("/api")
public class FabricsController extends ControllerConfig {

	@Autowired
	private FabricsServiceImpl fabricsServiceImpl;

	@PostMapping("/add-fab-stock")
	public String addFabricIn(@RequestBody Fabric fabrics) throws Exception {
		if (fabrics != null) {
			
			int msg = fabricsServiceImpl.saveFabrics(fabrics);
			if (msg == 1) {
				return "SuccessFully Inserted";
			}
		}
		return "Something Went Wrong While Saving";
	}

	@GetMapping("/getfabstock-data")
	public List<Fabric> getFabListData() {
		return fabricsServiceImpl.getAllFabricsDetails();
	}
}
