package org.bahmni.module.bahmniucc.model;

/**
 * Created by ucc-ian on 13/Dec/2017.
 */
public class NotificationResult {

    private boolean status;
    private String value;
    private Notification notification;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
