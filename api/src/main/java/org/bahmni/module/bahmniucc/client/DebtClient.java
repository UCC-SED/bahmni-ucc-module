package org.bahmni.module.bahmniucc.client;

import org.bahmni.module.bahmniucc.model.NotificationResult;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public interface DebtClient {

    void processFeed();

    void processMonitorData();

    void saveHackTagData(String visitType);

    String getHackTagData();

    boolean getDrugBalance(String drugName);

    ArrayList<NotificationResult> processNotifications(String patientUuid, String visitUuid);

    String readAuthenticationHeader();

    void storeAuthenticationHeader(String header, String issue_date, String expire_date);

    String checkDuplicateStatus(String name, String gender, String birthdate, String street, String council, String district, String region);
    List searchTribes(String searchNames);

}

