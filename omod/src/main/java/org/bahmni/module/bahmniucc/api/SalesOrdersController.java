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
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/salesOrders")
public class SalesOrdersController {

    @RequestMapping(method = RequestMethod.GET, value = "getSalesOrders")
    @ResponseBody
    public String getSalesOrders(@RequestParam("search") String search) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.getSalesOrders(search));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getSalesOrders_line")
    @ResponseBody
    public String getSalesOrders_line(@RequestParam("orderID") String orderID) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.getSalesOrders_line(orderID));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getSalesOrders_line_other")
    @ResponseBody
    public String getSalesOrders_line_other(@RequestParam("orderID") String orderID) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.getSalesOrders_line_other(orderID));
    }

    @RequestMapping(method = RequestMethod.GET, value = "cancelOrder")
    @ResponseBody
    public String cancelOrder(@RequestParam("orderID") String orderID) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.cancelOrder(orderID));
    }
    @RequestMapping(method = RequestMethod.GET, value = "cancelOrderLine")
    @ResponseBody
    public String cancelOrderLine(@RequestParam("orderlineID") String orderlineID) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.cancelOrderLine(orderlineID));
    }

    @RequestMapping(method = RequestMethod.GET, value = "paymentConfirmed")
    @ResponseBody
    public String paymentConfirmed(@RequestParam("orderID") String orderID) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.paymentConfirmed(orderID));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getPaidorders")
    @ResponseBody
    public String getPaidorders(@RequestParam("search") String search) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.getPaidorders(search));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getDiscountOrders")
    @ResponseBody
    public String getDiscountOrders(@RequestParam("search") String search) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.getDiscountOrders(search));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getCancelledorders")
    @ResponseBody
    public String getCancelledorders(@RequestParam("search") String search) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson( feedClient.getCancelledorders(search));
    }

    @RequestMapping(method = RequestMethod.GET, value = "saveDiscount")
    @ResponseBody
    public String saveDiscount(@RequestParam("dicountAmount") float dicountAmount,@RequestParam("paid") int paid,
                                     @RequestParam("exemptionUuid") String exemptionUuid,@RequestParam("saleOrderId") String saleOrderId ) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        int id_item = getItem_id( exemptionUuid);

        return new Gson().toJson(feedClient.saveDiscount(dicountAmount,id_item,paid,saleOrderId));
    }

    public int getItem_id(String itemUuid){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.getItem_id(itemUuid);
    }

}
