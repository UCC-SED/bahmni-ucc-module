package org.bahmni.module.bahmniucc.model;


public class outOfStock {
    private String price_listName ;
    private String  name;
    private  double qty;

    public String getprice_listName() {
        return price_listName;
    }

    public void setprice_listName(String price_listName) {
        this.price_listName = price_listName;
    }

    public double getqty() {
        return qty;
    }

    public void setqty(double qty) {
        this.qty = qty;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }


}
