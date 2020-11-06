package com.main.glory.servicesImpl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.main.glory.Dao.FabDataDao;
import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.fabric.FabStockDataDao;
import com.main.glory.Dao.fabric.FabStockMastDao;
import com.main.glory.Lookup.FabInMasterLookUp.MasterLookUpWithRecord;
import com.main.glory.model.fabric.FabStockData;
import com.main.glory.model.fabric.FabStockMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.glory.Dao.FabricsDao;
import com.main.glory.model.Fabric;
import com.main.glory.services.FabricsServicesInterface;

@Service("fabricsServiceImpl")
public class FabricsServiceImpl implements FabricsServicesInterface {

    @Autowired
    private FabricsDao fabricsDao;

    @Autowired
    private FabStockMastDao fabStockMastDao;

    @Autowired
    private FabStockDataDao fabStockDataDao;

    @Autowired
    private PartyDao partyDao;

    @Autowired
    private FabDataDao fabDataDao;

	@Transactional
    public void saveFabrics(FabStockMast fabStockMast) throws Exception {
        if (fabStockMast != null) {
            Long i = 1l;
            for (FabStockData e : fabStockMast.getFabStockData()) {
                e.setGr(i++);
            }
            FabStockMast x = fabStockMastDao.save(fabStockMast);
        } else {
            throw new Exception("Null Data Passed", new Exception("BR"));
        }
    }

    public List<Fabric> getAllFabricsDetails() {
        var getFabMasterData = fabricsDao.findAll();

        return getFabMasterData;

    }

    @Transactional
    public boolean updateFabric(FabStockMast fabStockMast) throws Exception {

	    Optional<FabStockMast> fabStockMast1 = fabStockMastDao.findById(fabStockMast.getId());

	    if(fabStockMast1.isEmpty()){
	        throw new Exception("No such stock present with id:"+ fabStockMast.getId());
        }

	    for(FabStockData fabStockData: fabStockMast.getFabStockData()){
	        Optional<FabStockData> fabStockData1 = fabStockDataDao.findById(fabStockData.getId());
	        if(fabStockData1.isPresent()){
	            if(fabStockData1.get().getBatchCreated() == true){
	                throw new Exception("Batch already created for id:"+fabStockData1.get().getId());
                }
            }
        }

	    fabStockMastDao.save(fabStockMast);
	    return true;
    }


    @Transactional
    public boolean deleteFabricsById(Long id) throws Exception{
        Optional<FabStockMast> fabStockMast = fabStockMastDao.findById(id);

        // check if this is present in the database
        if(fabStockMast.isEmpty()){
            throw new Exception("Fabric stock does not exist with id:"+id);
        }

        // check if any of its child is not already used in the batch
        for (FabStockData e : fabStockMast.get().getFabStockData()) {
            if (e.getBatchCreated()) {
                throw new Exception("Can't be deleted as batch is already created for id:" + e.getId());
            }
        }



        fabStockMastDao.deleteById(id);

        return true;
    }

    public List<MasterLookUpWithRecord> getFabStockMasterListRecord() {
        List<MasterLookUpWithRecord> listMaster = fabStockMastDao.findAllMasterWithDetails();
        return listMaster;
    }

    public Optional<FabStockMast> getFabRecordById(Long id) {
        var getData = fabStockMastDao.findById(id);
        return getData;
    }
}

