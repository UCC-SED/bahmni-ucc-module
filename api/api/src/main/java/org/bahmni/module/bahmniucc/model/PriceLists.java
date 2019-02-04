package org.bahmni.module.bahmniucc.model;

import java.sql.Date;

public class PriceLists {

    private int price_list_id;
    private Date date_recorded;
    private  int default_price;
    private String name;

    public int getprice_list_id() {
        return price_list_id;
    }

    public void setprice_list_id(int price_list_id) {
        this.price_list_id = price_list_id;
    }


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

    public int getdefault_price() {
        return default_price;
    }

    public void setdefault_price(int default_price) {
        this.default_price = default_price;
    }

}
