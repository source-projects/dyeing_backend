package com.main.glory.servicesImpl;

import com.main.glory.Dao.purchaseOrder.PurchaseOrderDao;
import com.main.glory.model.purchaseOrder.PurchaseOrder;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.responce.RateAndItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("purchaseOrderImpl")
public class PurchaseOrderImpl {

    @Autowired
    PurchaseOrderDao purchaseOrderDao;

    @Autowired
    SupplierServiceImpl supplierService;



    public Boolean addPurchaseOrder(PurchaseOrder purchaseOrder) throws Exception{

        if(purchaseOrder.getStatus()==1 || purchaseOrder.getStatus()==2 || purchaseOrder.getStatus()==0)
        {
            Optional<Supplier> supplierExist=supplierService.getSupplier(purchaseOrder.getSupplierId());
            Optional<SupplierRate> supplierItemExist=supplierService.getItemById(purchaseOrder.getItemId());

            if(!supplierExist.isPresent())
                throw new Exception("Supllier is not available with name:"+purchaseOrder.getSupplierName());

            if(!supplierItemExist.isPresent())
                throw new Exception("Item is not availble for supplier:"+ purchaseOrder.getSupplierName());

            purchaseOrderDao.save(purchaseOrder);
            return true;
        }
        return false;
    }

    public List<PurchaseOrder> getAllPurchaseOrder() {
        List<PurchaseOrder> purchaseOrderList=new ArrayList<>();
        for(PurchaseOrder purchaseOrder:purchaseOrderList)
        {
            if(purchaseOrder.getItemId()!=null && purchaseOrder.getSupplierId()!=null)
            {
                Optional<Supplier> supplierExist=supplierService.getSupplier(purchaseOrder.getSupplierId());
                Optional<SupplierRate> supplierItemExist = supplierService.getItemById(purchaseOrder.getItemId());

                if(!supplierExist.isPresent() && !supplierItemExist.isPresent())
                    continue;

                purchaseOrderList.add(purchaseOrder);
            }
        }
        return purchaseOrderList;
    }

    public void updatePurchaseOrder(PurchaseOrder purchaseOrder) throws Exception {
        Optional<PurchaseOrder> purchaseExist = purchaseOrderDao.findById(purchaseOrder.getId());

        if(!purchaseExist.isPresent())
            throw new Exception("Data not found for id:"+purchaseOrder.getId());

        Optional<Supplier> supplierExist=supplierService.getSupplier(purchaseOrder.getSupplierId());
        Optional<SupplierRate> supplierItemExist=supplierService.getItemById(purchaseOrder.getItemId());

        if(!supplierExist.isPresent())
            throw new Exception("Supllier is not available with name:"+purchaseOrder.getSupplierName());

        if(!supplierItemExist.isPresent())
            throw new Exception("Item is not availble for supplier:"+ purchaseOrder.getSupplierName());


        purchaseOrderDao.saveAndFlush(purchaseOrder);

    }
}
