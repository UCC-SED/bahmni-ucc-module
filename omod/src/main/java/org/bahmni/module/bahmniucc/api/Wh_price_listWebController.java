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

import java.util.List;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/pricelist")
public class Wh_price_listWebController {

    @RequestMapping(method = RequestMethod.GET, value = "insertPriceList")
    @ResponseBody
    public String insertPriceList(@RequestParam("name") String name,@RequestParam("defaulted") String defaultPriceList) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson(feedClient.priceList(name,defaultPriceList));
    }

    @RequestMapping(method = RequestMethod.GET, value = "selectPriceLists")
    @ResponseBody
    public String selectPriceLists() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        List price_list = feedClient.selectPriceLists();
        String price_listJson = new Gson().toJson(price_list);


        return price_listJson;
    }

}
