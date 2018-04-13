package org.bahmni.module.bahmniucc.controller;

import org.bahmni.module.bahmniucc.client.DebtClient;
import org.bahmni.module.bahmniucc.client.OpenErpPatientFeedClient;
import org.openmrs.api.context.Context;

public class DoctorRoomController {
    private static DoctorRoomController roomInstance = new DoctorRoomController();
    public static DoctorRoomController getRoomInstance() {
        return roomInstance;
    }

    private DoctorRoomController() {

    }

    public String updateRoom(String room, String patientUuid){
        DebtClient feedClient = Context.getService(OpenErpPatientFeedClient.class);
        String update = feedClient.updateRoom(room,patientUuid);
        return update;
    }

}
