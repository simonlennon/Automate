package com.simonlennon.automate.heating;

import com.simonlennon.automate.timeline.Activation;
import com.simonlennon.automate.timeline.BoostActivation;
import com.simonlennon.automate.timeline.Timeline;
import com.simonlennon.automate.timeline.TimelineStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by simon.lennon on 20/01/14.
 */
public class BoilerController {

	protected Timeline radsTimeline;
	protected Timeline tankTimeline;

	protected Timer t;

	protected Boiler boiler;
	protected Rads rads;

	protected boolean started;

	protected BoostActivation radBoost;
	protected BoostActivation tankBoost;

	public BoilerController() {
		boiler = new Boiler();
		rads = new Rads();
	}
	
	public void nightlyRollover(){
		
		//cancel all timer events 
		
		//get the timeline again
		
		//add any boosts to the timeline if we have them
		
		//schedule the events
		
		//check the status of everything
		
	}

	public void cancelBoost() {

		if (radBoost != null) {
			radsTimeline.removeActivation(radBoost);
			radBoost = null;
		}

		if (tankBoost != null) {
			tankTimeline.removeActivation(tankBoost);
			tankBoost = null;
		}

		checkAndSetDeviceStates();
	}

	public class BoostAlreadyActiveException extends Exception {
		BoostAlreadyActiveException(String msg) {
			super(msg);
		}
	}

	public void boost(int minutes, boolean fireRads)
			throws BoostAlreadyActiveException {

		if (radBoost != null || tankBoost != null) {
			throw new BoostAlreadyActiveException(
					"Boost is already active, cancel existing boost first.");
		}

		if (fireRads) {
			BoostActivation boost = new BoostActivation(minutes);
			radsTimeline.addActivation(boost);
			radBoost = boost;
		}

		BoostActivation boost = new BoostActivation(minutes);
		tankTimeline.addActivation(boost);
		tankBoost = boost;

		t.cancel();
		t = new Timer();

		scheduleEvents(radsTimeline);
		scheduleEvents(tankTimeline);

		checkAndSetDeviceStates();

	}

	public void restart() {
		shutdown();
		startup();
	}

	public void startup() {

		TimelineStore tls = new TimelineStore();
		radsTimeline = tls.getTodaysTimeline("RADS");
		tankTimeline = tls.getTodaysTimeline("TANK");

		t = new Timer();

		// Schedule events based on the timelines
		scheduleEvents(radsTimeline);
		scheduleEvents(tankTimeline);

		started = true;

		checkAndSetDeviceStates();

	}

	protected synchronized void handleTimelineEvent(EventTask eventTask) {

		System.out.println("handleTimelineEvent->"
				+ eventTask.getTimeline().getName() + ":"
				+ eventTask.getActivation().getStartTime() + ":"
				+ eventTask.getActivation().getEndTime());

		if (eventTask.getActivation() instanceof BoostActivation
				&& eventTask.getTimeline().getName().equals(TimelineStore.RADS)) {
			if (radBoost != null)
				radBoost = null;
		}
		if (eventTask.getActivation() instanceof BoostActivation
				&& eventTask.getTimeline().getName().equals(TimelineStore.TANK)) {
			if (tankBoost != null)
				tankBoost = null;
		}

		checkAndSetDeviceStates();

	}

	protected void checkAndSetDeviceStates() {

		Date now = new Date();
		if (shouldRadsBeActive(now) || shouldTankBeActive(now)) {
			if (!boiler.isOn())
				turnBoilerOn();
		} else {
			if (boiler.isOn())
				turnBoilerOff();
		}

		if (shouldRadsBeActive(now)) {
			if (!rads.isOn())
				turnRadsOn();
		} else {
			if (rads.isOn())
				turnRadsOff();
		}

	}

	protected void turnBoilerOff() {
		// if (boiler.isOn()) {
		boiler.turnOff();
		// }
	}

	protected void turnBoilerOn() {
		// if (!boiler.isOn()) {
		boiler.turnOn();
		// }
	}

	protected void turnRadsOn() {
		// if (!rads.isOn()) {
		rads.turnOn();
		// }
	}

	protected void turnRadsOff() {
		// if (rads.isOn()) {
		rads.turnOff();
		// }
	}

	protected boolean shouldRadsBeActive(Date now) {

		Activation[] activations = radsTimeline.getActivations();
		return findActivation(activations, now) != null;

	}

	protected boolean shouldTankBeActive(Date now) {

		Activation[] activations = tankTimeline.getActivations();
		return findActivation(activations, now) != null;

	}

	public ArrayList<Activation> getCurrentActivations() {
		ArrayList<Activation> currentActivations = new ArrayList<Activation>();

		Date now = new Date();

		ArrayList<Activation> currentTankActivations = findActivation(
				tankTimeline.getActivations(), now);

		if (currentTankActivations != null)
			currentActivations.addAll(currentTankActivations);

		ArrayList<Activation> currentRadsActivations = findActivation(
				radsTimeline.getActivations(), now);

		if (currentRadsActivations != null)
			currentActivations.addAll(currentRadsActivations);

		return currentActivations;

	}

	protected ArrayList<Activation> findActivation(Activation[] activations,
			Date time) {

		ArrayList<Activation> activationsForTime = new ArrayList<Activation>();

		for (Activation a : activations) {

			boolean start = time.after(a.getStartTime())
					|| time.equals(a.getStartTime());
			boolean end = time.after(a.getEndTime())
					|| time.equals(a.getEndTime());

			if (start && !end) {
				activationsForTime.add(a);
			}
		}

		if (activationsForTime.size() > 0) {
			return activationsForTime;
		} else {
			return null;
		}

	}

	public boolean isBoostingRads() {
		return radBoost != null;
	}

	public boolean isBoostingTank() {
		return tankBoost != null;
	}

	protected void scheduleEvents(Timeline tl) {

		Activation[] activations = tl.getActivations();
		Date now = new Date();

		for (Activation a : activations) {

			if (a.getStartTime().after(now)) {
				StartEventTask start = new StartEventTask(tl, a);
				t.schedule(start, a.getStartTime());
			}

			if (a.getEndTime().after(now)) {
				EndEventTask end = new EndEventTask(tl, a);
				t.schedule(end, a.getEndTime());
			}

		}

	}

	class EventTask extends TimerTask {

		protected Timeline timeline;
		protected Activation activation;

		public EventTask(Timeline timeline, Activation activation) {
			this.timeline = timeline;
			this.activation = activation;
		}

		@Override
		public void run() {

			BoilerController.this.handleTimelineEvent(this);

		}

		public Timeline getTimeline() {
			return timeline;
		}

		public Activation getActivation() {
			return activation;
		}
	}

	class StartEventTask extends EventTask {
		StartEventTask(Timeline timeline, Activation activation) {
			super(timeline, activation);
		}
	}

	class EndEventTask extends EventTask {
		EndEventTask(Timeline timeline, Activation activation) {
			super(timeline, activation);
		}
	}

	public void shutdown() {

		boiler.turnOff();
		rads.turnOff();

		if (t != null) {
			t.cancel();
		}
		t = null;
		radBoost = null;
		tankBoost = null;
		started = false;

	}

	public static void main(String[] args) {
		BoilerController controller = new BoilerController();
		controller.startup();
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Boiler getBoiler() {
		return boiler;
	}

	public Rads getRads() {
		return rads;
	}

}
