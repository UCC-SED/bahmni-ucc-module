package org.bahmni.module.bahmniucc.db;

import org.bahmni.module.bahmniucc.model.DebtorRow;
import org.bahmni.module.bahmniucc.model.Notification;
import org.bahmni.module.bahmniucc.model.NotificationResult;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public interface DebtorRowDAO {

    public void saveDebtorRow(List<DebtorRow> results);

    /**
     * Clear all the results
     */
    void clearAllResults();

    String readQuery();


    void insertHack(String visitType);

    void clearHack();

    String readHack();

    ArrayList<Notification> getNotifications();

    NotificationResult getNotificationResults(Notification notification);

    String readAuthenticationHeader();

    void storeAuthenticationHeader(String header, String issue_date, String expire_date);

    String checkDuplicateStatus(String name, String gender, String birthdate, String street, String council, String district, String region);


}
