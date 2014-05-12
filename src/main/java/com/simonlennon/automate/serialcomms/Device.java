package com.simonlennon.automate.serialcomms;

/**
 * Created by simon.lennon on 12/05/14.
 */
public enum Device {

    MASTERCONT(0), PONDCONT(1);

    private int address;

    Device(int address){
        this.address = address;
    }

    public String toString(){
        return ""+address;
    }

    public static Device findByAddress(int address){
        for(Device d:Device.values()){
            if(d.address==address){
                return d;
            }
        }
        return null;
    }
}
