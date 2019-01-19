package org.bahmni.module.bahmniucc.model;

import  java.util.Date;

public class getAddDrugs {

    private int drug_id;

    private Date date_created;

    private String dose_name;

    private String name;

    private String strength;

    private int concept_name_id;

    private int item_drug_id;

    private int dosage_form;

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

    public String getstrength() {
        return strength;
    }

    public void setstrength(String strength) {
        this.strength = strength;
    }

    public String getdose_name() {
        return dose_name;
    }

    public void setdose_name(String dose_name) {
        this.dose_name = dose_name;
    }

    public Date getdate_created() {
        return date_created;
    }

    public void setdate_created(Date date_created) {
        this.date_created = date_created;
    }

    public int getConcept_name_id() {
        return concept_name_id;
    }

    public void setConcept_name_id(int concept_name_id) {
        this.concept_name_id = concept_name_id;
    }

    public int getItem_drug_id() {
        return item_drug_id;
    }

    public void setItem_drug_id(int item_drug_id) {
        this.item_drug_id = item_drug_id;
    }

    public int getDosage_form() {
        return dosage_form;
    }

    public void setDosage_form(int dosage_form) {
        this.dosage_form = dosage_form;
    }
}
