package org.bahmni.module.bahmniucc.api;


import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.controller.TribeController;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/tribe")
public class TribeWebController extends BaseRestController {

    private Logger logger = Logger.getLogger(getClass());
    private TribeController tribeController = TribeController.getInstance();

    @RequestMapping(method = RequestMethod.GET, value = "searchNames")
    @ResponseBody
    public String searchTibeNames(@RequestParam("tribeName") String tribeName) throws Exception {

        String tribes = tribeController.getTribeNames(tribeName);
        return tribes;
    }





}
