package org.bahmni.module.bahmniucc.api;


import com.google.gson.Gson;
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
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/ledger_entry")
public class Ledger_EntryWebController {

    @RequestMapping(method = RequestMethod.GET, value = "insertLedger_entry")
    @ResponseBody
    public String insertLedger_entry(@RequestParam("item_id") String name, @RequestParam("LedgerEntryType") String LedgerEntryType,
                                  @RequestParam("quantity") Double quantity, @RequestParam("BatchNo") String BatchNo,
                                  @RequestParam("InvoiceNo") String InvoiceNo, @RequestParam("ExpiryDate") String ExpiryDate,
                                  @RequestParam("receiveDate") String receiveDate,@RequestParam("price_list_id") String price_list_id, @RequestParam("amount") float amount,@RequestParam("math") String math) throws Exception {

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return new Gson().toJson(feedClient.insertledger(name,LedgerEntryType,quantity,BatchNo,InvoiceNo,ExpiryDate,receiveDate, price_list_id,amount,math));
    }

    @RequestMapping(method = RequestMethod.GET, value = "selectLedger_entry")
    @ResponseBody
    public String selectLedger_entry(@RequestParam("item_name") String name, @RequestParam("BatchNo") String BatchNo,
                                     @RequestParam("InvoiceNo") String InvoiceNo) throws Exception {

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);

        return  new Gson().toJson( feedClient.selectLedger_entry(name,BatchNo,InvoiceNo));
    }

    @RequestMapping(method = RequestMethod.GET, value = "updateLedger_entry")
    @ResponseBody
    public String updateLedger_entry(@RequestParam("item_id") String name, @RequestParam("LedgerEntryType") String LedgerEntryType,
                                     @RequestParam("quantity") Double quantity, @RequestParam("BatchNo") String BatchNo,
                                     @RequestParam("InvoiceNo") String InvoiceNo, @RequestParam("ExpiryDate") String ExpiryDate,
                                     @RequestParam("receiveDate") String receiveDate,@RequestParam("price_list_id") String price_list_id, @RequestParam("amount") float amount,@RequestParam("math") String math,@RequestParam("ledgerEntryId") int ledger_entry_id) throws Exception {

        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return new Gson().toJson(feedClient.updateLedger(name,LedgerEntryType,quantity,BatchNo,InvoiceNo,ExpiryDate,receiveDate, price_list_id,amount,math,ledger_entry_id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "all")
    @ResponseBody
    public String getLedger_entry() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return  new Gson().toJson( feedClient.getLedger_entry());
    }

    @RequestMapping(method = RequestMethod.GET, value = "all_item")
    @ResponseBody
    public String get_all_ledger() throws Exception {
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        return  new Gson().toJson( feedClient.get_all_ledger());
    }

}
