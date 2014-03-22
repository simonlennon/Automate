package com.simonlennon.automate.heating;

import java.util.Date;

/**
 * Created by simon.lennon on 04/02/14.
 */
public class Rads {

    protected boolean on;
    protected Date lastOn;
    protected Date lastOff;

    public Rads(){

    }

    public void turnOn() {
        System.out.println("Rads turning on");
        lastOn = new Date();
        on = true;
    }

    public void turnOff() {
        System.out.println("Rads turning off");
        lastOff = new Date();
        on = false;
    }

    public boolean isOn() {
        return on;
    }

}