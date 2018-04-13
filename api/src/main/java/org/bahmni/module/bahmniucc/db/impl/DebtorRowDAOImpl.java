package org.bahmni.module.bahmniucc.db.impl;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.db.DebtorRowDAO;
import org.bahmni.module.bahmniucc.model.*;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;


import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    Query query = this.getSession().createSQLQuery(sql)
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


    Query query = this.getSession().createSQLQuery(sql)
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

    Query query = this.getSession().createSQLQuery(sql)
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


    Query query = this.getSession().createSQLQuery(sql)
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

    String sql = "select patient_id from openerp_debtor_list";


    Query query = this.getSession().createSQLQuery(sql)
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


    Query query = this.getSession().createSQLQuery(sql)
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

    Query query = this.getSession().createSQLQuery(stringQuery)
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

    Query query = this.getSession().createSQLQuery(stringQuery)
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

    Query query = this.getSession().createSQLQuery(notification.getNotification_sql())
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
    String sql = "update openmrs.person_attribute set value = '"+room+"' where person_id= (select person_id from openmrs.person where uuid='"+uuid+"') and person_attribute_type_id='52'";

    logger.info("Doctors Room " + sql);
    int res = getSession().createSQLQuery(sql).executeUpdate();
    logger.info("Update result " + res);
    return "";
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
}