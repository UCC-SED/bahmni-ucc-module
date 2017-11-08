package org.bahmni.module.bahmniucc.task;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.client.impl.OpenErpPatientFeedClientImpl;
import org.bahmni.module.bahmniucc.db.impl.DebtorRowDAOImpl;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public class OpenErpPatientDebtTask extends AbstractTask {

    private Logger logger = Logger.getLogger(OpenErpPatientDebtTask.class);


    @Override
    public void execute() {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        feedClient.processFeed();
    }


}
