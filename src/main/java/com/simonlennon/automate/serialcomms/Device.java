package com.simonlennon.automate.serialcomms;

/**
 * Created by simon.lennon on 12/05/14.
 */
public enum Device {

    MASTERCONT(0), PONDCONT(1), TESTDEVICE(2), ENV20(20), ENV21(21), ENV22(22), ENV23(23);

    private int address;

    Device(int address) {
        this.address = address;
    }

    public static Device findByAddress(int address) {
        for (Device d : Device.values()) {
            if (d.address == address) {
                return d;
            }
        }
        return null;
    }

    public String toString() {
        return "" + address;
    }

    public int getAddress() {
        return address;
    }
}
