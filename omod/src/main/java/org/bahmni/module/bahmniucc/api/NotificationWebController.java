package org.bahmni.module.bahmniucc.api;

import org.bahmni.module.bahmniucc.controller.NotificationController;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ucc-ian on 15/Nov/2017.
 */

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/emr")
public class NotificationWebController {

    private NotificationController notificationController = new NotificationController();

    @RequestMapping(method = RequestMethod.GET, value = "getNotifications")
    @ResponseBody
    public String getNotifications(@RequestParam("patientUuid") String patientUuid, @RequestParam("visitUuid") String visitUuid) throws Exception {

        return notificationController.getNotificationResultList(patientUuid, visitUuid);

    }

}
