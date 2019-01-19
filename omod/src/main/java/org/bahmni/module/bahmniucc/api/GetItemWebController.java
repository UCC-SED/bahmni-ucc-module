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
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/get_item")
public class GetItemWebController {
    @RequestMapping(method = RequestMethod.GET, value = "getDrug")
    @ResponseBody
    public String getDrug(@RequestParam("name") String name) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.getDrug(name));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getDrug_request")
    @ResponseBody
    public String getDrug_request(@RequestParam("drug_name") String drug_name) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.getDrug_request(drug_name));
    }

    @RequestMapping(method = RequestMethod.GET, value = "createdrug_requestOrder")
    @ResponseBody
    public String createItem(@RequestParam("item_id") int item_id,
                             @RequestParam("qty_req") int qty_req, @RequestParam("price_listId") int price_listId,
                             @RequestParam("userId") int userId )
            throws Exception {

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        //get sub store id
        int sub_store_id = get_sub_store_id( userId);


        return new Gson().toJson(feedClient.createdrug_requestOrder(item_id,qty_req,price_listId,userId,sub_store_id));
    }


    @RequestMapping(method = RequestMethod.GET, value = "getItems_unfiltered")
    @ResponseBody
    public String getItems_unfiltered(@RequestParam("name") String name) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.getItems_unfiltered(name));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getAddItems")
    @ResponseBody
    public String getAddItems(@RequestParam("name") String name) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson( feedClient.getAddItems(name));
    }

    @RequestMapping(method = RequestMethod.GET, value = "createItem")
    @ResponseBody
    public String createItem(@RequestParam("name") String name,@RequestParam("category") String category,
                             @RequestParam("strength") String strength, @RequestParam("dosageForm") String dosageForm )
            throws Exception {

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.createItem(name,category,strength,dosageForm));
    }

    @RequestMapping(method = RequestMethod.GET, value = "get_OutOfStockDrugs")
    @ResponseBody
    public String get_thresholdDrugs() throws Exception {

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.get_thresholdDrugs());
    }

    //NEW FUNCTION TO GET ALL THE ITEMS
    @RequestMapping(method = RequestMethod.GET, value = "all")
    @ResponseBody
    public String get_all_Item() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson( feedClient.getItems());
    }

    /**
     * FUNCTION TO EDIT ADD PRODUCT MODULE
     * @param:product_id,name,category,strength,dosageform
     *
     */
    @RequestMapping(method = RequestMethod.GET, value = "editItem")
    @ResponseBody
    public String editItem(@RequestParam("conceptNameId") int conceptNameId,@RequestParam("itemDrugId") int itemDrugId,@RequestParam("itemId") String itemId,@RequestParam("name") String name,
                           @RequestParam("strength") String strength, @RequestParam("dosageForm") String dosageForm,@RequestParam("dateCreated") String dateCreated)
            throws Exception {

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.editItem(conceptNameId,itemDrugId,itemId,name,strength,dosageForm,dateCreated));
    }

    public int get_sub_store_id(int userId){

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.get_sub_store_id(userId);

    }

}
