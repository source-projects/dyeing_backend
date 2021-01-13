package com.main.glory.model.dispatch.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateDispatch {
    Long createdBy;
    Long userHeadId;
    Long updatedBy;
    List<BatchAndStockId> batchAndStockIdList;
}
