package com.main.glory.Lookup.FabInMasterLookUp;

import com.main.glory.model.fabric.FabStockData;
import com.main.glory.model.fabric.FabStockMast;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class MasterLookUpPartyRecord extends FabStockMast {

    String partName;


    public MasterLookUpPartyRecord(Long id, String stockInType, Boolean batch, Long partyId, String billNo, String chlNo, Date billDate, Date chlDate, Long lotNo, String remark, Date createdDate, Date updatedDate, String createdBy, String updatedBy, Long userHeadId, Set<FabStockData> fabStockData, String partName) {
        super(id, stockInType, batch, partyId, billNo, chlNo, billDate, chlDate, lotNo, remark, createdDate, updatedDate, createdBy, updatedBy, userHeadId, fabStockData);
        this.partName = partName;
    }

    public MasterLookUpPartyRecord(FabStockMast other, String partName) {
        super(other);
        this.partName = partName;
    }

    public MasterLookUpPartyRecord(String partName) {
        this.partName = partName;
    }
}
