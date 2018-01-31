package org.bahmni.module.bahmniucc.model;

/**
 * Created by ucc-ian on 16/Nov/2017.
 */
public class Notification {

    private String name;
    private String notification_sql;
    private String notification_reaction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotification_sql() {
        return notification_sql;
    }

    public void setNotification_sql(String notification_sql) {
        this.notification_sql = notification_sql;
    }

    public String getNotification_reaction() {
        return notification_reaction;
    }

    public void setNotification_reaction(String notification_reaction) {
        this.notification_reaction = notification_reaction;
    }


}
