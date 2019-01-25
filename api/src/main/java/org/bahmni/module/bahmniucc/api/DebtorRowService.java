/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 *
 */

package org.bahmni.module.bahmniucc.api;

import org.bahmni.module.bahmniucc.model.DebtorRow;
import org.openmrs.api.OpenmrsService;

import java.util.List;

/**
 * Defines the services provided by the Data Integrity Module
 */
public interface DebtorRowService extends OpenmrsService {


	public void saveDebtorRow(List<DebtorRow> results);
	public void saveDebtorRowNew();
	public String updateRoom(String room, String uuid);
	public String insertPriceList(String room, String uuid);

	public String insertledger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,
							   String InvoiceNo, String ExpiryDate, String receiveDate,String price_list_id, float amount,String mathz );

	String updateStatus(int drug_id, int status,int concept_id);

    public String insertPhysical(String drugID, double quantity, int pricelistID, String receivedDate,String recorededDate,String batchNo);

	public String insertPrice(String drugID, float amount, int pricelistID,float buying);

	public String createItem(String name, String category, String strength, String dosageForm );

	public String update_drug_orderlinie(String order_id,int item_id,  int qty,  int patientId );

	public String cancelOrder(String orderID);

	String createdrug_requestOrder(int item_id,int qty_req,int price_listId,int userId,int sub_store_id);

	String reduct_drugQuantity(int qty,int item_id,int pricelistId);

	public String cancelOrderLine(String orderlineID);

	public  String paymentConfirmed(String orderID);

	public int updateStockQty(int conceptId, int qty, int priceList_id);

	public  String saveDiscount(float dicountAmount,int item,int paid, String orderID);

	public  String processCtrlNumber(String BillId, String PayCntrNum);

	public String add_control_number(String BillId,String control_number,String status,String status_code);

	String post_payment(String transaction_id,String bill_id,String control_number,String bill_amount,String paid_amount,String phone_number);

	List get_information(String keyword);

	/**
	 * Clear all the results
	 */
	public void clearAllResults();

	/**
	 -----------DEC 2018 ------
	 */
	public String editItem(int concept_name_id,int item_drug_id,String itemId,String name, String strength, String dosageForm,String dateCreated);
	public String editPrice(int item_price_id,String drugID, float amount, int pricelistID,String dateRecorded,float buying_price);
	public List getPriceList();
	public String editPhysical(int physical_inventory_id,String drug_id, double qty, int priceList, String receivedDate, String recorededDate, String batchNo);
	public List selectAllPhysical();
	String updateLedger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,String InvoiceNo, String ExpiryDate, String receiveDate, String price_list_id, float amount, String mathz,int ledger_entry_id);
	public List getLedger_entry();
	public List get_total_physical();
	public List get_physical_value();
	public List get_stock_value();
	public List get_total_stock();
	List get_expiry_stock();
	List get_expire_value();
	List get_all_ledger();
	List getItems();
	
	//Product Movement
	
	public String updateDispatch_Batch(int person_id_sub_store, String product_mvnt_status);
	public String updateDispatch_Row( int id, String product_mvnt_status);
	public String EditProductMovement(int id,int person_id_sub_store, int prod_mv_id, int item_id, String date_qty_requested,
	    int quantity_requested, int quantity_given, String date_qty_given, int person_id_main_store, int sub_store_id, int product_batch_no, 
	    String product_mvnt_status, int price_list_id  );
	    
}
