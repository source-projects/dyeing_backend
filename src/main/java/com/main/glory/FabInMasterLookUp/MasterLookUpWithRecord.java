package com.main.glory.FabInMasterLookUp;

import com.main.glory.model.Fabric;
import com.main.glory.model.FabricInRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class MasterLookUpWithRecord extends Fabric {
    private Long recordCount;

    public MasterLookUpWithRecord() {
    }
    public MasterLookUpWithRecord(Long id, Long stock_id, String stock_in_type, Long batch, Long party_id, String party_name, Date date, String bill_no, String chl_no, Date bill_date, Date chl_date, Long lot_no, String remark, Long bill_id, Date created_date, Date updated_date, String created_by, String updated_by, Long user_head_id, Long recordCount) {
        super(id, stock_id, stock_in_type, batch, party_id, party_name, date, bill_no, chl_no, bill_date, chl_date, lot_no, remark, bill_id, created_date, updated_date, created_by, updated_by, user_head_id);
        this.recordCount = recordCount;
    }
}
