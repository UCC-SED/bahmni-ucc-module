package org.bahmni.module.bahmniucc.model;

import java.util.Date;

public class getDrug_request_List {

    private int person_id_sub_store;
    private int item_id;
    private Date date_qty_requested;
    private double quantity_requested;
    private int sub_store_id;
    private String product_mvnt_status;
    private int price_list_id;

    public int getPerson_id_sub_store() {
        return person_id_sub_store;
    }

    public void setPerson_id_sub_store(int person_id_sub_store) {
        this.person_id_sub_store = person_id_sub_store;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public Date getDate_qty_requested() {
        return date_qty_requested;
    }

    public void setDate_qty_requested(Date date_qty_requested) {
        this.date_qty_requested = date_qty_requested;
    }

    public double getQuantity_requested() {
        return quantity_requested;
    }

    public void setQuantity_requested(double quantity_requested) {
        this.quantity_requested = quantity_requested;
    }

    public int getSub_store_id() {
        return sub_store_id;
    }

    public void setSub_store_id(int sub_store_id) {
        this.sub_store_id = sub_store_id;
    }

    public String getProduct_mvnt_status() {
        return product_mvnt_status;
    }

    public void setProduct_mvnt_status(String product_mvnt_status) {
        this.product_mvnt_status = product_mvnt_status;
    }

    public int getPrice_list_id() {
        return price_list_id;
    }

    public void setPrice_list_id(int price_list_id) {
        this.price_list_id = price_list_id;
    }
}
