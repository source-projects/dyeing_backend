package com.main.glory.servicesImpl;

import com.main.glory.Dao.purchaseOrder.PurchaseOrderDao;
import com.main.glory.model.purchaseOrder.PurchaseOrder;
import com.main.glory.model.purchaseOrder.ResponsePurchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("purchaseOrderImpl")
public class PurchaseOrderImpl {

    @Autowired
    PurchaseOrderDao purchaseOrderDao;


    public Boolean addPurchaseOrder(PurchaseOrder purchaseOrder) {

        if(purchaseOrder.getStatus()==1 || purchaseOrder.getStatus()==2 || purchaseOrder.getStatus()==0)
        {
            purchaseOrderDao.save(purchaseOrder);
            return true;
        }
        return false;
    }

    public List<ResponsePurchase> getAllPurchaseOrder() {
        return purchaseOrderDao.getAllWithUserName();
    }

    public void updatePurchaseOrder(PurchaseOrder purchaseOrder) throws Exception {
        Optional<PurchaseOrder> purchaseExist = purchaseOrderDao.findById(purchaseOrder.getId());

        if(!purchaseExist.isPresent())
            throw new Exception("Data not found for id:"+purchaseOrder.getId());

        purchaseOrderDao.saveAndFlush(purchaseOrder);

    }
}
