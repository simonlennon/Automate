package com.simonlennon.automate.timeline;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by simon.lennon on 31/01/14.
 */
public class OneDayTimeline implements Timeline {

    protected String name;
    protected Date expires;

    protected ArrayList<Activation> activations = new ArrayList<Activation>();

    public OneDayTimeline(String name, Date expires) {
        this.name = name;
        this.expires = expires;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Activation[] getActivations() {
        return activations.toArray(new Activation[activations.size()]);
    }

    public void addActivation(Activation activation) {
        this.activations.add(activation);
    }

    @Override
    public void removeActivation(Activation a) {
        activations.remove(a);
    }

    @Override
    public Date getExpiryTime() {
        return expires;
    }

}
