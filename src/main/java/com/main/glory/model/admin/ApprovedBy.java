package com.main.glory.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ApprovedBy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String contact;
    String email;


//    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "approvedId", referencedColumnName = "id")
    private List<DyeingSlipMast> dyeingSlipMasts;


    public ApprovedBy(String name) {
        this.name=name;
    }
}
