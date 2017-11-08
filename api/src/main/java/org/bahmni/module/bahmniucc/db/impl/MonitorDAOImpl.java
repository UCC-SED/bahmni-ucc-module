package org.bahmni.module.bahmniucc.db.impl;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.db.MonitorDAO;
import org.hibernate.Query;
import org.json.simple.JSONObject;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;

import java.util.List;

/**
 * Created by ucc-ian on 09/Oct/2017.
 */
public class MonitorDAOImpl implements MonitorDAO {


    private Logger logger = Logger.getLogger(getClass());

    private DbSessionFactory sessionFactory;


    @Override
    public String readQueries(List<String> queries) {

        JSONObject obj = new JSONObject();

        DbSession session = getSession();
        String hql = "select count(*) as  total, e.encounter_type, et.name from encounter e\n" +
                "join encounter_type et on et.encounter_type_id=e.encounter_type \n" +
                "where e.encounter_datetime=CURDATE() \n" +
                "group by  e.encounter_type";
        Query query = session.createQuery(hql);
        List results = query.list();

        if (results.size() > 0) {

            for (int x = 0; x < results.size(); x++) {

                Object[] obj_row = (Object[]) results.get(0);
                obj.put(obj_row[2], obj_row[0]);
            }
        }else
        {

            logger.info("No Data");
            return null;
        }

        logger.info(obj.toJSONString());

        return obj.toJSONString();
    }

    @Override
    public String readQuery() {

        JSONObject obj = new JSONObject();

        DbSession session = getSession();
        String hql = "select count(*) as  total, e.encounter_type, et.name from encounter e\n" +
                "join encounter_type et on et.encounter_type_id=e.encounter_type \n" +
                "where e.encounter_datetime=CURDATE() \n" +
                "group by  e.encounter_type";
        Query query = session.createQuery(hql);
        List results = query.list();

        if (results.size() > 0) {

            for (int x = 0; x < results.size(); x++) {

                Object[] obj_row = (Object[]) results.get(0);
                obj.put(obj_row[2], obj_row[0]);
            }
        }else
        {

            logger.info("No Data");
            return null;
        }

        logger.info(obj.toJSONString());

        return obj.toJSONString();

    }


    public DbSession getSession() {
        return sessionFactory.getCurrentSession();
    }
}
