package org.bahmni.module.bahmniucc.util;

/**
 * Created by ucc-ian on 28/Aug/2017.
 */

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

public class OpenERPUtils {

    public static final String HOST = "197.250.7.213";
    //public static final String HOST = "192.168.33.10";
    public static final int PORT = 8069;
    public static final String SCHEME = "http";
    public static final String DATABASE = "openerp";
    public static final String USER = "admin";
    public static final String PASSWORD = "password";
    public static final int PRODUCT_ID = 1960;

    private static Logger logger = Logger.getLogger(OpenERPUtils.class);

    private static void debug(Object[] orderObjects) {
        for (Object orderObject : orderObjects) {
            System.out.println("**************** Order Object Details *********** ");
            System.out.println(orderObject);
        }
    }

    public List<String> getDatabaseList() throws MalformedURLException, XmlRpcException {
        XmlRpcClient xmlrpcDb = new XmlRpcClient();

        XmlRpcClientConfigImpl xmlrpcConfigDb = new XmlRpcClientConfigImpl();
        xmlrpcConfigDb.setEnabledForExtensions(true);
        xmlrpcConfigDb.setServerURL(new URL(SCHEME, HOST, PORT, "/xmlrpc/db"));

        xmlrpcDb.setConfig(xmlrpcConfigDb);
        Vector<String> res = new Vector<String>();
        //Retrieve databases
        List<Object> params = new Vector<Object>();
        Object result = xmlrpcDb.execute("list", params);
        Object[] a = (Object[]) result;

        System.out.println(a.length);
        System.out.println(a.getClass());
        for (int i = 0; i < a.length; i++) {
            if (a[i] instanceof String) {
                res.addElement((String) a[i]);
            }
        }
        return res;
    }

    public int login() throws MalformedURLException, XmlRpcException {
        XmlRpcClient xmlrpcLogin = new XmlRpcClient();
        XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
        xmlrpcConfigLogin.setEnabledForExtensions(true);
        xmlrpcConfigLogin.setServerURL(new URL(SCHEME, AppGlobalProperties.OPENERP_HOST(), Integer.parseInt(AppGlobalProperties.OPENERP_PORT()), "/xmlrpc/common"));
        xmlrpcLogin.setConfig(xmlrpcConfigLogin);
        Object[] params = new Object[]{DATABASE, USER, PASSWORD};
        return (int) xmlrpcLogin.execute("login", params);

    }

    public Object findOrders(Integer connectionId, Integer erpCustomerId, Object[] saleOrderIds) throws MalformedURLException, XmlRpcException {
        XmlRpcClient xmlrpcClient = getXmlRpcClient();
        Object[] customerOrderIds = (saleOrderIds == null || saleOrderIds.length == 0)
                ? findSaleOrderIdsForCustomer(connectionId, erpCustomerId, xmlrpcClient)
                : saleOrderIds;

        if (customerOrderIds.length > 0) {
            List criteria = new Vector(); //criteria.add(69);
            Arrays.stream(customerOrderIds).forEach(e -> criteria.add(Integer.valueOf(e.toString()).intValue()));
            List<Object> orderReadParams = asList(
                    DATABASE, connectionId, PASSWORD, "sale.order", "read", criteria
            );
            return xmlrpcClient.execute("execute", orderReadParams.toArray());
        }

        return null;
    }


    private Object[] findSaleOrderIdsForCustomer(Integer connectionId, Integer erpCustomerId, XmlRpcClient xmlrpcClient) throws XmlRpcException {
        List criteria = new Vector();
        criteria.add(asList("partner_id", "=", erpCustomerId).toArray());
        List<Object> orderSearchParams = asList(
                DATABASE, connectionId, PASSWORD, "sale.order", "search", criteria
        );
        Object resultIds = xmlrpcClient.execute("execute", orderSearchParams.toArray());
        return (Object[]) resultIds;
    }


    private Object[] findCustomers(Integer connectionId, String customerName, String patientUuid) throws MalformedURLException, XmlRpcException {
        XmlRpcClient xmlrpcClient = getXmlRpcClient();

        List<Object> criteria = new ArrayList<Object>();
//        criteria.add(asList("name", "=", customerName).toArray());
//        criteria.add(asList("customer", "=", true).toArray());
        criteria.add(asList("uuid", "=", patientUuid).toArray());
        List<Object> customerSearchParams = asList(
                DATABASE, connectionId, PASSWORD, "res.partner", "search", criteria
        );
        Object customerIds = xmlrpcClient.execute("execute", customerSearchParams.toArray());

        criteria.clear();
        Arrays.stream((Object[]) customerIds).forEach(e -> criteria.add(Integer.valueOf(e.toString())));
        List<Object> readParams = asList(
                DATABASE, connectionId, PASSWORD, "res.partner", "read", criteria
        );

        /**
         * result - array of hashmap, "ref":patient identifier (e.g GAN203006)
         * "uuid": emr patient uuid (e.g. "uuid" ->
         * "6e6691b4-27c5-4733-9f26-342a28317423") "sale_order_ids": array of
         * int objects "invoice_ids": : array of int objects "id":ERP partner id
         * (int)
         */
        Object result = xmlrpcClient.execute("execute", readParams.toArray());
        return (Object[]) result;

    }


    public String insertSaleOrderLine(int loginID, int orderID, String patientCategory) throws MalformedURLException, XmlRpcException {

        XmlRpcClient xmlrpcClient = getXmlRpcClient();

        HashMap<Object, Object> params = new HashMap<Object, Object>();
        //IF CONDITION FOR DIFFERENT PRODUCTS
        if(patientCategory.equalsIgnoreCase("GENERAL OPD")) {
            params.put("name", "Consultation Fee");
            params.put("product_id", AppGlobalProperties.GLOBAL_CONSULTATION_PRODUCT_ID());
            params.put("type", "make_to_stock");
            params.put("state", "draft");
            params.put("price_unit", AppGlobalProperties.GLOBAL_CONSULTATION_AMOUNT());
            params.put("order_id", orderID);
        }
         if(patientCategory.equalsIgnoreCase("DIAGNOSTIC PATIENT")){
            params.put("name", "DIAGNOSTIC SERVICE FEE");
            params.put("product_id", AppGlobalProperties.GLOBAL_EXTERNAL_LAB_PRODUCT_ID());
            params.put("type", "make_to_stock");
            params.put("state", "draft");
            params.put("price_unit", AppGlobalProperties.GLOBAL_EXTERNAL_LAB_AMOUNT());
            params.put("order_id", orderID);

        }
        if(patientCategory.equalsIgnoreCase("CTC PATIENT")){
        return "true";
        }

        Vector<Object> arg = new Vector<Object>();

        arg.add("openerp");
        arg.add(loginID);
        arg.add("password");
        arg.add("sale.order.line");
        arg.add("create");
        arg.add(params);

        Object ret_id = xmlrpcClient.execute("execute", arg);
        logger.info("Created new partner address with id :" + ret_id.toString());
        return ret_id.toString();

    }


    public int createSaleorder(Integer connectionId, Integer erpCustomerId) throws MalformedURLException, XmlRpcException {
        XmlRpcClient xmlrpcClient = getXmlRpcClient();

        HashMap<Object, Object> params = new HashMap<Object, Object>();
        params.put("shop_id", "1");
        params.put("state", "draft");
        params.put("date_order", dateFormatter());
        params.put("user_id", 1);
        params.put("partner_id", erpCustomerId);
        params.put("partner_invoice_id", erpCustomerId);
        params.put("partner_shipping_id", erpCustomerId);
        params.put("picking_policy", "direct");
        params.put("order_policy", "manual");
        params.put("pricelist_id", 1);
        params.put("note", "note");

        Vector<Object> arg = new Vector<Object>();
        arg.add("openerp");
        arg.add((int) connectionId);
        arg.add("password");
        arg.add("sale.order");
        arg.add("create");
        arg.add(params);

        Object ret_id = xmlrpcClient.execute("execute", arg);
        logger.info("Created new partner address with id :" + ret_id.toString());

        return (int) ret_id;
    }

    public String dateFormatter() {
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public int findSaleOrderIdsForCustomer(Integer connectionId, Integer erpCustomerId) throws XmlRpcException, MalformedURLException {
        XmlRpcClient xmlrpcClient = getXmlRpcClient();
        List criteria = new Vector();
        criteria.add(asList("partner_id", "=", erpCustomerId).toArray());
        criteria.add(asList("invoice_exists", "=", false).toArray());
        List<Object> orderSearchParams = asList(
                DATABASE, connectionId, PASSWORD, "sale.order", "search", criteria
        );
        Object[] resultIds = (Object[]) xmlrpcClient.execute("execute", orderSearchParams.toArray());

        logger.info("Order Size " + resultIds.length);
        if (resultIds.length == 0) {

            return createSaleorder(connectionId, erpCustomerId);
        } else {
            return (int) resultIds[0];
        }
    }


    public boolean checkIfCustomerConsultationExemption(Integer connectionId, Integer erpCustomerId) throws XmlRpcException, MalformedURLException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -(Integer.parseInt(AppGlobalProperties.CONSULTATION_EXEMPTION_NO_DAYS())));
        Date dateBefore30Days = cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //check last date to get consultation fee
        XmlRpcClient xmlrpcClient = getXmlRpcClient();
        List criteria = new Vector();
        criteria.add(asList("partner_id", "=", erpCustomerId).toArray());
        criteria.add(asList("state", "=", "progress").toArray());
        criteria.add(asList("create_date", ">", dateFormat.format(dateBefore30Days)).toArray());
        List<Object> orderSearchParams = asList(
                DATABASE, connectionId, PASSWORD, "sale.order", "search", criteria
        );
        Object[] resultIds = (Object[]) xmlrpcClient.execute("execute", orderSearchParams.toArray());

        System.out.println("Order Size " + resultIds.length);

        if (resultIds.length == 0) {

            return true;
        } else {
            return false;
        }
    }

    public XmlRpcClient getLoginXmlRpcClient() throws MalformedURLException {

        XmlRpcClient xmlrpcLogin = new XmlRpcClient();
        XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
        xmlrpcConfigLogin.setEnabledForExtensions(true);
        xmlrpcConfigLogin.setServerURL(new URL(SCHEME, HOST, PORT, "/xmlrpc/common"));
        xmlrpcLogin.setConfig(xmlrpcConfigLogin);
        return xmlrpcLogin;
    }

    public XmlRpcClient getXmlRpcClient() throws MalformedURLException {
        XmlRpcClient xmlrpcClient = new XmlRpcClient();

        XmlRpcClientConfigImpl xmlrpcConfig = new XmlRpcClientConfigImpl();
        xmlrpcConfig.setEnabledForExtensions(true);
        xmlrpcConfig.setServerURL(new URL(SCHEME, AppGlobalProperties.OPENERP_HOST(), Integer.parseInt(AppGlobalProperties.OPENERP_PORT()), "/xmlrpc/object"));
        //xmlrpcConfig.setServerURL(new URL(SCHEME, HOST, PORT, "/xmlrpc/object"));
        xmlrpcClient.setConfig(xmlrpcConfig);
        return xmlrpcClient;
    }

    public Object[] findProduct(Integer connectionId, String productName) throws MalformedURLException, XmlRpcException {
        XmlRpcClient xmlrpcClient = getXmlRpcClient();

        List<Object> criteria = new ArrayList<Object>();
//        criteria.add(asList("name", "=", customerName).toArray());
//        criteria.add(asList("customer", "=", true).toArray());
        criteria.add(asList("name", "=", productName).toArray());
        List<Object> customerSearchParams = asList(
                DATABASE, connectionId, PASSWORD, "product.product", "search", criteria
        );
        Object customerIds = xmlrpcClient.execute("execute", customerSearchParams.toArray());

        criteria.clear();
        Arrays.stream((Object[]) customerIds).forEach(e -> criteria.add(Integer.valueOf(e.toString())));
        List<Object> readParams = asList(
                DATABASE, connectionId, PASSWORD, "product.product", "read", criteria
        );

        /**
         * result - array of hashmap, "ref":patient identifier (e.g GAN203006)
         * "uuid": emr patient uuid (e.g. "uuid" ->
         * "6e6691b4-27c5-4733-9f26-342a28317423") "sale_order_ids": array of
         * int objects "invoice_ids": : array of int objects "id":ERP partner id
         * (int)
         */

        System.out.println("Result " + readParams);

        Object result = xmlrpcClient.execute("execute", readParams.toArray());
        System.out.println("Result " + result);
        return (Object[]) result;
    }

    public int findCustomers(Integer connectionId, String patientUuid, boolean doubleCheck) throws MalformedURLException, XmlRpcException {
        XmlRpcClient xmlrpcClient = getXmlRpcClient();


        List<Object> criteria = new ArrayList<Object>();
        criteria.add(asList("ref", "=", patientUuid).toArray());
        List<Object> customerSearchParams = asList(
                DATABASE, connectionId, PASSWORD, "res.partner", "search", criteria
        );
        Object[] customerId = (Object[]) xmlrpcClient.execute("execute", customerSearchParams.toArray());

        if(doubleCheck && customerId.length<1)
        {
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           return  findCustomers( connectionId,  patientUuid, false);
        }

       if(customerId.length>0) {
           return (int) customerId[0];
       }else
       {
           return 0;
       }

    }

    public void sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            logger.info(response.toString());
        }

    }

    public static void sendPost(String url, String load) throws Exception {

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(load);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        StringBuffer response = null;
        if (responseCode == 200) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }

        logger.info(response.toString());
    }


}
