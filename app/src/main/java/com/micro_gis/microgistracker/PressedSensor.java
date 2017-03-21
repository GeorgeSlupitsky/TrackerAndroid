package com.micro_gis.microgistracker;

/**
 * Created by oleg on 10.06.16.
 */
public class PressedSensor {
    String name,date;
    int number;
    Points points;
    PressedSensor(String name, String date, Points p, int num ){
        this.name=name;
        this.date=date;
        this.points=p;
        this.number =num;

    }

}
