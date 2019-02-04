package org.bahmni.module.bahmniucc.model;

/**
 * Created by ucc-ian on 12/Oct/2017.
 */
public class HackItem implements  java.io.Serializable {

    private static final long serialVersionUID = 2063860525496931568L;


    public String getVisit_type() {
        return visit_type;
    }

    public void setVisit_type(String visit_type) {
        this.visit_type = visit_type;
    }

    private String visit_type;

}