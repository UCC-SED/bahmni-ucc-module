package org.bahmni.module.bahmniucc.controller;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.openmrs.api.context.Context;

import java.util.List;

public class PatientDeptController {
    private static PatientDeptController patientDeptInstance = new  PatientDeptController();
    private Logger logger = Logger.getLogger(getClass());

    public static PatientDeptController getPatientDeptInstance(){return patientDeptInstance;}

    private PatientDeptController(){

    }

    public String getPatientIds(){
        logger.info("Get patient identifiers");
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        List patientInDeptId = feedClient.getPatientInDept();
        String patientIds = new Gson().toJson(patientInDeptId);

        return patientIds;
    }


}
