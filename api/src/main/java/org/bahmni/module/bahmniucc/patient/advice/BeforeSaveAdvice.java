package org.bahmni.module.bahmniucc.patient.advice;

import org.bahmni.module.bahmniucc.patient.validations.PatientDeathValidation;
import org.bahmni.module.bahmniucc.patient.validations.UCCValidationRules;
import org.openmrs.Patient;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class BeforeSaveAdvice implements MethodBeforeAdvice {

    private static final String SAVE_PATIENT_METHOD_TO_INTERCEPT = "savePatient";

    @Override
    public void before(Method method, Object[] objects, Object o) {
        if (method.getName().equalsIgnoreCase(SAVE_PATIENT_METHOD_TO_INTERCEPT)) {
            Patient patient = (Patient) objects[0];
            if (patient.getPersonId() != null) {
                UCCValidationRules patientDeathValidation = new PatientDeathValidation(patient);
                patientDeathValidation.execute();
            }
        }
    }
}
