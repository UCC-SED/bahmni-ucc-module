package org.bahmni.module.bahmniucc.task;

import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.monitor.Monitor;
import org.bahmni.module.bahmniucc.monitor.impl.MonitorImpl;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Created by ucc-ian on 06/Oct/2017.
 */
public class SendMonitoringDataTask extends AbstractTask {
    @Override
    public void execute() {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        feedClient.processMonitorData();
    }
}
