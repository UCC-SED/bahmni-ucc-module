package org.bahmni.module.bahmniucc;

import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.PatientProgramAttribute;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.ProgramAttributeType;
import org.bahmni.module.bahmnicore.service.BahmniProgramWorkflowService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.PatientProgram;
import org.openmrs.api.APIException;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

public class UCCProgramServiceValidatorTest {
    @Mock
    private BahmniProgramWorkflowService bahmniProgramWorkflowService;
    private UCCProgramServiceValidator uccProgramServiceValidator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        uccProgramServiceValidator = new UCCProgramServiceValidator(bahmniProgramWorkflowService);
    }

    @Test(expected = APIException.class)
    public void shouldThrowExceptionWhenIDNumberIsAlreadyInUse() throws Exception {
        when(bahmniProgramWorkflowService.getPatientProgramByAttributeNameAndValue(any(String.class), any(String.class))).thenReturn(Arrays.asList((BahmniPatientProgram) getPatientProgram("ID Number", "123")));
        uccProgramServiceValidator.validate(getPatientProgram("ID Number", "123"));
    }

    private PatientProgram getPatientProgram(String key, String value) {
        ProgramAttributeType programAttributeType = new ProgramAttributeType();
        programAttributeType.setName(key);

        PatientProgramAttribute patientProgramAttribute = new PatientProgramAttribute();
        patientProgramAttribute.setValue(value);
        patientProgramAttribute.setAttributeType(programAttributeType);

        BahmniPatientProgram bahmniPatientProgram = new BahmniPatientProgram();
        bahmniPatientProgram.setAttribute(patientProgramAttribute);

        return bahmniPatientProgram;
    }
}