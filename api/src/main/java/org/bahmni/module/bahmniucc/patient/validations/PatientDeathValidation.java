package org.bahmni.module.bahmniucc.patient.validations;

import org.bahmni.module.bahmnicore.service.BahmniObsService;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bahmniemrapi.encountertransaction.contract.BahmniObservation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PatientDeathValidation implements UCCValidationRules {
    private Patient patient;

    public PatientDeathValidation(Patient patient) {
        this.patient = patient;
    }

    @Override
    public void execute() {
        VisitService visitService = Context.getVisitService();
        ConceptService conceptService = Context.getConceptService();
        Concept patientDateOfDeathConcept = conceptService.getConcept("Daftari la Kifo - Tarehe ya Kifo");

        List<Visit> allVisitForPatient = visitService.getVisits(null, Arrays.asList(patient), null, null, null, null, null, null, null, true, false);

        for (Visit visit: allVisitForPatient) {
            BahmniObsService bahmniObsService = Context.getService(BahmniObsService.class);
            Collection<BahmniObservation> bahmniObservations = bahmniObsService.getLatestObsByVisit(visit, Arrays.asList(patientDateOfDeathConcept), null, true);

            if (bahmniObservations.size() > 0) {
                throw new APIException("No activity will happen for a dead patient");
            }
        }
    }
}
