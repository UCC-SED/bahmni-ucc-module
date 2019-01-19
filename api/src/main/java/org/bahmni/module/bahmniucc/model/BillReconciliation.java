package org.bahmni.module.bahmniucc.model;

import java.util.Date;

public class BillReconciliation {

    private String gepg_transaction_id;
    private Date gepg_transaction_date;

    public String getGepg_transaction_id() {
        return gepg_transaction_id;
    }

    public void setGepg_transaction_id(String gepg_transaction_id) {
        this.gepg_transaction_id = gepg_transaction_id;
    }

    public Date getGepg_transaction_date() {
        return gepg_transaction_date;
    }

    public void setGepg_transaction_date(Date gepg_transaction_date) {
        this.gepg_transaction_date = gepg_transaction_date;
    }
}
