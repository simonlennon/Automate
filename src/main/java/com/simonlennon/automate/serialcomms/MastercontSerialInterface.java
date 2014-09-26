package com.simonlennon.automate.serialcomms;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by simon.lennon on 11/05/14.
 * <p/>
 * For working with the master controller over a serial interface
 */
public class MastercontSerialInterface implements SerialPortEventListener {

	private static Logger logger = LogManager
			.getLogger(MastercontSerialInterface.class);

	protected SerialPort serialPort;
	protected String portName;
	protected String inboundCmd = "";

	public void init(String portName) throws SerialPortException {
		this.portName = portName;
		serialPort = new SerialPort(portName);
		serialPort.openPort();
		serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		serialPort.addEventListener(this);

	}

	public void writeCmd(Command cmd) throws SerialPortException {

		logger.debug("writeCmd()" + cmd.toString());

		if (serialPort == null) {
			init(portName);
		}

		serialPort.writeBytes(cmd.toString().getBytes());// Write data to port

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

						if (";".equals(b)) {

                            try {
                                if (inboundCmd.trim().length() != 0) {
                                    handleCommand(inboundCmd.trim());
                                }
                            } finally {
                                inboundCmd = "";
                            }

						} else {
							inboundCmd += b;
						}
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

	protected void handleCommand(String cmd) {

		if (cmd.startsWith("DEBUG:")) {
			logger.debug("DEBUG Message from micro processor: " + cmd);
		} else if (cmd.startsWith("INFO:")) {
			logger.debug("INFO Message from micro processor: " + cmd);
		} else if (cmd.startsWith("WARN:")) {
			logger.debug("WARN Message from micro processor: " + cmd);
		} else if (cmd.startsWith("ERROR:")) {
			logger.debug("ERROR Message from micro processor: " + cmd);
		} else {
            logger.debug("handleCommand:"+cmd);
			try {
				fireCommand(cmd);
			} catch (InvalidCommandException e) {
				logger.warn("Could not read inbound command " + e.getMessage());
			}
		}

	}

	protected void fireCommand(String cmd) throws InvalidCommandException {
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

		for (CommandProcessor cp : commandProcessors) {
			cp.handleCommand(c);
		}

	}

	protected Vector<CommandProcessor> commandProcessors = new Vector<CommandProcessor>();

	public void addCommandListener(CommandProcessor cp) {
		commandProcessors.add(cp);
	}

}
