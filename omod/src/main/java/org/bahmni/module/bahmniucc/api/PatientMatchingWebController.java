/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bahmni.module.bahmniucc.api;

/**
 * @author ucc-ian
 */

import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.controller.PatientMatchingController;
import org.bahmni.module.bahmniucc.util.OpenERPUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ucc-ian on 19/Oct/2017.
 */
@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/emr")
public class PatientMatchingWebController extends BaseRestController {

    private Logger logger = Logger.getLogger(getClass());

    private PatientMatchingController patientMatchingController = PatientMatchingController.getInstance();

    @RequestMapping(method = RequestMethod.POST, value = "checkDuplicatePatients")
    @ResponseBody
    public String checkDuplicatePatients(@RequestBody String patient) throws Exception {

        String duplicateStatus = patientMatchingController.checkDuplicate(patient);
        if (duplicateStatus == null) {
            JSONObject obj = new JSONObject();
            obj.put("status", false);

            JSONArray patientArray = new JSONArray();
            patientArray.add(obj);

            JSONObject mainObj = new JSONObject();
            mainObj.put("patients", patientArray);
            return mainObj.toJSONString();
        } else {
            return duplicateStatus;
        }

    }

}
