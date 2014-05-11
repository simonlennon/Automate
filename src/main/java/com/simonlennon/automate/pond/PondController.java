package com.simonlennon.automate.pond;

import jssc.SerialPortException;

import com.simonlennon.automate.serialcomms.MastercontSerialInterface;

/**
 * Created by simon.lennon on 11/05/14.
 */
public class PondController {

	protected int transCounter;

	public MastercontSerialInterface msi;

	protected boolean on;

	public void turnOnPump() {
		try {
			msi.writeCmd("1:0:0:" + getNextTransID() + ":1;");
			on = true;
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected int getNextTransID() {

		if (transCounter == 1000) {
			transCounter = 0;
		}

		return transCounter++;

	}

	public void turnOffPump() {
		try {
			msi.writeCmd("1:0:0:" + getNextTransID() + ":2;");
			on = false;
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isOn() {
		return on;
	}

	public void setMastercontSerialInterface(MastercontSerialInterface msi) {
		this.msi = msi;
	}

}
