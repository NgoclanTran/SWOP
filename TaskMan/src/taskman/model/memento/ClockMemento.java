package taskman.model.memento;

import org.joda.time.DateTime;

import taskman.model.time.Clock;

public class ClockMemento {

	private Clock clock;
	private DateTime systemClock;

	/**
	 * The constructor of the class
	 * 
	 * @param clock
	 *            The clock object the clock memento is made for.
	 * @param systemClock
	 *            The system clock date time of the clock memento.
	 */
	public ClockMemento(Clock clock, DateTime systemClock) {
		this.clock = clock;
		this.systemClock = systemClock;
	}

	/**
	 * Returns the clock the memento is made for.
	 * 
	 * @return Returns the clock the memento is made for.
	 */
	public Clock getObject() {
		return this.clock;
	}

	/**
	 * Will return the state of the clock memento.
	 * 
	 * @return returns the state of the clock memento.
	 */
	public DateTime getState() {
		return this.systemClock;
	}
}