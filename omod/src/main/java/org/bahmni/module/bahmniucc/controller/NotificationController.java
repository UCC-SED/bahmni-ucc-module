package org.bahmni.module.bahmniucc.controller;

import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.model.NotificationResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.api.context.Context;

import java.util.ArrayList;

/**
 * Created by ucc-ian on 13/Dec/2017.
 */
public class NotificationController {


    public String getNotificationResultList(String patientUuid, String visitUuid) {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        ArrayList<NotificationResult> notificationResultsList = feedClient.processNotifications(patientUuid, visitUuid);
        return generateNotificationJsonResult(notificationResultsList);
    }


    private String generateNotificationJsonResult(ArrayList<NotificationResult> notificationResultsList) {

        JSONArray notificationArray = new JSONArray();
        for (int x = 0; x < notificationResultsList.size(); x++) {
            JSONObject testObject = new JSONObject();
            testObject.put("name", notificationResultsList.get(x).getNotification().getName())
                    .put("status", notificationResultsList.get(x).getStatus())
                    .put("value", notificationResultsList.get(x).getValue());

            notificationArray.put(testObject);
        }


        JSONObject contentObject = new JSONObject();
        contentObject.put("resourceType", "notificationList").put("notifications", notificationArray);
        return contentObject.toString();
    }


}
