package org.bahmni.module.bahmniucc;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.PatientProgramAttribute;
import org.bahmni.module.bahmnicore.service.BahmniProgramServiceValidator;
import org.bahmni.module.bahmnicore.service.BahmniProgramWorkflowService;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.api.APIException;
import org.openmrs.api.db.PatientDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.bahmni.module.bahmniucc.UCCModuleConstants.*;

@Component
public class UCCProgramServiceValidator implements BahmniProgramServiceValidator {
    @Autowired
    private BahmniProgramWorkflowService bahmniProgramWorkflowService;
    private PatientDAO patientDao;
    private  Log log = LogFactory.getLog(UCCProgramServiceValidator.class);

    @Autowired
    public UCCProgramServiceValidator(BahmniProgramWorkflowService bahmniProgramWorkflowService, PatientDAO patientDao) {
        this.bahmniProgramWorkflowService = bahmniProgramWorkflowService;
        this.patientDao = patientDao;
    }

    public UCCProgramServiceValidator() {
    }

    @Override
    public void validate(PatientProgram patientProgram) throws APIException {

        for (PatientProgramAttribute patientProgramAttribute : ((BahmniPatientProgram) patientProgram).getAttributes()) {
            if (PROGRAM_ATTRIBUTE_ID_NO.equals(patientProgramAttribute.getAttributeType().getName())) {
                List<BahmniPatientProgram> bahmniPatientPrograms = bahmniProgramWorkflowService.getPatientProgramByAttributeNameAndValue(PROGRAM_ATTRIBUTE_ID_NO, (String) patientProgramAttribute.getValue());
                if (isPatientProgramExists(patientProgram, bahmniPatientPrograms)) {
                    throw new APIException("ID Number is already in use");
                }
            }
        }


        Patient patient = ((BahmniPatientProgram) patientProgram).getPatient();
        if (((BahmniPatientProgram) patientProgram).getProgram().getProgramId() == EXPOSED_INFANT_PROGRAM_ID && patient.getPerson().getAge() > EXPOSED_INFANT_PROGRAM_MINIMUM_AGE) {
            throw new APIException("You can not Enroll a Patient who is more than years " + EXPOSED_INFANT_PROGRAM_MINIMUM_AGE + " old in Exposed Infant Program");
        }


        if (((BahmniPatientProgram) patientProgram).getProgram().getProgramId() == ANC_PROGRAM_ID
                && patient.getPerson().getGender().equalsIgnoreCase(MALE)   ) {
            throw new APIException("You can not Enroll a Male Patient or Age less than 10 years old in ANC Program");
        }else
        {
            log.info(((BahmniPatientProgram) patientProgram).getProgram().getProgramId());
            log.info(ANC_PROGRAM_ID);
            log.info(patient.getPerson().getAge() );
            log.info(PREGNANT_MINIMUM_AGE);
            log.info(patient.getPerson().getGender() );
            log.info(MALE);
        }
    }


    private void validateExposedInfantID(PatientProgram patientProgram, List<BahmniPatientProgram> bahmniPatientPrograms) {
        if (((BahmniPatientProgram) patientProgram).getProgram().getProgramId() == EXPOSED_INFANT_PROGRAM_ID) {
            {
                log.info("Exposed Infant Program");
                if (isProgramExists(patientProgram, bahmniPatientPrograms)) {
                    throw new APIException("Mother ID not Found");
                }
            }
        }

    }


    private boolean isProgramExists(PatientProgram patientProgram, List<BahmniPatientProgram> bahmniPatientPrograms) {
        String passedUuid = patientProgram.getUuid();

        String[] idSplit = passedUuid.split("-");

        log.info("Split ID " + idSplit[0]);

        if (CollectionUtils.isNotEmpty(bahmniPatientPrograms)) {
            for (BahmniPatientProgram bahmniPatientProgram : bahmniPatientPrograms) {
                if (idSplit[0].equals(patientProgram.getUuid())) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isPatientProgramExists(PatientProgram patientProgram, List<BahmniPatientProgram> bahmniPatientPrograms) {

        if (CollectionUtils.isNotEmpty(bahmniPatientPrograms)) {
            for (BahmniPatientProgram bahmniPatientProgram : bahmniPatientPrograms) {
                if (!bahmniPatientProgram.getUuid().equals(patientProgram.getUuid())) {
                    return true;
                }
            }
        }
        return false;
    }
}
