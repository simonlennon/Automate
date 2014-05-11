package com.simonlennon.automate.serialcomms;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by simon.lennon on 11/05/14.
 * 
 * For working with the master controller over a serial interface
 * 
 */
public class MastercontSerialInterface implements SerialPortEventListener {

	private static Logger logger = LogManager
			.getLogger(MastercontSerialInterface.class);

	protected SerialPort serialPort;
	protected String portName;
	protected String inboundCmd;

	public void init(String portName) throws SerialPortException {
		this.portName = portName;
		serialPort = new SerialPort(portName);
		serialPort.openPort();
		serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

	}

	public void writeCmd(String cmd) throws SerialPortException {
		if (serialPort == null) {
			init(portName);
		}

		serialPort.writeBytes(cmd.getBytes());// Write data to port

	}

	public void stop() throws SerialPortException {
		if (serialPort != null) {
			serialPort.closePort();// Close serial port
			serialPort = null;
		}
	}

	public void serialEvent(SerialPortEvent event) {

		if (event.isRXCHAR()) {// If data is available
			try {

				for (int i = 0; i < event.getEventValue(); i++) {
					String b = serialPort.readString(1);

					if (b != null) {
						inboundCmd += b;
					}

					if (";".equals(b)) {

						logger.debug("serialEvent() inbound msg: " + inboundCmd);

						inboundCmd = "";
					}
				}

			} catch (SerialPortException ex) {
				System.out.println(ex);
			}

		} else if (event.isCTS()) {// If CTS line has changed state
			if (event.getEventValue() == 1) {// If line is ON
				logger.debug("serialEvent() CTS - ON");
			} else {
				logger.debug("serialEvent() CTS - OFF");
			}
		} else if (event.isDSR()) {// /If DSR line has changed state
			if (event.getEventValue() == 1) {// If line is ON
				logger.debug("serialEvent() DSR - ON");
			} else {
				logger.debug("serialEvent() DSR - OFF");
			}
		}

	}

}
