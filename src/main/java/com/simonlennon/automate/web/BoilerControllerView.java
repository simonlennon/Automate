package com.simonlennon.automate.web;

import com.simonlennon.automate.heating.BoilerController;

/**
 * Created by simon.lennon on 30/01/14.
 */
public class BoilerControllerView {

    protected BoilerController bc;

    public BoilerControllerView(BoilerController bc){
        this.bc = bc;
    }

    public boolean getBoilerActive(){
        return bc.getBoiler().isOn();
    }

    public boolean getRadsActive(){
        return bc.getRads().isOn();
    }

    public BoilerController getBoiler(){
        return bc;
    }

}
