package org.bahmni.module.bahmniucc.model;

import java.util.Date;

public class getSalesOrders_line {
    private String order_id;
    private float amount;
    private String item_name;
    private Date date_ordered;
    private String item_type;
    private String item_id;
    private int order_line_id;
    private int qty;

    public String getorder_id() {
        return order_id;
    }

    public void setorder_id(String order_id) {
        this.order_id = order_id;
    }

    public float getamount() {
        return amount;
    }

    public void setamount(float amount) {
        this.amount = amount;
    }

    public String getitem_name() {
        return item_name;
    }

    public void setitem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setdate_ordered(Date date_ordered) {
        this.date_ordered = date_ordered;
    }

    public Date getdate_ordered() {
        return date_ordered;
    }

    public String getitem_id() {
        return item_id;
    }

    public void setitem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getitem_type() {
        return item_type;
    }

    public void setitem_type(String item_type) {
        this.item_type = item_type;
    }

    public int getorder_line_id() {
        return order_line_id;
    }

    public void setorder_line_id(int order_line_id) {
        this.order_line_id = order_line_id;
    }

    public int getqty() {
        return qty;
    }

    public void setqty(int qty) {
        this.qty = qty;
    }



}
