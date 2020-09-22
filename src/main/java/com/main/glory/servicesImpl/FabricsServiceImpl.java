package com.main.glory.servicesImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.main.glory.Dao.FabDataDao;
import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.fabric.FabStockMastDao;
import com.main.glory.FabInMasterLookUp.MasterLookUpWithRecord;
import com.main.glory.model.BatchGrDetail;
import com.main.glory.model.FabricInRecord;
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
    private PartyDao partyDao;

    @Autowired
    private FabDataDao fabDataDao;

	@Transactional
    public int saveFabrics(FabStockMast fabStockMast) throws Exception {
        try {
            if (fabStockMast != null) {
                FabStockMast x = fabStockMastDao.save(fabStockMast);
                return 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public List<Fabric> getAllFabricsDetails() {
        var getFabMasterData = fabricsDao.findAll();

        return getFabMasterData;

    }

    @Transactional
    public boolean updateFabricsDetails(Fabric fabrics) throws Exception {
        var partyIndex = fabricsDao.findById(fabrics.getId());

        if (!partyIndex.isPresent())
            return false;
        else {
            // List<FabricInRecord> fabstocdata = fabDataDao.getAllFabStockById(fabs.getId());
            fabDataDao.setisDeActive(fabrics.getId());
            Fabric x = fabricsDao.save(fabrics);
            fabrics.getFabricInRecord().forEach(e -> {
                e.setControlId(x.getId());
            });
            fabDataDao.saveAll(fabrics.getFabricInRecord());


//            fabstocdata.forEach(c -> {
//                c.setIsActive("0");
//                c.setControlId(fabs.getId());
//            });
//            fabDataDao.saveAll(fabstocdata);
//            fabs.getFabricInRecord().forEach(c->{
//                c.setIsActive("1");
//            });

        }
        return true;
    }


    public boolean deleteFabricsById(Long id) {
        var findId = fabricsDao.findById(id);
        if (findId.get().getId() != null) {
            fabricsDao.deleteById(findId.get().getId());
            return true;
        }
        return false;
    }

    public List<MasterLookUpWithRecord> getFabStockMasterListRecord() {
        List<MasterLookUpWithRecord> listMaster = fabStockMastDao.findAllMasterWithDetails();
        return listMaster;
    }

    public FabStockMast getFabRecordById(Long id) {
        var getData = fabStockMastDao.findById(id);
        return getData.get();
    }
}

