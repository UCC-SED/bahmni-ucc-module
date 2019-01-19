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
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/newOrders")
public class DrugBillingController {
    private Logger logger = Logger.getLogger(getClass());
    @RequestMapping(method = RequestMethod.GET, value = "drugBilling")
    @ResponseBody
    public String drugBilling(@RequestParam("provider") String provider,@RequestParam("locationUuid") String locationUuid,@RequestParam("patient_uuid") String patient_uuid, @RequestParam("payment_type") String payment_type, @RequestParam("drug_orders") String drug_orders) throws Exception {

        String fedBack = null;
        JSONObject drugOrders = new JSONObject(drug_orders);
        Iterator keys = drugOrders.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            JSONObject value = drugOrders.getJSONObject((String) key);
            String item_uuid = value.getString("item_uuid");
            int qty = value.getInt("qty");

            if(check_if_orderlineExist( patient_uuid, item_uuid)){

                fedBack =new Gson().toJson(  update_drug_orderlinie( locationUuid, provider, patient_uuid, item_uuid,  qty));
            }else{

                 fedBack = new Gson().toJson( create_drug_orderlinie( locationUuid, provider, patient_uuid, item_uuid,  qty, payment_type));

            }



        }


        return fedBack;
    }

    @RequestMapping(method = RequestMethod.GET, value = "stockReduction")
    @ResponseBody
    public String stockReduction(@RequestParam("orders") String orders, @RequestParam("payment_type") String payment_type) throws Exception{
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        JSONObject orders_uuid = new JSONObject(orders);
        Iterator keys = orders_uuid.keys();
        while (keys.hasNext()) {
            Object key = keys.next();
            JSONObject value = orders_uuid.getJSONObject((String) key);
            String order_uuid = value.getString("order_uuid");
            //getQTYofTheOrder
           int qty = get_qty_conceptId(order_uuid,"qty");
            //getConceptId
            int conceptId = get_qty_conceptId(order_uuid,"concept");
            int priceListId = gePriceListID(payment_type);

            //go reduct qty
          int ret =  feedClient.updateStockQty(conceptId, qty,priceListId);
          if(ret == 1) {
              return new Gson().toJson("updated");
          }


        }



        return null;
    }

    public int get_qty_conceptId( String order_uuid, String type){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return  feedClient.get_qty_conceptId( order_uuid, type);

    }

    public String create_drug_orderlinie(String locationUuid,String providerUuid,String patientIdentifier,String item_uuid, int qty, String payment_type){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
       int patientId = getPatientID( patientIdentifier);
       int visitID =  get_location_id(locationUuid);
       int item_id = getDrugItem_id( item_uuid);
        int priceListId = gePriceListID(payment_type);
       float amount =getItem_amount( item_id, priceListId);
       String order_id =feedClient.getSaleOrderId(patientIdentifier,"uuid");
        if(order_id == null) {order_id=uniqueCurrentTimeMS(); }
        String feed =    feedClient.createorder_line( order_id, item_id, "DRUG",  qty,  amount,  patientId, visitID,priceListId );

                if(feed.equalsIgnoreCase("")){
                    feedClient.reduct_drugQuantity(qty,item_id,priceListId);
                }

        return feed;
                 }

    public String update_drug_orderlinie(String locationUuid,String providerUuid,String patientIdentifier,String item_uuid, int qty){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        int patientId = getPatientID( patientIdentifier);
        int visitID =  get_location_id(locationUuid);
        int item_id = getDrugItem_id( item_uuid);
        String order_id =feedClient.getSaleOrderId(patientIdentifier,"uuid");
        return feedClient.update_drug_orderlinie( order_id, item_id,  qty,  patientId );
    }

    public boolean check_if_orderlineExist(String patientIdentifier,String item_uuid){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return feedClient.check_if_orderlineExist(patientIdentifier,item_uuid,"drug");
    }

    public int gePriceListID(String paymentType){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.gePriceListID(paymentType);
    }

    public int getPatientID(String patientIdentifier){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.getPatientID(patientIdentifier,"uuid");
    }

    public int getDrugItem_id(String itemUuid){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.getDrugItem_id(itemUuid);
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
