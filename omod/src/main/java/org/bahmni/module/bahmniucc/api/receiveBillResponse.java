package org.bahmni.module.bahmniucc.api;


import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/ReceiveBilling")
public class receiveBillResponse {

    String GEPG_URL = "http://154.118.230.18:80/api/bill/qrequest";
    String returnedValue;
    private Logger logger = Logger.getLogger(getClass());

    @RequestMapping(method = RequestMethod.POST, value = "receiveCtrlNumber")
    @ResponseBody
    public String receiveCtrlNumber(@RequestParam("BillId") String BillId, @RequestParam("TrxSts") String TrxSts,
                                    @RequestParam("PayCntrNum") String PayCntrNum, @RequestParam("TxnStsCode") String TxnStsCode){
        logger.info("GePG receiveCtrlNumber: " + BillId +" " +TrxSts +" "+PayCntrNum+" "+TxnStsCode+" ");

        return  processCtrlNumber( BillId,  TrxSts,  PayCntrNum, TxnStsCode );
    }

    public  String processCtrlNumber(String BillId, String TrxSts, String PayCntrNum,String TxnStsCode){
        if(TrxSts.equalsIgnoreCase("GS")){

            DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
            return feedClient.processCtrlNumber(BillId, PayCntrNum);
        }

        return null;
    }



}
