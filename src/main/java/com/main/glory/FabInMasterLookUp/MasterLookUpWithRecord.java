package com.main.glory.FabInMasterLookUp;

import com.main.glory.model.Fabric;
import com.main.glory.model.FabricInRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class MasterLookUpWithRecord extends Fabric {

    public MasterLookUpWithRecord() {
    }

    public MasterLookUpWithRecord(Long id, Long stockId, String stockInType, Long batch, Long partyId, String partyName, Date date, String billNo, String chlNo, Date billDate, Date chlDate, Long lotNo, String remark, Long billId, Date createdDate, Date updatedDate, String createdBy, String updatedBy, Long userHeadId, Long recordCount) {
        super(id, stockId, stockInType, batch, partyId, partyName, date, billNo, chlNo, billDate, chlDate, lotNo, remark, billId, createdDate, updatedDate, createdBy, updatedBy, userHeadId, recordCount);
    }
}
