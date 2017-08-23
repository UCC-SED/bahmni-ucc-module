package org.bahmni.module.bahmniucc.client.impl;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.bahmni.module.bahmniucc.UCCModuleConstants;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.db.impl.DebtorRowDAOImpl;
import org.bahmni.module.bahmniucc.model.Debtor;
import org.joda.time.DateTime;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public class OpenErpPatientFeedClientImpl implements DebtClient {
 private Logger logger = Logger.getLogger(OpenErpPatientFeedClientImpl.class);
    private DebtorRowDAOImpl debtorDAO = new DebtorRowDAOImpl();

    @Override
    public void processFeed() {

        try {
            logger.info("openelisatomfeedclient:processing feed " + DateTime.now());
            int LoginID = Login();
            getFeedClient(LoginID);
        } catch (Exception e) {

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

            debtorDAO.deleteRows();
            Object[] a = (Object[]) resultread;
            for (Object object : a) {

                if (object instanceof String) {
                    System.out.println((String) object);
                } else if (object instanceof Integer) {
                    System.out.println((Integer) object);
                } else {
                    Debtor debtor = getDebtorElements((java.util.HashMap) object);
                    debtorDAO.saveRow(debtor);

                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

    }


    public int Login() {

        try {

            logger.info("Login Function" + DateTime.now());
            XmlRpcClient xmlrpcLogin = new XmlRpcClient();

            XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
            xmlrpcConfigLogin.setEnabledForExtensions(true);
            xmlrpcConfigLogin.setServerURL(new URL("http", UCCModuleConstants.OPENERP_HOST, UCCModuleConstants.OPENERP_PORT, "/xmlrpc/common"));

            xmlrpcLogin.setConfig(xmlrpcConfigLogin);

            Object[] params = new Object[]{UCCModuleConstants.OPENERP_DB, UCCModuleConstants.OPENERP_USERNAME, UCCModuleConstants.OPENERP_PASSWORD};
            Object id = xmlrpcLogin.execute("login", params);
            logger.info("Login Function Excuted" + DateTime.now());
            if (id instanceof Integer) {

                return (Integer) id;
            }

            logger.info(id);

            return -1;
        } catch (XmlRpcException e) {

            logger.info("XmlException Error while logging to OpenERP: ", e);
            e.printStackTrace();
            return -2;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("XmlException Error while logging to OpenERP: ", e);
            e.printStackTrace();
            return -3;
        }
    }


    public Debtor getDebtorElements(Map mp) {
        Iterator it = mp.entrySet().iterator();

        Debtor debtor = new Debtor();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            switch ((String) pair.getKey()) {
                case UCCModuleConstants.OPENERP_NAME_ELEMENT:
                    debtor.invoice_id = (String) pair.getValue();
                    break;
                case UCCModuleConstants.OPENERP_PARTNER_ELEMENT:
                    Object[] items = (Object[]) pair.getValue();
                    String nameAndID = (String) items[1];
                    String[] split = nameAndID.split("\\[");
                    debtor.patient_name = split[0];
                    debtor.patient_id = split[1].replace("]", "");
                    break;
                case UCCModuleConstants.OPENERP_AMOUNT_ELEMENT:
                    debtor.chargeable_amount = (double) pair.getValue();
                    break;
                case UCCModuleConstants.OPENERP_QUANTITY_ELEMENT:
                    debtor.default_quantity = (int) pair.getValue();
                    break;
                case UCCModuleConstants.OPENERP_DATE_ELEMENT:
                    debtor.date_created = (Date) pair.getValue();
                    break;
                default:

            }
        }


        return debtor;
    }


}
