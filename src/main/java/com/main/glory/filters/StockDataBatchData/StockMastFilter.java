package com.main.glory.filters.StockDataBatchData;


import java.util.Date;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

import lombok.Getter;
import lombok.Setter;
 
@Getter
@Setter
public class StockMastFilter {
    Long id;
    String stockInType;
    Long partyId;
    String billNo;
    String billDate;
    String chlDate;
    String unit;
    Long updatedBy;
    Long createdBy;
    Long userHeadId;
    Boolean isProductionPlanned;
    String createdDate;
    Date receiveDate;
    String updatedDate;
    Long qualityId;
        
}
