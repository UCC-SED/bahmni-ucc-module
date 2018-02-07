package org.bahmni.module.bahmniucc.controller;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.json.JSONObject;
import org.openmrs.api.context.Context;
import org.springframework.http.converter.json.GsonFactoryBean;

import java.util.List;
import java.util.Map;

public class TribeController {
    private static TribeController ourInstance = new TribeController();
    private Logger logger = Logger.getLogger(getClass());

    public static TribeController getInstance() {
        return ourInstance;
    }

    private TribeController() {

    }

    public String getTribeNames(String searchName){
        logger.info("search name for " + searchName);
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        List tribeData = feedClient.searchTribes(searchName);
       String TribeJson = new Gson().toJson(tribeData);



        return TribeJson;
    }



}
