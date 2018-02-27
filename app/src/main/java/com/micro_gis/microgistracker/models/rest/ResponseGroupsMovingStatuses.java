package com.micro_gis.microgistracker.models.rest;

/**
 * Created by postp on 25.02.2018.
 */

public enum ResponseGroupsMovingStatuses {

    SUCCESS("SUCCESS"),
    WARNING("WARNING"),
    FORBIDDEN("FORBIDDEN"),
    ERROR("ERROR"),
    KEY_LIFECYCLE_RANGE_OUT("KEY_LIFECYCLE_RANGE_OUT"),
    KEY_LEFT("KEY_LEFT"),
    TARGET_LEFT("TARGET_LEFT"),
    ACCOUNT_ID_IS_NOT_VALID("ACCOUNT_ID_IS_NOT_VALID"),
    DEVICE_ID_IS_NOT_VALID("DEVICE_ID_IS_NOT_VALID"),
    WARNING_TEMPORARILY_SUSPENDED("has been temporarily suspended due to debts"),
    WARNING_KEY_HAS_NOT_ACCESS("This key have not access permission to account"),
    WARNING_HAVE_NOT_GROUP("have not group"),
    WARNING_DOES_NOT_HAVE_ACCESS_TO_THE_DEVICE("does not have access to the device with id");

    private final String status;

    ResponseGroupsMovingStatuses(final String status){
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
