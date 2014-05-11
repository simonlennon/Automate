package com.simonlennon.automate.serialcomms;

import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * Created by simon.lennon on 11/05/14.
 *
 * For working with the master controller over a serial interface
 *
 */
public class MastercontSerialInterface {

    SerialPort serialPort;
    String portName;

    public void init(String portName) throws SerialPortException {
        this.portName = portName;
        serialPort = new SerialPort(portName);
        serialPort.setParams(SerialPort.BAUDRATE_9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

    }

    public void writeCmd(String cmd) throws SerialPortException {
        if(serialPort == null){
            init(portName);
        }

        serialPort.writeBytes(cmd.getBytes());//Write data to port

    }

    public void stop() throws SerialPortException {
        if(serialPort!=null){
            serialPort.closePort();//Close serial port
            serialPort = null;
        }
    }

}
