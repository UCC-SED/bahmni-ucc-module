package org.bahmni.module.bahmniucc.model;

public class DrugItem {

    private int drug_id;
    private int item_id;

    private String name;
    private boolean retired;

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

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public boolean getretired() {
        return retired;
    }

    public void setretired(boolean retired) {
        this.retired = retired;
    }

}
