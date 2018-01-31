package org.bahmni.module.bahmniucc.patient.validations;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.bahmni.module.bahmniucc.util.OpenERPUtils;
import org.openmrs.Visit;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import java.net.MalformedURLException;

/**
 * Created by ucc-ian on 28/Aug/2017.
 */
public class PatientConsultationFeeOrder implements UCCValidationRules {
    private Logger logger = Logger.getLogger(getClass());

    OpenERPUtils util = new OpenERPUtils();

    @ManyToOne(cascade = CascadeType.ALL)
    private Visit visit;

    public PatientConsultationFeeOrder(Visit visit) {
        this.visit = visit;
    }

    @Override
    public void execute() {

    }

    @Override
    public void sendConsultationOrder() {


        try {
            Object loginID=util.login();

            int customerid = util.findCustomers((int) loginID,visit.getPatient().getPatientIdentifier().getIdentifier());

            int findSaleOrderIdsForCustomer = util.findSaleOrderIdsForCustomer((int) loginID, customerid);

            logger.info("Sale Order " + findSaleOrderIdsForCustomer);

            util.insertSaleOrderLine((int) loginID, findSaleOrderIdsForCustomer);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }

    }


}
