package org.bahmni.module.bahmniucc.controller;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.model.NotificationResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.api.context.Context;

import java.util.ArrayList;

/**
 * Created by ucc-ian on 29/Jan/2018.
 */
public class PatientMatchingController {
    private static PatientMatchingController ourInstance = new PatientMatchingController();
    private Logger logger = Logger.getLogger(getClass());

    public static PatientMatchingController getInstance() {
        return ourInstance;
    }

    private PatientMatchingController() {

    }

    public String checkDuplicate(String patientJson) {
        JSONObject obj = new JSONObject(patientJson);

        logger.info(" birthDate " + obj.getString("birthdate"));
        String birthDate = obj.getString("birthdate").split("T")[0];
        String givenName = obj.getString("givenName");
        String familyName = obj.getString("familyName");
        String gender = obj.getString("gender");

        JSONObject addressObj = obj.getJSONObject("address");

        String street = addressObj.getString("address2");
        String council = addressObj.getString("address3");
        String district = addressObj.getString("countyDistrict");
        String region = addressObj.getString("address4");

        logger.info(" birthDate " + birthDate);

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.checkDuplicateStatus(givenName + " " + familyName, gender, birthDate, street, council, district, region);

    }




}
