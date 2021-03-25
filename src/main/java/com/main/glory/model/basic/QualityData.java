package com.main.glory.model.basic;


import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
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
    Long qualityNameId;
    String qualityName;
    String qualityType;
    String unit;
    Long partyId;
    String partyName;
    private Double wtPer100m;


    public QualityData(Quality quality1, QualityName qualityName) {
        this.qualityEntryId=quality1.getId();
        this.qualityId=quality1.getQualityId();
        this.qualityName=qualityName.getQualityName();
        this.qualityType=quality1.getQualityType();
        this.unit=quality1.getUnit();
        this.partyId=quality1.getPartyId();
        this.wtPer100m=quality1.getWtPer100m();
        this.qualityNameId = quality1.getQualityNameId();

    }

    public QualityData(Quality quality1, QualityName qualityName, Party partName) {
        this.qualityEntryId=quality1.getId();
        this.qualityId=quality1.getQualityId();
        this.qualityName=qualityName.getQualityName();
        this.qualityType=quality1.getQualityType();
        this.unit=quality1.getUnit();
        this.partyId=quality1.getPartyId();
        this.wtPer100m=quality1.getWtPer100m();
        this.qualityNameId = quality1.getQualityNameId();
        this.partyName=partName.getPartyName();
    }
}
