package org.bahmni.module.bahmniucc.model;

/**
 * Created by ucc-ian on 09/Oct/2017.
 */
public class MonitorItem implements  java.io.Serializable {

private static final long serialVersionUID = 2063860525096931568L;

private Integer value;
private String indicator;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }


    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }
}
