package org.bahmni.module.bahmniucc.api;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/Dhis_posting")
public class Dhis_IntergrationController {

    private Logger logger = Logger.getLogger(getClass());
    String returnedValue = null;
    String DHIS_URL = "https://dhis.hisptz.org/dev/api/dataValueSets";


    @RequestMapping(method = RequestMethod.GET, value = "postingData")
    @ResponseBody

    public String postingData(@RequestParam("report_name") String report_name,@RequestParam("period_type") String period_type, @RequestParam("time_period") String time_period, @RequestParam("sqlPath") String sqlPath) throws Exception {
        //get json of the specific report name
        JSONParser parser = new JSONParser();
        JSONObject jsonReports = (JSONObject) parser.parse(new FileReader("/var/www/bahmni_config/openmrs/apps/dhis_ucc/"+report_name+".json"));

        String sqlQuery = new String(Files.readAllBytes(Paths.get(sqlPath)));
        String startDate = null;
        logger.info("period_type :"+ period_type);

        if(period_type.equalsIgnoreCase("daily")){
            startDate =   time_period;
            sqlQuery = sqlQuery.replaceAll("#startDate#",startDate);
            sqlQuery = sqlQuery.replaceAll("#endDate#",startDate);

            //go get dta of this query
            List res = get_dbData(sqlQuery);
            String msg =null;
            String dhIsPeriod = null;
            if(res != null) {
                dhIsPeriod = startDate.replaceAll("-","");
                msg = createJson(res, jsonReports, dhIsPeriod);
                return sendToDHIS(msg);
            }

        }
        if(period_type.equalsIgnoreCase("monthly")){
            startDate =   time_period;

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate endDate = LocalDate.parse(startDate);
            endDate = endDate.withDayOfMonth(
                    endDate.getMonth().length(endDate.isLeapYear()));

            String endD = dtf.format(endDate);
            logger.info("query endDate:"+ endDate);
            sqlQuery = sqlQuery.replaceAll("#startDate#",startDate);
            sqlQuery = sqlQuery.replaceAll("#endDate#",endD);
            //go get dta of this query
            List res = get_dbData(sqlQuery);
            String msg =null;
            String dhIsPeriod = null;
            if(res != null) {
                LocalDate local = LocalDate.parse(startDate);
                int year  = local.getYear();
                int month = local.getMonthValue();
                String x = null;
                if(month < 10){
                   x = 0+""+month;

                }else{
                   x = ""+month;
                }

                dhIsPeriod = year+x;
                logger.info("dhIsPeriod :"+ dhIsPeriod);
                msg = createJson(res, jsonReports, dhIsPeriod);
                return sendToDHIS(msg);
            }

        }

        if(period_type.equalsIgnoreCase("Quarterly")){
            startDate =   time_period;
            Date startDateParsed=new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            String endDate = getLastDayOfQuarter(startDateParsed);

            String endD = endDate;
            logger.info("query endDate:"+ endDate);
            sqlQuery = sqlQuery.replaceAll("#startDate#",startDate);
            sqlQuery = sqlQuery.replaceAll("#endDate#",endD);
            //go get dta of this query
            List res = get_dbData(sqlQuery);
            String msg =null;
            String dhIsPeriod = null;
            if(res != null) {
                LocalDate local = LocalDate.parse(startDate);
                int year  = local.getYear();
                int quarter = (startDateParsed.getMonth() / 3) + 1;

                dhIsPeriod = year+"Q"+quarter;
                logger.info("dhIsPeriod :"+ dhIsPeriod);
                msg = createJson(res, jsonReports, dhIsPeriod);
                return sendToDHIS(msg);
            }

        }


        return null;
    }

    private static String getLastDayOfQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)/3 * 3 + 2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format1.format(cal.getTime());
        return formattedDate;
    }

    public List get_dbData(String qry){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        List results =feedClient.get_dbData(qry);

        return results;
    }

    public String createJson (List res, JSONObject jsonReports, String period){
        String orgUnit = (String)jsonReports.get("orgUnit");
        String dataSet = (String)jsonReports.get("dataSet");
        JSONArray dataValuesArray = (JSONArray) jsonReports.get("dataValues");

        //get current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String completeData = dtf.format(localDate);

        JSONObject json = new JSONObject();
        json.put("dataSet", dataSet);
        json.put("orgUnit", orgUnit);
        json.put("completeData", completeData);
        json.put("period", period);

        org.json.JSONArray array = new org.json.JSONArray();
        JSONObject item = new JSONObject();

        int rowCheck = 1;
        for(int i=0; i<res.size(); i++) {
            int ClmnCheck = 1;

            Object[] objArr = (Object[]) res.get(i);
            for(Object objz: objArr){
                Iterator it = dataValuesArray.iterator();
                if(objz == null || objz.equals("null")){
                    objz=0;
                }
                while (it.hasNext()) {
                    JSONObject obj = (JSONObject) it.next();
                    long row = (long) obj.get("row");
                    long clm = (long) obj.get("column");
                    if (rowCheck == row) {
                        if (ClmnCheck == clm) {
                            String categoryOptionCombo = (String) obj.get("categoryOptionCombo");
                            item.put("categoryOptionCombo", categoryOptionCombo);
                            String dataElement = (String) obj.get("dataElement");
                            item.put("dataElement", dataElement);
                            item.put("value", objz);
                            array.put(item);


                        }
                    }


                }
                ClmnCheck++;



            }

            rowCheck++;

        }
        json.put("dataValues",array);
        logger.info("DHIS 2"+json.toString());

        return json.toString();
    }

    public String sendToDHIS(String msg) {
        String order_sumId = null;
        try {
            String username ="integration";
            String password ="INTEGRATION@2018";

            StringBuffer content = new StringBuffer();
            URL url = new URL(this.DHIS_URL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            conn.setRequestProperty ("Authorization", basicAuth);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(msg);
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
        }

        return this.returnedValue;
    }


}
