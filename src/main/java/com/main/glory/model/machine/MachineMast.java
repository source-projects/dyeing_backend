package com.main.glory.model.machine;

import com.main.glory.model.program.ProgramRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MachineMast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String machineName;
    @ApiModelProperty(hidden = true)
    Date createdDate;
    @ApiModelProperty(hidden = true)
    Date updatedDate;


    @ApiModelProperty(hidden = true)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    private List<MachineRecord> machineRecords;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

}
