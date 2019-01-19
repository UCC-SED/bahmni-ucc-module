package org.bahmni.module.bahmniucc.model;

import java.util.Date;

public class getPaid_canceledOrders {
    private String order_id;
    private float total_amount;
    private String Ctrl_number;
    private float discount;
    private int discount_id;
    private String identifier;
    private Date date_ordered;
    private String full_name;
    private String paid_status;
    private Date date_payed;
    private Date cancelled_date;

    public String getorder_id() {
        return order_id;
    }

    public void setorder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getpaid_status() {
        return paid_status;
    }

    public void setpaid_status(String paid_status) {
        this.paid_status = paid_status;
    }

    public String getidentifier() {
        return identifier;
    }

    public void setidentifier(String identifier) {
        this.identifier = identifier;
    }

    public float gettotal_amount() {
        return total_amount;
    }

    public void settotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public String getCtrl_number() {
        return Ctrl_number;
    }

    public void setCtrl_number(String Ctrl_number) {
        this.Ctrl_number = Ctrl_number;
    }

    public float getdiscount() {
        return discount;
    }

    public void setdiscount(float discount) {
        this.discount = discount;
    }

    public int getdiscount_id() {
        return discount_id;
    }

    public void setdiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public void setdate_ordered(Date date_ordered) {
        this.date_ordered = date_ordered;
    }

    public Date getdate_ordered() {
        return date_ordered;
    }

    public void setdate_payed(Date date_payed) {
        this.date_payed = date_payed;
    }

    public Date getdate_payed() {
        return date_payed;
    }

    public void setcancelled_date(Date cancelled_date) {
        this.cancelled_date = cancelled_date;
    }

    public Date getcancelled_date() {
        return cancelled_date;
    }

    public String getfull_name() {
        return full_name;
    }

    public void setfull_name(String full_name) {
        this.full_name = full_name;
    }



}
