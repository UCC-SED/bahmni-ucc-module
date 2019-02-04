package org.bahmni.module.bahmniucc.patient.validations;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.bahmni.module.bahmnicore.service.BahmniObsService;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.bahmniemrapi.encountertransaction.contract.BahmniObservation;


import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import static org.bahmni.module.bahmniucc.UCCModuleConstants.DEATH_REGISTRY_CONDITION;

public class PatientDeathValidation implements UCCValidationRules {
    private Logger logger = Logger.getLogger(getClass());
    @ManyToOne(cascade = CascadeType.ALL)
    private Patient patient;


    private static final Log log = LogFactory.getLog(PatientDeathValidation.class);

    public PatientDeathValidation(Patient patient) {
        this.patient = patient;
    }

    @Override
    public void execute() {
        VisitService visitService = Context.getVisitService();
        ConceptService conceptService = Context.getConceptService();
        Concept patientDateOfDeathConcept = conceptService.getConcept(DEATH_REGISTRY_CONDITION);

        List<Visit> allVisitForPatient = visitService.getVisits(null, Arrays.asList(patient), null, null, null, null, null, null, null, true, false);

        for (Visit visit : allVisitForPatient) {
            BahmniObsService bahmniObsService = Context.getService(BahmniObsService.class);
            Collection<BahmniObservation> bahmniObservations = bahmniObsService.getLatestObsByVisit(visit, Arrays.asList(patientDateOfDeathConcept), null, true);

            if (bahmniObservations.size() > 0) {
                for (BahmniObservation deathRegistConfirmationObs : bahmniObservations) {
                    log.info("Death Value " + deathRegistConfirmationObs.getValueAsString());
                    if (deathRegistConfirmationObs.getValueAsString().equalsIgnoreCase("Ndiyo") || deathRegistConfirmationObs.getValueAsString().equalsIgnoreCase("Haihusiki")) {
                        throw new APIException("This is a confirmed dead patient you can not edit or start visit");
                    }

                }
            }
        }
    }

    @Override
    public void sendConsultationOrder() {
        logger.info("Sending Consultation Fee");
    }


}
