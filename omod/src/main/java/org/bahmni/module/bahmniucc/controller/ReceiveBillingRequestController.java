package org.bahmni.module.bahmniucc.controller;

import com.google.gson.Gson;
import java.util.List;
import org.apache.log4j.Logger;

import org.openmrs.api.context.Context;

public class ReceiveBillingRequestController {
    private static ReceiveBillingRequestController ourInstance = new ReceiveBillingRequestController();
    private Logger logger = Logger.getLogger(this.getClass());

    public static ReceiveBillingRequestController getInstance() {
        return ourInstance;
    }

    private ReceiveBillingRequestController() {
    }

    public String getNames() {

        return null;
    }
}
