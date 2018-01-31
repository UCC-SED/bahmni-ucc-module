package org.bahmni.module.bahmniucc.model;

import java.util.Date;

/**
 * Created by ucc-ian on 03/Jan/2018.
 */
public class NHIFFolioItem {

    private String folioItemID;
    private String folioID;
    private String itemCode;
    private int itemQuantity;
    private double unitPrice;
    private double amountClaimed;
    private String approvalRefNo;
    private String createdBy;
    private Date dateCreated;

    public String getFolioItemID() {
        return folioItemID;
    }

    public void setFolioItemID(String folioItemID) {
        this.folioItemID = folioItemID;
    }

    public String getFolioID() {
        return folioID;
    }

    public void setFolioID(String folioID) {
        this.folioID = folioID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getAmountClaimed() {
        return amountClaimed;
    }

    public void setAmountClaimed(double amountClaimed) {
        this.amountClaimed = amountClaimed;
    }

    public String getApprovalRefNo() {
        return approvalRefNo;
    }

    public void setApprovalRefNo(String approvalRefNo) {
        this.approvalRefNo = approvalRefNo;
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
