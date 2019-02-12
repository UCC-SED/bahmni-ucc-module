package org.bahmni.module.bahmniucc.db;

import org.bahmni.module.bahmniucc.model.DebtorRow;
import org.bahmni.module.bahmniucc.model.Notification;
import org.bahmni.module.bahmniucc.model.NotificationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public interface DebtorRowDAO {

    public void saveDebtorRow(List<DebtorRow> results);

    /**
     * Clear all the results
     */
    void clearAllResults();

    String readQuery();


    void insertHack(String visitType);

    void clearHack();

    String readHack();

    ArrayList<Notification> getNotifications();

    NotificationResult getNotificationResults(Notification notification);

    String readAuthenticationHeader();

    void storeAuthenticationHeader(String header, String issue_date, String expire_date);

    String checkDuplicateStatus(String name, String gender, String birthdate, String street, String council, String district, String region);

    List searchTribes(String searchNames);

    List getPatientInDept();

    int get_sub_store_id(int userId);

   List getDrug_request(String drug_name);

    String createdrug_requestOrder(int item_id,int qty_req,int price_listId,int userId,int sub_store_id);

    String updateRoom(String room, String uuid);

    String insertPriceList(String name, String defaultPrice);

     String insertledger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,
                               String InvoiceNo, String ExpiryDate, String receiveDate,String price_list_id, float amount, String mathz );

    String updateStatus(int drug_id, int status,int concept_id);

    String  insertPhysical(String drug_id, double qty, int priceList, String receivedDate,String recorededDate,String batchNo);

    String insertPrice(String drugID, float amount, int pricelistID,float buying);

    String createItem(String name, String category, String strength, String dosageForm );

    List getDrug(String name);

    List selectPriceLists();

    int get_qty_conceptId(String order_uuid, String type);

    int updateStockQty(int conceptId, int qty, int priceList_id);

    List selectPhysical(String name,String byDate,String byBatch);

    String reduct_drugQuantity(int qty,int item_id,int pricelistId);

    List getItems_unfiltered(String name);

    List selectLedger_entry(String name,String batchNo, String invID);

    List getAddItems(String name);

    List getSalesOrders(String search);

    List get_dbData(String qry);

    List getSalesOrders_line(String search);

    List getSalesOrders_line_other(String orderID);

    String cancelOrder(String orderID);

    String cancelOrderLine(String orderlineID);

    String paymentConfirmed(String orderID);

    List getPaidorders(String search);

    List getCancelledorders(String search);

    int readBillingCategoryIdByName(String categoryName);

    String getSaleOrderId(String patientIdentifier, String type);

    boolean checkIfCustomerConsultationExemption(String patientIdentifier, int dated);

    String createorder_line(String order_id,int item_id, String item_type, int qty, float amount, int patient_id, int visit_id, int payment_category );

    List selectPrice(String name);

    String billItems(String orderID);

    boolean check_if_orderlineExist(String patientIdentifier, String item_uuid, String type);

    int get_location_id(String locationUuid);

    int getItem_id(String itemUuid);

    int getDrugItem_id(String itemUuid);

    List getDiscountOrders(String search);

    float getItem_amount(int itemId, int priceListId);

    int getPatientID(String patientIdentifier,String type);

    int gePriceListID(String paymentType);

    void saveDebtorRowNew();

    boolean getDrugBalance(String drugName);

    List get_thresholdDrugs();

    String processCtrlNumber(String BillId, String PayCntrNum);

    String update_drug_orderlinie(String order_id,int item_id,  int qty,  int patientId );

    String saveDiscount(float dicountAmount,int item,int paid, String orderID);

    String add_control_number(String bill_id,String control_number,String status,String status_code);

    String get_reconciliation_data(String bill_id);

    String post_payment(String transaction_id,String bill_id,String control_number,String bill_amount,String paid_amount,String phone_number);

    String check_amount(String bill_id);

    String check_control_number(String control_number);

    String check_bill_id(String bill_id);

    String add_reconciliation_response(String reconciliation_id,String bill_id,String transaction_id, String control_number, float amount, String payer_reference_id, String transaction_date,String account_number, String remarks );

    String add_gep_response(String response_id,String message, String response_type);

    String add_cancel_status(String bill_id,String message);

    List get_information(String keyword);

    /**
     ------------
     */
    String  editItem(int concept_name_id,int item_drug_id,String itemId,String name,String strength,String dosageForm,String dateCreated);
    String editPrice(int item_price_id,String drugID, float amount, int pricelistID,String dateRecorded,float buying_price);
    String editPhysical(int physical_inventory_id,String drug_id, double qty, int priceList, String receivedDate, String recorededDate, String batchNo);
    List selectAllPhysical();
    String updateLedger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,String InvoiceNo, String ExpiryDate, String receiveDate, String price_list_id, float amount, String mathz,int ledger_entry_id);
    List getLedger_entry();
    List get_total_physical();
    List get_physical_value();
    List get_stock_value();
    List get_total_stock();
    List get_expiry_stock();
    List get_expire_value();
    List get_all_ledger();
    List getPriceList();
    List getItems();
    
    /**
     * 
     PRODUCT MOVEMENT
     * 
     */
    String updateDispatch_Batch(int person_id_sub_store, String product_mvnt_status);
    String updateDispatch_Row( int id, String product_mvnt_status);
    String EditProductMovement(int id,int person_id_sub_store, int prod_mv_id, int item_id, String date_qty_requested,int quantity_requested, int quantity_given, String date_qty_given, int person_id_main_store, int sub_store_id, int product_batch_no, String product_mvnt_status, int price_list_id );


    /*
     LOCATION MAPPING
     */

    String createLocation(int personID, int locationID );
    String editLocation( int id, int personID, int locationID);
    List getLocation_List();

    // userID list
    List userID_List(String fullname);

    //location Tag
    List Location_Tag_List(String Location_name);

}
