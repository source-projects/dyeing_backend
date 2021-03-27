package com.main.glory.model.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class MaterialPhotos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String picUrl;
    Long controlId;
    String type;//bill,material

}
