package org.bahmni.module.bahmniucc.api;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.controller.PatientBillingCategoryController;
import org.bahmni.module.bahmniucc.util.OpenERPUtils;
import org.json.simple.JSONObject;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ucc-ian on 02/Mar/2018.
 */

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/emr")
public class PatientBillingCategoryWebController extends BaseRestController {


    private Logger logger = Logger.getLogger(getClass());
    OpenERPUtils util = new OpenERPUtils();
    PatientBillingCategoryController billingCategoryController= PatientBillingCategoryController.getInstance();

    @RequestMapping(method = RequestMethod.GET, value = "updatePatientBillingCategory")
    @ResponseBody
    public String updateSingleDeliveryOrder(@RequestParam("patientIdentifier") String patientIdentifier, @RequestParam("paymentCategoryName") String paymentCategoryName) throws Exception {


        logger.info("patientIdentifier " + patientIdentifier);
        logger.info("paymentCategoryName " + paymentCategoryName);

        Object loginID = util.login();
        int customerid = util.findCustomers((int) loginID, patientIdentifier, true);

        boolean updateStatus=billingCategoryController.updateBillingCategory(paymentCategoryName, patientIdentifier, customerid, (int)loginID);
        JSONObject obj = new JSONObject();
        obj.put("updateStatus", updateStatus);
        return obj.toJSONString();

    }
}
