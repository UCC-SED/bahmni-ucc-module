package org.bahmni.module.bahmniucc.db.impl;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.db.DebtorRowDAO;
import org.bahmni.module.bahmniucc.model.*;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;

import java.math.BigInteger;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public class DebtorRowDAOImpl implements DebtorRowDAO {

    private Logger logger = Logger.getLogger(getClass());

    private DbSessionFactory sessionFactory;


    @Override
    public int readBillingCategoryIdByName(String categoryName)
    {

        String sql = "select id, category_name,erp_pricelist_id, record_date from openerp_pricelist_mapping where category_name ='" + categoryName + "'";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(BillingCategory.class));
        List results = query.list();

        if (results.size() > 0) {
            BillingCategory billingCategory = (BillingCategory) results.get(0);
            return billingCategory.getErp_pricelist_id();

        } else {
            return 1;
        }

    }
    @Override
    public String getSaleOrderId(String patientIdentifier, String type)
    { String sql = null;
        if (type.equalsIgnoreCase("uuid")){
            sql = "select wh_order_line.order_id as order_id from wh_order_line,wh_order_sumUp where wh_order_sumUp.order_id = wh_order_line.order_id and discount_id=0 and  wh_order_sumUp.paid_status=0 and wh_order_line.cancelled_status=0 and wh_order_line.patient_id in (select person_id as patient_id from person where uuid = '" + patientIdentifier + "') LIMIT 1";

        }else {
             sql = "select wh_order_line.order_id as order_id from wh_order_line,wh_order_sumUp where wh_order_sumUp.order_id = wh_order_line.order_id and discount_id=0 and wh_order_sumUp.paid_status=0 and wh_order_line.cancelled_status=0 and wh_order_line.patient_id in (select patient_id from patient_identifier where identifier ='" + patientIdentifier + "') LIMIT 1";
        }
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(SaleOrderId.class));
        List results = query.list();

        if (results.size() > 0) {
            SaleOrderId salesOrders_id = (SaleOrderId) results.get(0);
            logger.info("salesOrders_line.getorder_id"+ salesOrders_id.getorder_id());
            return salesOrders_id.getorder_id();

        } else {
            return null;
        }

    }

    @Override
    public boolean getDrugBalance(String drugName){
        String sql = "select qty from wh_qty_onhand " +
                "inner join drug on concept_id = item_id " +
                "and drug.name like '%"+drugName+"%' and qty > 0";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
        .setResultTransformer(Transformers.aliasToBean(outOfStock.class));
        List results = query.list();

        if (results.size() > 0) {
            return true;
        } else {
            return false;
        }


    }
    @Override
    public List get_dbData(String qry){

        String db = "mart";

        if(db.equalsIgnoreCase("mart")){
            List resultsdata = new ArrayList();

            Connection connection = martConnection();
            if(connection != null){
                try {
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(qry);
                    int columnCount = rs.getMetaData().getColumnCount();
                    logger.info("get_dbData: Results next: "+columnCount);
                    if(rs.next()) {
                        //logger.info("get_dbData: Results: "+rs.getString(2));
                        while (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 0; i < columnCount; i++) {
                                row[i] = rs.getObject(i + 1);
                                logger.info("get_dbData: Results rows: "+row[i]);
                            }
                            resultsdata.add(row);

                        }
                        return resultsdata;
                    }else {
                        return null;
                    }

                }catch (SQLException e){
                    logger.info("get_dbData: error"+e.getMessage());
                    logger.info("get_dbData: error"+e.getMessage());
                    System.out.println("Connection failure.");
                    e.printStackTrace();
                }finally {
                    try {
                        connection.close();
                    }catch (SQLException e){
                        logger.info("get_dbData: error"+e.getMessage());
                        logger.info("get_dbData: error"+e.getMessage());
                    }
                }
            }



        }else {


            String sql = qry;
            List query = this.getSession().createSQLQuery(sql).list();

            List results = query;

            if (results.size() > 0) {
                logger.info("get_dbData:" + results.get(0));
                return results;

            } else {

                logger.info("no data get_dbData");
                return null;
            }

        }
        return null;
    }

    @Override
    public List get_thresholdDrugs(){
        String sql = "select drug.name as name,qty,wh_price_list.name as price_listName " +
                "from wh_qty_onhand,drug,wh_price_list where wh_qty_onhand.priceList_id=wh_price_list.price_list_id and " +
                "drug.concept_id = wh_qty_onhand.item_id " +
                "and 100 >= qty " +
                "order by qty desc";


        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
            .setResultTransformer(Transformers.aliasToBean(outOfStock.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no data");
            return null;
        }


    }


    @Override
    public boolean checkIfCustomerConsultationExemption(String patientIdentifier, int dated){
        dated = dated * -1;
        logger.info("order line insert"+ dated);
        String sql = "select order_id,item_type,item_id,order_line_id,concept_name.name as item_name,amount,qty,date_ordered from wh_order_line,\n" +
                "concept_name where concept_name.concept_id = wh_order_line.item_id and  paid_status=0 and cancelled_status=0 and patient_id in " +
                "(select patient_id from patient_identifier where identifier ='"+patientIdentifier+"') and" +
                " date_ordered between adddate(now(),"+dated+") and now()";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getSalesOrders_line.class));
        List results = query.list();

        if (results.size() > 0) {

            return true;

        } else {
            return false;
        }

    }

    @Override
    public String getDrug_request(String drug_name)
    {
        String bysearch= null;
        if(drug_name !="") {
            bysearch = "where drugs.name like  '%"+drug_name+"%' ";
        }
        else
            bysearch="";

        String sql = "select person_id_sub_store,item_id,date_qty_requested,quantity_requested," +
                "sub_store_id,product_mvnt_status,price_list_id from wh_product_movement,person_name,drugs  "+bysearch+" order by date_qty_requested desc limit 50";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getPaid_canceledOrders.class));

        List results = query.list();

        if (results.size() > 0) {

            return null;

        } else {

            logger.info("no data");
            return null;
        }

    }

    @Override
    public String createdrug_requestOrder(int item_id,int qty_req,int price_listId,int userId, int sub_store_id){
        DbSession session = getSession();

        String sql = "INSERT INTO wh_product_movement (person_id_sub_store,item_id,date_qty_requested,quantity_requested,sub_store_id,product_mvnt_status,price_list_id)" +
                " VALUES('" + userId + "','" + item_id + "',now(),'" + qty_req + "','" + sub_store_id + "','PENDING', '" + price_listId + "')";
        logger.info("Insert wh_order_line " + sql);
        int res = getSession().createSQLQuery(sql).executeUpdate();
        logger.info("insert into wh_order_line: " + res);
        session.beginTransaction().commit();
        return "updated";

    }

    @Override
    public int get_sub_store_id(int userId){
        String sql = "select location_id as sub_store_id from wh_person_location_map  where  person_id = '"+ userId +"')";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(Get_sub_store_id.class));

        List results = query.list();

        if (results.size() > 0) {
            Get_sub_store_id data_store = (Get_sub_store_id)results.get(0);
            return data_store.getsub_store_id();

        } else {

            return 0;
        }

    }

    @Override
    public String createorder_line(String order_id,int item_id, String item_type, int qty, float amount, int patient_id, int location_id, int payment_category ){
        DbSession session = getSession();
        logger.info("order line insert"+ order_id);
            String sql = "INSERT INTO wh_order_line (order_id,item_id,item_type,qty,amount,patient_id,date_ordered,location_id,payment_category)" +
                    " VALUES('" + order_id + "','" + item_id + "','" + item_type + "','" + qty + "','" + amount + "','" + patient_id + "', now(),'" + location_id + "','"+payment_category+"')";
            logger.info("Insert wh_order_line " + sql);
            int res = getSession().createSQLQuery(sql).executeUpdate();
            logger.info("insert into wh_order_line: " + res);
            session.beginTransaction().commit();
            return "updated";
     }
    @Override
    public String reduct_drugQuantity(int qty, int item_id, int pricelistId){

        DbSession session = getSession();
        String sql_price = "UPDATE  wh_qty_onhand SET qty = qty - "+qty+" where item_id = '"+item_id+"' and priceList_id = "+pricelistId+"";
        logger.info("upadate wh_qty_onhand " + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        session.beginTransaction().commit();
        return "updated";
    }

    @Override
    public String update_drug_orderlinie(String order_id,int item_id,  int qty,  int patientId ){
        String sql = "select order_id,item_type,item_id,order_line_id,concept_name.name as item_name,amount,qty,date_ordered from wh_order_line,\n" +
                "concept_name where concept_name.concept_id = wh_order_line.item_id and concept_name_type = 'FULLY_SPECIFIED' and  paid_status=0 and cancelled_status=0 and " +
                " patient_id = '"+patientId+"' and wh_order_line.item_id='"+item_id+"'";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getSalesOrders_line.class));
        List results = query.list();


        if (results.size() > 0) {
            getSalesOrders_line salesOrders_line = (getSalesOrders_line) results.get(0);

            float qty_db = salesOrders_line.getqty();
            float amount = salesOrders_line.getamount();
            float qty_after = qty - qty_db;

            String amountToUpdate = null;
            if(qty_after == 0){
                qty_after = qty;
                amountToUpdate = "sameAMount";
            }

            if(qty_after !=0) {

                float totalAmount = qty_after * amount;
                if(amountToUpdate == null){
                    amountToUpdate = "total_amount + "+totalAmount;
                }else{
                    amountToUpdate=""+totalAmount;
                }
                DbSession session = getSession();
                String order_qry = "UPDATE wh_order_sumUp SET total_amount= "+amountToUpdate+" WHERE order_id='" + order_id + "'";
                int res = getSession().createSQLQuery(order_qry).executeUpdate();
                logger.info("update new druglined" +order_qry);
                session.beginTransaction().commit();
                String order_line = "UPDATE wh_order_line SET qty=" + qty + " WHERE order_id='" + order_id + "' and paid_status=0 and cancelled_status=0 " +
                        "and patient_id = '" + patientId + "' and wh_order_line.item_id='" + item_id + "'";
                int resLine = getSession().createSQLQuery(order_line).executeUpdate();
                session.beginTransaction().commit();


                return "updated";
            }

        }else {

            return "updated";
        }
        return "failed";
    }

   public int get_qty_conceptId(String order_uuid, String type){
       String sql = null;
        if(type == "concept") {
             sql = "select concept_id as item from orders where uuid='" + order_uuid + "'";
        }else{
             sql = "select quantity as item from drug_order do\n" +
                    "inner join orders or on or.order_id = do.order_id\n" +
                    "where or.uuid='" + order_uuid + "'";
        }

       org.hibernate.Query query = this.getSession().createSQLQuery(sql)
               .setResultTransformer(Transformers.aliasToBean(qtyStockReduction.class));

       List results = query.list();

       if (results.size() > 0) {
           qtyStockReduction qtyStock= (qtyStockReduction) results.get(0);

           int itemData = qtyStock.getitem();

           return itemData;

       } else {


           return 0;
       }

    }

    @Override
    public int get_location_id(String locationUuid){
        String sql = "select location_id,parent_location from location where uuid = '" + locationUuid + "'";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(location.class));

        List results = query.list();

        if (results.size() > 0) {
            location location = (location) results.get(0);

            int location_id = location.getlocation_id();
            Integer parent_location = location.getparent_location();
            if(parent_location != null)
                return parent_location;
            else
            return location_id;

        } else {

            logger.info("no visit_type_id");
            return 0;
        }

    }

    @Override
    public int getPatientID(String patientIdentifier,String type) {
        String sql = null;
        if(type.equalsIgnoreCase("uuid")){
             sql = "select person_id as patient_id from person where uuid = '" + patientIdentifier + "'";

        }else {
             sql = "select patient_id from patient_identifier where identifier = '" + patientIdentifier + "'";
        }
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PatientID.class));

        List results = query.list();

        if (results.size() > 0) {
            PatientID patientData = (PatientID) results.get(0);

            int patientID = patientData.getPatient_id();
            return patientID;

        } else {

            logger.info("no patient");
            return 0;
        }

    }
    @Override
    public int updateStockQty(int conceptId, int qty, int priceList_id){
        DbSession session = getSession();
        String sql_price = "update wh_qty_onhand set qty=qty - "+qty+" where item_id = "+conceptId+" and priceList_id = '"+priceList_id+"'";
        logger.info("Insert into " + sql_price);

        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("insert into after: " + res);
        session.beginTransaction().commit();

        return 1;

    }





    @Override
    public void saveDebtorRowNew(){
        //deleteDebtor();
        DbSession session = getSession();
        String sql_price = "INSERT INTO openerp_debtor_list (invoice_id,patient_id,patient_name,chargeable_amount,default_quantity,date_created) " +
                "select distinct( wh_order_sumUp.order_id),identifier,concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) as full_name,total_amount,'1',wh_order_sumUp.date_ordered" +
                "FROM wh_order_sumUp,person_name pn,patient_identifier pi,wh_order_line where pi.patient_id=wh_order_sumUp.patient_id and wh_order_sumUp.paid_status=0" +
                " and wh_order_sumUp.cancelled_statues=0 and pn.person_id=wh_order_sumUp.patient_id AND wh_order_line.order_id=wh_order_sumUp.order_id and item_type='LAB_RAD_PROCED'";
        logger.info("Insert into " + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("insert into after: " + res);
        session.beginTransaction().commit();
    }

    public void deleteDebtor(){
        DbSession session = getSession();
        String sql_price = "delete from openerp_debtor_list where invoice_id IN (select order_id from  wh_order_sumUp where paid_status=1 OR cancelled_statues=1)";
        logger.info("Insert into " + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("insert into after: " + res);
        session.beginTransaction().commit();

    }

    @Override
    public void saveDebtorRow(List<DebtorRow> results) {
        DbSession session = getSession();
        for (DebtorRow result : results) {
            if (checkIFPatientIsActiveAndInCashPaymentMode(result.getPatient_id())) {
                String save_sql = "INSERT INTO openerp_debtor_list\n" +
                        "(invoice_id,\n" +
                        "patient_id,\n" +
                        "patient_name,\n" +
                        "chargeable_amount,\n" +
                        "default_quantity,\n" +
                        "date_created)\n" +
                        "VALUES\n" +
                        "( '" + result.getInvoice_id() + "'," +
                        "'" + result.getPatient_id() + "'," +
                        "'" + result.getPatient_name() + "'," +
                        "'" + result.getChargeable_amount() + "'," +
                        "'" + result.getDefault_quantity() + "'," +
                        "'" + result.getDate_created() + "')";

                logger.info("saveDebtorRow " + save_sql);
                getSession().createSQLQuery(save_sql).executeUpdate();
            }
        }
    }


    public boolean checkIFPatientIsActiveAndInCashPaymentMode(String patientID) {
        String sql = "SELECT DISTINCT\n" +
                "    pi.identifier AS patientID\n" +
                "FROM\n" +
                "    visit v\n" +
                "        JOIN\n" +
                "    person_name pn ON v.patient_id = pn.person_id\n" +
                "        AND pn.voided = 0\n" +
                "        JOIN\n" +
                "    patient_identifier pi ON v.patient_id = pi.patient_id\n" +
                "        JOIN\n" +
                "    (SELECT distinct test_obs.person_id,\n" +
                "        test_obs.obs_group_id,\n" +
                "            test_obs.concept_id,\n" +
                "            test_obs.obs_datetime,\n" +
                "            test_obs.value_coded AS paymentCategory\n" +
                "    FROM\n" +
                "        obs test_obs\n" +
                "    INNER JOIN concept c ON c.concept_id = test_obs.concept_id\n" +
                "        AND test_obs.voided = 0\n" +
                "    INNER JOIN concept_name cn ON c.concept_id = cn.concept_id\n" +
                "        AND cn.concept_name_type = 'FULLY_SPECIFIED'\n" +
                "        AND cn.name IN ('Payment Category')\n" +
                "    WHERE\n" +
                "        test_obs.value_coded = 4030  AND\n" +
                "        test_obs.obs_datetime= (select MAX(obs_datetime) from obs obs2 where obs2.obs_id=test_obs.obs_id)) AS cp ON cp.person_id = pn.person_id\n" +
                "WHERE\n" +
                "    v.date_stopped IS NULL AND v.voided = 0";

        logger.info("sql " + sql);

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PatientIdentifiers.class));

        List results = query.list();
        logger.info("Sizeee " + results.size());
        for (int x = 0; x < results.size(); x++) {
            if (((PatientIdentifiers) results.get(x)).getPatientID().equalsIgnoreCase(patientID)) {
                return true;
            }
        }

        return false;

    }

    @Override
    public void clearAllResults() {

        getSession().createSQLQuery("DELETE from openerp_debtor_list").executeUpdate();
    }


    @Override
    public void storeAuthenticationHeader(String header, String issue_date, String expire_date) {


        SimpleDateFormat formatterOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // your template here


        SimpleDateFormat formatterIn = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss 'GMT'");

        String sql = null;
        try {
            sql = "INSERT INTO `openmrs`.`nhif_authentication_header`\n" +
                    "(header, issue_date, expire_date)\n" +
                    "VALUES\n" +
                    "('" + header + "', '" + formatterOut.format(formatterIn.parse(issue_date)) + "','" + formatterOut.format(formatterIn.parse(expire_date)) + "' )";

            logger.info(sql);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        getSession().createSQLQuery(sql).executeUpdate();


    }

    @Override
    public String checkDuplicateStatus(String name, String gender, String birthdate, String street, String council, String district, String region) {


        String sql = "select distinct\n" +
                "          concat(pn.given_name,' ', pn.family_name) as name,\n" +
                "          pi.identifier as identifier,\n" +
                "          pe.gender,\n" +
                "          pe.birthdate,\n" +
                "          pea.address2 as street,\n" +
                "          pea.county_district as district ,\n" +
                "          pea.address3 as council ,\n" +
                "          pea.address4 as region,\n" +
                "          concat(\"\",pe.uuid) as uuid\n" +
                "        from patient p\n" +
                "        join person_name pn on p.patient_id = pn.person_id and pn.voided = 0\n" +
                "        join patient_identifier pi on p.patient_id = pi.patient_id \n" +
                "        join patient_identifier_type pit on pi.identifier_type = pit.patient_identifier_type_id\n" +
                "        join global_property gp on gp.property=\"bahmni.primaryIdentifierType\" and gp.property_value=pit.uuid\n" +
                "        join person pe on pe.person_id = p.patient_id\n" +
                "\t\tjoin person_address pea on pe.person_id = pea.person_id\n" +
                "        \n" +
                "        where \n" +
                "        pe.gender='" + gender + "'and " +
                " concat(pn.given_name,' ', pn.family_name)='" + name + "' and " +
                "pe.birthdate='" + birthdate + "' and " +
                "pea.address2='" + street + "' and " +
                "pea.county_district='" + district + "'and " +
                " pea.address3='" + council + "' and " +
                "  pea.address4='" + region + "'";
        logger.info("checkDuplicateStatus sql " + sql);

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PatientMatching.class));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject obj = new JSONObject();
        List results = query.list();

        logger.info("checkDuplicateStatus results.size() " + results.size());
        if (results.size() > 0) {
            PatientMatching patientMatching = (PatientMatching) results.get(0);
            obj.put("status", true);
            obj.put("birthDate", dateFormat.format(patientMatching.getBirthdate()));
            obj.put("council", patientMatching.getCouncil());
            obj.put("district", patientMatching.getDistrict());
            obj.put("gender", patientMatching.getGender());
            obj.put("identifier", patientMatching.getIdentifier());
            obj.put("name", patientMatching.getName());
            obj.put("uuid", patientMatching.getUuid());
            obj.put("street", patientMatching.getStreet());


        } else {
            logger.info("No Duplicate");
            return null;
        }

        JSONArray patientArray = new JSONArray();
        patientArray.add(obj);

        JSONObject mainObj = new JSONObject();
        mainObj.put("patients", patientArray);

        return mainObj.toJSONString();
    }

    @Override
    public List searchTribes(String searchNames) {

        String sql = "select id,tribe_name from tribes where tribe_name LIKE '%" + searchNames + "%'";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(Tribes.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no tribe");
            return null;
        }


    }

    @Override
    public List getPatientInDept() {

        String sql = "select distinct( identifier) as patient_id from wh_order_sumUp,wh_order_line,patient_identifier where\n" +
                "patient_identifier.patient_id=wh_order_sumUp.patient_id and wh_order_line.order_id=wh_order_sumUp.order_id and\n" +
                "wh_order_sumUp.paid_status = 0 and wh_order_sumUp.cancelled_statues = 0 and wh_order_line.payment_category=1 and item_type IN ('LAB_RAD_PROCED')";


        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PatientInDepts.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no patient in dept");
            return null;
        }


    }


    @Override
    public String readAuthenticationHeader() {

        String sql = "select header from nhif_authentication_header where expire_date > NOW() order by issue_date desc limit 1";


        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .addScalar("header", StandardBasicTypes.TEXT)
                .setResultTransformer(Transformers.aliasToBean(AuthenticationHeader.class));

        List results = query.list();

        if (results.size() > 0) {

            AuthenticationHeader authenticationHeader = (AuthenticationHeader) results.get(0);
            return authenticationHeader.getHeader();

        } else {

            logger.info("No Token");
            return "No Token";
        }


    }

    @Override
    public String readQuery() {

        String stringQuery = "select count(*) as  value,  et.name as indicator from encounter e\n" +
                "join encounter_type et on et.encounter_type_id=e.encounter_type \n" +
                "where DATE(e.encounter_datetime)=CURDATE() \n" +
                "group by  e.encounter_type";

        Query query = this.getSession().createSQLQuery(stringQuery)
                .addScalar("value", StandardBasicTypes.INTEGER)
                .addScalar("indicator", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(MonitorItem.class));


        JSONObject obj = new JSONObject();
        List results = query.list();

        if (results.size() > 0) {
            for (int x = 0; x < results.size(); x++) {
                MonitorItem monitorItem = (MonitorItem) results.get(x);
                obj.put(monitorItem.getIndicator(), monitorItem.getValue());
            }
        } else {

            logger.info("No Data");
            return null;
        }

        logger.info(obj.toJSONString());

        return obj.toJSONString();

    }


    @Override
    public String readHack() {


        String stringQuery = "select visit_type from hack_obs";

        org.hibernate.Query query = this.getSession().createSQLQuery(stringQuery)
                .addScalar("visit_type", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(HackItem.class));


        List results = query.list();

        HackItem hackItem = (HackItem) results.get(0);
        return hackItem.getVisit_type();

    }


    @Override
    public ArrayList<Notification> getNotifications() {

        ArrayList<Notification> notificationsList = new ArrayList<>();
        String stringQuery = "select name, notification_sql, notification_reaction from notifications_rules where status='ACTIVE'";

        org.hibernate.Query query = this.getSession().createSQLQuery(stringQuery)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("notification_sql", StandardBasicTypes.STRING)
                .addScalar("notification_reaction", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(Notification.class));

        List results = query.list();

        for (int x = 0; x < results.size(); x++) {
            Notification notification = (Notification) results.get(x);
            notificationsList.add(notification);
        }
        return notificationsList;
    }


    @Override
    public NotificationResult getNotificationResults(Notification notification) {

        org.hibernate.Query query = this.getSession().createSQLQuery(notification.getNotification_sql())
                .addScalar("status", StandardBasicTypes.BOOLEAN)
                .addScalar("value", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(NotificationResult.class));

        List results = query.list();
        NotificationResult notificationResult = (NotificationResult) results.get(0);

        return notificationResult;
    }


    @Override
    public void insertHack(String visitType) {
        String sql = "INSERT INTO `openmrs`.`hack_obs`\n" +
                "(visit_type)\n" +
                "VALUES\n" +
                "('" + visitType + "')";

        getSession().createSQLQuery(sql).executeUpdate();
    }



    @Override
    public  String updateRoom(String room, String uuid) {
        DbSession session = getSession();
        String sqlz = "update person_attribute set value = '" + room + "' " +
                "where person_id= (select person_id from person where uuid='" + uuid + "') " +
                "and person_attribute_type_id='52'";
        logger.info("Doctors Room " + sqlz);
        int res = getSession().createSQLQuery(sqlz).executeUpdate();
        logger.info("Update result " + res);
        session.beginTransaction().commit();
        return "updated";
    }

    /**
     * @param drugID
     * @param amount      ============================ HERE IS WHERE WE INSERT PRICE ===============================
     * @param pricelistID
     * @return
     */
    @Override
    public String insertPrice(String drugID, float amount, int pricelistID,float buying) {
        DbSession session = getSession();
        String sql_price = "INSERT INTO wh_item_price (item_id,amount,price_list_id,date_recorded,buying_price) VALUES('" + drugID + "','" + amount + "','" + pricelistID + "', now(),"+buying+")";
        logger.info("Insert wh_physical_inventory " + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("insert into wh_physical_inventory: " + res);
        session.beginTransaction().commit();
        return "updated";

    }

    @Override
    public String insertPriceList(String name, String defaultPrice) {
        DbSession session = getSession();
        String sql_price = "INSERT INTO wh_price_list (name,default_price,date_recorded) VALUES('" + name + "', '" + defaultPrice + "', now())";
        logger.info("Insert into " + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("insert into after: " + res);
        session.beginTransaction().commit();
        return "updated";
    }

    @Override
   public String createItem(String name, String category, String strength, String dosageForm ){
        DbSession session = getSession();
       //insert into concept table
        String sql_price = "INSERT INTO concept (retired,datatype_id,class_id,is_set,creator,date_created,uuid) VALUES(0,4,3,0,4, now(),uuid())";
        logger.info("Insert into " + sql_price);
        logger.info("Insert into " + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("insert into after: " + res);
        session.beginTransaction().commit();

        //get concept id
        BigInteger conceptID = (BigInteger)  session.createSQLQuery("SELECT LAST_INSERT_ID()") .uniqueResult();
        logger.info("conceptID get it: " + conceptID);
        //get dosage form id
        String sql = "SELECT concept_id FROM openmrs.concept where uuid='"+dosageForm+"'";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(dose.class));

        List results = query.list();
        int doseID =1;
        Iterator it = results.iterator();
        while(it.hasNext()) {
            dose sd = (dose) it.next();
            doseID = sd.getconcept_id();
        }

        //insert concept name
        String sql_name = "INSERT INTO concept_name (concept_id,name,locale,locale_preferred,creator,date_created,concept_name_type,voided,uuid) VALUES("+conceptID+",'"+name+"','en',1,4, now(),'FULLY_SPECIFIED',0,uuid())";
        logger.info("Insert into " + sql_name);
        logger.info("Insert into " + sql_name);
        int res2 = getSession().createSQLQuery(sql_name).executeUpdate();
        logger.info("insert into after: " + res);
        session.beginTransaction().commit();

        //insert into drug

        String sql_drugname = "INSERT INTO drug (concept_id,name,combination,dosage_form,creator,date_created,retired,uuid,strength) VALUES("+conceptID+",'"+name+"',0,"+doseID+",4, now(),0,uuid(),'"+strength+"')";
        logger.info("Insert into " + sql_drugname);
        logger.info("Insert into " + sql_drugname);
        int res3 = getSession().createSQLQuery(sql_drugname).executeUpdate();
        logger.info("insert into after: " + res3);
        session.beginTransaction().commit();


        return "updated";
    }

    /**
     * @param name -----------------------GET SEARCH PRODUCT ------------------------------------------------
     * @return
     */
    @Override
    public List getAddItems(String name) {
        String sql = "SELECT concept.concept_id as drug_id,drug.name as name,drug.drug_id as item_drug_id,strength,drug.dosage_form as dosage_form,concept_name.name as dose_name,concept_name.concept_name_id as concept_name_id,drug.date_created as date_created from drug,concept,concept_name   " +
                "WHERE dosage_form=concept.concept_id and concept.concept_id = concept_name.concept_id and drug.name like '%" + name + "%' LIMIT 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getAddDrugs.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no drug");
            return null;
        }
    }

    /**
     * @return ===============GETTING ALL THE DRUGS==========================================
     */
    @Override
    public List getItems() {
        String sql = "SELECT concept.concept_id as drug_id,drug.name as name,drug.drug_id as item_drug_id,strength,drug.dosage_form as dosage_form,concept_name.name as dose_name,concept_name.concept_name_id as concept_name_id,drug.date_created as date_created from drug,concept,concept_name " +
                "WHERE dosage_form=concept.concept_id and concept.concept_id = concept_name.concept_id";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getAddDrugs.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no drug");
            return null;
        }
    }

    @Override
    public List getSalesOrders(String search){
        String bysearch= null;
        if(search !="") {
            bysearch = "and ( wh_order_sumUp.patient_id IN (SELECT patient_identifier.patient_id FROM patient_identifier WHERE identifier LIKE '%"+search+"%') or concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) like '%"+search+"%')";
        }
        else
            bysearch="";

        String sql = "select order_id,total_amount,request_status,Ctrl_number,discount,identifier,discount_id,date_ordered,concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) as full_name from wh_order_sumUp,\n" +
                "person_name pn,patient_identifier pi where pi.patient_id=wh_order_sumUp.patient_id and paid_status=0 and cancelled_statues=0 and pn.person_id=wh_order_sumUp.patient_id  "+bysearch+" order by date_ordered desc limit 30";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getSalesOrders.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no data");
            return null;
        }

    }
    @Override
    public String cancelOrder(String orderID){
        DbSession session = getSession();
        String sql_price = "UPDATE wh_order_sumUp SET cancelled_statues=1, cancelled_date = now() WHERE order_id='"+orderID+"'";
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        session.beginTransaction().commit();

        String sql_price2 = "UPDATE wh_order_line SET cancelled_status=1, cancelled_date = now() WHERE order_id='"+orderID+"'";
        int res1 = getSession().createSQLQuery(sql_price2).executeUpdate();
        session.beginTransaction().commit();
        return "updated";

    }
    @Override
    public String cancelOrderLine(String orderlineID){
        DbSession session = getSession();
        String sql_price = "UPDATE wh_order_line SET cancelled_status=1, cancelled_date = now() WHERE order_line_id='"+orderlineID+"'";
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        session.beginTransaction().commit();

        String sql_amount = "SELECT amount,qty from wh_order_line WHERE order_line_id='"+orderlineID+"'";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql_amount)
                .setResultTransformer(Transformers.aliasToBean(Amount_qty.class));

        List results = query.list();
        logger.info("Insert ledger " + sql_amount);
        if (results.size() > 0) {
            Amount_qty amount_qty = (Amount_qty)results.get(0);
            float amount = amount_qty.getamount() * amount_qty.getqty();
            String sql = "UPDATE wh_order_sumUp SET total_amount=total_amount -"+amount+" WHERE order_id IN (select order_id from wh_order_line where order_line_id='"+orderlineID+"')";
            int wr = getSession().createSQLQuery(sql).executeUpdate();
            session.beginTransaction().commit();

        }
        return "updated";

    }
    @Override
    public String saveDiscount(float dicountAmount,int item,int paid, String orderID){
       String date_payed = " ";
       if(paid == 1)
        date_payed = ", date_payed=now()";
        DbSession session = getSession();
        String sql_price = "UPDATE wh_order_sumUp SET discount="+ dicountAmount +",total_amount = total_amount - "+ dicountAmount +",discount_id= "+item+",paid_status="+ paid +" "+date_payed+" WHERE order_id='"+orderID+"'";
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        session.beginTransaction().commit();

        return "updated";
    }

    @Override
    public  boolean check_if_orderlineExist(String patientIdentifier, String item_uuid,String type){
        String sql = "";
        if(type.equalsIgnoreCase("notDrug")){
            sql = "select order_line_id" +
                    " from wh_order_sumUp, wh_order_line,\n" +
                    "concept,person where  wh_order_sumUp.order_id = wh_order_line.order_id and concept.concept_id = wh_order_line.item_id and  \n" +
                    "wh_order_line.patient_id =person.person_id and wh_order_line.paid_status=0  and wh_order_line.cancelled_status = 0 and wh_order_sumUp.discount=0 \n" +
                    "and person.uuid ='"+patientIdentifier+"' \n" +
                    "and ( concept.uuid = '"+item_uuid+"') LIMIT 1";

        }else {
            sql = "select order_line_id " +
                    "from wh_order_sumUp, wh_order_line,person,\n" +
                    "drug where drug.concept_id = wh_order_line.item_id and wh_order_sumUp.order_id = wh_order_line.order_id and " +
                    "person.person_id =wh_order_line.patient_id and wh_order_line.paid_status=0  and wh_order_line.cancelled_status = 0 and wh_order_sumUp.discount=0 " +
                    "and person.uuid ='"+patientIdentifier+"' and drug.uuid='" + item_uuid + "' LIMIT 1";
        }
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getSalesOrders_line.class));
        List results = query.list();
        logger.info("Insert ledger " + sql);
        if (results.size() > 0) {

            return true;


        } else {
            return false;
        }

    }
    @Override
    public String paymentConfirmed(String orderID){
        DbSession session = getSession();
        String order_qry = "UPDATE wh_order_sumUp SET paid_status=1, date_payed = now() WHERE order_id='"+orderID+"'";
        int res = getSession().createSQLQuery(order_qry).executeUpdate();
        session.beginTransaction().commit();

        //update orderlines which has not cancelled

        String orderline_qry = "UPDATE wh_order_line SET paid_status=1, date_payed = now() WHERE order_id='"+orderID+"' and cancelled_status=0";
        int res2 = getSession().createSQLQuery(orderline_qry).executeUpdate();
        session.beginTransaction().commit();

        return "updated";
    }
    @Override
    public List getDiscountOrders(String search){
        String bysearch= null;
        if(search !="") {
            bysearch = "and ( wh_order_sumUp.patient_id IN (SELECT patient_identifier.patient_id FROM patient_identifier WHERE identifier LIKE '%"+search+"%') or concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) like '%"+search+"%')";
        }
        else
            bysearch="";

        String sql = "select order_id,paid_status,total_amount,date_payed,identifier,cancelled_date,Ctrl_number,discount,discount_id,date_ordered,concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) as full_name from wh_order_sumUp,\n" +
                "person_name pn,patient_identifier pi where pi.patient_id=wh_order_sumUp.patient_id and discount_id >0 and pn.person_id=wh_order_sumUp.patient_id  "+bysearch+" order by date_ordered desc limit 30";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getPaid_canceledOrders.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no data");
            return null;
        }



    }

    @Override
   public List getPaidorders(String search){
        String bysearch= null;
        if(search !="") {
            bysearch = "and ( wh_order_sumUp.patient_id IN (SELECT patient_identifier.patient_id FROM patient_identifier WHERE identifier LIKE '%"+search+"%') or concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) like '%"+search+"%')";
        }
        else
            bysearch="";

        String sql = "select order_id,paid_status,total_amount,date_payed,cancelled_date,Ctrl_number,discount,identifier,discount_id,date_ordered,concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) as full_name from wh_order_sumUp,\n" +
                "person_name pn,patient_identifier pi where pi.patient_id=wh_order_sumUp.patient_id and paid_status=1 and cancelled_statues=0 and pn.person_id=wh_order_sumUp.patient_id  "+bysearch+" order by date_ordered desc limit 30";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getPaid_canceledOrders.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no data");
            return null;
        }



    }
    @Override
    public List getCancelledorders(String search){
        String bysearch= null;
        if(search !="") {
            bysearch = "and ( wh_order_sumUp.patient_id IN (SELECT patient_identifier.patient_id FROM patient_identifier WHERE identifier LIKE '%"+search+"%') or concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) like '%"+search+"%')";
        }
        else
            bysearch="";

        String sql = "select order_id,paid_status,total_amount,date_payed,identifier,cancelled_date,Ctrl_number,discount,discount_id,date_ordered,concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) as full_name from wh_order_sumUp,\n" +
                "person_name pn,patient_identifier pi where pi.patient_id=wh_order_sumUp.patient_id and  cancelled_statues=1 and pn.person_id=wh_order_sumUp.patient_id  "+bysearch+" order by date_ordered desc limit 30";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getPaid_canceledOrders.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no data");
            return null;
        }

    }
    @Override
    public List getSalesOrders_line_other(String orderID){
        String orderIDby= null;
        if(orderID !="") {
            orderIDby = " and order_id='"+orderID+"'";
        }
        else
            orderIDby="";

        String sql = "select order_id,item_type,item_id,order_line_id,concept_name.name as item_name,amount,qty,date_ordered from wh_order_line,\n" +
                "concept_name where concept_name.concept_id = wh_order_line.item_id and wh_order_line.cancelled_status = 0 and  concept_name_type = 'FULLY_SPECIFIED'  "+orderIDby+" group by " +
                "order_line_id order by date_ordered";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getSalesOrders_line.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no data");
            return null;
        }


    }
    @Override
    public String billItems(String orderID) {
        String orderIDby= null;
        if(orderID !="") {
            orderIDby = " and order_id='"+orderID+"'";
        }
        else
            orderIDby="";

        String sql = "select order_id,item_type,item_id,order_line_id,concept_name.name as item_name,amount,qty,date_ordered from wh_order_line,\n" +
                "concept_name where concept_name.concept_id = wh_order_line.item_id and wh_order_line.paid_status = 0 and wh_order_line.cancelled_status = 0 and  concept_name_type = 'FULLY_SPECIFIED'  "+orderIDby+" group by " +
                "order_line_id order by date_ordered";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getSalesOrders_line.class));

        Iterator  results = query.list().iterator();
        String items = "";
        while (results.hasNext()) {
            getSalesOrders_line itemID = (getSalesOrders_line) results.next();
            String gsfcode = null;
            if(itemID.getitem_type().equalsIgnoreCase("DRUG"))
                gsfcode = "drug_gfscode";
            if(itemID.getitem_type().equalsIgnoreCase("LAB_RAD_PROCED"))
                gsfcode = "labRed_gfscode";
            if(itemID.getitem_type().equalsIgnoreCase("consultation"))
                gsfcode = "consultation_gfscode";

                 items +="<BillItem> " +
                         "<BillItemRef>"+itemID.getorder_line_id()+"</BillItemRef>" +
                         "<UseItemRefOnPay>N</UseItemRefOnPay>" +
                         "<BillItemAmt>"+ itemID.getamount()*itemID.getqty()  +"</BillItemAmt>" +
                         "<BillItemEqvAmt>"+ itemID.getamount()*itemID.getqty()  +"</BillItemEqvAmt>" +
                         "<BillItemMiscAmt>0</BillItemMiscAmt>" +
                         "<GfsCode>"+ gsfcode +"</GfsCode>" +
                         "</BillItem>";


        }
        return items;

    }

    @Override
    public List getSalesOrders_line(String orderID){
        String orderIDby= null;
        if(orderID !="") {
            orderIDby = " and order_id='"+orderID+"'";
        }
        else
            orderIDby="";

        String sql = "select order_id,item_type,item_id,order_line_id,concept_name.name as item_name,amount,qty,date_ordered from wh_order_line,\n" +
                "concept_name where concept_name.concept_id = wh_order_line.item_id and wh_order_line.paid_status = 0 and wh_order_line.cancelled_status = 0 and  concept_name_type = 'FULLY_SPECIFIED'  "+orderIDby+" group by " +
                "order_line_id order by date_ordered";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getSalesOrders_line.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no data");
            return null;
        }

    }
    @Override
   public String processCtrlNumber(String BillId, String PayCntrNum){
        DbSession session = getSession();
        String sql_price = "UPDATE wh_order_sumUp SET Ctrl_number = '"+PayCntrNum+"' WHERE order_id='"+BillId+"'";
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("insert into ledger: " + res);
        session.beginTransaction().commit();

        return "gepgBillSubResAck><TrxStsCode>7101</TrxStsCode> </gepgBillSubRespAck>";
    }

    @Override
    public String insertledger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,
                               String InvoiceNo, String ExpiryDate, String receiveDate,String price_list_id, float amount, String mathz ){
        DbSession session = getSession();

        String sql_price = "INSERT INTO wh_ledger_entry (item_id,ledger_type,quantity,batch_no," +
                "invoice_no,price_list_id,received_date,expiry_date,amount) VALUES('"+ item_name +"', '"+ LedgerEntryType +"'," +
                " '"+quantity+"','"+BatchNo+"', '"+InvoiceNo+"','"+price_list_id+"'," +
                " '"+receiveDate+"', '"+ExpiryDate+"','"+amount+"')";
        logger.info("Insert ledger " + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("insert into ledger: " + res);
        session.beginTransaction().commit();

        String sql = "SELECT qty from wh_qty_onhand   WHERE item_id = '" + item_name + "'";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(onHand.class));

        List results = query.list();

        if (results.size() > 0) {
            String sql_onhand = "UPDATE wh_qty_onhand set qty=qty + "+ quantity +" where item_id = '"+ item_name +"'";
            logger.info("Insert wh_qty_onhand " + sql_onhand);
            int sql_onh = getSession().createSQLQuery(sql_onhand).executeUpdate();
            logger.info("insert wh_qty_onhand: " + sql_onh);
            session.beginTransaction().commit();


        } else {
            String sql_onhand = "INSERT INTO wh_qty_onhand (item_id, qty, priceList_id) VALUES " +
                    "('"+item_name+"','"+quantity+"','"+price_list_id+"')";
            logger.info("Insert wh_qty_onhand " + sql_onhand);
            int sql_onh = getSession().createSQLQuery(sql_onhand).executeUpdate();
            logger.info("insert wh_qty_onhand: " + sql_onh);
            session.beginTransaction().commit();

        }

        return "updated";
    }

    //====================================INSERT PHYSICAL INVENTORY ==========================
    @Override
    public String insertPhysical(String drug_id, double qty, int priceList, String receivedDate, String recorededDate, String batchNo) {
        DbSession session = getSession();
        String sql_price = "INSERT INTO wh_physical_inventory (item_id,qnty,price_list_id,date_recorded,receivedDate,inventoryDate,batchNo) VALUES('" + drug_id + "','" + qty + "','" + priceList + "', now(),'" + receivedDate + "','" + recorededDate + "','" + batchNo + "')";
        logger.info("Insert wh_physical_inventory " + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("insert into wh_physical_inventory: " + res);
        session.beginTransaction().commit();
        return "updated";
    }

    //=========================GET PRICE LIST=========================

    @Override
    public List selectPrice(String name) {
        String byName = null;
        if (name != "") {
            byName = "and drug.name LIKE '%" + name + "%'";
        } else
            byName = "";

        String sql = "SELECT wh_item_price.date_recorded as date_recorded,wh_item_price.item_price_id as item_price_id,amount,wh_item_price.buying_price as buying_price,drug.name as name,wh_price_list.name as priceList_name from wh_item_price,drug,wh_price_list WHERE wh_item_price.price_list_id = wh_price_list.price_list_id and drug.concept_id=wh_item_price.item_id " + byName + "  ORDER BY date_recorded DESC";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PriceItem.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no data");
            return null;
        }

    }

    //============================================ GET PRICE LIST ====================================
    @Override
    public List getPriceList() {
        String sql = "SELECT wh_item_price.date_recorded as date_recorded,wh_item_price.item_price_id as item_price_id,amount,wh_item_price.buying_price as buying_price,drug.name as name,wh_price_list.name as priceList_name from wh_item_price,drug,wh_price_list WHERE wh_item_price.price_list_id = wh_price_list.price_list_id and drug.concept_id=wh_item_price.item_id ORDER BY date_recorded DESC LIMIT 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PriceItem.class));

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no data");
            return null;
        }

    }

    @Override
    public int gePriceListID(String paymentType){
        String sql = "SELECT price_list_id FROM openmrs.wh_price_list where name = '"+paymentType+"'";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(GePriceListID.class));

        List results = query.list();

        if (results.size() > 0) {

            GePriceListID itemID = (GePriceListID) results.get(0);
            return itemID.getprice_list_id();

        } else {


            return 0;
        }
    }

    @Override
    public int getDrugItem_id(String itemUuid){
        String sql = "SELECT concept_id from drug   WHERE uuid = '" + itemUuid + "' LIMIT 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(DrugConceptId.class));

        List results = query.list();

        if (results.size() > 0) {

            DrugConceptId itemID = (DrugConceptId) results.get(0);
            return itemID.getconcept_id();

        } else {

            logger.info("no drug");
            return 0;
        }

    }

    @Override
    public int getItem_id(String itemUuid){
        String sql = "SELECT concept_id from concept   WHERE uuid = '" + itemUuid + "' LIMIT 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(DrugConceptId.class));

        List results = query.list();

        if (results.size() > 0) {

            DrugConceptId itemID = (DrugConceptId) results.get(0);
            return itemID.getconcept_id();

        } else {

            logger.info("no drug");
            return 0;
        }

    }
    @Override
    public float getItem_amount(int itemId, int priceListId){
        String sql = "SELECT amount from wh_item_price WHERE wh_item_price.item_id='"+ itemId +"' and wh_item_price.price_list_id ='"+priceListId+"' and amount != 0";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PriceItem.class));

        List results = query.list();
        logger.info("Insert ledger " + sql);
        if (results.size() > 0) {
            PriceItem priceitem = (PriceItem)results.get(0);
            return priceitem.getamount();

        } else {

            logger.info("no data");
            return 0;
        }

    }

    //===================================== ACTIVATE AND DEACTIVATE DRUGS ==========================================
    @Override
    public List getItems_unfiltered(String name) {
        String sql = "SELECT concept_id as drug_id,drug_id as item_id,name,retired as retired from drug WHERE name like '%" + name + "%' LIMIT 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(getItems_unfiltered.class));

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no drug");
            return null;
        }
    }

    @Override
    public List getDrug(String name) {

        String sql = "SELECT concept_id as drug_id,drug_id as item_id,name,retired as retired from drug  WHERE name like '%" + name + "%' LIMIT 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(DrugItem.class));

        List results = query.list();
        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no drug");
            return null;
        }
    }

    @Override
    public String updateStatus(int drug_id, int status,int concept_id) {
        DbSession session = getSession();
        String sql_activate = "UPDATE drug SET retired="+status+",concept_id ="+concept_id+" WHERE drug_id= "+drug_id+"";
        logger.info("update status " + sql_activate);
        int res = getSession().createSQLQuery(sql_activate).executeUpdate();
        logger.info("update status after: " + res);
        session.beginTransaction().commit();
        return " status updated";
    }

    @Override
    public List selectPriceLists(){
        ArrayList<Notification> priceListsArray = new ArrayList<>();
        String sql = "SELECT price_list_id,name,date_recorded,default_price from wh_price_list   ORDER BY date_recorded DESC LIMIT 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PriceLists.class));

       // JSONObject obj = new JSONObject();
        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no Pricelist");
            return null;
        }

    }

    //============================== SEARCH THE PHYSICAL INVENTORY================================

    @Override
    public List selectPhysical(String name, String byDate, String byBatch) {
        String byName = null;
        String byDatedSearch = null;
        String byBatchEarch = null;

        if (name != "") {
            byName = "and drug.name LIKE '%" + name + "%'";
        } else
            byName = "";

        if (byDate != "") {
            byDatedSearch = "and wh_physical_inventory.inventoryDate LIKE '%" + byDate + "%'";
        } else
            byDatedSearch = "";

        if (byBatch != "") {
            byBatchEarch = "and wh_physical_inventory.batchNo LIKE '%" + byBatch + "%'";
        } else
            byBatchEarch = "";


        String sql = "SELECT wh_physical_inventory.inventoryDate as inventoryDate,wh_physical_inventory.physical_inventory_id as physical_inventory_id,wh_physical_inventory.batchNo as batchNo,wh_physical_inventory.receivedDate as expire_date, wh_physical_inventory.date_recorded as recorded_date,qnty,drug.name as name,wh_price_list.name as price_name from " +
                "wh_physical_inventory,drug,wh_price_list WHERE wh_physical_inventory.price_list_id = wh_price_list.price_list_id and drug.concept_id=wh_physical_inventory.item_id " + byName + " " + byDatedSearch + " " + byBatchEarch + "  ORDER BY wh_physical_inventory.date_recorded DESC";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PhysicalInv.class));
        logger.info("search inv: " + sql);
        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {
            logger.info("no physical inv");
            return null;
        }

    }

    @Override
    public List selectAllPhysical() {
        String sql = "SELECT wh_physical_inventory.inventoryDate as inventoryDate,wh_physical_inventory.physical_inventory_id as physical_inventory_id,wh_physical_inventory.batchNo as batchNo,wh_physical_inventory.receivedDate as expire_date, wh_physical_inventory.date_recorded as recorded_date,qnty,drug.name as name,wh_price_list.name as price_name from " +
                "wh_physical_inventory,drug,wh_price_list WHERE wh_physical_inventory.price_list_id = wh_price_list.price_list_id and drug.concept_id=wh_physical_inventory.item_id ORDER BY wh_physical_inventory.date_recorded DESC";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PhysicalInv.class));
        logger.info("select physical: " + sql);
        List results = query.list();
        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no physical inv");
            return null;
        }

    }

    //================================= GET ALL THE LEDGER ITEMS ===============================================
    @Override
    public List selectLedger_entry(String name, String batchNo, String invID) {
        String byName = null;
        if (name != "") {
            byName = " and drug.name LIKE '%" + name + "%'";
        } else
            byName = "";

        String bybatchNo = null;
        if (name != "") {
            bybatchNo = " and batch_no LIKE '%" + batchNo + "%'";
        } else
            bybatchNo = "";

        String byInvNo = null;
        if (name != "") {
            byInvNo = " and invoice_no LIKE '%" + invID + "%'";
        } else
            byInvNo = "";

        String sql = "SELECT ledger_type,quantity,batch_no,wh_ledger_entry.item_id as item_id,wh_ledger_entry.ledger_entry_id as ledger_entry_id,wh_qty_onhand.qty as onHand, drug.name as name,drug.strength as strength,drug.dosage_form as dosage_form,invoice_no,wh_price_list.name as price_list_name,received_date,expiry_date,amount from wh_ledger_entry,drug,wh_price_list,wh_qty_onhand WHERE wh_qty_onhand.item_id =wh_ledger_entry.item_id and wh_qty_onhand.priceList_id=wh_ledger_entry.price_list_id and   wh_price_list.price_list_id=wh_ledger_entry.price_list_id and drug.concept_id=wh_ledger_entry.item_id " + byName + " " + bybatchNo + " " + byInvNo + "  ORDER BY received_date DESC LIMIT 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(Ledger_entry.class));

        logger.info("inv: " + sql);

        List results = query.list();

        if (results.size() > 0) {

            return results;

        } else {

            logger.info("no hysical inv");
            return null;
        }

    }

    //================================= GET ALL THE LEDGER ITEMS ===============================================
    @Override
    public List getLedger_entry() {

        String sql = "SELECT ledger_type,quantity,batch_no,wh_ledger_entry.item_id as item_id,wh_ledger_entry.ledger_entry_id as ledger_entry_id,wh_qty_onhand.qty as onHand, drug.name as name,drug.strength as strength,drug.dosage_form as dosage_form,invoice_no,wh_price_list.name as price_list_name,received_date,expiry_date,amount from wh_ledger_entry,drug,wh_price_list,wh_qty_onhand WHERE wh_qty_onhand.item_id =wh_ledger_entry.item_id and wh_qty_onhand.priceList_id=wh_ledger_entry.price_list_id and wh_price_list.price_list_id=wh_ledger_entry.price_list_id and drug.concept_id=wh_ledger_entry.item_id ORDER BY received_date DESC LIMIT 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(Ledger_entry.class));

        logger.info("ledger: " + sql);

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no ledger");
            return null;
        }

    }

    //FUNCTION TO UPDATE CONTROL NUMBER
    @Override
    public String add_control_number(String bill_id, String control_number, String status, String status_code) {
        DbSession session = getSession();
        String sql_control_number = "UPDATE wh_order_sumUp SET Ctrl_number='"+control_number+"',payment_type = 'GePG' WHERE order_id='"+bill_id+"'";
        logger.info("update control number " + sql_control_number);
        int res = getSession().createSQLQuery(sql_control_number).executeUpdate();
        logger.info("update after: " + res);
        session.beginTransaction().commit();
        return "<gepgBillSubReq> " +
                "<TrxStsCode>7101</TrxStsCode>" +
                "</gepgBillSubReq>";

    }

    //FUNCTION TO UPDATE CONTROL NUMBER
    @Override
    public String add_cancel_status(String bill_id,String status) {
        DbSession session = getSession();
        String sql_control_number = "UPDATE wh_order_sumUp SET request_status='"+status+"' WHERE order_id='"+bill_id+"'";
        logger.info("update cancel status " + sql_control_number);
        int res = getSession().createSQLQuery(sql_control_number).executeUpdate();
        logger.info("update after: " + res);
        session.beginTransaction().commit();
        return "success";

    }

    //FUNCTION TO UPDATE POST BILLING INFORMATION
    @Override
    public String post_payment(String transaction_id,String bill_id, String control_number, String bill_amount, String paid_amount, String phone_number) {
        DbSession session = getSession();
        String sql_post_payment = "UPDATE wh_order_sumUp SET paid_status = 1,date_payed = now(),payerPhoneNUmber = '"+phone_number+"',trxId = '"+transaction_id+"' WHERE total_amount<='"+paid_amount+"' and order_id='"+bill_id+"' and Ctrl_number='"+control_number+"'";
        logger.info("update control number " + sql_post_payment);
        int res = getSession().createSQLQuery(sql_post_payment).executeUpdate();
        logger.info("update after: " + res);
        session.beginTransaction().commit();
        return "<gepgPmtSpInfoAck> " +
                "<TrxStsCode>7101 </TrxStsCode>" +
                "</gepgPmtSpInfoAck>";

    }

    //FUNCTION TO GET PAYMENT RECONCILIATION DATA VALUES
    @Override
    public String get_reconciliation_data(String bill_id){
        String sql = "select trxId as gepg_transaction_id,date_payed as gepg_transaction_date from wh_order_sumUp where order_id = '"+bill_id+"'";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(BillReconciliation.class));

        List results = query.list();
        if (results.size() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            BillReconciliation reconciliation = (BillReconciliation) results.get(0);
            return "<gepgSpReconcReq" +
                    "<SpReconcReqId>"+ reconciliation.getGepg_transaction_id()+"</SpReconcReqId>" +
                    "<SpCode>1001</SpCode>" +
                    "<SpSysId>TMH001</SpSysId>" +
                    "<TnxDt>"+dateFormat.format(reconciliation.getGepg_transaction_date())+"</TnxDt>" +
                    "<ReconcOpt>1</ReconcOpt>" +
                    "</gepgSpReconcReq>";

        } else {
            logger.info("no data returned");
            return "failed to post reconciliation data";
        }

    }

    //DATABASE DATA VALIDATIONS

    //FUNCTION TO GET PAYMENT RECONCILIATION DATA VALUES
    @Override
    public String check_amount(String bill_id){
        String sql = "select total_amount as gepg_amount from wh_order_sumUp where order_id = '"+bill_id+"'";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(DataValidation.class));

        List results = query.list();
        if (results.size() > 0) {
            DataValidation validation = (DataValidation) results.get(0);
            return String.valueOf(validation.getGepg_amount());

        } else {
            logger.info("no data returned");
            return "-1";
        }

    }

    @Override
    public String check_control_number(String control_number){
        String sql = "select Ctrl_number as gepg_control_number from wh_order_sumUp where Ctrl_number='"+control_number+"'";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(DataValidation.class));

        List results = query.list();
        if (results.size() > 0) {
            DataValidation validation = (DataValidation) results.get(0);
            return validation.getGepg_control_number();

        } else {
            logger.info("no data returned");
            return "-1";
        }

    }

    @Override
    public String check_bill_id(String bill_id){
        String sql = "select order_id as gepg_order_id from wh_order_sumUp where order_id='"+bill_id+"'";

        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(DataValidation.class));

        List results = query.list();
        if (results.size() > 0) {
            DataValidation validation = (DataValidation) results.get(0);
            return validation.getGepg_order_id();

        } else {
            logger.info("no data returned");
            return "-1";
        }

    }

    //INSERTING RECONCILIATION RESPONSE DATA
    @Override
    public String add_reconciliation_response(String reconciliation_id,String bill_id,String transaction_id, String control_number, float amount, String payer_reference_id, String transaction_date,String account_number, String remarks ){
        DbSession session = getSession();
        logger.info("reconciliation insert"+ bill_id);
        String sql = "INSERT INTO gepg_reconsiliation(reconRegId,billId,trnId,dateCreated,ctrlNum,amount,pay_refId,trxnDateTime,ctrlAccNum,remarks)" +
                " VALUES('"+reconciliation_id+"','" + bill_id + "','" + transaction_id + "', now(),'"+ control_number + "','" + amount + "','" + payer_reference_id + "','" + transaction_date + "','"+account_number+"','"+remarks+"')";
        logger.info("Insert gepg_reconsiliation " + sql);
        int res = getSession().createSQLQuery(sql).executeUpdate();
        logger.info("insert into gepg_reconsiliation: " + res);
        session.beginTransaction().commit();
        return "<gepgSpReconcRespAck> " +
                "<ReconcStsCode>7101 </ReconcStsCode>" +
                "</gepgSpReconcRespAck>";
    }

    //INSERTING GEPG RESPONSE DATA
    @Override
    public String add_gep_response(String response_id,String message, String response_type){
        DbSession session = getSession();
        logger.info("response insert"+ response_id);
        String sql = "INSERT INTO gepgResponse(resp_id,createdDate,msg,resp_type)" +
                " VALUES('" + response_id + "',now(),'" + message + "','"+ response_type + "')";
        logger.info("Insert gepgResponse " + sql);
        int res = getSession().createSQLQuery(sql).executeUpdate();
        logger.info("insert into gepgResponse: " + res);
        session.beginTransaction().commit();
        return "response inserted";
    }

    @Override
    public List get_information(String keyword){
        String bysearch = null;
        if(keyword !="") {
            bysearch = "and ( wh_order_sumUp.patient_id IN (SELECT patient_identifier.patient_id FROM patient_identifier WHERE identifier LIKE '%"+keyword+"%' inner join gepg_reconsiliation on gepg_reconsiliation.BillId = wh_order_sumUp.order_id) or concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) like '%"+keyword+"%' inner join gepg_reconsiliation on gepg_reconsiliation.BillId = wh_order_sumUp.order_id)";
        }
        else
            bysearch="";

        String sql = "select total_amount,Ctrl_number,billId,trnId,dateCreated,amount,pay_refId,trxnDateTime,ctrlAccNum,remarks,concat(pn.given_name,' ',ifnull(pn.middle_name,''),' ',  ifnull(pn.family_name,'')) as full_name from wh_order_sumUp\n" +
                "inner join person_name pn on pn.person_id=wh_order_sumUp.patient_id\n" +
                "inner join patient_identifier pi on pi.patient_id=wh_order_sumUp.patient_id\n" +
                "inner join gepg_reconsiliation on gepg_reconsiliation.BillId = wh_order_sumUp.order_id   \n" +
                "order by dateCreated desc limit 100";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(AllData.class));

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no data");
            return null;
        }

    }

    //update add items
    @Override
    public String editItem(int concept_name_id,int item_drug_id, String itemId, String name, String strength, String dosageForm, String dateCreated) {
        DbSession session = getSession();

        String sql = "SELECT concept_id FROM openmrs.concept where uuid='" + dosageForm + "'";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(dose.class));

        List results = query.list();
        int dose_ID = 1;
        Iterator it = results.iterator();
        while (it.hasNext()) {
            dose sd = (dose) it.next();
            dose_ID = sd.getconcept_id();
        }

        //update concept name
        String sql_update_concept = "UPDATE concept_name SET concept_id = '"+itemId+"',name='" +name+ "',locale = 'en',locale_preferred = 1,creator= 4,date_created='" +dateCreated+ "',concept_name_type='FULLY_SPECIFIED',voided=0,uuid = uuid() WHERE concept_name_id=" + concept_name_id + "";
        logger.info("Update into " + sql_update_concept);
        int res = getSession().createSQLQuery(sql_update_concept).executeUpdate();
        logger.info("update item after: " + res);
        session.beginTransaction().commit();

        //update drug information
        String sql_update_drug = "UPDATE drug SET concept_id = '"+itemId+"',name='" + name + "',combination = 0,dosage_form = " +dose_ID+ ",creator=4,date_created='" + dateCreated + "',retired=0,uuid=uuid(),strength = '" + strength + "' WHERE drug_id=" + item_drug_id + "";
        logger.info("update drug " + sql_update_drug);
        int res2 = getSession().createSQLQuery(sql_update_drug).executeUpdate();
        logger.info("update drug result: " + res2);
        session.beginTransaction().commit();

        return "updated";
    }

    /**
     * @param drugID
     * @param amount      ============================ HERE IS WHERE WE EDIT PRICE ===============================
     * @param pricelistID
     * @return
     */
    @Override
    public String editPrice(int item_price_id,String drugID, float amount, int pricelistID, String dateRecorded,float buying_price) {
        DbSession session = getSession();
        String sql_price = "UPDATE wh_item_price SET item_id = '"+drugID+"',amount=" + amount + ",price_list_id = " + pricelistID + ",date_recorded = '" + dateRecorded + "',buying_price = "+buying_price+" WHERE item_price_id="+item_price_id+ "";
        logger.info("update price" + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("update price result: " + res);
        session.beginTransaction().commit();
        return "updated";
    }

    //==============================================  EDIT PHYSICAL ===========================================================

    @Override
    public String editPhysical(int physical_inventory_id,String drug_id, double qty, int priceList, String receivedDate, String recorededDate, String batchNo) {
        DbSession session = getSession();
        String sql_price = "UPDATE wh_physical_inventory SET item_id = '"+drug_id+"',qnty=" +qty + ",price_list_id = " + priceList + ",date_recorded = '" +recorededDate + "',receivedDate='"+receivedDate+"',inventoryDate = '"+recorededDate+"',batchNo='"+batchNo+"' WHERE physical_inventory_id="+physical_inventory_id+ "";
        logger.info("update inventory" + sql_price);
        int res = getSession().createSQLQuery(sql_price).executeUpdate();
        logger.info("update inventory result: " + res);
        session.beginTransaction().commit();
        return "inventory updated";
    }

    //================================== UPDATE LEDGER ITEM ===============================================
    @Override
    public String updateLedger(String item_name, String LedgerEntryType, Double quantity, String BatchNo,
                               String InvoiceNo, String ExpiryDate, String receiveDate, String price_list_id, float amount, String mathz,int ledger_entry_id) {
        DbSession session = getSession();

        String sql_ledger = "UPDATE wh_ledger_entry SET item_id = '"+item_name+"',ledger_type='" +LedgerEntryType + "',quantity = " + quantity + ",batch_no = '" +BatchNo + "',invoice_no='"+InvoiceNo+"',price_list_id = '"+price_list_id+"',received_date='"+receiveDate+"',expiry_date='"+ExpiryDate+"',amount ="+amount+" WHERE ledger_entry_id="+ledger_entry_id+ "";
        logger.info("sql ledger " + sql_ledger);
        int res = getSession().createSQLQuery(sql_ledger).executeUpdate();
        logger.info("update ledger: " + res);
        session.beginTransaction().commit();

        String sql_onhand = "UPDATE wh_qty_onhand set qty=qty + " + quantity + ",priceList_id = '"+price_list_id+"' where item_id = '" + item_name + "'";
        logger.info("Insert wh_qty_onhand " + sql_onhand);
        int sql_onh = getSession().createSQLQuery(sql_onhand).executeUpdate();
        logger.info("insert wh_qty_onhand: " + sql_onh);
        session.beginTransaction().commit();

//        String sql = "SELECT qty from wh_qty_onhand  WHERE item_id = '" + item_name + "'";
//        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
//                .setResultTransformer(Transformers.aliasToBean(onHand.class));
//
//        List results = query.list();
//
//        if (results.size() > 0) {
//            String sql_onhand = "UPDATE wh_qty_onhand set qty=qty + " + quantity + " where item_id = '" + item_name + "'";
//            logger.info("Insert wh_qty_onhand " + sql_onhand);
//            int sql_onh = getSession().createSQLQuery(sql_onhand).executeUpdate();
//            logger.info("insert wh_qty_onhand: " + sql_onh);
//            session.beginTransaction().commit();
//
//        } else {
//            String sql_onhand = "INSERT INTO wh_qty_onhand (item_id, qty, priceList_id) VALUES " +
//                    "('" + item_name + "','" + quantity + "','" + price_list_id + "')";
//            logger.info("Insert wh_qty_onhand " + sql_onhand);
//            int sql_onh = getSession().createSQLQuery(sql_onhand).executeUpdate();
//            logger.info("insert wh_qty_onhand: " + sql_onh);
//            session.beginTransaction().commit();
//
//        }

        return "updated";
    }

    //================================= GET TOTAL PHYSICAL ===============================================
    @Override
    public List get_total_physical() {
        String sql = "SELECT sum(qnty) as quantity from wh_physical_inventory";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PhysicalInv.class));

        logger.info("total quantity: " + sql);

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no ledger");
            return null;
        }

    }

    //================================= GET TOTAL PHYSICAL VALUE===============================================
    @Override
    public List get_physical_value() {
        String sql = "SELECT sum(wh_physical_inventory.qnty * wh_item_price.buying_price) as physical_value FROM wh_physical_inventory inner join wh_item_price ON wh_physical_inventory.item_id = wh_item_price.item_id";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PhysicalInv.class));

        logger.info("total value: " + sql);

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no ledger");
            return null;
        }

    }

    //================================= GET TOTAL STOCK ===============================================
    @Override
    public List get_total_stock() {
        String sql = "SELECT sum(qty) as stock_quantity from wh_qty_onhand";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PhysicalInv.class));

        logger.info("total quantity: " + sql);

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no ledger");
            return null;
        }

    }

    //================================= GET TOTAL PHYSICAL VALUE===============================================
    @Override
    public List get_stock_value() {
        String sql = "SELECT sum(wh_qty_onhand.qty * wh_item_price.buying_price) as stock_value FROM wh_qty_onhand inner join wh_item_price ON wh_qty_onhand.item_id = wh_item_price.item_id";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PhysicalInv.class));

        logger.info("total value: " + sql);

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no ledger");
            return null;
        }

    }

    //================================= GET TOTAL STOCK EXPIRE QUANTITY===============================================
    @Override
    public List get_expiry_stock() {
        String sql = "SELECT sum(quantity) as expiry_quantity FROM wh_ledger_entry WHERE wh_ledger_entry.expiry_date >= DATE(now()) AND wh_ledger_entry.expiry_date <= DATE_ADD(DATE(now()), INTERVAL 90 DAY)";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PhysicalInv.class));

        logger.info("expiry value: " + sql);

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no expiry");
            return null;
        }

    }

    //================================= GET TOTAL PHYSICAL EXPIRY VALUE ===============================================
    @Override
    public List get_expire_value() {
        String sql = "SELECT sum(wh_ledger_entry.quantity * wh_item_price.buying_price) as expiry_value FROM wh_ledger_entry join wh_item_price ON wh_ledger_entry.item_id = wh_item_price.item_id WHERE wh_ledger_entry.expiry_date >= DATE(now()) AND wh_ledger_entry.expiry_date <= DATE_ADD(DATE(now()), INTERVAL 90 DAY)";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(PhysicalInv.class));

        logger.info("total value: " + sql);

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no ledger");
            return null;
        }

    }
    //============================UPDATE PRODUCT MOVEMENT (edit) ==========================================
    
    @Override
    public String EditProductMovement(int id,int person_id_sub_store, int prod_mv_id, int item_id, String date_qty_requested,int quantity_requested, int quantity_given, String date_qty_given, int person_id_main_store, int sub_store_id, int product_batch_no, String product_mvnt_status, int price_list_id ) {
        DbSession session = getSession();

        String sql_edit = "UPDATE wh_product_movement SET person_id_sub_store='" +person_id_sub_store + "',prod_mv_id = " + prod_mv_id + ",item_id = '"
        +item_id + "',date_qty_requested='"+date_qty_requested+"',quantity_requested = '"+quantity_requested+"',quantity_given='"
        		+quantity_given+"',date_qty_given='"+date_qty_given+"',person_id_main_store ='"+person_id_main_store+"',sub_store_id='"+
        sub_store_id+"', product_batch_no='"+product_batch_no+"',product_mvnt_status='"+product_mvnt_status+"',price_list_id='"+price_list_id+"' WHERE id='"+id+ "'";
        logger.info("sql product movement " + sql_edit);
        int res = getSession().createSQLQuery(sql_edit).executeUpdate();
        logger.info("update Product movement: " + res);
        session.beginTransaction().commit();
        return "updated";
    }
    
    
//=============================UPDATE PRODUCT MOVEMENT (dispatch batch) ============================================
    
    @Override
    public String updateDispatch_Batch(int person_id_sub_store, String product_mvnt_status) {
        DbSession session = getSession();

        String sqlDispatch = "UPDATE wh_product_movement SET product_mvnt_status='"+product_mvnt_status+"'"
        		+ " WHERE person_id_sub_store='"+person_id_sub_store+ "'";
        logger.info("sql product movement batch updated" + sqlDispatch);
        int res = getSession().createSQLQuery(sqlDispatch).executeUpdate();
        logger.info("update Product movement batch dispatched: " + res);
        session.beginTransaction().commit();
        return "updated";
    }
    
//==============================UPDATE PRODUCT MOVEMENT (dispatch row) ====================================================
    
    @Override
    public String updateDispatch_Row( int id, String product_mvnt_status) {
        DbSession session = getSession();

        String sqlDispatch = "UPDATE wh_product_movement SET product_mvnt_status='"+product_mvnt_status+"'"
        		+ " WHERE id='"+id+ "'";
        logger.info("sql product movement dispatched" + sqlDispatch);
        int res = getSession().createSQLQuery(sqlDispatch).executeUpdate();
        logger.info("update Product movement row dispatched: " + res);
        session.beginTransaction().commit();
        return "updated";
    }


    //================================= GET ALL THE DRUG ITEM ===============================================
    @Override
    public List get_all_ledger() {
        String sql = "SELECT sum(wh_ledger_entry.quantity * wh_item_price.buying_price) as amount, drug.name as drug_name,wh_ledger_entry.expiry_date as expiry_date,wh_ledger_entry.batch_no as batch_no,wh_price_list.name as price_list_name from drug,wh_ledger_entry,wh_price_list,wh_item_price WHERE wh_ledger_entry.item_id = wh_item_price.item_id and wh_ledger_entry.price_list_id = wh_price_list.price_list_id and wh_ledger_entry.item_id = drug.concept_id and  wh_ledger_entry.expiry_date >= DATE(now()) AND wh_ledger_entry.expiry_date <= DATE_ADD(DATE(now()), INTERVAL 90 DAY)";
        org.hibernate.Query query = this.getSession().createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(Ledger_entry.class));

        logger.info("drug sql: " + sql);

        List results = query.list();

        if (results.size() > 0) {
            return results;

        } else {

            logger.info("no drug");
            return null;
        }

    }


    @Override
    public void clearHack() {
        getSession().createSQLQuery("DELETE from hack_obs").executeUpdate();
    }

    public DbSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(DbSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public DbSession getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Connection martConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/analytics", "analytics", "");
            logger.info("get_dbData: imo connected");
        } catch (SQLException e) {
            logger.info("get_dbData: error"+e.getMessage());
            logger.info("get_dbData: error"+e.getMessage());
            e.printStackTrace();

        }
        return connection;

    }

}
