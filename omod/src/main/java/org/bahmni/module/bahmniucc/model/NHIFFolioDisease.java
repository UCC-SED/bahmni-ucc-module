package org.bahmni.module.bahmniucc.model;

import java.util.Date;

/**
 * Created by ucc-ian on 03/Jan/2018.
 */
public class NHIFFolioDisease {

    private String folioDiseaseID;
    private String folioID;
    private String diseaseCode;
    private String status;
    private String createdBy;
    private Date dateCreated;

    public String getFolioDiseaseID() {
        return folioDiseaseID;
    }

    public void setFolioDiseaseID(String folioDiseaseID) {
        this.folioDiseaseID = folioDiseaseID;
    }

    public String getFolioID() {
        return folioID;
    }

    public void setFolioID(String folioID) {
        this.folioID = folioID;
    }

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }



}
