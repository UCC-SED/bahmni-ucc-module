package org.bahmni.module.bahmniucc.model;

public class location {
    private int location_id;
    private Integer  parent_location;
    public Integer getparent_location() {
        return parent_location;
    }

    public void setparent_location(Integer parent_location) {
        this.parent_location = parent_location;
    }

    public int getlocation_id() {
        return location_id;
    }

    public void setlocation_id(int location_id) {
        this.location_id = location_id;
    }

}
