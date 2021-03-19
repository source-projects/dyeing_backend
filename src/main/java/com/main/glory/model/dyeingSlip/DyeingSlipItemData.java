package com.main.glory.model.dyeingSlip;

import com.main.glory.model.dyeingProcess.DyeingChemicalData;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class DyeingSlipItemData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String itemName;
    Long itemId;
    Long supplierId;
    String supplierName;
    Double qty;
    Long controlId;
    Boolean isColor=false;




    public DyeingSlipItemData(ShadeData shadeData, SupplierRate supplierRate, Supplier supplier, Double totalBatchWt) {
        this.itemName=shadeData.getItemName();
        this.itemId=supplierRate.getId();
        this.supplierId=shadeData.getSupplierId();
        this.supplierName=supplier.getSupplierName();
        this.isColor=true;
        this.qty=(totalBatchWt*shadeData.getConcentration())/100;
    }

    public DyeingSlipItemData(DyeingChemicalData dyeingChemicalData) {
    }

    public DyeingSlipItemData(Supplier supplier, SupplierRate supplierRate) {
        this.supplierName=supplier.getSupplierName();
        this.supplierId=supplier.getId();
        this.itemId=supplierRate.getId();
    }


}
