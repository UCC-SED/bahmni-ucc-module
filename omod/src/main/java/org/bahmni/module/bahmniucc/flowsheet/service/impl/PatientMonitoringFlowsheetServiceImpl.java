package org.bahmni.module.bahmniucc.flowsheet.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.bahmni.module.bahmnicore.dao.ObsDao;
import org.bahmni.module.bahmnicore.dao.OrderDao;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.BahmniPatientProgram;
import org.bahmni.module.bahmnicore.model.bahmniPatientProgram.PatientProgramAttribute;
import org.bahmni.module.bahmnicore.service.BahmniConceptService;
import org.bahmni.module.bahmniucc.UCCModuleConstants;
import org.bahmni.module.bahmniucc.flowsheet.model.FlowsheetAttribute;
import org.bahmni.module.bahmniucc.flowsheet.service.PatientMonitoringFlowsheetService;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

import static org.bahmni.module.bahmniucc.UCCModuleConstants.PROGRAM_ATTRIBUTE_ID_NO;

@Service
public class PatientMonitoringFlowsheetServiceImpl implements PatientMonitoringFlowsheetService {

    private OrderDao orderDao;
    private ObsDao obsDao;
    private BahmniConceptService bahmniConceptService;



    @Autowired
    public PatientMonitoringFlowsheetServiceImpl(OrderDao orderDao, ObsDao obsDao, BahmniConceptService bahmniConceptService) {
        this.orderDao = orderDao;
        this.obsDao = obsDao;
        this.bahmniConceptService = bahmniConceptService;
    }



    @Override
    public FlowsheetAttribute getFlowsheetAttributesForPatientProgram(BahmniPatientProgram bahmniPatientProgram, PatientIdentifierType primaryIdentifierType, OrderType orderType, Set<Concept> concepts) {
        FlowsheetAttribute flowsheetAttribute = new FlowsheetAttribute();

        Collection<PatientProgramAttribute> activeAttributes = bahmniPatientProgram.getActiveAttributes();

        String programID=null;
        String hivStatus=null;
        for (PatientProgramAttribute patientProgramAttribute : activeAttributes)
        {
            if (PROGRAM_ATTRIBUTE_ID_NO.equals(patientProgramAttribute.getAttributeType().getName())) {
                programID= (String) patientProgramAttribute.getValue();
            }else if(UCCModuleConstants.PROGRAM_ATTRIBUTE_HIV_STATUS.equals(patientProgramAttribute.getAttributeType().getName()))
            {
              Concept  hivStatusConcept = (Concept) patientProgramAttribute.getValue();

                List<Obs> hivStatusConceptObs = obsDao.getObsByPatientProgramUuidAndConceptNames(bahmniPatientProgram.getUuid(), Arrays.asList(hivStatusConcept.getName().getName()), null, null, null, null);

                if (CollectionUtils.isNotEmpty(hivStatusConceptObs)) {
                    hivStatus = hivStatusConceptObs.get(0).getValueCoded().getName().getName();
                }
            }

        }

        List<Obs> startDateConceptObs = obsDao.getObsByPatientProgramUuidAndConceptNames(bahmniPatientProgram.getUuid(), Arrays.asList(UCCModuleConstants.TI_TREATMENT_START_DATE), null, null, null, null);
        Date startDate = null;
        if (CollectionUtils.isNotEmpty(startDateConceptObs)) {
            startDate = startDateConceptObs.get(0).getValueDate();
        }
        Date newDrugTreatmentStartDate = getNewDrugTreatmentStartDate(bahmniPatientProgram.getUuid(), orderType, concepts);
        List<Obs> consentForEndTbStudyObs = obsDao.getObsByPatientProgramUuidAndConceptNames(bahmniPatientProgram.getUuid(), Arrays.asList(UCCModuleConstants.FSN_TI_ENDTB_STUDY_CONSENT_QUESTION), null, null, null, null);
        String consentForEndTbStudy = null;

        if (CollectionUtils.isNotEmpty(consentForEndTbStudyObs)) {
            consentForEndTbStudy = consentForEndTbStudyObs.get(0).getValueCoded().getShortNameInLocale(Context.getUserContext().getLocale()).getName();
        }

        List<Obs> hivSeroStatusObs = obsDao.getObsByPatientProgramUuidAndConceptNames(bahmniPatientProgram.getUuid(), Arrays.asList(UCCModuleConstants.BASLINE_HIV_SEROSTATUS_RESULT, UCCModuleConstants.LAB_HIV_TEST_RESULT), null, null, null, null);
   /*     String hivStatus = null;

        if (CollectionUtils.isNotEmpty(hivSeroStatusObs)) {
            hivStatus = hivSeroStatusObs.get(0).getValueCoded().getName().getName();
        }*/

        String baselineXRayStatus = null;

        startDate=bahmniPatientProgram.getDateEnrolled();

        if (startDate != null) {
            Date minDate = DateUtils.addDays(startDate, -90);
            Date maxDate = DateUtils.addDays(startDate, 30);
            List<Obs> baslineXrayObs = obsDao.getObsByPatientProgramUuidAndConceptNames(bahmniPatientProgram.getUuid(), Arrays.asList(UCCModuleConstants.XRAY_EXTENT_OF_DISEASE), null, null, null, null);
            baselineXRayStatus = isObsDatePresentWithinDateRange(minDate, maxDate, baslineXrayObs);
        }

        flowsheetAttribute.setNewDrugTreatmentStartDate(newDrugTreatmentStartDate);
        flowsheetAttribute.setMdrtbTreatmentStartDate(bahmniPatientProgram.getDateEnrolled());
        flowsheetAttribute.setTreatmentRegistrationNumber(getProgramAttribute(bahmniPatientProgram, UCCModuleConstants.PROGRAM_ATTRIBUTE_REG_NO));
        //flowsheetAttribute.setPatientEMRID(bahmniPatientProgram.getPatient().getPatientIdentifier(primaryIdentifierType).getIdentifier());
        flowsheetAttribute.setPatientEMRID(programID);
        flowsheetAttribute.setConsentForEndtbStudy(consentForEndTbStudy);
        flowsheetAttribute.setHivStatus(hivStatus);
        flowsheetAttribute.setBaselineXRayStatus(baselineXRayStatus);
        return flowsheetAttribute;
    }

    private String isObsDatePresentWithinDateRange(Date minDate, Date maxDate, List<Obs> baslineXrayObs) {
        for (Obs obs : baslineXrayObs) {
            Date obsDate = obs.getObsDatetime();
            if (obsDate.equals(minDate) || obsDate.equals(maxDate)) {
                return "Yes";
            }
            if (obsDate.after(minDate) && obsDate.before(maxDate)) {
                return "Yes";
            }
        }
        return "No";
    }

    @Override
    public Date getStartDateForDrugConcepts(String patientProgramUuid, Set<String> drugConcepts, OrderType orderType) {
        return getNewDrugTreatmentStartDate(patientProgramUuid, orderType, getConceptObjects(drugConcepts));
    }



    private Set<Concept> getConceptObjects(Set<String> conceptNames) {
        Set<Concept> conceptsList = new HashSet<>();
        for (String concept : conceptNames) {
            conceptsList.add(bahmniConceptService.getConceptByFullySpecifiedName(concept));
        }
        return conceptsList;
    }

    private Date getNewDrugTreatmentStartDate(String patientProgramUuid, OrderType orderType, Set<Concept> concepts) {
        List<Order> orders = orderDao.getOrdersByPatientProgram(patientProgramUuid, orderType, concepts);
        if (orders.size() > 0) {
            Order firstOrder = orders.get(0);
            Date newDrugTreatmentStartDate = firstOrder.getScheduledDate() != null ? firstOrder.getScheduledDate() : firstOrder.getDateActivated();
            for (Order order : orders) {
                Date toCompare = order.getScheduledDate() != null ? order.getScheduledDate() : order.getDateActivated();
                if (newDrugTreatmentStartDate.compareTo(toCompare) > 0) {
                    newDrugTreatmentStartDate = toCompare;
                }
            }
            return newDrugTreatmentStartDate;
        }
        return null;
    }

    private String getProgramAttribute(BahmniPatientProgram bahmniPatientProgram, String attribute) {
        for (PatientProgramAttribute patientProgramAttribute : bahmniPatientProgram.getActiveAttributes()) {
            if (patientProgramAttribute.getAttributeType().getName().equals(attribute))
                return patientProgramAttribute.getValueReference();
        }
        return "";
    }

}
