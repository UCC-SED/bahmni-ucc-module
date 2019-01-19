package org.bahmni.module.bahmniucc.model;

public class getItems_unfiltered {

    private int drug_id;
    private int item_id;

    private boolean retired;
    private String name;

    public int getdrug_id() {
        return drug_id;
    }

    public void setdrug_id(int drug_id) {
        this.drug_id = drug_id;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public boolean getretired() {
        return retired;
    }

    public void setretired(boolean retired) {
        this.retired = retired;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

}
