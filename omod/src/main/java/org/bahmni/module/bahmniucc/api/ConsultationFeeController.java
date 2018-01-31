/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bahmni.module.bahmniucc.api;

/**
 *
 * @author ucc-ian
 */
import java.net.MalformedURLException;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.util.OpenERPUtils;
import org.json.simple.JSONObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ucc-ian on 19/Oct/2017.
 */
@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/emr")
public class ConsultationFeeController extends BaseRestController {

    OpenERPUtils util = new OpenERPUtils();
    private Logger logger = Logger.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET, value = "createConsultationQuotation")
    @ResponseBody
    public String getDrugStatus(@RequestParam("patientIdentifier") String patientIdentifier) throws Exception {

        Object loginID = util.login();
        int customerid = util.findCustomers((int) loginID, patientIdentifier);
        int findSaleOrderIdsForCustomer = util.findSaleOrderIdsForCustomer((int) loginID, customerid);
        String orderId=util.insertSaleOrderLine((int) loginID, findSaleOrderIdsForCustomer);

        JSONObject obj = new JSONObject();
        return obj.toJSONString();
    }

}
