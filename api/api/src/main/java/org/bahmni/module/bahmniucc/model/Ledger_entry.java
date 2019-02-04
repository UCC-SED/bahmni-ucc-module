package org.bahmni.module.bahmniucc.model;

import java.sql.Date;

public class Ledger_entry {

    private String item_id;
    private String ledger_type;
    private String batch_no;
    private String invoice_no;
    private String price_list_name;
    private Date received_date;
    private Date expiry_date;
    private double amount;
    private Double quantity;
    private String name;
    private Double onHand;
    private int ledger_entry_id;
    private int dosage_form;
    private String strength;
    private String drug_name;


    public String getledger_type_id() {
        return ledger_type;
    }

    public void setledger_type_id(String ledger_type) {
        this.ledger_type = ledger_type;
    }


    public Date getreceived_date() {
        return received_date;
    }

    public void setreceived_date(Date received_date) {
        this.received_date = received_date;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getbatch_no() {
        return batch_no;
    }

    public void setbatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getinvoice_no() {
        return invoice_no;
    }

    public void setinvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getprice_list_id() {
        return price_list_name;
    }

    public void setprice_list_id(String price_list_name) {
        this.price_list_name = price_list_name;
    }
    public Date getexpiry_date() {
        return expiry_date;
    }

    public void setexpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }

    public Double getonHand() {
        return onHand;
    }

    public void setonHand(Double onHand) {
        this.onHand = onHand;
    }

    public Double getquantity() {
        return quantity;
    }

    public void setquantity(Double quantity) {
        this.quantity = quantity;
    }

    public double getamount() {
        return amount;
    }

    public void setamount(double amount) {
        this.amount = amount;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getLedger_entry_id() {
        return ledger_entry_id;
    }

    public void setLedger_entry_id(int ledger_entry_id) {
        this.ledger_entry_id = ledger_entry_id;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public int getdosage_form() {
        return dosage_form;
    }

    public void setdosage_form(int dosage_form) {
        this.dosage_form = dosage_form;
    }

    public String getdrug_name() {
        return drug_name;
    }

    public void setdrug_name(String drug_name) {
        this.drug_name = drug_name;
    }
}
