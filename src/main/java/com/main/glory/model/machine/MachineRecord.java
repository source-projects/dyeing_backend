package com.main.glory.model.machine;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Entity
@Setter
@Getter
@Table
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MachineRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Double speed;
    Date createdDate;
    Date updatedDate;
    Long controlId;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }



}
