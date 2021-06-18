package com.main.glory.model.employee;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EmployeeData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
    String url;
    String type;//qr,document,profile
    @Transient
    MultipartFile file;
    Long controlId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Multipart getFile() {
        return (Multipart) file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Long getControlId() {
        return controlId;
    }

    public void setControlId(Long controlId) {
        this.controlId = controlId;
    }
}
