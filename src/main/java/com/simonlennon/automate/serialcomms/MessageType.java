package com.simonlennon.automate.serialcomms;

/**
 * Created by simon.lennon on 12/05/14.
 */
public enum MessageType {

    DATA(0), ACK(1), NACK(2);

    private int type;

    MessageType(int type) {
        this.type = type;
    }

    public static MessageType findByType(int type) {
        for (MessageType t : MessageType.values()) {
            if (t.type == type) {
                return t;
            }
        }
        return null;
    }

    public String toString() {
        return "" + type;
    }

}
