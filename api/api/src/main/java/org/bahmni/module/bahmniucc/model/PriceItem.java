package org.bahmni.module.bahmniucc.model;

import java.sql.Date;

public class PriceItem {

    private String priceList_name;
    private Date date_recorded;
    private float amount;
    private String name;
    private int item_price_id;
    private float buying_price;

    public String getprice_list_id() {
        return priceList_name;
    }

    public void setpriceList_name(String priceList_name) { this.priceList_name = priceList_name; }

    public Date getdate_recorded() {
        return date_recorded;
    }

    public void setdate_recorded(Date date_recorded) {
        this.date_recorded = date_recorded;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public float getamount() {
        return amount;
    }

    public void setamount(float amount) {
        this.amount = amount;
    }

    public int getItem_price_id() {
        return item_price_id;
    }

    public void setItem_price_id(int item_price_id) {
        this.item_price_id = item_price_id;
    }

    public float getBuying_price() {
        return buying_price;
    }

    public void setBuying_price(float buying_price) {
        this.buying_price = buying_price;
    }

}
