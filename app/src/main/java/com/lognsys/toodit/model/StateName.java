package com.lognsys.toodit.model;

/**
 * Created by admin on 24-02-2017.
 */

public class StateName {

    private String stateId;
    private String stateName;
    private String countryId;

    public StateName(String stateId, String stateName, String countryId) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.countryId = countryId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String toString() {
        return stateName;
    }
}
