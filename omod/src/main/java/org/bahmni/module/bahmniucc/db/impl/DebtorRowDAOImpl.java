package org.bahmni.module.bahmniucc.db.impl;

import org.bahmni.module.bahmniucc.db.DebtorRowDAO;
import org.bahmni.module.bahmniucc.db.HibernateUtil;
import org.bahmni.module.bahmniucc.model.Debtor;
import org.bahmni.module.bahmniucc.model.DebtorRow;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public class DebtorRowDAOImpl implements DebtorRowDAO {
    @Override
    public void saveRow(Debtor debtor) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();


        DebtorRow debtorRow = new DebtorRow();
        debtorRow.setChargeable_amount(debtor.chargeable_amount);
        debtorRow.setDate_created(debtor.date_created);
        debtorRow.setDefault_quantity(debtor.default_quantity);
        debtorRow.setInvoice_id(debtor.invoice_id);
        debtorRow.setPatient_name(debtor.patient_name);
        debtorRow.setPatient_id(debtor.patient_id);
        session.save(debtorRow);
        session.getTransaction().commit();
        session.close();

    }

    @Override
    public void deleteRows() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String hql = String.format("TRUNCATE TABLE %s", "openerp_debtor_list");
        Query query = session.createQuery(hql);
        query.executeUpdate();
        session.close();
    }
}
