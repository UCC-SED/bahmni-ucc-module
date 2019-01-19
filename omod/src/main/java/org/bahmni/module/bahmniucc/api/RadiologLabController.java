package org.bahmni.module.bahmniucc.api;


import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.json.JSONObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/RadiologyLabOrders")
public class RadiologLabController {
    private Logger logger = Logger.getLogger(getClass());
    @RequestMapping(method = RequestMethod.GET, value = "billOrders")
    @ResponseBody
    public String billOrders(@RequestParam("provider") String provider, @RequestParam("locationUuid") String locationUuid, @RequestParam("patient_uuid") String patient_uuid, @RequestParam("payment_type") String payment_type,@RequestParam("orders") String orders) throws Exception {

        String fedBack = null;
        JSONObject Orders = new JSONObject(orders);
        Iterator keys = Orders.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            JSONObject value = Orders.getJSONObject((String) key);
            String item_uuid = value.getString("item_uuid");
            logger.info("drug Order item_uuid" + item_uuid);
            int qty = 1;
            if(check_if_orderlineExist( patient_uuid, item_uuid)){

                return fedBack = new Gson().toJson("updated");
            }else{
                fedBack = new Gson().toJson( create_orderline( locationUuid, provider, patient_uuid, item_uuid,  qty,payment_type));
            }



        }


        return fedBack;
    }

    public String create_orderline(String locationUuid,String providerUuid,String patientIdentifier,String item_uuid, int qty, String payment_type){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        int patientId = getPatientID( patientIdentifier);
        int visitID =  get_location_id(locationUuid);
        int item_id = getItem_id( item_uuid);
        int priceListId = gePriceListID(payment_type);
        logger.info("priceListId " + priceListId);
        float amount =getItem_amount( item_id, priceListId);
        logger.info("amount priceListId " + amount);
        String order_id =feedClient.getSaleOrderId(patientIdentifier,"uuid");
        if(order_id == null) {order_id=uniqueCurrentTimeMS(); }

        return feedClient.createorder_line( order_id, item_id, "LAB_RAD_PROCED",  qty,  amount,  patientId, visitID, priceListId);
    }



    public boolean check_if_orderlineExist(String patientIdentifier,String item_uuid){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return feedClient.check_if_orderlineExist(patientIdentifier,item_uuid,"notDrug");
    }

    public int getPatientID(String patientIdentifier){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.getPatientID(patientIdentifier,"uuid");
    }

    public int gePriceListID(String paymentType){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.gePriceListID(paymentType);
    }

    public int getItem_id(String itemUuid){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.getItem_id(itemUuid);
    }

    public float getItem_amount(int itemId, int priceListId){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.getItem_amount(itemId, priceListId);
    }

    public int get_location_id(String locationUuid){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.get_location_id(locationUuid);
    }

    public String uniqueCurrentTimeMS() {
        long now = System.currentTimeMillis();
        return  "SO"+ now;
    }





}
