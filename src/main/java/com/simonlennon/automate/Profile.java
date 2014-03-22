package com.simonlennon.automate;

import com.simonlennon.automate.timeline.Activation;

import java.util.List;

/**
 * Created by simon.lennon on 23/01/14.
 */
public class Profile {

    protected String name;
    protected String ID;
    protected String description;
    protected List<Activation> activations;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Activation> getActivations() {
        return activations;
    }

    public void setActivations(List<Activation> activations) {
        this.activations = activations;
    }

}
