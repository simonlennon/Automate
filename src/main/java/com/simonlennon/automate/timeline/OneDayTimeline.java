package com.simonlennon.automate.timeline;

import java.util.ArrayList;

/**
 * Created by simon.lennon on 31/01/14.
 */
public class OneDayTimeline implements Timeline {

	protected String name;

	protected ArrayList<Activation> activations = new ArrayList<Activation>();

	public OneDayTimeline(String name) {
		this.name = name;
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

}
