package org.bahmni.module.bahmniucc.controller;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.util.OpenERPUtils;
import org.openmrs.api.context.Context;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Vector;

import static org.bahmni.module.bahmniucc.util.OpenERPUtils.DATABASE;
import static org.bahmni.module.bahmniucc.util.OpenERPUtils.PASSWORD;

/**
 * Created by ucc-ian on 02/Mar/2018.
 */
public class PatientBillingCategoryController {

    private static PatientBillingCategoryController ourInstance = new PatientBillingCategoryController();
    OpenERPUtils util = new OpenERPUtils();
    private Logger logger = Logger.getLogger(getClass());


    public static PatientBillingCategoryController getInstance() {
        return ourInstance;
    }

    private PatientBillingCategoryController() {
    }


    public boolean updateBillingCategory(String billingCategory, String patientIdentifier, int customerId, int loginId) {
        try {

            DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

            return writeResPartner(customerId, loginId, feedClient.getBillingCategoryId(billingCategory));


        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
            return false;

        }


    }


    public boolean writeResPartner(int partnerId, int loginId, int priceListId) {

        try {

            logger.info("partnerId " + partnerId);
            logger.info("loginId " + loginId);
            logger.info("priceListId " + priceListId);

            XmlRpcClient xmlrpcClient = util.getXmlRpcClient();
            

            HashMap<Object, Object> params = new HashMap<Object, Object>();
            params.put("property_product_pricelist", priceListId);

            Vector<Object> arg = new Vector<Object>();
            arg.add("openerp");
            arg.add( loginId);
            arg.add(PASSWORD);
            arg.add("res.partner");
            arg.add("write");
            arg.add(partnerId);
            arg.add(params);

            Object ret_id = xmlrpcClient.execute("execute", arg);
            logger.info("Update res.partner state with id :" + ret_id.toString());
            return (boolean) ret_id;


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return false;
        }

    }


}
