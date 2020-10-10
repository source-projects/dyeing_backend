package com.main.glory.FabInMasterLookUp;

import com.main.glory.model.Fabric;
import com.main.glory.model.FabricInRecord;
import com.main.glory.model.fabric.FabStockData;
import com.main.glory.model.fabric.FabStockMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class MasterLookUpWithRecord extends FabStockMast {

    Long recordCount;
    Double totalMtr;
    Double totalWt;

    public MasterLookUpWithRecord(Long id, String stockInType, Long batch, Long partyId, Date date, String billNo, String chlNo, Date billDate, Date chlDate, Long lotNo, String remark, Date createdDate, Date updatedDate, String createdBy, String updatedBy, Long userHeadId, Set<FabStockData> fabStockData, Long recordCount, Double totalMtr, Double totalWt) {
        super(id, stockInType, batch, partyId, date, billNo, chlNo, billDate, chlDate, lotNo, remark, createdDate, updatedDate, createdBy, updatedBy, userHeadId, fabStockData);
        this.recordCount = recordCount;
        this.totalMtr = totalMtr;
        this.totalWt = totalWt;
    }

    public MasterLookUpWithRecord(Long id, String stockInType, Long batch, Long partyId, Date date, String billNo, String chlNo, Date billDate, Date chlDate, Long lotNo, String remark, Date createdDate, Date updatedDate, String createdBy, String updatedBy, Long userHeadId, Long recordCount, Double totalMtr, Double totalWt) {
        super(id, stockInType, batch, partyId, date, billNo, chlNo, billDate, chlDate, lotNo, remark, createdDate, updatedDate, createdBy, updatedBy, userHeadId, null);
        this.recordCount = recordCount;
        this.totalMtr = totalMtr;
        this.totalWt = totalWt;
    }

    public MasterLookUpWithRecord(Long recordCount, Double totalMtr, Double totalWt) {
        this.recordCount = recordCount;
        this.totalMtr = totalMtr;
        this.totalWt = totalWt;
    }

    public MasterLookUpWithRecord() {

    }
}
