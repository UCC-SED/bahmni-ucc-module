package org.bahmni.module.bahmniucc;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bahmni.module.bahmnicore.dao.ObsDao;
import org.openmrs.*;
import org.openmrs.api.EncounterService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.PatientDAO;
import org.openmrs.module.bahmniemrapi.encountertransaction.command.EncounterDataPreSaveCommand;
import org.openmrs.module.bahmniemrapi.encountertransaction.contract.BahmniEncounterTransaction;
import org.openmrs.module.bahmniemrapi.encountertransaction.contract.BahmniObservation;
import org.openmrs.module.episodes.Episode;
import org.openmrs.module.episodes.dao.impl.EpisodeDAO;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.bahmni.module.bahmniucc.UCCModuleConstants.*;


@Component
public class RulesForFormFilled implements EncounterDataPreSaveCommand {

    private ObsDao obsDao;
    private EpisodeDAO episodeDAO;
    private PatientDAO patientDao;
    private EncounterService encounterService;
    private PatientProgram patientProgram;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
    private BahmniObservation returnObs;
    private static final Log log = LogFactory.getLog(ConditionsForFilledForms.class);

    @Autowired
    public RulesForFormFilled(ObsDao obsDao, EpisodeDAO episodeDAO, EncounterService encounterService, PatientDAO patientDao) {
        this.obsDao = obsDao;
        this.episodeDAO = episodeDAO;
        this.encounterService = encounterService;
        this.patientDao = patientDao;

    }

    @Override
    public BahmniEncounterTransaction update(BahmniEncounterTransaction bahmniEncounterTransaction) {
        //rulesForFilledForms(bahmniEncounterTransaction);
        return bahmniEncounterTransaction;
    }

    private void rulesForFilledForms(BahmniEncounterTransaction bahmniEncounterTransaction) {
        HashMap<String, List<BahmniObservation>> mapOfTemplateForms = new HashMap();
        for (BahmniObservation observation : bahmniEncounterTransaction.getObservations()) {
            if (mapOfTemplateForms.containsKey(observation.getConcept().getName())) {
                mapOfTemplateForms.get(observation.getConcept().getName()).add(observation);
            } else
                mapOfTemplateForms.put(observation.getConcept().getName(), new ArrayList<>(Arrays.asList(observation)));
        }

        for (Map.Entry<String, List<BahmniObservation>> templateObs : mapOfTemplateForms.entrySet()) {
            String conceptName = templateObs.getKey();
            log.info("conceptName " + conceptName);
            switch (conceptName) {
                case CTC_VISIT_FORM:
                    if (bahmniEncounterTransaction.getPatientProgramUuid() == null) {

                        throw new RuntimeException("Patient is not enrolled in " + HIV_PROGRAM_NAME + "");
                    }

                case HIV_PROGRAM_POC:
                    if (bahmniEncounterTransaction.getPatientProgramUuid() == null) {
                        throw new RuntimeException("Patient is not enrolled in " + HIV_PROGRAM_NAME + "");
                    }

                case EXPOSED_INFANT_PROGRAM:
                    if (bahmniEncounterTransaction.getPatientProgramUuid() == null) {
                        throw new RuntimeException("Patient is not enrolled in " + EXPOSED_INFANT_PROGRAM_NAME + "");
                    }

                case TB_PROGRAM:
                    if (bahmniEncounterTransaction.getPatientProgramUuid() == null) {
                        throw new RuntimeException("Patient is not enrolled in " + TB_PROGRAM_NAME + "");
                    }
                case DEATH_REGISTRY_FORM_NAME:
                    getObsValue(bahmniEncounterTransaction.getObservations(), DEATH_REGISTRY_DEATH_DATE);
                    Patient pateint = patientDao.getPatientByUuid(bahmniEncounterTransaction.getPatientUuid());
                    Person person = pateint.getPerson();
                    person.setDead(true);
                   // person.setDeathDate(getDate(returnObs.getValue()));
                    VisitService visitService = Context.getVisitService();

                    List<Visit> openVisits = visitService.getVisits(null, null, null, null, null, null, null, null, null, false, false);
                    log.info("Visit size" + openVisits.size());
                    for (Visit openVisit : openVisits) {
                        log.info("Visit uuid" + openVisit.getUuid());
                        if(person.getPersonId()== openVisit.getPatient().getPerson().getPersonId()) {
                            visitService.endVisit(openVisit, new Date());
                            // visitService.voidVisit(openVisit,"Patient Is Dead");
                        }
                    }
                    case RCH_ENROLLMENT:
                        log.info("RCH ENROLLMENT");
                        getObsValue(bahmniEncounterTransaction.getObservations(), ANC_LAST_MP_DATE);

            }
        }
    }


    private void getObsValue(Collection<BahmniObservation> groupMembers, String conceptName) {
        for (BahmniObservation groupMember : groupMembers) {
            if (groupMember.getGroupMembers().size() > 0) {
                getObsValue(groupMember.getGroupMembers(), conceptName);
            } else if (conceptName.equals(groupMember.getConcept().getName())) {
                returnObs = groupMember;

            }
        }
    }

    private Date getDate(Object value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        try {
            return simpleDateFormat.parse((String) value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
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
