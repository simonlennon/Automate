package com.simonlennon.automate.pond;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPortException;

import com.simonlennon.automate.serialcomms.MastercontSerialInterface;

/**
 * Created by simon.lennon on 11/05/14.
 * 
 */
public class PondController {

	protected int transCounter;

	public MastercontSerialInterface msi;

	protected boolean on;

	private static Logger logger = LogManager.getLogger(PondController.class);

	protected int getNextTransID() {

		if (transCounter == 1000) {
			transCounter = 0;
		}

		return transCounter++;

	}

	public void turnOnPump() {
		try {

			logger.debug("turnOnPump() writing command to serial");
			msi.writeCmd("1:0:0:" + getNextTransID() + ":1;");
			on = true;

			logger.debug("turnOnPump() command written");
		} catch (SerialPortException e) {

			logger.debug("turnOnPump() serial error", e);
			logger.error("Serial error turning on pond pump. Turn on debug for stack trace.");
		}
	}

	public void turnOffPump() {
		try {

			logger.debug("turnOffPond() writing command to serial");
			msi.writeCmd("1:0:0:" + getNextTransID() + ":2;");
			on = false;

			logger.debug("turnOffPond() command written");
		} catch (SerialPortException e) {

			logger.debug("turnOffPond() serial error", e);
			logger.error("Serial error turning off pond pump. Turn on debug for stack trace.");
		}
	}

	public boolean isOn() {
		return on;
	}

	public void setMastercontSerialInterface(MastercontSerialInterface msi) {
		this.msi = msi;
	}

}
