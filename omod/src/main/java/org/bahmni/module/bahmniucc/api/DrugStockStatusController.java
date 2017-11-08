package org.bahmni.module.bahmniucc.api;

import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
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
public class DrugStockStatusController extends BaseRestController {


    @RequestMapping(method = RequestMethod.GET, value = "getDrugStatus")
    @ResponseBody
    public String getDrugStatus(@RequestParam("drugName") String drugName) throws Exception {

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        boolean stockStatus =   feedClient.getDrugBalance(drugName);

        JSONObject obj = new JSONObject();
        obj.put("stockStatus", stockStatus);
        return obj.toJSONString();
    }




}