package org.bahmni.module.bahmniucc.api;

import org.bahmni.module.bahmniucc.controller.ReceiveBillingRequestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/rest/v1/erp"})
public class ReceiveBillingRequestWebController {
    private ReceiveBillingRequestController r_bill = ReceiveBillingRequestController.getInstance();

    public ReceiveBillingRequestWebController() {
    }

    @RequestMapping(
            method = {RequestMethod.GET},
            value = {"names"}
    )
    @ResponseBody
    public String searchTibeNames() throws Exception {
        String names = this.r_bill.getNames();
        return names;
    }
}
