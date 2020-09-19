package com.main.glory.servicesImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.main.glory.Dao.FabDataDao;
import com.main.glory.Dao.PartyDao;
import com.main.glory.FabInMasterLookUp.MasterLookUpWithRecord;
import com.main.glory.model.BatchGrDetail;
import com.main.glory.model.FabricInRecord;
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
    private PartyDao partyDao;

    @Autowired
    private FabDataDao fabDataDao;

    @Override
//	@Transactional
    public int saveFabrics(Fabric fabrics) throws Exception {
        try {
            if (fabrics != null) {
                Fabric x = fabricsDao.save(fabrics);
                fabrics.getFabricInRecord().forEach(e -> {
                    e.setControlId(x.getId());
                });
                fabDataDao.saveAll(fabrics.getFabricInRecord());
                return 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Fabric> getAllFabricsDetails() {
        var getFabMasterData = fabricsDao.findAll();

        return getFabMasterData;

    }

    @Override
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


    @Override
    public boolean deleteFabricsById(Long id) {
        var findId = fabricsDao.findById(id);
        if (findId.get().getId() != null) {
            fabricsDao.deleteById(findId.get().getId());
            return true;
        }
        return false;
    }

    @Override
    public List<MasterLookUpWithRecord> getFabStockMasterListRecord() {
        List<MasterLookUpWithRecord> listMaster = fabricsDao.getFabStockMasterRecordList();
        return listMaster;
    }

    @Override
    public Fabric getFabRecordById(Long id) {
        var getData = fabricsDao.findById(id);
        if (getData.isPresent()) {
            var fablistData = fabDataDao.getAllFabStockById(getData.get().getId());
            String getPartyName = partyDao.getPartyNameByPartyId(getData.get().getId());
            getData.get().setPartyName(getPartyName);
            getData.get().setFabricInRecord(fablistData);
        }
        return getData.get();
    }
}

