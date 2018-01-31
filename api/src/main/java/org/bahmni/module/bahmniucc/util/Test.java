/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bahmni.module.bahmniucc.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.bahmni.module.bahmniucc.UCCModuleConstants;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.model.DebtorRow;
import org.joda.time.DateTime;
import org.openmrs.api.context.Context;

/**
 *
 * @author ucc-ian
 */
public class Test {
    
     OpenERPUtils util = new OpenERPUtils();

    public static void main(String[] args) throws Exception {

        System.out.println("OYOOOO");
        Test test = new Test();
        test.processFeed();
       // test.sendConsultationOrder();
    }

    public void processFeed() {

        try {
            System.out.println("openelisatomfeedclient:processing feed " + DateTime.now());
            int LoginID = Login();
            getFeedClient(LoginID);
           //util.findProduct(LoginID, "Aceclofenac 100mg");
        } catch (Exception e) {

        }

    }
    


    public int Login() {

        try {

            XmlRpcClient xmlrpcLogin = new XmlRpcClient();

            XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
            xmlrpcConfigLogin.setEnabledForExtensions(true);
            xmlrpcConfigLogin.setServerURL(new URL("http", UCCModuleConstants.OPENERP_HOST, UCCModuleConstants.OPENERP_PORT, "/xmlrpc/common"));

            xmlrpcLogin.setConfig(xmlrpcConfigLogin);

            Object[] params = new Object[]{UCCModuleConstants.OPENERP_DB, UCCModuleConstants.OPENERP_USERNAME, UCCModuleConstants.OPENERP_PASSWORD};
            Object id = xmlrpcLogin.execute("login", params);
            if (id instanceof Integer) {

                System.out.println("login " + id);
                return (Integer) id;
            }

            

            return -1;
        } catch (XmlRpcException e) {

            e.printStackTrace();
            return -2;
        } catch (Exception e) {
            e.printStackTrace();
            return -3;
        }
    }
    
    
    public void getFeedClient(int LoginID) {

        try {

            XmlRpcClient xmlrpcLogin = new XmlRpcClient();

            XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
            xmlrpcConfigLogin.setEnabledForExtensions(true);

            xmlrpcConfigLogin.setServerURL(new URL("http", UCCModuleConstants.OPENERP_HOST, UCCModuleConstants.OPENERP_PORT, "/xmlrpc/object"));

            xmlrpcLogin.setConfig(xmlrpcConfigLogin);

            Object[] args2 = new Object[1];
            Object[] subargs = new Object[3];
            subargs[0] = "invoice_exists";
            subargs[1] = "=";
            subargs[2] = false;

            args2[0] = subargs;

            Object[] params = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID, UCCModuleConstants.OPENERP_PASSWORD, UCCModuleConstants.OPENERP_ORDER_MODEL, UCCModuleConstants.OPENERP_SEARCH_FUNCTION, args2};
            Object result = (Object[]) xmlrpcLogin.execute("execute", params);

            Object[] searchQuery = new Object[]{};
            Vector readqueryVector = new Vector();
            readqueryVector.addElement(searchQuery);
            Object[] read = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID, UCCModuleConstants.OPENERP_PASSWORD, UCCModuleConstants.OPENERP_ORDER_MODEL, UCCModuleConstants.OPENERP_READ_FUNCTION, result, searchQuery};
            Object resultread = (Object[]) xmlrpcLogin.execute("execute", read);


            Object[] a = (Object[]) resultread;

            List<DebtorRow> debtorList = new ArrayList<>();

           // debtorRowDAO.clearAllResults();

           System.out.println("debtorList.size " + a.length);
            for (Object object : a) {
                if (object instanceof String) {
                } else if (object instanceof Integer) {
                } else {
                   // debtorList.add(getDebtorElements((HashMap) object));
                }
            }

           // debtorRowDAO.saveDebtorRow(debtorList);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    
    
       public void sendConsultationOrder() {


        try {
            Object loginID=util.login();

            int customerid = util.findCustomers((int) loginID,"GAN200063");

            int findSaleOrderIdsForCustomer = util.findSaleOrderIdsForCustomer((int) loginID, customerid);

            System.out.println("Sale Order " + findSaleOrderIdsForCustomer);

            util.insertSaleOrderLine((int) loginID, findSaleOrderIdsForCustomer);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

    }

}
