package org.bahmni.module.bahmniucc.model;

import java.util.Date;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public class Debtor {

    public String invoice_id;
    public String patient_id;
    public String patient_name;
    public String product_name;
    public Double chargeable_amount;
    public int default_quantity;
    public String comment;
    public Date date_created;

    public Debtor() {
    }

    public Debtor(String invoice_id, String patient_id, String patient_name, String product_name, Double chargeable_amount, int default_quantity, String comment, Date date_created) {

        this.invoice_id = invoice_id;
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.product_name = product_name;
        this.chargeable_amount = chargeable_amount;
        this.default_quantity = default_quantity;
        this.comment = comment;
        this.date_created = date_created;
    }
}
