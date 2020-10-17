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
    private Long  programControlId;
    private Long shadeNo;
    private Long partyShadeNo;
    private Long quantity;
    private Long batchId;
    private Long lotNo;
    private String colourTone;
    private String  remark;

//    @ManyToOne(cascade = {CascadeType.ALL},fetch= FetchType.EAGER)
//    private List<Program> program_record;

}
