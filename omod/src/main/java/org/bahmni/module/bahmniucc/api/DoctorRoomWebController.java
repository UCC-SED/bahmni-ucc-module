package org.bahmni.module.bahmniucc.api;

import org.bahmni.module.bahmniucc.controller.DoctorRoomController;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/SaveDoctorRoom")
public class DoctorRoomWebController extends BaseRestController {
    private DoctorRoomController doctorRoomController =DoctorRoomController.getRoomInstance();

    @RequestMapping(method = RequestMethod.GET, value = "DoctorRoom")
    @ResponseBody
    public void updateRoom(@RequestParam("room") String room, @RequestParam("patientuuid") String patientUuid) throws Exception {

        String update =doctorRoomController.updateRoom(room,patientUuid);

    }

}
