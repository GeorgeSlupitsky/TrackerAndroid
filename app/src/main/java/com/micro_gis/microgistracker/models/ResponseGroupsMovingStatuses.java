package com.micro_gis.microgistracker.models;

/**
 * Created by postp on 25.02.2018.
 */

public enum ResponseGroupsMovingStatuses {

    SUCCESS("SUCCESS"),
    WARNING("WARNING");

    private final String status;

    ResponseGroupsMovingStatuses(final String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
