package com.micro_gis.microgistracker;

/**
 * Created by User9 on 14.03.2018.
 */

public interface Communicator {

    void event (String account, String key, String id, Long dateFrom, Long dateTo, String duration);

}
