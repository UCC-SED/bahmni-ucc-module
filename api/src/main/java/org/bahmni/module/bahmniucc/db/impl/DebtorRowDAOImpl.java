package org.bahmni.module.bahmniucc.db.impl;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.db.DebtorRowDAO;
import org.bahmni.module.bahmniucc.model.DebtorRow;
import org.bahmni.module.bahmniucc.model.HackItem;
import org.bahmni.module.bahmniucc.model.MonitorItem;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.json.simple.JSONObject;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;

import java.util.List;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public class DebtorRowDAOImpl implements DebtorRowDAO {

    private Logger logger = Logger.getLogger(getClass());

    private DbSessionFactory sessionFactory;


    @Override
    public void saveDebtorRow(List<DebtorRow> results) {
        DbSession session = getSession();
        for (DebtorRow result : results) {


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


            getSession().createSQLQuery(save_sql).executeUpdate();
            // session.save(result);
        }
    }


    @Override
    public void clearAllResults() {

        getSession().createSQLQuery("DELETE from openerp_debtor_list").executeUpdate();
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
    public void insertHack(String visitType) {
        String sql = "INSERT INTO `openmrs`.`hack_obs`\n" +
                "(visit_type)\n" +
                "VALUES\n" +
                "('" + visitType + "')";

        getSession().createSQLQuery(sql).executeUpdate();
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
