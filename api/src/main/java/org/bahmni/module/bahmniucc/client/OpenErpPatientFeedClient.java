package org.bahmni.module.bahmniucc.client;

import java.util.List;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public interface OpenErpPatientFeedClient extends DebtClient{

    String get_reconciliation_data(String bill_id);

    String check_amount(String bill_id);

    String check_control_number(String control_number);

    String check_bill_id(String bill_id);

    String add_reconciliation_response(String reconciliation_id,String bill_id,String transaction_id, String control_number, float amount, String payer_reference_id, String transaction_date,String account_number, String remarks );

    String add_gep_response(String response_id,String message, String response_type);

    String add_cancel_status(String bill_id,String message);

    List get_information(String keyword);

    /**
     -------------DEC 2018 ------
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
}
