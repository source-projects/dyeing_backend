package com.main.glory.controller;

import java.util.List;

import com.main.glory.FabInMasterLookUp.MasterLookUpWithRecord;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.BatchGrDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.main.glory.model.Fabric;
import com.main.glory.servicesImpl.FabricsServiceImpl;

@RestController
@RequestMapping("/api")
public class FabricsController extends ControllerConfig {

	@Autowired
	private FabricsServiceImpl fabricsServiceImpl;

	@PostMapping("/add-fab-stock")
	public boolean addFabricIn(@RequestBody Fabric fabrics) throws Exception {
		if (fabrics != null) {

			int msg = fabricsServiceImpl.saveFabrics(fabrics);
			if (msg == 1) {
				return true;
			}
		}
		return false;
	}

	@GetMapping("/getfabstock-data")
	public List<MasterLookUpWithRecord> getFabListData() {
		//return fabricsServiceImpl.getAllFabricsDetails();
		List<MasterLookUpWithRecord> getFabricDataByQualityId = fabricsServiceImpl.getFabStockMasterListRecord();
		if (getFabricDataByQualityId.isEmpty()) {
			System.out.println("No Record Found..");
		}
		return getFabricDataByQualityId;
	}

	@GetMapping(value = "/get-fab-stock-by-id/{id}")
	public Fabric getFabStockDataById(@PathVariable(value = "id") Long id) {
		if (id != null) {
			Fabric fabData = fabricsServiceImpl.getFabRecordById(id);
			System.out.println(fabData.getParty_name().toString());
			return fabData;
		}
		return null;
	}


	@PostMapping("/update-stock")
	public boolean updateFabricIn(@RequestBody Fabric fabrics) throws Exception {
		if (fabrics != null) {
			boolean flag = fabricsServiceImpl.updateFabricsDetails(fabrics);
			if (flag) {
				return true;
			}
		}
		return false;
	}

	@DeleteMapping(value = "/delete-stock/{id}")
	public boolean deleteFabDetailsByID(@PathVariable(value = "id") Long id) {
		boolean flag = false;
		if (id != null) {
			flag = fabricsServiceImpl.deleteFabricsById(id);
		}
		if (flag)
			return true;
		else
			return false;
	}
}