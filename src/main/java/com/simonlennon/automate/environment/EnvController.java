package com.simonlennon.automate.environment;

import com.simonlennon.automate.controller.Controller;
import com.simonlennon.automate.controller.UserDataListiner;
import com.simonlennon.automate.controller.UserDataSource;
import com.simonlennon.automate.serialcomms.Command;
import com.simonlennon.automate.serialcomms.CommandProcessor;
import com.simonlennon.automate.serialcomms.Device;
import com.simonlennon.automate.serialcomms.MastercontSerialInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by simon.lennon on 09/12/2014.
 */
public class EnvController implements Controller, CommandProcessor, UserDataSource {

    protected Map<Integer, Integer> txKeys = new HashMap<Integer, Integer>();

    protected Map<Integer, String> envData = new HashMap<Integer, String>();

    protected MastercontSerialInterface msi;

    protected Vector<UserDataListiner> users = new Vector();

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {
        txKeys = new HashMap<Integer, Integer>();
        envData = new HashMap<Integer, String>();
    }

    @Override
    public void setMastercontSerialInterface(MastercontSerialInterface msi) {
        this.msi = msi;
    }

    @Override
    public void handleCommand(Command cmd) {
        Device from = cmd.getFrom();
        if (from.getAddress() >= 20 && from.getAddress() <= 39) {

            if (txKeys.containsKey(from.getAddress()) && cmd.getTransactionID() == txKeys.get(from.getAddress())) {
                //We have already processed this command recently
            } else {
                txKeys.put(from.getAddress(), cmd.getTransactionID());
                String data = cmd.getParams().get(0) != null ? cmd.getParams().get(0).toString() : "";
                envData.put(from.getAddress(), data);
                fireUserDataEvent();
            }
        }
    }

    protected void fireUserDataEvent() {
        for (UserDataListiner udl : users) {
            udl.handleUserDataEvent(1, this.envData);
        }
    }

    @Override
    public void addUserDataListiner(UserDataListiner userDataListiner) {
        if (!users.contains(userDataListiner)) {
            users.add(userDataListiner);
        }
    }

    @Override
    public void removeUserDataListiner(UserDataListiner userDataListiner) {
        if (users.contains(userDataListiner)) {
            users.remove(userDataListiner);
        }
    }

    public Map<Integer, String> getEnvData() {
        return envData;
    }
}
