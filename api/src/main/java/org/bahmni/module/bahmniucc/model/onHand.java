package org.bahmni.module.bahmniucc.model;

import java.sql.Date;

public class onHand {
    private int price_list_id;
    private Date date_recorded;
    private  int item_id;
    private double qty;

    public int getprice_list_id() {
        return price_list_id;
    }

    public void setprice_list_id(int price_list_id) {
        this.price_list_id = price_list_id;
    }

    public double getqty() {
        return qty;
    }

    public void setqty(double qty) {
        this.qty = qty;
    }

    public int getitem_id() {
        return item_id;
    }

    public void setitem_id(int item_id) {
        this.item_id = item_id;
    }




}
