package com.simonlennon.automate.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonlennon.automate.generic.GenericController;
import com.simonlennon.automate.heating.BoilerController;
import com.simonlennon.automate.pond.PondController;
import com.simonlennon.automate.serialcomms.InvalidCommandException;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by simon.lennon on 30/01/14.
 */
public class StatusServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(StatusServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonData = mapper.readValue(request.getReader(), Map.class);

        BoilerControllerView bcv = (BoilerControllerView) getServletContext().getAttribute("bcv");
        PondController pc = (PondController) getServletContext().getAttribute("pc");
        GenericController gc = (GenericController) getServletContext().getAttribute("gc");

        response.setContentType("application/json");

        if ("boost".equals(jsonData.get("opp"))) {
            boost(bcv, jsonData);
            outputHeatingStatus(bcv, mapper, response);
        } else if ("cancelBoost".equals(jsonData.get("opp"))) {
            bcv.getBoiler().cancelBoost();
            outputHeatingStatus(bcv, mapper, response);
        } else if ("loadHeatingStatus".equals(jsonData.get("opp"))) {
            outputHeatingStatus(bcv, mapper, response);
        } else if ("loadPumpStatus".equals(jsonData.get("opp"))) {
            mapper.writeValue(response.getOutputStream(), getPondStatus(pc));
        } else if ("pondPumpOn".equals(jsonData.get("opp"))) {
            pc.turnOnPump();
            mapper.writeValue(response.getOutputStream(), getPondStatus(pc));
        } else if ("pondPumpOff".equals(jsonData.get("opp"))) {
            pc.turnOffPump();
            mapper.writeValue(response.getOutputStream(), getPondStatus(pc));
        } else if ("pondToManual".equals(jsonData.get("opp"))) {
            pc.switchToManual();
            mapper.writeValue(response.getOutputStream(), getPondStatus(pc));
        } else if ("pondToAuto".equals(jsonData.get("opp"))) {
            pc.switchToAuto();
            mapper.writeValue(response.getOutputStream(), getPondStatus(pc));
        }
        //Generic page commands
        else if ("loadGenericStatus".equals(jsonData.get("opp"))){
            outputGenericStatus(gc,mapper,response);
        } else if ("issueGenericCommand".equals(jsonData.get("opp"))){
            sendGenericCommand(gc, jsonData);
            outputGenericStatus(gc,mapper,response);
        }  else if ("genericClearOutboundHistory".equals(jsonData.get("opp"))){
            gc.clearOutboundHistory();
            outputGenericStatus(gc,mapper,response);
        }  else if ("genericClearInboundHistory".equals(jsonData.get("opp"))){
            gc.clearInboundHistory();
            outputGenericStatus(gc,mapper,response);
        } else {
            logger.debug("unknown command: "+ jsonData.get("opp"));
        }

    }

    public void sendGenericCommand(GenericController gc, Map<String, Object> jsonData) {

        String command = jsonData.get("command").toString();

        try {
            gc.sendCommand(command);
        }  catch (InvalidCommandException e) {
            throw new RuntimeException(e);
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }

    }

    protected void outputGenericStatus(GenericController gc, ObjectMapper mapper, HttpServletResponse response) throws IOException {
        GenericCommandStatus status = new GenericCommandStatus(gc.getOutboundHistory(), gc.getInboundHistory());
        mapper.writeValue(response.getOutputStream(), status);
    }

    protected void outputHeatingStatus(BoilerControllerView bcv, ObjectMapper mapper, HttpServletResponse response) throws IOException {
        Status status = new Status(bcv.getRadsActive() ? "on" : "off", bcv.getBoilerActive() ? "on" : "off", bcv.getBoiler().isBoostingTank() ? "on" : "off", bcv.getBoiler().isBoostingRads() ? "on" : "off");
        mapper.writeValue(response.getOutputStream(), status);
    }

    protected PondStatus getPondStatus(PondController pc){
        PondStatus ps = new PondStatus(pc.isPumpOn()?"on":"off",pc.getMode());
        return ps;
    }


    public void boost(BoilerControllerView bcv, Map<String, Object> jsonData) {

        String duration = jsonData.get("duration").toString();
        String fireRads = jsonData.get("fireRads").toString();

        try {
            bcv.getBoiler().boost(Integer.parseInt(duration), Boolean.parseBoolean(jsonData.get("fireRads").toString()));
        } catch (BoilerController.BoostAlreadyActiveException e) {
            throw new RuntimeException(e);
        }

    }

    class GenericCommandStatus {

        ArrayList outboundHistory;
        ArrayList inboundHistory;

        GenericCommandStatus(ArrayList outboundHistory, ArrayList inboundHistory){
            this.outboundHistory = outboundHistory;
            this.inboundHistory = inboundHistory;
        }

        public String getLastName(){
            return "Roger...";
        }

        public ArrayList getOutboundHistory() {
            return outboundHistory;
        }

        public ArrayList getInboundHistory() {
            return inboundHistory;
        }

    }

    class PondStatus {

        PondStatus(String pumpStatus, String mode) {
            this.pumpStatus = pumpStatus;
            this.mode = mode;
        }

        public String getPumpStatus() {
            return pumpStatus;
        }

        public void setPumpStatus(String pumpStatus) {
            this.pumpStatus = pumpStatus;
        }

        String pumpStatus;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        String mode;
    }

    class Status {

        String radsStatus;
        String boilerStatus;
        String tankBoostStatus;
        String radsBoostStatus;

        Status(String radsStatus, String boilerStatus, String tankBoostStatus, String radsBoostStatus) {
            this.radsStatus = radsStatus;
            this.boilerStatus = boilerStatus;
            this.tankBoostStatus = tankBoostStatus;
            this.radsBoostStatus = radsBoostStatus;
        }

        public String getBoilerStatus() {
            return boilerStatus;
        }

        public void setBoilerStatus(String boilerStatus) {
            this.boilerStatus = boilerStatus;
        }

        public String getRadsStatus() {
            return radsStatus;
        }

        public void setRadsStatus(String radsStatus) {
            this.radsStatus = radsStatus;
        }

        public String getRadsBoostStatus() {
            return radsBoostStatus;
        }

        public void setRadsBoostStatus(String radsBoostStatus) {
            this.radsBoostStatus = radsBoostStatus;
        }

        public String getTankBoostStatus() {
            return tankBoostStatus;
        }

        public void setTankBoostStatus(String tankBoostStatus) {
            this.tankBoostStatus = tankBoostStatus;
        }

    }

}
