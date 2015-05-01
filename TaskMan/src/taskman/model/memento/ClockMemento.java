package taskman.model.memento;

import org.joda.time.DateTime;

public class ClockMemento {

	private DateTime systemClock;
	/**
	 * The constructor of the class
	 * @param systemClock
	 * 			The system clock of the clock memento
	 */
	public ClockMemento(DateTime systemClock) {
		this.systemClock = systemClock;
	}
	/**
	 * Will return the state of the clockmemento
	 * @return returns the state of the clockmemento
	 */
	public DateTime getState() {
		return this.systemClock;
	}
}