package org.bahmni.module.bahmniucc.api;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.controller.NHIFController;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ucc-ian on 02/Jan/2018.
 */

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/nhif")
public class NHIFWebController extends BaseRestController {


    private Logger logger = Logger.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET, value = "verification")
    @ResponseBody
    public String nhifVerification(@RequestParam("cardNo") String cardNo, @RequestParam("visitType") String visitType, @RequestParam("referralNo") String referralNo) throws Exception {

        NHIFController nhifController = NHIFController.getInstance();
        return nhifController.getAuthorizationCardStatus(cardNo, visitType, referralNo);

    }


    @RequestMapping(method = RequestMethod.POST, value = "claim")
    @ResponseBody
    public void nhifClaim(@RequestParam("cardNo") String cardNo, @RequestParam("visitType") String visitType, @RequestParam("referralNo") String referralNo) throws Exception {

        NHIFController nhifController = NHIFController.getInstance();
       // return nhifController.getAuthorizationCardStatus(cardNo, visitType, referralNo);

    }
}
