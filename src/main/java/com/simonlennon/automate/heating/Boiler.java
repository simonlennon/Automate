package com.simonlennon.automate.heating;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by simon.lennon on 20/01/14.
 */
public class Boiler {


    protected boolean on;
    protected Date lastOn;
    protected Date lastOff;


    public Boiler() {

    }


    public void turnOn() {
        System.out.println("Boiler turning on");
        lastOn = new Date();
        on = true;
    }

    public void turnOff() {
        System.out.println("Boiler turning off");
        lastOff = new Date();
        on = false;
    }


    public boolean isOn() {
        return on;
    }


}
