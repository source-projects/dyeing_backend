package com.main.glory.model.quality.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetQualityReport {
    Long qualityEntryId;
    Date fromDate;
    Date toDate;
}
