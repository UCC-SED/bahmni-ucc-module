package org.bahmni.module.bahmniucc.api;


import org.apache.log4j.Logger;
import org.apache.ws.commons.util.Base64;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.json.JSONObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/billing")
public class SendBillingRequestWebController {
    String GEPG_URL = "http://154.118.230.18:80/api/bill/sigqrequest";
    String returnedValue;
    private Logger logger = Logger.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET, value = "sendBillRequest")
    @ResponseBody
    public String sendBillRe(@RequestParam("saleOrderID") String saleOrderID, @RequestParam("amount") float amount,
                             @RequestParam("identifier") String identifier, @RequestParam("fullName") String fullName,
                             @RequestParam("BillGenBy") String BillGenBy){

        //get current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String genDate = dtf.format(localDate);

        //expire in 5 days
        String expirDate =dtf.format(localDate.now().plusDays(5));

        String billItems=billItems( saleOrderID);
        String ret = sendBill(saleOrderID,amount,expirDate,genDate,identifier,fullName,BillGenBy,billItems);
        logger.info("GePG feedback " + ret);

        String resp = getRespValues( ret);
        logger.info("GePG checked feedback " + resp);
        if (resp.contains("success")){
            DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
            feedClient.add_cancel_status(saleOrderID, "pending");
            JSONObject object = new JSONObject();
            object.put("status","success");
            object.put("message","success request control Num");
            return object.toString();
        }
        else {
            JSONObject object = new JSONObject();
            object.put("status","failed");
            object.put("message","failed to request");
            return object.toString();
        }

    }


    

    public String billItems(String orderID){

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        feedClient.billItems(orderID);

        return  feedClient.billItems(orderID);
    }

    @RequestMapping(method = RequestMethod.GET, value = "cancelBillRequest")
    @ResponseBody
    public String cancelBillRequest(@RequestParam("BillId") String BillId){

        logger.info("GePG CANCEL: " + BillId );

        String feedback =  cancelBillRequestProcess( BillId);
        String resp =  getCancelValues( feedback);
        if (resp.contains("success")){
            JSONObject object = new JSONObject();
            object.put("status","success");
            object.put("message","success cancel bill");
            return object.toString();
        }
        else {
            JSONObject object = new JSONObject();
            object.put("status","failed");
            object.put("message","failed to cancel bill");
            return object.toString();
        }
    }


    public String cancelBillRequestProcess(String BillId){
        try {
            String Gepg_Com = "default.sp.in";
            String Gepg_Code = "SP222";
            StringBuffer content = new StringBuffer();
            URL url = new URL(this.GEPG_URL);
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

            logger.info("GePG message " + msg);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String str;
            while((str = in.readLine()) != null) {
                content.append(str);
            }

            in.close();
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }



    }

    public SendBillingRequestWebController() {
    }

    public String sendBill(String saleOrderID,float amount,String expirDate,String genDate,
                           String identifier,String fullName,String BillGenBy,String billItems) {
        String order_sumId = null;
        try {
            String Gepg_Com = "default.sp.in";
            String Gepg_Code = "SP222";
            StringBuffer content = new StringBuffer();
            URL url = new URL(this.GEPG_URL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Gepg-Com", "default.sp.in");
            conn.setRequestProperty("Gepg-Code", "SP222");
            conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            String msg = "<gepgBillSubReq>" +
                    "<BillHdr>" +
                    "<SpCode>SP222</SpCode>" +
                    "<RtrRespFlg>true</RtrRespFlg>" +
                    "</BillHdr>" +
                    "<BillTrxInf> " +
                    "<BillId>"+ saleOrderID +"</BillId>" +
                    "<SubSpCode>1001</SubSpCode>" +
                    "<SpSysId>TMH001</SpSysId>" +
                    "<BillAmt>"+ amount  +"</BillAmt>" +
                    "<MiscAmt>0</MiscAmt>" +
                    "<BillExprDt>"+ expirDate +"</BillExprDt>" +
                    "<PyrId>"+ identifier +"</PyrId>" +
                    "<PyrName>"+ fullName +"</PyrName>" +
                    "<BillDesc>Property Rate</BillDesc>" +
                    "<BillGenDt>"+ genDate +"</BillGenDt>" +
                    "<BillGenBy>"+  BillGenBy +"</BillGenBy>" +
                    "<BillApprBy>"+  BillGenBy +"</BillApprBy>" +
                    "<PyrCellNum>255717035833</PyrCellNum>" +
                    "<PyrEmail/>" +
                    "<Ccy>TZS</Ccy>" +
                    "<BillEqvAmt>"+ amount  +"</BillEqvAmt>" +
                    "<RemFlag>false</RemFlag>" +
                    "<BillPayOpt>1</BillPayOpt>" +
                    "<BillItems>" +
                    billItems +
                    "</BillItems>" +
                    "</BillTrxInf>" +
                    "</gepgBillSubReq>";

            logger.info("GePG load private key ");
            byte[] signz= sign(msg, "/opt/openmrs/keys/private_key.pem");

            String enMsg = "<Gepg>"+msg+"<gepgSignature>"+Base64.encode(signz)+"</gepgSignature></Gepg>";
            logger.info("GePG message 22" + enMsg);
            wr.writeBytes(enMsg);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String str;
            while((str = in.readLine()) != null) {
                content.append(str);
            }

            in.close();
            this.returnedValue = content.toString();
        } catch (Exception var11) {
            var11.printStackTrace();
            return var11.getMessage();
        }

        return this.returnedValue;
    }
    public byte[] sign(String data, String fileName) throws InvalidKeyException, Exception{
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(loadPrivateKey( fileName));
        rsa.update(data.getBytes());
        return rsa.sign();
    }
    //get private key	.pem
    public PrivateKey loadPrivateKey(String fileName)
            throws IOException, GeneralSecurityException {
        PrivateKey key = null;
        InputStream is = null;
        logger.info("GePG filename 1" + fileName);

        try {
            //is = fileName.getClass().getResourceAsStream( fileName);
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder builder = new StringBuilder();
            boolean inKey = false;
            logger.info("GePG filename " + fileName);
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (!inKey) {
                    if (line.startsWith("-----BEGIN ") &&
                            line.endsWith(" PRIVATE KEY-----")) {
                        inKey = true;
                    }
                    continue;
                }
                else {
                    if (line.startsWith("-----END ") &&
                            line.endsWith(" PRIVATE KEY-----")) {
                        inKey = false;
                        break;
                    }
                    builder.append(line);
                }
            }
            //
            byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            key = kf.generatePrivate(keySpec);
            logger.info("GePG key " + key.toString());
        }catch (Exception var11){
            var11.printStackTrace();
        }

        finally {
            //closeSilent(is);
        }
        return key;
    }

    public String getRespValues(String feedback){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setEncoding("UTF-8");
            src.setCharacterStream(new StringReader(feedback));
            logger.info("GePG feedback inner " + feedback);
            Document doc = builder.parse(src);
            String TrxStsCode = doc.getElementsByTagName("TrxStsCode").item(0).getTextContent();
            if(TrxStsCode.equalsIgnoreCase("7101")){
                return "success";
            }else{

                return "failed";
            }

        }catch (Exception e){
            logger.error("req bill except: "+e.getMessage());
            return  e.getMessage();
        }


    }

    public String getCancelValues(String feedback){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource src = new InputSource();
            src.setEncoding("UTF-8");
            src.setCharacterStream(new StringReader(feedback));
            logger.error("cancell inner: "+feedback);
            Document doc = builder.parse(src);
            //String BillId = doc.getElementsByTagName("BillId").item(0).getTextContent();
            String TrxSts = doc.getElementsByTagName("TrxStsCode").item(0).getTextContent();
            if(TrxSts.equalsIgnoreCase("7101")){
                DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
                //feedClient.processCtrlNumber(BillId, "");

                return "success";
            }else{

                return "failed";
            }



        }catch (Exception e){
            logger.error("cancell bill: "+e.getMessage());
            return  e.getMessage();
        }


    }


}