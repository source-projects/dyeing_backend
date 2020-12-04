package com.main.glory.model.quality.response;

import com.main.glory.model.quality.Quality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllQualtiy {
    private Long id;
    private String  qualityId;
    private String  qualityName;
    private String qualityType;
    private String unit;


    public GetAllQualtiy(Quality quality) {
        this.id=quality.getId();
        this.qualityId=quality.getQualityId();
        this.qualityName=quality.getQualityName();
        this.qualityType=quality.getQualityType();
        this.unit=quality.getUnit();
    }
}
