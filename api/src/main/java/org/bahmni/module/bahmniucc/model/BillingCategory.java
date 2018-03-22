package org.bahmni.module.bahmniucc.model;

import java.util.Date;

/**
 * Created by ucc-ian on 02/Mar/2018.
 */
public class BillingCategory {

    private int id;
    private String category_name;
    private int erp_pricelist_id;
    private Date record_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getErp_pricelist_id() {
        return erp_pricelist_id;
    }

    public void setErp_pricelist_id(int erp_pricelist_id) {
        this.erp_pricelist_id = erp_pricelist_id;
    }

    public Date getRecord_date() {
        return record_date;
    }

    public void setRecord_date(Date record_date) {
        this.record_date = record_date;
    }


}
