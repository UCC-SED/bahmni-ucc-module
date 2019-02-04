package org.bahmni.module.bahmniucc.client.impl;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
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
            //int LoginID = Login();
           // getFeedClient(LoginID);
            saveDebtorRowNew();
        } catch (Exception e) {

        }

    }
    public void saveDebtorRowNew(){
       debtorRowDAO.saveDebtorRowNew();
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
    public int gePriceListID(String paymentType){

      return  debtorRowDAO.gePriceListID( paymentType);
    }

    @Override
    public int get_sub_store_id(int userId){

        return debtorRowDAO.get_sub_store_id( userId);
    }

    @Override
    public String getDrug_request(String drug_name){
        return debtorRowDAO.getDrug_request( drug_name);
    }

    @Override
    public String createdrug_requestOrder(int item_id,int qty_req,int price_listId,int userId,int sub_store_id){

        return debtorRowDAO.createdrug_requestOrder( item_id, qty_req, price_listId, userId,sub_store_id);
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
    public String updateRoom(String room, String patientUuid) {

        return debtorRowDAO.updateRoom(room, patientUuid);
    }
    @Override
    public String priceList(String name, String defaultPriceList){


        return debtorRowDAO.insertPriceList(name,defaultPriceList);
    }
    @Override
    public int get_qty_conceptId(String order_uuid, String type){
        return debtorRowDAO.get_qty_conceptId( order_uuid,  type);
    }
    @Override
    public int updateStockQty(int conceptId, int qty, int priceList_id){

        return debtorRowDAO.updateStockQty(conceptId, qty,  priceList_id);
    }

    @Override
    public List get_dbData(String qry){
        return debtorRowDAO.get_dbData(qry);
    }

    @Override
    public String insertPhysical(String drug_id, double qty, int priceList,String receivedDate,String recorededDate, String batchNo){


        return debtorRowDAO.insertPhysical(drug_id,qty,priceList,  receivedDate, recorededDate, batchNo);
    }
    @Override
    public  String reduct_drugQuantity(int qty,int item_id,int pricelistId){

        return debtorRowDAO.reduct_drugQuantity(qty,item_id,pricelistId);
    }

    @Override
    public List getDrug(String name){

        return debtorRowDAO.getDrug(name);
    }

    @Override
    public String insertledger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,
                               String InvoiceNo, String ExpiryDate, String receiveDate ,String price_list_id, float amount,String mathz){

        return debtorRowDAO.insertledger(item_name,LedgerEntryType,quantity,BatchNo,InvoiceNo,ExpiryDate,receiveDate,price_list_id,amount, mathz);

    }

    @Override
    public String insertPrice(String drugID, float amount, int pricelistID,float buying){
        return debtorRowDAO.insertPrice(drugID,amount,pricelistID,buying);
    }

    @Override
    public List selectPrice(String name){

     return  debtorRowDAO.selectPrice(name);
    }
    @Override
    public String createItem(String name, String category, String strength, String dosageForm ){
    return debtorRowDAO.createItem(name,category,strength,dosageForm);
    }
    @Override
    public List selectPriceLists(){
        return debtorRowDAO.selectPriceLists();
    }
    @Override
    public List getAddItems(String name){
    return debtorRowDAO.getAddItems(name);
    }
    @Override
    public List getSalesOrders(String search){
    return debtorRowDAO.getSalesOrders(search);
    }
    @Override
    public List getDiscountOrders(String search){
        return debtorRowDAO.getDiscountOrders(search);
    }
    public String cancelOrder(String orderID){
        return debtorRowDAO.cancelOrder(orderID);
    }
    @Override
    public  String cancelOrderLine(String orderlineID){
        return debtorRowDAO.cancelOrderLine(orderlineID);

    }
    @Override
    public String saveDiscount(float dicountAmount,int item,int paid, String orderID){
        return debtorRowDAO.saveDiscount( dicountAmount, item, paid, orderID);
    }

    @Override
    public String paymentConfirmed(String orderID){

        return debtorRowDAO.paymentConfirmed(orderID);
    }
    @Override
    public List getPaidorders(String search){

        return debtorRowDAO.getPaidorders(search);
    }
    @Override
    public List getSalesOrders_line(String orderID){
    return debtorRowDAO.getSalesOrders_line(orderID);
    }

    @Override
    public  List getSalesOrders_line_other(String orderID){
       return debtorRowDAO.getSalesOrders_line_other(orderID);
    }

    @Override
    public List selectPhysical(String name,String byDate,String byBatch){

        return debtorRowDAO.selectPhysical(name,byDate,byBatch);
    }
    @Override
    public List selectLedger_entry(String name,String batchNo, String invID){
        return debtorRowDAO.selectLedger_entry(name,batchNo,invID);
    }
    @Override
   public List getCancelledorders(String search){
        return debtorRowDAO.getCancelledorders(search);
    }

    @Override
    public String  updateStatus(int drug_id, int status,int concept_id){
        return debtorRowDAO.updateStatus(drug_id,status,concept_id);
    }

    @Override
    public String billItems(String orderID){
        return debtorRowDAO.billItems(orderID);
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
    public String processCtrlNumber(String BillId, String PayCntrNum){
        return debtorRowDAO.processCtrlNumber( BillId,  PayCntrNum);
    }

    @Override
    public void saveHackTagData(String visitType) {
        debtorRowDAO.clearHack();
        debtorRowDAO.insertHack(visitType);

    }
    @Override
    public List getItems_unfiltered(String name){

        return debtorRowDAO.getItems_unfiltered(name);
    }

    @Override
    public int getBillingCategoryId(String categoryName) {
       return  debtorRowDAO.readBillingCategoryIdByName(categoryName);
    }

    @Override
    public  String getSaleOrderId(String patientIdentifier, String type) {
        return  debtorRowDAO.getSaleOrderId(patientIdentifier,type);
    }
    @Override
    public boolean checkIfCustomerConsultationExemption(String patientIdentifier, int dated){

        return  debtorRowDAO.checkIfCustomerConsultationExemption(patientIdentifier,dated);
    }

    @Override
    public String createorder_line(String order_id,int item_id, String item_type, int qty, float amount, int patient_id, int visit_id, int payment_category )
    {
        return  debtorRowDAO.createorder_line(order_id, item_id,  item_type,  qty,  amount,  patient_id,  visit_id,  payment_category);
    }

    @Override
    public String process_control_number(String bill_id, String status, String control_number, String transaction_code) {
        return  debtorRowDAO.add_control_number(bill_id,control_number,status,transaction_code);
    }

    @Override
    public String post_payment(String transaction_id,String bill_id, String control_number, String bill_amount, String paid_amount, String phone_number) {
        return  debtorRowDAO.post_payment(transaction_id,bill_id,control_number,bill_amount,paid_amount,phone_number);
    }

    @Override
    public String update_drug_orderlinie(String order_id,int item_id,  int qty,  int patientId ){
        return  debtorRowDAO.update_drug_orderlinie(order_id, item_id,   qty, patientId);

    }


    @Override
    public int getPatientID(String patientIdentifier,String type){
        return  debtorRowDAO.getPatientID(patientIdentifier,type);
    }
    @Override
    public int get_location_id(String locationUuid){
        return  debtorRowDAO.get_location_id(locationUuid);
    }

    @Override
    public String getHackTagData() {

        return debtorRowDAO.readHack();
    }

    @Override
    public boolean check_if_orderlineExist(String patientIdentifier, String item_uuid,String type){

        return debtorRowDAO.check_if_orderlineExist( patientIdentifier,  item_uuid,type);
    }
    @Override
    public int getDrugItem_id(String itemUuid){
        return debtorRowDAO.getDrugItem_id(itemUuid);
    }
    @Override
    public float getItem_amount(int itemId, int priceListId){

        return debtorRowDAO.getItem_amount(itemId,priceListId);
    }
    @Override
    public int getItem_id(String itemUuid){

        return debtorRowDAO.getItem_id(itemUuid);
    }
    @Override
    public boolean getDrugBalance(String drugName) {

        return debtorRowDAO.getDrugBalance(drugName);
    }
    @Override
    public List get_thresholdDrugs(){

        return debtorRowDAO.get_thresholdDrugs();
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
            Object result = xmlrpcClient.execute("execute", params);

            Object[] searchQuery = new Object[]{};
            Vector readqueryVector = new Vector();
            readqueryVector.addElement(searchQuery);
            Object[] read = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID, UCCModuleConstants.OPENERP_PASSWORD, UCCModuleConstants.OPENERP_ORDER_MODEL, UCCModuleConstants.OPENERP_READ_FUNCTION, result, searchQuery};
            Object resultread =  xmlrpcClient.execute("execute", read);

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
            Object result = xmlrpcClient.execute("execute", params);

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
            args[2] = subargs3;

            Object[] searchQuery = new Object[]{};
            Vector readqueryVector = new Vector();
            readqueryVector.addElement(searchQuery);
            Object[] read = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID,
                    UCCModuleConstants.OPENERP_PASSWORD, "stock.move",
                    UCCModuleConstants.OPENERP_SEARCH_FUNCTION, args2};

            Object resultread = xmlrpcClient.execute("execute", read);

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
            Object productResult = xmlrpcClient.execute("execute", params);

            logger.info("productResult value " + ((Object[]) productResult)[0]);


            Object[] stockQuantity = new Object[2];
            Object[] product_id = new Object[3];
            product_id[0] = "name";
            product_id[1] = "=";
            product_id[2] = drugName;

            Object[] qty_available = new Object[3];
            qty_available[0] = "stock_available";
            qty_available[1] = ">";
            qty_available[2] = 0;


            stockQuantity[0] = product_id;
            //stockQuantity[1] = qty_available;


            Object[] read = new Object[]{UCCModuleConstants.OPENERP_DB, LoginID,
                    UCCModuleConstants.OPENERP_PASSWORD, "stock.move",
                    UCCModuleConstants.OPENERP_READ_FUNCTION, stockQuantity};

            Object resultread = xmlrpcClient.execute("execute", read);

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

    @Override
    public String get_reconciliation_data(String bill_id){
        return debtorRowDAO.get_reconciliation_data(bill_id);
    }

    @Override
    public String check_amount(String bill_id){
        return debtorRowDAO.check_amount(bill_id);
    }

    @Override
    public String check_control_number(String control_number){
        return debtorRowDAO.check_control_number(control_number);
    }

    @Override
    public String check_bill_id(String bill_id){
        return debtorRowDAO.check_bill_id(bill_id);
    }

    @Override
    public String add_reconciliation_response(String reconciliation_id,String bill_id,String transaction_id, String control_number, float amount, String payer_reference_id, String transaction_date,String account_number, String remarks ){
        return debtorRowDAO.add_reconciliation_response(reconciliation_id,bill_id,transaction_id, control_number,amount,payer_reference_id,transaction_date,account_number,remarks);
    }

    @Override
    public String add_gep_response(String response_id,String message, String response_type){
        return debtorRowDAO.add_gep_response(response_id,message,response_type);
    }

    @Override
    public String add_cancel_status(String bill_id,String status){
        return debtorRowDAO.add_cancel_status(bill_id,status);
    }

    @Override
    public List get_information(String keyword){
        return debtorRowDAO.get_information(keyword);
    }

    //========================================DEC 2018 =====================================================

    @Override
    public String editItem(int concept_name_id,int item_drug_id,String itemId, String name, String strength, String dosageForm,String dateCreated) {
        return debtorRowDAO.editItem(concept_name_id,item_drug_id,itemId,name,strength,dosageForm,dateCreated);
    }
    @Override
    public String editPrice(int item_price_id,String drugID, float amount, int pricelistID,String dateRecorded,float buying_price) {
        return debtorRowDAO.editPrice(item_price_id,drugID,amount,pricelistID,dateRecorded,buying_price);
    }


    @Override
    public  String editPhysical(int physical_inventory_id,String drug_id, double qty, int priceList, String receivedDate, String recorededDate, String batchNo) {
        return debtorRowDAO.editPhysical(physical_inventory_id,drug_id,qty,priceList,receivedDate,recorededDate,batchNo);
    }

    @Override
    public String updateLedger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,String InvoiceNo, String ExpiryDate, String receiveDate, String price_list_id, float amount, String mathz,int ledger_entry_id){
        return debtorRowDAO.updateLedger(item_name,LedgerEntryType,quantity,BatchNo,InvoiceNo,ExpiryDate,receiveDate,price_list_id,amount,mathz,ledger_entry_id);
    }

    @Override
    public List getLedger_entry(){
        return debtorRowDAO.getLedger_entry();
    }

    @Override
    public List get_total_physical(){
        return debtorRowDAO.get_total_physical();
    }

    @Override
    public List get_physical_value(){
        return debtorRowDAO.get_physical_value();
    }

    @Override
    public List get_stock_value(){
        return debtorRowDAO.get_stock_value();
    }

    @Override
    public List get_total_stock(){
        return debtorRowDAO.get_total_stock();
    }


    @Override
    public List selectAllPhysical() {
        return debtorRowDAO.selectAllPhysical();
    }

    @Override
    public List getPriceList() {
        return debtorRowDAO.getPriceList();
    }

    @Override
    public List get_expiry_stock() {
        return debtorRowDAO.get_expiry_stock();
    }

    @Override
    public List get_expire_value() {
        return debtorRowDAO.get_expire_value();
    }

    @Override
    public List get_all_ledger() {
        return debtorRowDAO.get_all_ledger();
    }

    @Override
    public List getItems() {
        return debtorRowDAO.getItems();
    }

    @Override
    public String EditProductMovement (int id,int person_id_sub_store, int prod_mv_id, int item_id, String date_qty_requested,int quantity_requested, int quantity_given, String date_qty_given, int person_id_main_store, int sub_store_id, int product_batch_no, String product_mvnt_status, int price_list_id ){
        return debtorRowDAO.EditProductMovement(id,person_id_sub_store,prod_mv_id,item_id,date_qty_requested,quantity_requested,quantity_given,date_qty_given,person_id_main_store,sub_store_id,product_batch_no,product_mvnt_status,price_list_id );
    }

    @Override
    public String updateDispatch_Batch(int person_id_sub_store, String product_mvnt_status) {
    	return debtorRowDAO.updateDispatch_Batch(person_id_sub_store,product_mvnt_status);
    }

    @Override
    public String updateDispatch_Row( int id, String product_mvnt_status) {
        return debtorRowDAO.updateDispatch_Row(id,product_mvnt_status);
    }

    @Override
    public String createLocation( int personID, int locationId) {
        return debtorRowDAO.createLocation(personID, locationId);
    }
    @Override
    public String editLocation( int id, int personId, int locationID) {
        return debtorRowDAO.editLocation(id,personId, locationID);
    }
    @Override
    public List getLocation_List() { return debtorRowDAO.getLocation_List();
    }

    @Override
    public List userID_List(String fullname) { return debtorRowDAO.userID_List(fullname);
    }

    @Override
    public List Location_Tag_List(String Location_name) { return debtorRowDAO.Location_Tag_List(Location_name);
    }
}
