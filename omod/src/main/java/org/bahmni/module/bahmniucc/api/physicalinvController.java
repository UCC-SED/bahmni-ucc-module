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
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/physicalInv")
public class physicalinvController {

    @RequestMapping(method = RequestMethod.GET, value = "insertPhysical")
    @ResponseBody
    public String insertPhysical(@RequestParam("item_id") String item_id, @RequestParam("quantity") Double quantity, @RequestParam("priceList") int priceList,
                                 @RequestParam("receivedDate") String receivedDate,@RequestParam("recorededDate") String recorededDate, @RequestParam("batchNo") String batchNo) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson(feedClient.insertPhysical(item_id,quantity,priceList,receivedDate,recorededDate,batchNo));
    }

    @RequestMapping(method = RequestMethod.GET, value = "selectPhysical")
    @ResponseBody
    public String selectPhysical(@RequestParam("name") String name,@RequestParam("byDate") String byDate
    ,@RequestParam("byBatch") String byBatch) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.selectPhysical(name,byDate,byBatch));
    }

    @RequestMapping(method = RequestMethod.GET, value = "all")
    @ResponseBody
    public String selectAllPhysical() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson( feedClient.selectAllPhysical());
    }

    @RequestMapping(method = RequestMethod.GET, value = "updatePhysical")
    @ResponseBody
    public String editPhysical(@RequestParam("physical_inventory_id") int physical_inventory_id,@RequestParam("item_id") String item_id, @RequestParam("quantity") Double quantity, @RequestParam("priceList") int priceList,
                               @RequestParam("receivedDate") String receivedDate,@RequestParam("recorededDate") String recorededDate, @RequestParam("batchNo") String batchNo) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.editPhysical(physical_inventory_id,item_id,quantity,priceList,receivedDate,recorededDate,batchNo));
    }

    //============================ ANALYSIS =============================================
    @RequestMapping(method = RequestMethod.GET, value = "total_quantity")
    @ResponseBody
    public String getTotalPhysical() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.get_total_physical());
    }

    @RequestMapping(method = RequestMethod.GET, value = "physical_value")
    @ResponseBody
    public String getPhysicalValue() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.get_physical_value());
    }

    @RequestMapping(method = RequestMethod.GET, value = "total_stock")
    @ResponseBody
    public String getTotalStock() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.get_total_stock());
    }

    @RequestMapping(method = RequestMethod.GET, value = "stock_value")
    @ResponseBody
    public String getStockValue() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.get_stock_value());
    }

    @RequestMapping(method = RequestMethod.GET, value = "expire_quantity")
    @ResponseBody
    public String getExpiryStock() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.get_expiry_stock());
    }

    @RequestMapping(method = RequestMethod.GET, value = "expire_value")
    @ResponseBody
    public String getExpireValue() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.get_expire_value());
    }


}
