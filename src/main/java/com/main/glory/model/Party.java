package com.main.glory.model;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="party")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id", updatable = false, nullable = false)
    private Long id;
    private String party_name;
    private String party_address1;
    private String party_address2;
    private String contact_no;
    private Integer pincode;
    private String city;
    private String state;
    @Column(name="GSTIN")
    private String GSTIN;
    private String mail_id;
    private Date created_date;
    private String created_by;
    private Date updated_date;
    private String updated_by;
    private Integer debtor;
    private Integer creditor;
    private Integer internal_transfer;
    private Integer is_active;
    private String party_type;
    private String payment_terms;
    private Double percentage_discount;
    @Column(name="gst_percentage")
    private Double gst_percentage;
    private Integer user_head_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getParty_address1() {
        return party_address1;
    }

    public void setParty_address1(String party_address1) {
        this.party_address1 = party_address1;
    }

    public String getParty_address2() {
        return party_address2;
    }

    public void setParty_address2(String party_address2) {
        this.party_address2 = party_address2;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGSTIN() {
        return GSTIN;
    }

    public void setGSTIN(String GSTIN) {
        this.GSTIN = GSTIN;
    }

    public String getMail_id() {
        return mail_id;
    }

    public void setMail_id(String mail_id) {
        this.mail_id = mail_id;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Date getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(Date updated_date) {
        this.updated_date = updated_date;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public Integer getDebtor() {
        return debtor;
    }

    public void setDebtor(Integer debtor) {
        this.debtor = debtor;
    }

    public Integer getCreditor() {
        return creditor;
    }

    public void setCreditor(Integer creditor) {
        this.creditor = creditor;
    }

    public Integer getInternal_transfer() {
        return internal_transfer;
    }

    public void setInternal_transfer(Integer internal_transfer) {
        this.internal_transfer = internal_transfer;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
    }

    public String getParty_type() {
        return party_type;
    }

    public void setParty_type(String party_type) {
        this.party_type = party_type;
    }

    public String getPayment_terms() {
        return payment_terms;
    }

    public void setPayment_terms(String payment_terms) {
        this.payment_terms = payment_terms;
    }

    public Double getPercentage_discount() {
        return percentage_discount;
    }

    public void setPercentage_discount(Double percentage_discount) {
        this.percentage_discount = percentage_discount;
    }

    public Double getGst_percentage() {
        return gst_percentage;
    }

    public void setGst_percentage(Double gst_percentage) {
        this.gst_percentage = gst_percentage;
    }

    public Integer getUser_head_id() {
        return user_head_id;
    }

    public void setUser_head_id(Integer user_head_id) {
        this.user_head_id = user_head_id;
    }

    public Party() {
    }
}
