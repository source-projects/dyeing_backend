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
    Long dyeingProcessMastId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<DyeingplcData> dyeingplcDataList;


}
