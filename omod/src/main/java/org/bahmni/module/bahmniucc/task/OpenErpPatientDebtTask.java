package org.bahmni.module.bahmniucc.task;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.impl.OpenErpPatientFeedClientImpl;
import org.bahmni.module.bahmniucc.db.impl.DebtorRowDAOImpl;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public class OpenErpPatientDebtTask extends AbstractTask {

    private Logger logger = Logger.getLogger(OpenErpPatientDebtTask.class);
    private DebtorRowDAOImpl debtorDAO = new DebtorRowDAOImpl();

    @Override
    public void execute() {
        OpenErpPatientFeedClientImpl feedClient = new OpenErpPatientFeedClientImpl();
        feedClient.processFeed();


       // logger.info("Just Running");


    }


}
