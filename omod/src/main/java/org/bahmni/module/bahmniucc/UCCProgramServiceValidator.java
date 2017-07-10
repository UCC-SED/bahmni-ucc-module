package org.bahmni.module.bahmniucc;

import org.apache.commons.collections.CollectionUtils;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.PatientProgramAttribute;
import org.bahmni.module.bahmnicore.service.BahmniProgramServiceValidator;
import org.bahmni.module.bahmnicore.service.BahmniProgramWorkflowService;
import org.openmrs.PatientProgram;
import org.openmrs.api.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.bahmni.module.bahmniucc.UCCModuleConstants.PROGRAM_ATTRIBUTE_ID_NO;

@Component
public class UCCProgramServiceValidator implements BahmniProgramServiceValidator {
    @Autowired
    private BahmniProgramWorkflowService bahmniProgramWorkflowService;

    @Autowired
    public UCCProgramServiceValidator(BahmniProgramWorkflowService bahmniProgramWorkflowService) {
        this.bahmniProgramWorkflowService = bahmniProgramWorkflowService;
    }

    public UCCProgramServiceValidator(){}

    @Override
    public void validate(PatientProgram patientProgram) throws APIException {
        for (PatientProgramAttribute patientProgramAttribute :
                ((BahmniPatientProgram) patientProgram).getAttributes()) {
            if (PROGRAM_ATTRIBUTE_ID_NO.equals(patientProgramAttribute.getAttributeType().getName())) {
                List<BahmniPatientProgram> bahmniPatientPrograms = bahmniProgramWorkflowService.getPatientProgramByAttributeNameAndValue(PROGRAM_ATTRIBUTE_ID_NO, (String) patientProgramAttribute.getValue());
                if (isPatientProgramExists(patientProgram, bahmniPatientPrograms)) {
                    throw new APIException("ID Number is already in use");
                }
            }
        }
    }

    private boolean isPatientProgramExists(PatientProgram patientProgram, List<BahmniPatientProgram> bahmniPatientPrograms) {

        if(CollectionUtils.isNotEmpty(bahmniPatientPrograms)) {
            for(BahmniPatientProgram bahmniPatientProgram : bahmniPatientPrograms) {
                if(!bahmniPatientProgram.getUuid().equals(patientProgram.getUuid())) {
                    return true;
                }
            }
        }
        return false;
    }
}
