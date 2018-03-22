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
import static org.bahmni.module.bahmniucc.util.OpenERPUtils.DATABASE;
import static org.bahmni.module.bahmniucc.util.OpenERPUtils.PASSWORD;
import org.joda.time.DateTime;
import org.openmrs.api.context.Context;

/**
 *
 * @author ucc-ian
 */
public class Test {



    public static void main(String[] args) throws Exception {

        System.out.println("OYOOOO");
        Test test = new Test();
        test.updatePriceList("111531-0-47"); 
     //   test.processFeed();
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

   
    public boolean updatePriceList(String patientIdentifier) {

        try {
            Object loginId = Login();

             XmlRpcClient xmlrpcLogin = new XmlRpcClient();

            XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
            xmlrpcConfigLogin.setEnabledForExtensions(true);

            xmlrpcConfigLogin.setServerURL(new URL("http", UCCModuleConstants.OPENERP_HOST, UCCModuleConstants.OPENERP_PORT, "/xmlrpc/object"));

            xmlrpcLogin.setConfig(xmlrpcConfigLogin);  
            
            Object[] productArgs = new Object[1];
            Object[] name = new Object[3];
            name[0] = "ref";
            name[1] = "=";
            name[2] = patientIdentifier;

            productArgs[0] = name;

            Object[] productParams = new Object[]{DATABASE, loginId, PASSWORD, "res.partner", "search", productArgs};
            Object productResult = (Object[]) xmlrpcLogin.execute("execute", productParams);

            System.out.println("result value " + ((Object[]) productResult)[0]);

          
            Object[] a = (Object[]) productResult;

            System.out.println("result size " + a.length);

            if (a.length > 0) {
                System.out.println((int) a[0]);
                return writeResPartner(a[0], loginId);
            }

        } catch (XmlRpcException e) {
            System.out.println("XmlRpcException " + e.getMessage());
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
            return false;

        }

        return false;
    }

    
     public boolean writeResPartner(Object partnerId, Object loginId) {
        XmlRpcClient xmlrpcClient = null;
        try {
              xmlrpcClient = new XmlRpcClient();

            XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
            xmlrpcConfigLogin.setEnabledForExtensions(true);

            xmlrpcConfigLogin.setServerURL(new URL("http", UCCModuleConstants.OPENERP_HOST, UCCModuleConstants.OPENERP_PORT, "/xmlrpc/object"));

            xmlrpcClient.setConfig(xmlrpcConfigLogin);


            HashMap<Object, Object> params = new HashMap<Object, Object>();
            params.put("property_product_pricelist", 5);

            Vector<Object> arg = new Vector<Object>();
            arg.add("openerp");
            arg.add((int) loginId);
            arg.add(PASSWORD);
            arg.add("res.partner");
            arg.add("write");
            arg.add(partnerId);
            arg.add(params);

            Object ret_id = xmlrpcClient.execute("execute", arg);
            System.out.println("Update stock.move state with id :" + ret_id.toString());
            return (boolean)ret_id;



        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (XmlRpcException e) {
            e.printStackTrace();
            return false;
        }

    }
}
