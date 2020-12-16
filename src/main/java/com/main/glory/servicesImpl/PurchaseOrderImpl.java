package com.main.glory.servicesImpl;

import com.main.glory.Dao.SupplierRateDao;
import com.main.glory.Dao.purchaseOrder.PurchaseOrderDao;
import com.main.glory.model.purchaseOrder.PurchaseOrder;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.responce.RateAndItem;

import com.main.glory.model.purchaseOrder.ResponsePurchase;

import com.main.glory.model.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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

    @Autowired
    SupplierRateDao supplierRateDao;
    @Autowired
    UserServiceImpl userService;



    public Boolean addPurchaseOrder(PurchaseOrder purchaseOrder) throws Exception{

        if(purchaseOrder.getStatus()==1 || purchaseOrder.getStatus()==2 || purchaseOrder.getStatus()==0)
        {
            Optional<Supplier> supplierExist=supplierService.getSupplier(purchaseOrder.getSupplierId());
            Optional<SupplierRate> supplierItemExist=supplierRateDao.findByIdAndSupplierId(purchaseOrder.getItemId(),purchaseOrder.getSupplierId());

            if(!supplierExist.isPresent())
                throw new Exception("Supplier is not available with name:"+purchaseOrder.getSupplierName());

            if(supplierItemExist.isEmpty())
                throw new Exception("Item is not availble for supplier:"+ purchaseOrder.getSupplierName());

            purchaseOrderDao.save(purchaseOrder);
            return true;
        }
        return false;
    }


    public List<ResponsePurchase> getAllPurchaseOrder() {
        List<ResponsePurchase> responsePurchasesList=new ArrayList<>();

        List<PurchaseOrder> purchaseOrderList = purchaseOrderDao.findAll();
        for(PurchaseOrder purchaseOrder:purchaseOrderList)
        {
            if(purchaseOrder.getItemId()!=null && purchaseOrder.getSupplierId()!=null)
            {
                Optional<Supplier> supplierExist=supplierService.getSupplier(purchaseOrder.getSupplierId());
                Optional<SupplierRate> supplierItemExist = supplierService.getItemById(purchaseOrder.getItemId());
                UserData user = userService.getUserById(purchaseOrder.getUserHeadId());

                if(!supplierExist.isPresent() && !supplierItemExist.isPresent() && user==null )
                    continue;

                ResponsePurchase responsePurchase = new ResponsePurchase(purchaseOrder,user.getUserName());
                responsePurchasesList.add(responsePurchase);
            }
        }
        return responsePurchasesList;
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
