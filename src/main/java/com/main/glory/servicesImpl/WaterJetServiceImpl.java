package com.main.glory.servicesImpl;

import com.main.glory.Dao.waterJet.WaterJetDao;
import com.main.glory.model.waterJet.WaterJet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("waterJetServiceImpl")
public class WaterJetServiceImpl {

    @Autowired
    WaterJetDao waterJetDao;

    public void saveWaterJet(WaterJet waterJetData) {
        waterJetDao.save(waterJetData);
    }

    public WaterJet getWaterJetById(Long id) throws Exception{

        Optional<WaterJet> waterJet = waterJetDao.findById(id);
        if(!waterJet.isPresent())
            throw new Exception("no data found");

        return waterJet.get();
    }

    public void deleteWaterJet(Long id) throws Exception{

        Optional<WaterJet> waterJet = waterJetDao.findById(id);
        if(!waterJet.isPresent())
            throw new Exception("Data not found");

        waterJetDao.deleteById(id);
    }

    public void updateWaterJet(WaterJet waterJet) throws Exception{
        Optional<WaterJet> waterJetIsPresent=waterJetDao.findById(waterJet.getId());
        if(!waterJetIsPresent.isPresent())
            throw new Exception("Data not found");

        waterJetDao.saveAndFlush(waterJet);
    }

    public List<WaterJet> getAllWaterJet() throws Exception{
        List<WaterJet> waterJetList = waterJetDao.findAll();
        if(waterJetList.isEmpty())
            throw new Exception("no data found");

        return waterJetList;

    }
}
