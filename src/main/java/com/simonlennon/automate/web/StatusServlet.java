package com.simonlennon.automate.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simonlennon.automate.heating.BoilerController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by simon.lennon on 30/01/14.
 */
public class StatusServlet extends HttpServlet {


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonData = mapper.readValue(request.getReader(), Map.class);

        //We will always return the current status
        BoilerControllerView bcv = (BoilerControllerView) getServletContext().getAttribute("bcv");

        if ("boost".equals(jsonData.get("opp"))) {
            boost(bcv, jsonData);
        } else if ("cancelBoost".equals(jsonData.get("opp"))) {
            bcv.getBoiler().cancelBoost();
        } else if ("loadStatus".equals(jsonData.get("opp"))) {
            Status status = new Status(bcv.getRadsActive() ? "on" : "off", bcv.getBoilerActive() ? "on" : "off", bcv.getBoiler().isBoostingTank() ? "on" : "off", bcv.getBoiler().isBoostingRads() ? "on" : "off");
            response.setContentType("application/json");
            mapper.writeValue(response.getOutputStream(), status);
        } else if ("loadPumpStatus".equals(jsonData.get("opp"))) {
            response.setContentType("application/json");
            mapper.writeValue(response.getOutputStream(), ps);
        } else if ("pondPumpOn".equals(jsonData.get("opp"))) {
            ps.pumpStatus = "on";
        } else if ("pondPumpOff".equals(jsonData.get("opp"))) {
            ps.pumpStatus = "off";
        }


    }

    PondStatus ps = new PondStatus("off");


    public void boost(BoilerControllerView bcv, Map<String, Object> jsonData) {

        String duration = jsonData.get("duration").toString();
        String fireRads = jsonData.get("fireRads").toString();

        try {
            bcv.getBoiler().boost(Integer.parseInt(duration), Boolean.parseBoolean(jsonData.get("fireRads").toString()));
        } catch (BoilerController.BoostAlreadyActiveException e) {
            throw new RuntimeException(e);
        }

    }

    class PondStatus {

        PondStatus(String pumpStatus) {
            this.pumpStatus = pumpStatus;
        }

        public String getPumpStatus() {
            return pumpStatus;
        }

        public void setPumpStatus(String pumpStatus) {
            this.pumpStatus = pumpStatus;
        }

        String pumpStatus;
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
