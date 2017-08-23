package org.bahmni.module.bahmniucc;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bahmni.module.bahmnicore.dao.ObsDao;
import org.bahmni.module.bahmnicore.dao.PatientDao;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.module.bahmniemrapi.encountertransaction.command.EncounterDataPreSaveCommand;
import org.openmrs.module.bahmniemrapi.encountertransaction.contract.BahmniEncounterTransaction;
import org.openmrs.module.bahmniemrapi.encountertransaction.contract.BahmniObservation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.bahmni.module.bahmniucc.UCCModuleConstants.*;

@Component
public class ConditionsForFilledForms implements EncounterDataPreSaveCommand {

    private static final Log log = LogFactory.getLog(ConditionsForFilledForms.class);

    private BahmniObservation returnObs;
    private ObsDao obsDao;


    @Autowired
    public ConditionsForFilledForms(ObsDao obsDao) {
        this.obsDao = obsDao;
    }

    @Override
    public BahmniEncounterTransaction update(BahmniEncounterTransaction bahmniEncounterTransaction) {
        whoStageValidation(bahmniEncounterTransaction, CTC_VISIT_FORM, WHO_STAGE_ID);
        pregnancyStatusValidation(bahmniEncounterTransaction, CTC_VISIT_FORM, PREGNANCY_STATUS_ID);

        return bahmniEncounterTransaction;
    }


    private void pregnancyStatusValidation(BahmniEncounterTransaction bahmniEncounterTransaction,
                                           String templateConceptName,
                                           String whoStageConceptName) {
        boolean previousStage;
        boolean currentStage;
        for (BahmniObservation observation : bahmniEncounterTransaction.getObservations()) {
            if (templateConceptName.equals(observation.getConcept().getName())) {

                Object latestValue = getLatestValue(bahmniEncounterTransaction.getPatientUuid(), PREGNANCY_STATUS_ID, "Boolean");

                if (latestValue != null) {
                    previousStage = (boolean) latestValue;
                    globalGetObservationFor(observation.getGroupMembers(), whoStageConceptName);
                    currentStage = (Boolean) returnObs.getValue();

                    if (!pregnancyStatusValidator(previousStage, currentStage) && !rulesForFilledForms(bahmniEncounterTransaction, PREGNANCY_FORM_NAME)) {
                        throw new RuntimeException("YOU HAVE TO FILL PREGNANCY DELIVERY FORM");
                    }
                }


            }
        }
    }

    private void whoStageValidation(BahmniEncounterTransaction bahmniEncounterTransaction,
                                    String templateConceptName,
                                    String whoStageConceptName) {
        String previousStage;
        String currentStage;
        for (BahmniObservation observation : bahmniEncounterTransaction.getObservations()) {
            if (templateConceptName.equals(observation.getConcept().getName())) {

                Object latestValue = getLatestValue(bahmniEncounterTransaction.getPatientUuid(), WHO_STAGE_ID, "Text");

                if (latestValue != null) {
                    previousStage = (String) latestValue;
                    globalGetObservationFor(observation.getGroupMembers(), whoStageConceptName);
                    LinkedHashMap hashmapValue = (LinkedHashMap) returnObs.getValue();

                    currentStage = (String) hashmapValue.get("value");


                    if (!whoStageValidator(previousStage, currentStage)) {
                        throw new RuntimeException("WHO STAGE IS INVALID");
                    }
                }


            }
        }
    }


    private boolean pregnancyStatusValidator(boolean previousValue, boolean currentValue) {

        if (previousValue && !currentValue) {
            return false;
        }

        return true;
    }


    private boolean whoStageValidator(String previousWHOStage, String currentWHOStage) {

        int previousStage = Integer.parseInt(previousWHOStage.trim().replaceAll("Clinical Stage ", ""));

        int currentStage = Integer.parseInt(currentWHOStage.trim().replaceAll("Clinical Stage ", ""));
        if (currentStage < previousStage) {
            return false;
        }

        return true;
    }

    private Object getLatestValue(String patientUuid, String conceptName, String valueType) {
        List<Obs> obsList = obsDao.getLatestObsFor(patientUuid, conceptName, 10);
        if (obsList.size() < 1) {
            return null;
        }
        for (int x = 0; x < obsList.size(); x++) {

            switch (valueType) {
                case "Coded":
                    return obsList.get(x).getValueCoded().getName().getName();

                case "Int":
                    return obsList.get(x).getValueNumeric();

                case "Text":
                    return obsList.get(x).getValueText();

                case "Boolean":
                    return obsList.get(x).getValueAsBoolean();
            }


        }

        return null;

    }


    private boolean rulesForFilledForms(BahmniEncounterTransaction bahmniEncounterTransaction, String fornName) {
        HashMap<String, List<BahmniObservation>> mapOfTemplateForms = new HashMap();
        for (BahmniObservation observation : bahmniEncounterTransaction.getObservations()) {
            if (mapOfTemplateForms.containsKey(observation.getConcept().getName())) {
                mapOfTemplateForms.get(observation.getConcept().getName()).add(observation);
            } else
                mapOfTemplateForms.put(observation.getConcept().getName(), new ArrayList<>(Arrays.asList(observation)));
        }
        for (Map.Entry<String, List<BahmniObservation>> templateObs : mapOfTemplateForms.entrySet()) {
            String conceptName = templateObs.getKey();

            if (conceptName.equals(fornName)) {
                return true;
            }


        }
        return false;
    }


    private void globalGetObservationFor(Collection<BahmniObservation> groupMembers, String conceptName) {
        for (BahmniObservation groupMember : groupMembers) {
            if (groupMember.getGroupMembers().size() > 0) {
                globalGetObservationFor(groupMember.getGroupMembers(), conceptName);
            } else if (conceptName.equals(groupMember.getConcept().getName())) {
                returnObs = groupMember;

            } else {
            }
        }

    }


    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }
}
