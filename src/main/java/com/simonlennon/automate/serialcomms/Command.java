package com.simonlennon.automate.serialcomms;

import java.util.Vector;

/**
 * Created by simon.lennon on 12/05/14.
 */
public class Command {

    protected Device to;
    protected Device from;
    protected MessageType type;

    public Device getTo() {
        return to;
    }

    public Device getFrom() {
        return from;
    }

    public MessageType getType() {
        return type;
    }

    public int getTransactionID() {
        return transactionID;
    }
    public void setTransactionID(int transactionID){
        this.transactionID = transactionID;
    }
    public int getCommand() {
        return command;
    }

    public Vector getParams() {
        return params;
    }

    protected int transactionID;
    protected int command;
    protected Vector params = new Vector();

    public static final String sepChar = ":";
    public static final String endChar = ";";

    public Command(Device to, Device from, MessageType type, int transactionID, int command){
        this.to = to;
        this.from = from;
        this.type = type;
        this.transactionID = transactionID;
        this.command = command;

    }

    public void addParam(String param){
        params.add(param);
    }

    public String toString(){

        String s = to+sepChar+from+sepChar+type+sepChar+transactionID+sepChar+command;

        if(!params.isEmpty()){
            for(Object o: params){
                s+=sepChar+o;
            }
        }

        s+=endChar;
        return s;

    }

}
