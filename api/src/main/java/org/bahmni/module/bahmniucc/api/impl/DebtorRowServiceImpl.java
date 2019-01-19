package org.bahmni.module.bahmniucc.api.impl;

import org.bahmni.module.bahmniucc.api.DebtorRowService;
import org.bahmni.module.bahmniucc.db.DebtorRowDAO;
import org.bahmni.module.bahmniucc.model.DebtorRow;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ucc-ian on 24/Aug/2017.
 */
public class DebtorRowServiceImpl  extends BaseOpenmrsService implements DebtorRowService {

    private DebtorRowDAO debtorRowDAO;

    public void setDebtorRowDAO(DebtorRowDAO debtorRowDAO) {
        this.debtorRowDAO = debtorRowDAO;
    }

    @Override
    @Transactional(readOnly = false)
    public void saveDebtorRow(List<DebtorRow> results) {
        debtorRowDAO.saveDebtorRow(results);
    }
    @Override
    @Transactional(readOnly = false)
    public String updateRoom(String room, String uuid) {
        debtorRowDAO.updateRoom(room,uuid);
        return "updated";
    }
    @Override
    @Transactional(readOnly = false)
    public String createdrug_requestOrder(int item_id,int qty_req,int price_listId,int userId,int sub_store_id){

        return debtorRowDAO.createdrug_requestOrder( item_id, qty_req, price_listId, userId,sub_store_id);
    }

    @Override
    @Transactional(readOnly = false)
    public int updateStockQty(int conceptId, int qty, int priceList_id){

        return debtorRowDAO.updateStockQty( conceptId,  qty, priceList_id);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveDebtorRowNew(){  debtorRowDAO.saveDebtorRowNew();}

    @Override
    @Transactional(readOnly = false)
    public String insertPriceList(String name, String defaultvalue){

        return debtorRowDAO.insertPriceList(name, defaultvalue);
    }

    @Override
    @Transactional(readOnly = false)
    public String insertledger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,
                               String InvoiceNo, String ExpiryDate, String receiveDate,String price_list_id, float amount,String mathz ){

        return debtorRowDAO.insertledger(item_name, LedgerEntryType,quantity,BatchNo,InvoiceNo,ExpiryDate,receiveDate,price_list_id,amount,  mathz);
    }

    @Override
    @Transactional(readOnly = false)
    public String  updateStatus(int drug_id, int status,int concept_id){
        return debtorRowDAO.updateStatus(drug_id,status,concept_id);
    }

    @Override
    @Transactional(readOnly = false)
    public String  insertPhysical(String drug_id, double qty, int priceList, String receivedDate,String recorededDate,String batchNo){

        return debtorRowDAO.insertPhysical(drug_id,qty,priceList,receivedDate,recorededDate,batchNo);
    }

    @Override
    @Transactional
    public String processCtrlNumber(String BillId, String PayCntrNum){

        return debtorRowDAO.processCtrlNumber(BillId,PayCntrNum);
    }

    @Override
    @Transactional(readOnly = false)
    public String add_control_number(String BillId, String control_number, String status, String status_code) {
        return debtorRowDAO.add_control_number(BillId,control_number,status,status_code);
    }

    @Override
    public String post_payment(String transaction_id, String bill_id, String control_number, String bill_amount, String paid_amount, String phone_number) {
        return debtorRowDAO.post_payment(transaction_id,bill_id,control_number,bill_amount,paid_amount,phone_number);
    }

    @Override
    public List get_information(String keyword) {
        return null;
    }

    @Override
    @Transactional(readOnly = false)
    public String insertPrice(String drugID, float amount, int pricelistID,float buying){
        return debtorRowDAO.insertPrice(drugID,amount,pricelistID,buying);
    }


    @Override
    @Transactional(readOnly = false)
    public String createItem(String name, String category, String strength, String dosageForm ){
       return debtorRowDAO.createItem(name,category,strength,dosageForm);
    }

    @Override
    @Transactional(readOnly = false)
    public  String cancelOrder(String orderID){
        return debtorRowDAO.cancelOrder(orderID);
    }

    @Override
    @Transactional(readOnly = false)
    public String update_drug_orderlinie(String order_id,int item_id,  int qty,  int patientId ){
        return  debtorRowDAO.update_drug_orderlinie(order_id, item_id,   qty, patientId);

    }

    @Override
    @Transactional(readOnly = false)
    public  String cancelOrderLine(String orderlineID){
        return debtorRowDAO.cancelOrderLine(orderlineID);

    }

    @Override
    @Transactional(readOnly = false)
    public String reduct_drugQuantity(int qty, int item_id,int pricelistId){
        return debtorRowDAO.reduct_drugQuantity(qty,item_id, pricelistId);

    }

    @Override
    @Transactional(readOnly = false)
    public  String saveDiscount(float dicountAmount,int item,int paid, String orderID){
        return debtorRowDAO.saveDiscount(dicountAmount,item,paid,orderID);
    }

    @Override
    @Transactional(readOnly = false)
    public String paymentConfirmed(String orderID){

        return debtorRowDAO.paymentConfirmed(orderID);
    }

    @Override
    @Transactional(readOnly = false)
    public void clearAllResults() {
        debtorRowDAO.clearAllResults();
    }

    /**
     -----------DEC 2018-------
     */

    @Override
    public String editItem(int concept_name_id,int item_drug_id,String itemId, String name, String strength, String dosageForm,String dateCreated) {
        return debtorRowDAO.editItem(concept_name_id,item_drug_id,itemId,name,strength,dosageForm,dateCreated);
    }

    @Override
    public String editPrice(int item_price_id,String drugID, float amount, int pricelistID,String dateRecorded,float buying_price) {
        return debtorRowDAO.editPrice(item_price_id,drugID,amount,pricelistID,dateRecorded,buying_price);
    }


    @Override
    public String editPhysical(int physical_inventory_id,String drug_id, double qty, int priceList, String receivedDate, String recorededDate, String batchNo) {
        return debtorRowDAO.editPhysical(physical_inventory_id,drug_id,qty,priceList,receivedDate,recorededDate,batchNo);
    }

    @Override
    public  String updateLedger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,String InvoiceNo, String ExpiryDate, String receiveDate, String price_list_id, float amount, String mathz,int ledger_entry_id) {
        return debtorRowDAO.updateLedger(item_name,LedgerEntryType,quantity,BatchNo,InvoiceNo,ExpiryDate,receiveDate,price_list_id,amount,mathz,ledger_entry_id);
    }

    @Override
    public List get_physical_value() {
        return debtorRowDAO.get_physical_value();
    }

    @Override
    public List get_total_physical(){
        return debtorRowDAO.get_total_physical();
    }

    @Override
    public List getLedger_entry(){
        return debtorRowDAO.getLedger_entry();
    }

    @Override
    public List selectAllPhysical() {
        return debtorRowDAO.selectAllPhysical();
    }

    @Override
    public List getPriceList(){
        return debtorRowDAO.getPriceList();
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
    public List get_expiry_stock() {
        return debtorRowDAO.get_expiry_stock();
    }

    @Override
    public List get_expire_value() {
        return debtorRowDAO.get_expire_value();
    }

    @Override
    public List getItems() {
        return debtorRowDAO.getItems();
    }

    @Override
    public List get_all_ledger() {
        return debtorRowDAO.get_all_ledger();
    }

}
