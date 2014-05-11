package com.simonlennon.automate.pond;

import com.simonlennon.automate.serialcomms.MastercontSerialInterface;

/**
 * Created by simon.lennon on 11/05/14.
 */
public class PondController {

    public MastercontSerialInterface msi;

    protected boolean on;

    public void turnOnPump(){
        on = true;
    }

    public void turnOffPump(){
        on = false;
    }

    public boolean isOn(){
        return on;
    }

    public void setMastercontSerialInterface(MastercontSerialInterface msi) {
        this.msi = msi;
    }

}
