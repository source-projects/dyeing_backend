package com.main.glory.model.dyeingProcess.DyeingPLC;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Entity
public class DyeingplcMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(unique=true)
    Long dyeingProcessMastId;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<DyeingplcData> dyeingplcDataList;


    public DyeingplcMast(DyeingplcMast dyeingplcMast) {
        this.id = dyeingplcMast.getId()==null?null:dyeingplcMast.getId();
        this.dyeingProcessMastId = dyeingplcMast.getDyeingProcessMastId()==null?null:dyeingplcMast.getDyeingProcessMastId();
        this.dyeingplcDataList = dyeingplcMast.getDyeingplcDataList()==null?null:dyeingplcMast.getDyeingplcDataList();
    }
}
