package org.bahmni.module.bahmniucc.api;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.json.JSONObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/receiveResponse")
public class BillSubmissionResponseController {

    String GEPG_URL = "http://154.118.230.18:80/api";
    String RECONCILIATION_URL = GEPG_URL + "/reconciliations/sp_qrequest";
    String CANCEL_BILL_URL = GEPG_URL + "/bill/cancel-request";
    String returnedValue;

    private Logger logger = Logger.getLogger(getClass());

    //FUNCTION TO GET CONTROL NUMBER
    @RequestMapping(method = RequestMethod.POST, value = "get_control_number")
    @ResponseBody
    public String get_control_number(@RequestBody String data) {
        try {
            logger.info("information: " + data);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setEncoding("UTF-8");
            src.setCharacterStream(new StringReader(data));
            Document doc = builder.parse(src);
            String TrxStsCode = doc.getElementsByTagName("TrxSts").item(0).getTextContent();
            if (TrxStsCode.equalsIgnoreCase("GS")) {
                String BillId = doc.getElementsByTagName("BillId").item(0).getTextContent();
                String TrxSts = doc.getElementsByTagName("TrxSts").item(0).getTextContent();
                String PayCntrNum = doc.getElementsByTagName("PayCntrNum").item(0).getTextContent();
                DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
                String db_bill_id = feedClient.check_bill_id(BillId);
                String status = check_bill_id(db_bill_id, BillId);
                if (status.contains("billId exits")) {
                    feedClient.add_cancel_status(BillId, "completed");
                    return feedClient.process_control_number(BillId, TrxSts, PayCntrNum, TrxStsCode);
                } else {
                    return "<gepgBillSubReq> " +
                            "<TrxStsCode>7242;7627</TrxStsCode>" +
                            "</gepgBillSubReq>";
                }

            } else {
                return "<gepgBillSubReq> " +
                        "<TrxStsCode>7242;7627</TrxStsCode>" +
                        "</gepgBillSubReq>";
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return "<gepgBillSubReq> " +
                    "<TrxStsCode>7242;7627</TrxStsCode>" +
                    "</gepgBillSubReq>";
        }
    }

    //FUNCTION TO POST PAYMENT
    @RequestMapping(method = RequestMethod.POST, value = "post_payment_information")
    @ResponseBody
    public String post_payment(@RequestBody String information) {
        try {
            logger.info("information: " + information);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setEncoding("UTF-8");
            src.setCharacterStream(new StringReader(information));

            Document doc = builder.parse(src);
            String TrxId = doc.getElementsByTagName("TrxId").item(0).getTextContent();
            String SpCode = doc.getElementsByTagName("SpCode").item(0).getTextContent();
            String PayRefId = doc.getElementsByTagName("PayRefId").item(0).getTextContent();
            String BillId = doc.getElementsByTagName("BillId").item(0).getTextContent();
            String PayCtrNum = doc.getElementsByTagName("PayCtrNum").item(0).getTextContent();
            String BillAmt = doc.getElementsByTagName("BillAmt").item(0).getTextContent();
            String PaidAmt = doc.getElementsByTagName("PaidAmt").item(0).getTextContent();
            String BillPayOpt = doc.getElementsByTagName("BillPayOpt").item(0).getTextContent();
            String CCy = doc.getElementsByTagName("CCy").item(0).getTextContent();
            String TrxDtTm = doc.getElementsByTagName("TrxDtTm").item(0).getTextContent();
            String UsdPayChnl = doc.getElementsByTagName("UsdPayChnl").item(0).getTextContent();
            String PyrCellNum = doc.getElementsByTagName("PyrCellNum").item(0).getTextContent();
            String PyrName = doc.getElementsByTagName("PyrName").item(0).getTextContent();
            String PyrEmail = doc.getElementsByTagName("PyrEmail").item(0).getTextContent();
            String PspReceiptNumber = doc.getElementsByTagName("PspReceiptNumber").item(0).getTextContent();
            String PspName = doc.getElementsByTagName("PspName").item(0).getTextContent();

            DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
            String db_amount = feedClient.check_amount(BillId);
            String db_control_number = feedClient.check_control_number(PayCtrNum);
            String status = check_data(db_amount, PaidAmt, db_control_number, PayCtrNum);
            if (status.contains("billId,controlNumber and amountPaid correct") && PyrCellNum != null && PyrCellNum != "") {
                feedClient = Context.getService(OpenErpPatientFeedClient.class);
                return feedClient.post_payment(TrxId, BillId, PayCtrNum, BillAmt, PaidAmt, PyrCellNum);
            } else {
                return status;
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return "failed to get result";
        }
    }

    //FUNCTION TO GET RECONCILIATION INFORMATION
    @RequestMapping(method = RequestMethod.GET, value = "get_reconciliation_data")
    @ResponseBody
    public String get_reconciliation_data(@RequestParam("bill_id") String bill_id) throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        String result = request_reconciliation(feedClient.get_reconciliation_data(bill_id));
        String acknowledgement = get_acknowledgement(result);

        if (acknowledgement.contains("successfully")) {
            JSONObject object = new JSONObject();
            object.put("status", "success");
            object.put("message", "success reconciled");
            logger.info("");
            return object.toString();
        } else {
            JSONObject object = new JSONObject();
            object.put("status", "failed");
            object.put("message", "failed to reconcile");
            return object.toString();
        }
    }

    //*****FUNCTION TO GET ALL THE INFORMATION FROM THE DATABASE
    @RequestMapping(method = RequestMethod.GET, value = "get_concile")
    @ResponseBody
    public String get_all_data(@RequestParam("keyword") String keyword) throws Exception {
        logger.info("keyword :"+keyword);
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson(feedClient.get_information(keyword));
    }

    //POST RECONCILIATION
    public String request_reconciliation(String message) {
        try {
            String Gepg_Com = "default.sp.in";
            String Gepg_Code = "SP222";
            StringBuffer content = new StringBuffer();
            String getXmlData = this.get_data(5);
            URL url = new URL(this.RECONCILIATION_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Gepg-Com", "default.sp.in");
            conn.setRequestProperty("Gepg-Code", "SP222");
            conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            String msg = message;
            wr.writeBytes(msg);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String str;
            while ((str = in.readLine()) != null) {
                content.append(str);
            }
            in.close();
            this.returnedValue = content.toString();
            logger.info("returned: " + returnedValue);
        } catch (Exception e) {
            logger.error("some error" + e.getMessage());
            e.printStackTrace();
            e.getMessage();
        }

        return this.returnedValue;
    }

    //GET RECONCILIATION ACKNOWLEDGEMENT
    public String get_acknowledgement(String feedback) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(feedback));

            Document doc = builder.parse(src);
            String status = doc.getElementsByTagName("ReconcStsCode").item(0).getTextContent();
            if (status.equalsIgnoreCase("7242")) {
                return "successfully";
            } else {
                return "failed";
            }

        } catch (Exception e) {
            return null;
        }

    }

    //GETTING RECONCILIATION RESPONSE
    @RequestMapping(method = RequestMethod.POST, value = "get_reconciliation_response")
    @ResponseBody
    public String get_reconciliation_response(@RequestBody String data) {
        try {
            logger.info("information: " + data);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setEncoding("UTF-8");
            src.setCharacterStream(new StringReader(data));

            Document doc = builder.parse(src);
            String SpReconcReqId = doc.getElementsByTagName("SpReconcReqId").item(0).getTextContent();
            String SpCode = doc.getElementsByTagName("SpCode").item(0).getTextContent();
            String SpName = doc.getElementsByTagName("SpName").item(0).getTextContent();
            String ReconcStsCode = doc.getElementsByTagName("ReconcStsCode").item(0).getTextContent();
            String SpBillId = doc.getElementsByTagName("SpBillId").item(0).getTextContent();
            String BillCtrNum = doc.getElementsByTagName("BillCtrNum").item(0).getTextContent();
            String pspTrxId = doc.getElementsByTagName("pspTrxId").item(0).getTextContent();
            String paidAmt = doc.getElementsByTagName("PaidAmt").item(0).getTextContent();
            String CCy = doc.getElementsByTagName("CCy").item(0).getTextContent();
            String PayRefId = doc.getElementsByTagName("PayRefId").item(0).getTextContent();
            String TrxDtTm = doc.getElementsByTagName("TrxDtTm").item(0).getTextContent();
            String CtrAccNum = doc.getElementsByTagName("CtrAccNum").item(0).getTextContent();
            String UsdPayChnl = doc.getElementsByTagName("UsdPayChnl").item(0).getTextContent();
            String PspName = doc.getElementsByTagName("PspName").item(0).getTextContent();
            String PspCode = doc.getElementsByTagName("PspCode").item(0).getTextContent();
            String Remarks = doc.getElementsByTagName("Remarks").item(0).getTextContent();

            if (paidAmt == null || SpBillId == null || CtrAccNum == null) {
                return "<gepgSpReconcRespAck> " +
                        "<ReconcStsCode>7242;7627</ReconcStsCode>" +
                        "</gepgSpReconcRespAck>";
            } else {
                DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
                return feedClient.add_reconciliation_response(SpReconcReqId, SpBillId, pspTrxId, BillCtrNum, Float.parseFloat(paidAmt), PayRefId, TrxDtTm, CtrAccNum, Remarks);
            }

        } catch (Exception e) {
            return "<gepgSpReconcRespAck> " +
                    "<ReconcStsCode>7242;7627</ReconcStsCode>" +
                    "</gepgSpReconcRespAck>";
        }
    }

    public String get_data(int num) {
        return "";
    }

    //FUNCTION TO CANCEL BILL REQUEST
    @RequestMapping(method = RequestMethod.GET, value = "cancel_bill_request")
    @ResponseBody
    public String cancelBillRequest(@RequestParam("BillId") String BillId) {
        logger.info("GePG CANCEL: " + BillId);
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        String db_bill_id = feedClient.check_bill_id(BillId);
        String status = check_bill_id(db_bill_id, BillId);
        if (status.contains("billId exits")) {
            String feedback = cancelBillRequestProcess(BillId);
            return getCancelValues(feedback);
        } else {
            return status;
        }
    }

    //POST CANCEL BILL REQUEST
    public String cancelBillRequestProcess(String BillId) {
        try {
            logger.info("paramater :" + BillId);
            String Gepg_Com = "default.sp.in";
            String Gepg_Code = "SP222";
            StringBuffer content = new StringBuffer();
            String getXmlData = this.get_data(5);
            URL url = new URL(this.CANCEL_BILL_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Gepg-Com", "default.sp.in");
            conn.setRequestProperty("Gepg-Code", "SP222");
            conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            String msg = "<gepgBillCanclReq> " +
                    "<SpCode> SP222</SpCode>" +
                    "<BillId> " + BillId + " </BillId> " +
                    "</gepgBillCanclReq>";
            wr.writeBytes(msg);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String str;
            while ((str = in.readLine()) != null) {
                content.append(str);
            }
            in.close();
            return content.toString();
        } catch (Exception var11) {
            logger.info("error :" + var11.getMessage());
            var11.printStackTrace();
            var11.getMessage();
            return null;
        }

    }

    //FUNCTION TO GET CANCELLATION RESULT DATA
    public String getCancelValues(String data) {
        logger.info("GEPG cancel data:"+data);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(data));

            Document doc = builder.parse(src);
            String BillId = doc.getElementsByTagName("BillId").item(0).getTextContent();
            String TrxSts = doc.getElementsByTagName("TrxSts").item(0).getTextContent();
            String TrxStsCode = doc.getElementsByTagName("TrxStsCode").item(0).getTextContent();
            if (TrxSts.equalsIgnoreCase("GS")) {
                DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
                String status = feedClient.add_cancel_status(BillId, "cancelled");
                if (status.equalsIgnoreCase("success")) {
                    JSONObject object = new JSONObject();
                    object.put("status", "success");
                    object.put("message", "success cancel");
                    return object.toString();
                } else {
                    JSONObject object = new JSONObject();
                    object.put("status", "failed");
                    object.put("message", "failed to cancel");
                    return object.toString();
                }
            } else {
                JSONObject object = new JSONObject();
                object.put("status", "failed");
                object.put("message", "failed to cancel");
                return object.toString();
            }

        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("status", "failed");
            object.put("message", "failed to cancel");
            return object.toString();
        }

    }

    //FUNCTION TO COMPARE DATA BTN DATABASE AND RECEIVED
    public String check_data(String db_amount, String received_amount, String db_control_umber, String received_control_number) {
        if (Float.parseFloat(db_amount) <= Float.parseFloat(received_amount) &&
                db_control_umber.equalsIgnoreCase(received_control_number) && Float.parseFloat(db_amount)>=0) {
            return "billId,controlNumber and amountPaid correct";
        } else {
            return "either billId,controlNumber or amountPaid is not correct";
        }
    }

    //CHECK BILL_ID FOR GETTING CONTROL NUMBER
    public String check_bill_id(String db_bill_id, String received_bill_id) {
        logger.info("db id :" + db_bill_id);
        logger.info("rc id :" + received_bill_id);
        if (db_bill_id.equalsIgnoreCase(received_bill_id)) {
            return "billId exits";
        } else {
            return "billId does not exist";
        }
    }

    //FUNCTION TO ADD DATA TO THE LOG DATABASE
    private String add_log_information(String response_id, String message, String response_type) {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return feedClient.add_gep_response(response_id, message, response_type);
    }

    //CHECK CONTROL NUMBER
    public String check_control_number(String db_control_number, String received_control_number) {
        logger.info("db number :" + db_control_number);
        logger.info("rc number :" + received_control_number);
        if (db_control_number.equalsIgnoreCase(received_control_number)) {
            return "control number exits";
        } else {
            return "control number does not exist";
        }
    }
}