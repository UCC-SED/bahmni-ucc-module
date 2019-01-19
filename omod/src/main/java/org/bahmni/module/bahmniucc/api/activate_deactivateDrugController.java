package org.bahmni.module.bahmniucc.api;


import com.google.gson.Gson;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/activateDeactivate")
public class activate_deactivateDrugController {


    @RequestMapping(method = RequestMethod.GET, value = "updateStatus")
    @ResponseBody
    public String updateStatus(@RequestParam("drug_id") int drug_id, @RequestParam("status") int status,@RequestParam("concept_id") int concept_id) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.updateStatus(drug_id,status,concept_id));
    }

}
