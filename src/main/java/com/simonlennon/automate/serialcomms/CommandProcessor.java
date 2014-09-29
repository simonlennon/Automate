package com.simonlennon.automate.serialcomms;

/**
 * Created by simon.lennon on 12/05/14.
 */
public interface CommandProcessor {

    void setMastercontSerialInterface(MastercontSerialInterface mcsi);

    void handleCommand(Command cmd);

}
