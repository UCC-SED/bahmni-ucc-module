package org.bahmni.module.bahmniucc.api;


import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.controller.PatientDeptController;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/PatientInDept")
public class PatientInDeptWebController {
    private Logger logger = Logger.getLogger(getClass());
    private PatientDeptController patientDeptController = PatientDeptController.getPatientDeptInstance();

    @RequestMapping(method = RequestMethod.GET, value = "getPatientIds")
    @ResponseBody
    public String getPatientInDept() throws Exception {

        String patientIds = patientDeptController.getPatientIds();
        return patientIds;
    }
}
