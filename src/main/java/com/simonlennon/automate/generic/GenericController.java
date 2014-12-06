package com.simonlennon.automate.generic;

import com.simonlennon.automate.controller.Controller;
import com.simonlennon.automate.serialcomms.*;
import jssc.SerialPortException;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by simon.lennon on 29/09/2014.
 */
public class GenericController implements Controller, CommandProcessor {

    protected ArrayList<Command> outboundHistory = new ArrayList<Command>();
    protected ArrayList<Command> inboundHistory = new ArrayList<Command>();

    public ArrayList<Command> getOutboundHistory() {
        return outboundHistory;
    }

    public ArrayList<Command> getInboundHistory() {
        return inboundHistory;
    }


    protected MastercontSerialInterface mcsi;

    @Override
    public void setMastercontSerialInterface(MastercontSerialInterface mcsi) {
        this.mcsi = mcsi;
    }

    @Override
    public void handleCommand(Command cmd) {

        if(inboundHistory.size()>40){
           inboundHistory.remove(0);
        }

        inboundHistory.add(cmd);

    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }

    public void clearOutboundHistory(){
        outboundHistory = new ArrayList<Command>();
    }
    public void clearInboundHistory(){
        inboundHistory = new ArrayList<Command>();
    }

    public void sendCommand(String cmd) throws InvalidCommandException, SerialPortException {
        StringTokenizer st = new StringTokenizer(cmd, ":");
        int tokenCount = st.countTokens();

        if (tokenCount < 5) {
            throw new InvalidCommandException("Malformed command received: "
                    + cmd);
        }

        Command c;

        try {
            // The to address
            Device to = Device.findByAddress(Integer.parseInt(st.nextToken()
                    .toString()));
            // The from address
            Device from = Device.findByAddress(Integer.parseInt(st.nextToken()
                    .toString()));
            // The message type
            MessageType type = MessageType.findByType(Integer.parseInt(st
                    .nextToken().toString()));
            // The transaction ID
            int txid = Integer.parseInt(st.nextToken().toString());
            // The command
            int command = Integer.parseInt(st.nextToken().toString());

            if (to == null || from == null || type == null) {
                throw new InvalidCommandException(
                        "Malformed command received: " + cmd);
            }

            c = new Command(to, from, type, txid, command);

        } catch (NumberFormatException ex) {
            throw new InvalidCommandException(
                    "Malformed command received, could not parse number from string: "
                            + cmd);
        }

        // The rest are params
        while (st.hasMoreTokens()) {
            c.addParam(st.nextToken().toString());
        }

       if(outboundHistory.size()>9){
           outboundHistory.remove(0);
       }

       outboundHistory.add(c);

       mcsi.writeCmd(c);

    }


}
