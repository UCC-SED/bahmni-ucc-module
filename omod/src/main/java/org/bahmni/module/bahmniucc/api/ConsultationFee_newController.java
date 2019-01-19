package org.bahmni.module.bahmniucc.api;

//import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.util.AppGlobalProperties;
import org.json.simple.JSONObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/emr")
public class ConsultationFee_newController  {
    //OpenERPUtils util = new OpenERPUtils();
  //  private Logger logger = Logger.getLogger(getClass());

    // PatientBillingCategoryController billingCategoryController = PatientBillingCategoryController.getInstance();


    @RequestMapping(method = RequestMethod.GET, value = "createConsultationQuotation_new")
    @ResponseBody
    public String createConsultationQuotation_new(@RequestParam("patientIdentifier") String patientIdentifier,@RequestParam("locationUuid") String locationUuid, @RequestParam("paymentCategoryName") String paymentCategoryName, @RequestParam("patientCategory") String patientCategory) throws Exception {
        //if its vertical or not
        //logger.info("consult"+ patientIdentifier);
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        int excepDays = Integer.parseInt(AppGlobalProperties.CONSULTATION_EXEMPTION_NO_DAYS());

        if (feedClient.checkIfCustomerConsultationExemption(patientIdentifier, excepDays)) {
          //  logger.info("exempted here"+ patientIdentifier);

           // logger.info("Exempted form Consultation Fee");
            JSONObject obj = new JSONObject();
            obj.put("exemptioStatus", true);
            obj.put("status", true);
            return obj.toJSONString();
        } else {

            String item_type ="consultation";
            //logger.info("exempted here"+ patientIdentifier);
            int qty = 1;

            int patient_id = getPatientID(patientIdentifier);

            int visit_id = get_location_id(locationUuid);
            int item_id = 0;
            int payment_categoryID=0;
            int amount = 0;
            if(patientCategory.equalsIgnoreCase("DIAGNOSTIC PATIENT")){

                item_id = Integer.parseInt(AppGlobalProperties.EXTERNAL_SERVICE());

                if (paymentCategoryName.equalsIgnoreCase("Cash")) {
                    amount = Integer.parseInt(AppGlobalProperties.EXTERNAL_SERVICE_AMOUNT());
                    payment_categoryID=1;
                } else {
                    amount = Integer.parseInt(AppGlobalProperties.EXTERNAL_SERVICE_INSURANCE_AMOUNT());
                    payment_categoryID=2;
                }

            }else {

                item_id = Integer.parseInt(AppGlobalProperties.GLOBAL_CONSULTATION_PRODUCT_ID());

                if (paymentCategoryName.equalsIgnoreCase("Cash")) {
                    amount = Integer.parseInt(AppGlobalProperties.GLOBAL_CONSULTATION_AMOUNT());
                    payment_categoryID=1;
                } else {
                    amount = Integer.parseInt(AppGlobalProperties.GLOBAL_CONSULTATION_INSURANCE_AMOUNT());
                    payment_categoryID=2;
                }
            }
            String order_id =feedClient.getSaleOrderId(patientIdentifier,"identifier");
            if(order_id == null) {order_id=uniqueCurrentTimeMS(); }

            float amount1 = (float)amount;

            String feed = feedClient.createorder_line( order_id, item_id, item_type,  qty,  amount1,  patient_id, visit_id, payment_categoryID );
            if(feed.equalsIgnoreCase("updated")){
                JSONObject obj = new JSONObject();
                obj.put("status", true);
                return obj.toJSONString();

            }else{
                JSONObject obj = new JSONObject();
                obj.put("status", false);
                return obj.toJSONString();
            }


        }

    }


    /*   public boolean updateSingleDeliveryOrder(String patientIdentifier, String paymentCategoryName) throws Exception {
       logger.info("patientIdentifier " + patientIdentifier);
           logger.info("paymentCategoryName " + paymentCategoryName);

           Object loginID = util.login();
           int customerid = util.findCustomers((int) loginID, patientIdentifier, true);

           boolean updateStatus = billingCategoryController.updateBillingCategory(paymentCategoryName, patientIdentifier, customerid, (int) loginID);

           return updateStatus;
       }
   */
    public int getPatientID(String patientIdentifier){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.getPatientID(patientIdentifier,"identifier");
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
