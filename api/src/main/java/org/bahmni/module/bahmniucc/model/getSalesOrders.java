package org.bahmni.module.bahmniucc.model;

import java.util.Date;

public class getSalesOrders {

    private String order_id;
    private float total_amount;
    private String Ctrl_number;
    private String identifier;
    private float discount;
    private int discount_id;
    private Date date_ordered;
    private String full_name;
    private String request_status;

    public String getorder_id() {
        return order_id;
    }

    public void setorder_id(String order_id) {
        this.order_id = order_id;
    }

    public float gettotal_amount() {
        return total_amount;
    }

    public String getidentifier() {
        return identifier;
    }

    public void setidentifier(String identifier) {
        this.identifier = identifier;
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

    public String getfull_name() {
        return full_name;
    }

    public void setfull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }
}
