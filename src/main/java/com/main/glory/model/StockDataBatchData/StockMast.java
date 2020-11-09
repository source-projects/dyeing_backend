package com.main.glory.model.StockDataBatchData;

import com.main.glory.model.CommonModel.CommonField;
import com.main.glory.model.user.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class StockMast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String stockInType;
    Long partyId;
    String billNo;
    String billDate;
    Long chalNo;
    String chlDate;
    String unit;
    String updatedBy;
    String createdBy;
    String createDate;
    String updatedDate;



}
