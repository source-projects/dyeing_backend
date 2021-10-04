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
public class QualityParty {
    Long qualityEntryId;
    String qualityId;
    String qualityName;
    String qualityType;
    Long partyId;
    String partyName;
    private String unit;

    public QualityParty(Quality quality) {
        this.qualityEntryId=quality.getId();
        this.qualityId=quality.getQualityId();
        this.qualityName=quality.getQualityName().getQualityName();
        this.qualityType=quality.getQualityType();
        this.partyId=quality.getParty().getId();
        this.unit=quality.getUnit();
    }
}
