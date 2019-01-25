package org.bahmni.module.bahmniucc.client;

import org.bahmni.module.bahmniucc.model.NotificationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public interface DebtClient {

    void processFeed();

    void processMonitorData();

    void saveHackTagData(String visitType);

    String getHackTagData();

    int get_sub_store_id(int userId);

    boolean getDrugBalance(String drugName);

    ArrayList<NotificationResult> processNotifications(String patientUuid, String visitUuid);

    String readAuthenticationHeader();

    void storeAuthenticationHeader(String header, String issue_date, String expire_date);

    String checkDuplicateStatus(String name, String gender, String birthdate, String street, String council, String district, String region);

    List searchTribes(String searchNames);

    List get_thresholdDrugs();

    List getPatientInDept();

    List get_dbData(String qry);

    String getDrug_request(String drug_name);

    String updateRoom(String room, String patientUuid);

    String priceList(String name, String defaultPriceList);

    String insertledger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,
                                        String InvoiceNo, String ExpiryDate, String receiveDate,String price_list_id, float amount,String mathz );

    String updateStatus(int drug_id, int status,int concept_id);

    String createdrug_requestOrder(int item_id,int qty_req,int price_listId,int userId,int sub_store_id);

    String  insertPhysical(String drug_id, double qty, int priceList,String receivedDate,String recorededDate,String batchNo);

    String createItem(String name, String category, String strength, String dosageForm );

    String insertPrice(String drugID, float amount, int pricelistID,float buying);

    String billItems(String orderID);

    String reduct_drugQuantity(int qty, int item_id,int pricelistId);

    List selectPriceLists();

    String processCtrlNumber(String BillId, String PayCntrNum);

    List selectPhysical(String name,String byDate,String byBatch);

    List getDrug(String name);

    List getItems_unfiltered(String name);

    List selectPrice(String name);

    List getAddItems(String name);

    List getSalesOrders(String search);

    List getSalesOrders_line(String orderID);

    List getSalesOrders_line_other(String orderID);

    List selectLedger_entry(String name,String batchNo, String invID);

    String cancelOrder(String orderID);

    String cancelOrderLine(String orderlineID);

    String paymentConfirmed(String orderID);

    List getDiscountOrders(String search);

    List getPaidorders(String search);

    int get_qty_conceptId(String order_uuid, String type);

    int updateStockQty(int conceptId, int qty, int priceList_id);

    List getCancelledorders(String search);

    int getBillingCategoryId(String categoryName);

    boolean checkIfCustomerConsultationExemption(String patientIdentifier, int dated);

    String getSaleOrderId(String patientIdentifier,String type);

    int getPatientID(String patientIdentifier,String type);

    int get_location_id(String locationUuid);

    int getItem_id(String itemUuid);

    boolean check_if_orderlineExist(String patientIdentifier, String item_uuid,String type);

    int getDrugItem_id(String itemUuid);

    int gePriceListID(String paymentType);

    void saveDebtorRowNew();

    float getItem_amount(int itemId, int priceListId);

    String saveDiscount(float dicountAmount,int item,int paid, String orderID);

    String update_drug_orderlinie(String order_id,int item_id,  int qty,  int patientId );

    String createorder_line(String order_id,int item_id, String item_type, int qty, float amount, int patient_id, int visit_id , int payment_category);

    String process_control_number(String bill_id,String status,String control_number,String transaction_code);

    String post_payment(String transaction_id,String bill_id,String control_number,String bill_amount,String paid_amount,String phone_number);

    String get_reconciliation_data(String bill_id);

    String check_amount(String bill_id);

    String check_control_number(String control_number);

    String check_bill_id(String bill);

    String add_reconciliation_response(String reconciliation_id,String bill_id,String transaction_id, String control_number, float amount, String payer_reference_id, String transaction_date,String account_number, String remarks );

    String add_gep_response(String response_id,String message, String response_type);

    String add_cancel_status(String bill_id,String message);

    List get_information(String keyword);

    /**
     -------- DEC 2018 -------
     */
    String editItem(int concept_name_id,int item_drug_id,String itemId,String name, String strength, String dosageForm,String dateCreated);
    String editPrice(int item_price_id,String drugID, float amount, int pricelistID,String dateRecorded,float buying_price);
    List getPriceList();
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
    List getItems();
    
    /**  
     .............EDIT PRODUCT MOVENTMENT
     */
     
    String updateDispatch_Batch(int person_id_sub_store, String product_mvnt_status);
    String updateDispatch_Row( int id, String product_mvnt_status);
    String EditProductMovement(int id,int person_id_sub_store, int prod_mv_id, int item_id, String date_qty_requested,
    int quantity_requested, int quantity_given, String date_qty_given, int person_id_main_store, int sub_store_id, int product_batch_no, 
    String product_mvnt_status, int price_list_id  );
    
}

