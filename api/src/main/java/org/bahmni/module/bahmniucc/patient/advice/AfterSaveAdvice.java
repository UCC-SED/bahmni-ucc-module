package org.bahmni.module.bahmniucc.patient.advice;

import org.apache.log4j.Logger;
import org.bahmni.module.bahmniucc.patient.validations.PatientConsultationFeeOrder;
import org.bahmni.module.bahmniucc.patient.validations.UCCValidationRules;
import org.openmrs.Visit;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;


/**
 * Created by ucc-ian on 28/Aug/2017.
 */


public class AfterSaveAdvice implements AfterReturningAdvice {
    private Logger logger = Logger.getLogger(getClass());
    private String SAVE_VISIT_METHOD_TO_INTERCEPT = "saveVisit";

    @Override
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {

     /*  if (method.getName().equalsIgnoreCase(SAVE_VISIT_METHOD_TO_INTERCEPT)) {
            Visit visit = (Visit) objects[0];
            if (visit.getStopDatetime() == null && visit.getEncounters().size() == 1) {
                logger.info("Sending Consultation Fee");
                UCCValidationRules patientConsultationFeeOrder = new PatientConsultationFeeOrder(visit);
                patientConsultationFeeOrder.sendConsultationOrder();
            }
        }*/
    }


}
