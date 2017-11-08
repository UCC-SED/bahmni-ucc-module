package org.bahmni.module.bahmniucc.model;

import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;
import java.util.Date;


public class DebtorRow extends BaseOpenmrsObject implements Serializable {


    private int id;


    private String invoice_id;


    private String patient_id;


    private String patient_name;


    private String product_name;


    private Double chargeable_amount;


    private double default_quantity;


    private String comment;

    private String date_created;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }


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

    public Double getDefault_quantity() {
        return default_quantity;
    }

    public void setDefault_quantity(double default_quantity) {
        this.default_quantity = default_quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }


}
