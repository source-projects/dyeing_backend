package com.main.glory.model.basic;


import com.main.glory.model.quality.Quality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QualityData {

    Long qualityEntryId;
    String qualityId;
    String qualityName;
    String qualityType;
    String unit;

    public QualityData(Quality quality1) {
        this.qualityEntryId=quality1.getId();
        this.qualityId=quality1.getQualityId();
        this.qualityName=quality1.getQualityName();
        this.qualityType=quality1.getQualityType();
        this.unit=quality1.getUnit();
    }
}
