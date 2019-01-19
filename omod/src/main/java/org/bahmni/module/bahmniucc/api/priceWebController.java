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
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/price")
public class priceWebController {


    @RequestMapping(method = RequestMethod.GET, value = "insertPrice")
    @ResponseBody
    public String insertPrice(@RequestParam("item_id") String item_id, @RequestParam("amount") float amount, @RequestParam("priceList") int priceList,@RequestParam("buying_price") float buying_price) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.insertPrice(item_id,amount,priceList,buying_price));
    }

    @RequestMapping(method = RequestMethod.GET, value = "selectPrice")
    @ResponseBody
    public String selectPrice(@RequestParam("name") String name) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson(feedClient.selectPrice(name));
    }

    //FUNCTION TO GET ALL THE PRICE ITEMS
    @RequestMapping(method = RequestMethod.GET, value = "all")
    @ResponseBody
    public String getPrice() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.getPriceList());
    }


    //==========================FUNCTION TO EDIT PRICE==========================================================
    @RequestMapping(method = RequestMethod.GET, value = "updatePriceItem")
    @ResponseBody
    public String editPrice(@RequestParam("itemPriceId") int item_price_id,@RequestParam("itemId") String item_id, @RequestParam("amount") float amount, @RequestParam("priceList") int priceList,@RequestParam("dateRecorded") String dateRecorded,@RequestParam("buying_price") float buying_price) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.editPrice(item_price_id,item_id,amount,priceList,dateRecorded,buying_price));
    }


}
