package org.bahmni.module.bahmniucc.model;

import java.sql.Date;

public class PhysicalInv {

    private String price_name;
    private Date date_recorded;
    private Date received_date;
    private Date inventoryDate;
    private Date recorded_date;
    private float qnty;
    private String name;
    private int physical_inventory_id;
    private String batchNo;
    private Date expire_date;
    private double quantity;
    private double physical_value;
    private double stock_quantity;
    private double stock_value;
    private double expiry_quantity;
    private double expiry_value;

    public String getprice_list_id() {
        return price_name;
    }

    public void setprice_list_id(String price_name) {
        this.price_name = price_name;
    }

    public Date getdate_recorded() {
        return date_recorded;
    }

    public void setdate_recorded(Date date_recorded) {
        this.date_recorded = date_recorded;
    }

    public Date getreceived_data() {
        return received_date;
    }

    public void sereceived_data(Date received_date) {
        this.received_date = received_date;
    }

    public Date getinventoryDate() {
        return inventoryDate;
    }

    public void setinventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public float getqnty() {
        return qnty;
    }

    public void setqnty(float qnty) {
        this.qnty = qnty;
    }

    public int getPhysical_inventory_id() {
        return physical_inventory_id;
    }

    public void setPhysical_inventory_id(int physical_inventory_id) {
        this.physical_inventory_id = physical_inventory_id;
    }

    public Date getRecorded_date() {
        return recorded_date;
    }

    public void setRecorded_date(Date recorded_date) {
        this.recorded_date = recorded_date;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(Date expire_date) {
        this.expire_date = expire_date;
    }

    public double getquantity() {
        return quantity;
    }

    public void setquantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPhysical_value() {
        return physical_value;
    }

    public void setPhysical_value(double physical_value) {
        this.physical_value = physical_value;
    }

    public double getstock_quantity() {
        return stock_quantity;
    }

    public void setstock_quantity(double stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public double getstock_value() {
        return stock_value;
    }

    public void setstock_value(double stock_value) {
        this.stock_value = stock_value;
    }

    public String getprice_name() {
        return price_name;
    }

    public void setprice_name(String price_name) {
        this.price_name = price_name;
    }

    public double getexpiry_quantity() {
        return expiry_quantity;
    }

    public void setexpiry_quantity(double expiry_quantity) {
        this.expiry_quantity = expiry_quantity;
    }

    public double getExpiry_value() {
        return expiry_value;
    }

    public void setExpiry_value(double expiry_value) {
        this.expiry_value = expiry_value;
    }
}
