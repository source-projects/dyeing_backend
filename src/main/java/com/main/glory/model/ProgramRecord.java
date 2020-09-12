package com.main.glory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="program_record")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "program_control_id")
    private Long  programControlId;
    private Long shade_no;
    private Long party_shade_no;
    private Long quantity;
    private Long batch;
    private Long lot_no;
    private String colour_tone;
    private String  remark;

//    @ManyToOne(cascade = {CascadeType.ALL},fetch= FetchType.EAGER)
//    private List<Program> program_record;

}
