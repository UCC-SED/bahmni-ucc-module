package org.bahmni.module.bahmniucc.controller;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.AppGlobalProperties;
import org.bahmni.module.bahmniucc.UCCModuleConstants;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.bahmni.module.bahmniucc.network.HttpGetRequest;
import org.bahmni.module.bahmniucc.network.HttpPostRequest;
import org.bahmni.module.bahmniucc.network.HttpRequest;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.openmrs.api.context.Context;

import java.util.ArrayList;

/**
 * Created by ucc-ian on 02/Jan/2018.
 */
public class NHIFController {


    private HttpRequest httpRequest = new HttpRequest();
    private static NHIFController nhifController = new NHIFController();

    DebtClient debtClient = Context.getService(OpenErpPatientFeedClient.class);

    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");

    private Logger logger = Logger.getLogger(getClass());

    private NHIFController() {
    }

    public static NHIFController getInstance() {
        return nhifController;
    }

    public String getAuthorizationCardStatus(String cardNo, String visitTypeID, String referralNo) {

        String authorizationHeader = getAuthenticationHeader(AppGlobalProperties.GLOBAL_NHIF_USERNAME(), AppGlobalProperties.GLOBAL_NHIF_PASSWORD());

        String requestUrl = AppGlobalProperties.GLOBAL_NHIF_BASE_URL() + "verification/AuthorizeCard?CardNo=" + cardNo + "&VisitTypeID=" + visitTypeID + "&ReferralNo=" + referralNo;

        HttpGetRequest httpGetRequest = new HttpGetRequest(httpRequest, requestUrl, authorizationHeader, "application/x-www-form-urlencoded;charset=UTF-8");
        String autherizationCardStatusResponse = httpGetRequest.execute();
        logger.info("autherizationCardStatusResponse Response " + autherizationCardStatusResponse);

        return autherizationCardStatusResponse;
    }

    public String getAuthenticationHeader(String username, String password) {

        String authenticationHeader = debtClient.readAuthenticationHeader();

        if (authenticationHeader.equalsIgnoreCase("No Token")) {
            String content = "grant_type=password&username=" + username + "&password=" + password;
            HttpPostRequest httpPostRequest = new HttpPostRequest(httpRequest, AppGlobalProperties.GLOBAL_NHIF_REQUEST_TOKEN_URL(), "", content, "text/xml; charset=utf-8");
            String authenticationHeaderResponse = httpPostRequest.execute();

            logger.info("getAuthenticationHeader Response " + authenticationHeaderResponse);

            JSONObject obj = new JSONObject(authenticationHeaderResponse);
            String accessToken = obj.getString("access_token");
            String tokenType = obj.getString("token_type");
            String issued = obj.getString(".issued");
            String expires = obj.getString(".expires");


            debtClient.storeAuthenticationHeader(tokenType + " " + accessToken,issued, expires );

            return tokenType + " " + accessToken;
        } else {
            return authenticationHeader;
        }


    }


    public void generateClaimFolio(ArrayList<String> visitIDList) {

    }


}
