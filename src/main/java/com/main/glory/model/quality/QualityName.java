package com.main.glory.model.quality;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class QualityName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String qualityName;//should be unique
    Long createdBy;
    Date createdDate;
    Long updatedBy;
    Date updatedDate;
    @ColumnDefault("0")
    Double rate;
    @PrePersist
    public void create()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }
    @PreUpdate
    public void update()
    {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

}
