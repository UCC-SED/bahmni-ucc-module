package org.bahmni.module.bahmniucc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */

@Entity
@Table
public class DebtorRow {

    @Id
    @GeneratedValue
    public int id;
    public String invoice_id;
    public String patient_id;
    public String patient_name;
    public String product_name;
    public Double chargeable_amount;
    public int default_quantity;
    public String comment;
    public Date date_created;

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Double getChargeable_amount() {
        return chargeable_amount;
    }

    public void setChargeable_amount(Double chargeable_amount) {
        this.chargeable_amount = chargeable_amount;
    }

    public int getDefault_quantity() {
        return default_quantity;
    }

    public void setDefault_quantity(int default_quantity) {
        this.default_quantity = default_quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }


    public DebtorRow() {
    }

    public DebtorRow(String invoice_id, String patient_id, String patient_name, String product_name, Double chargeable_amount, int default_quantity, String comment, Date date_created) {

        this.invoice_id = invoice_id;
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.product_name = product_name;
        this.chargeable_amount = chargeable_amount;
        this.default_quantity = default_quantity;
        this.comment = comment;
        this.date_created = date_created;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;

    }
}
