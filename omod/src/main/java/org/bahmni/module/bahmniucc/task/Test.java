package org.bahmni.module.bahmniucc.task;

import org.bahmni.module.bahmniucc.client.impl.OpenErpPatientFeedClientImpl;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public class Test {

    public static void main(String[] args) {
        OpenErpPatientFeedClientImpl feedClient = new OpenErpPatientFeedClientImpl();
        feedClient.processFeed();

    }
}
