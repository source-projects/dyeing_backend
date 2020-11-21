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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long shadeNo;
    private String partyShadeNo;
    private Long quantity;
    private String colourTone;
    private String  remark;

//    @ManyToOne(cascade = {CascadeType.ALL},fetch= FetchType.EAGER)
//    private List<Program> program_record;

}
