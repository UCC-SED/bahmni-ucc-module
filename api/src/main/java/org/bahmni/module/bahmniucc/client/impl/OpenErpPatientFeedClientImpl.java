package org.bahmni.module.bahmniucc.client.impl;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.bahmni.module.bahmniucc.UCCModuleConstants;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.db.DebtorRowDAO;
import org.bahmni.module.bahmniucc.model.DebtorRow;
import org.bahmni.module.bahmniucc.model.Notification;
import org.bahmni.module.bahmniucc.model.NotificationResult;
import org.bahmni.module.bahmniucc.util.AppGlobalProperties;
import org.bahmni.module.bahmniucc.util.OpenERPUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.bahmni.module.bahmniucc.util.OpenERPUtils.SCHEME;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
@Component("openErpPatientFeedClient")
public class OpenErpPatientFeedClientImpl implements OpenErpPatientFeedClient {

    private Logger logger = Logger.getLogger(getClass());
    private DebtorRowDAO debtorRowDAO;
    public static final String SCHEME = "http";
    public static final String DATABASE = "openerp";
    public static final String USER = "admin";
    public static final String PASSWORD = "password";

    @Autowired
    public OpenErpPatientFeedClientImpl(DebtorRowDAO debtorRowDAO) {
        this.debtorRowDAO = debtorRowDAO;
    }

    @Override
    public void processFeed() {

        try {
            logger.info("openelisatomfeedclient:processing feed " + DateTime.now());
            int LoginID = Login();
            getFeedClient(LoginID);
        } catch (Exception e) {

        }

    }

    @Override
    public void processMonitorData() {
        String monitorItemJson = debtorRowDAO.readQuery();

        try {
            OpenERPUtils.sendPost(UCCModuleConstants.SUPERSETURL, monitorItemJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String readAuthenticationHeader() {
        String authenticationHeader = debtorRowDAO.readAuthenticationHeader();
        return authenticationHeader;
    }


    @Override
    public void storeAuthenticationHeader(String header, String issue_date, String expire_date) {
        debtorRowDAO.storeAuthenticationHeader(header, issue_date, expire_date);

    }

    @Override
    public String checkDuplicateStatus(String name, String gender, String birthdate, String street, String council, String district, String region) {
        return debtorRowDAO.checkDuplicateStatus(name, gender, birthdate, street, council, district, region);
    }

    @Override
    public List searchTribes(String searchNames) {
        return debtorRowDAO.searchTribes(searchNames);
    }

    @Override
    public List getPatientInDept() {
        return debtorRowDAO.getPatientInDept();
    }


    @Override
    public ArrayList<NotificationResult> processNotifications(String patientUuid, String visitUuid) {
        ArrayList<NotificationResult> notificationResultsList = new ArrayList<>();
        ArrayList<Notification> notificationsList = debtorRowDAO.getNotifications();

        for (int x = 0; x < notificationsList.size(); x++) {
            NotificationResult notificationResult = debtorRowDAO.getNotificationResults(notificationsList.get(x));
            notificationResult.setNotification(notificationsList.get(x));
            notificationResultsList.add(notificationResult);
        }
        return notificationResultsList;
    }

    @Override
    public void saveHackTagData(String visitType) {
        debtorRowDAO.clearHack();
        debtorRowDAO.insertHack(visitType);

    }

    @Override
    public int getBillingCategoryId(String categoryName) {
       return  debtorRowDAO.readBillingCategoryIdByName(categoryName);
    }

    @Override
    public String getHackTagData() {

        return debtorRowDAO.readHack();
    }

    @Override
    public boolean getDrugBalance(String drugName) {

        try {
            logger.info("openelisatomfeedclient:processing feed " + DateTime.now());
            int LoginID = Login();
            logger.info("LoginID" + LoginID);
            return getStockAvaibilityStatusNew(LoginID, drugName);
        } catch (Exception e) {
            return false;
        }
    }

    public void getFeedClient(int LoginID) {

        try {

            XmlRpcClient xmlrpcClient = getXmlRpcClient();

            Object[] args2 = new Object[1];
            Object[] subargs = new Object[3];
            subargs[0] = "invoice_exists";
            subargs[1] = "=";
            subargs[2] = false;

            args2[0] = subargs;

            Object[] params = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID, UCCModuleConstants.OPENERP_PASSWORD, UCCModuleConstants.OPENERP_ORDER_MODEL, UCCModuleConstants.OPENERP_SEARCH_FUNCTION, args2};
            Object result = (Object[]) xmlrpcClient.execute("execute", params);

            Object[] searchQuery = new Object[]{};
            Vector readqueryVector = new Vector();
            readqueryVector.addElement(searchQuery);
            Object[] read = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID, UCCModuleConstants.OPENERP_PASSWORD, UCCModuleConstants.OPENERP_ORDER_MODEL, UCCModuleConstants.OPENERP_READ_FUNCTION, result, searchQuery};
            Object resultread = (Object[]) xmlrpcClient.execute("execute", read);

            Object[] a = (Object[]) resultread;

            List<DebtorRow> debtorList = new ArrayList<>();

            logger.info("Clear all results");
            debtorRowDAO.clearAllResults();

            logger.info("Object size " + a.length);
            for (Object object : a) {
                if (object instanceof String) {
                } else if (object instanceof Integer) {
                } else {
                    debtorList.add(getDebtorElements((HashMap) object));
                }
            }

            logger.info("Saving");
            debtorRowDAO.saveDebtorRow(debtorList);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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


    public int Login() throws MalformedURLException, XmlRpcException {
        XmlRpcClient xmlrpcLogin = new XmlRpcClient();
        XmlRpcClientConfigImpl xmlrpcConfigLogin = new XmlRpcClientConfigImpl();
        xmlrpcConfigLogin.setEnabledForExtensions(true);
        xmlrpcConfigLogin.setServerURL(new URL(SCHEME, AppGlobalProperties.OPENERP_HOST(), Integer.parseInt(AppGlobalProperties.OPENERP_PORT()), "/xmlrpc/common"));
        xmlrpcLogin.setConfig(xmlrpcConfigLogin);
        Object[] params = new Object[]{DATABASE, USER, PASSWORD};
        return (int) xmlrpcLogin.execute("login", params);

    }

    public DebtorRow getDebtorElements(Map mp) {
        Iterator it = mp.entrySet().iterator();

        DebtorRow debtor = new DebtorRow();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //logger.info((String) pair.getKey());
            switch ((String) pair.getKey()) {

                case UCCModuleConstants.OPENERP_NAME_ELEMENT:
                    debtor.setInvoice_id((String) pair.getValue());
                    break;
                case UCCModuleConstants.OPENERP_PARTNER_ELEMENT:
                    try {
                        Object[] items = (Object[]) pair.getValue();
                        String nameAndID = (String) items[1];
                        logger.info("nameAndID " + nameAndID);
                        String[] split = nameAndID.split("\\[");
                        logger.info("name " + split[0]);
                        debtor.setPatient_name(split[0]);
                        logger.info("ID " + split[1].replace("]", ""));
                        debtor.setPatient_id(split[1].replace("]", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info(e.getMessage());
                    }
                    break;
                case UCCModuleConstants.OPENERP_AMOUNT_ELEMENT:
                    debtor.setChargeable_amount((double) pair.getValue());

                    break;
                case UCCModuleConstants.OPENERP_QUANTITY_ELEMENT:
                    debtor.setDefault_quantity((double) pair.getValue());
                    break;
                case UCCModuleConstants.OPENERP_DATE_ELEMENT:
                    debtor.setDate_created((String) pair.getValue());
                    break;
                default:

            }
        }

        return debtor;
    }

    public boolean getStockAvaibilityStatus(int LoginID, String drugName) {

        try {
            logger.info("parameter " + drugName);
            XmlRpcClient xmlrpcClient = getXmlRpcClient();

            Object[] args2 = new Object[1];
            Object[] subargs = new Object[3];
            subargs[0] = "name";
            subargs[1] = "=";
            subargs[2] = drugName;

            args2[0] = subargs;

            Object[] params = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID,
                    UCCModuleConstants.OPENERP_PASSWORD, "product.product",
                    UCCModuleConstants.OPENERP_SEARCH_FUNCTION, args2};
            Object result = (Object[]) xmlrpcClient.execute("execute", params);

            logger.info("result value " + ((Object[]) result)[0]);

            Object[] args = new Object[3];
            Object[] subarg = new Object[3];
            subarg[0] = "product_id";
            subarg[1] = "=";
            subarg[2] = ((Object[]) result)[0];

            Object[] subargs2 = new Object[3];
            subargs2[0] = "state";
            subargs2[1] = "=";
            subargs2[2] = "done";

            Object[] subargs3 = new Object[3];
            subargs3[0] = "location_dest_id";
            subargs2[1] = "=";
            subargs3[2] = 14;

            args[0] = subarg;
            args[1] = subargs2;
            // args[2] = subargs3;

            Object[] searchQuery = new Object[]{};
            Vector readqueryVector = new Vector();
            readqueryVector.addElement(searchQuery);
            Object[] read = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID,
                    UCCModuleConstants.OPENERP_PASSWORD, "stock.move",
                    UCCModuleConstants.OPENERP_SEARCH_FUNCTION, args2};

            Object resultread = (Object[]) xmlrpcClient.execute("execute", read);

            logger.info("result string " + resultread.toString());
            Object[] a = (Object[]) resultread;

            logger.info("result size " + a.length);

            if (a.length > 0) {
                return true;
            }
            return false;
        } catch (XmlRpcException e) {
            logger.info("XmlRpcException " + e.getMessage());
            e.printStackTrace();
            return false;

        } catch (Exception e) {
            logger.info("Exception " + e.getMessage());
            e.printStackTrace();
            return false;

        }

    }


    public boolean getStockAvaibilityStatusNew(int LoginID, String drugName) {

        try {
            logger.info("parameter " + drugName);
            XmlRpcClient xmlrpcClient = getXmlRpcClient();

            Object[] args2 = new Object[1];

            Object[] name = new Object[3];
            name[0] = "name";
            name[1] = "=";
            name[2] = drugName;


            args2[0] = name;


            Object[] params = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID,
                    UCCModuleConstants.OPENERP_PASSWORD, "product.product",
                    UCCModuleConstants.OPENERP_SEARCH_FUNCTION, args2};
            Object productResult = (Object[]) xmlrpcClient.execute("execute", params);

            logger.info("productResult value " + ((Object[]) productResult)[0]);


            Object[] stockQuantity = new Object[2];
            Object[] product_id = new Object[3];
            product_id[0] = "name";
            product_id[1] = "=";
            product_id[2] = drugName;

            Object[] qty_available = new Object[3];
            qty_available[0] = "qty_available";
            qty_available[1] = ">";
            qty_available[2] = 0;


            stockQuantity[0] = product_id;
            //stockQuantity[1] = qty_available;


            Object[] read = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID,
                    UCCModuleConstants.OPENERP_PASSWORD, "product.product",
                    UCCModuleConstants.OPENERP_READ_FUNCTION, stockQuantity};

            Object resultread = (Object[]) xmlrpcClient.execute("execute", read);

            logger.info("result string " + resultread.toString());
            Object[] a = (Object[]) resultread;

            logger.info("result size " + a.length);


            if (((Object[]) resultread).length > 0) {
                return true;
            }
            return false;
        } catch (XmlRpcException e) {
            e.printStackTrace();
            logger.info("XmlRpcException " + e.getMessage());
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception " + e.getMessage());
            return false;

        }

    }

}
