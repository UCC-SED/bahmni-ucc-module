package org.bahmni.module.bahmniucc.patient.validations;

import org.openmrs.Visit;

public interface UCCValidationRules {
    public void execute();
    public void sendConsultationOrder();
}
