package taskman.model.time;

import org.joda.time.DateTime;

import taskman.model.memento.ClockMemento;

public class Clock extends TimeSubject {

	private DateTime systemTime;

	/**
	 * The constructor of the class.
	 */
	public Clock() {
		systemTime = new DateTime(0);
	}

	/**
	 * Will return the current system time of the clock.
	 * 
	 * @return the system time of the clock
	 */
	public DateTime getSystemTime() {
		return systemTime;
	}

	/**
	 * Will set the system time of the clock to the given system time
	 * 
	 * @param systemTime
	 *            The system time to which the clock will be set.
	 * 
	 * @post The system time of the clock will be set to the parameter.
	 * 
	 * @throws IllegalArgumentException
	 *             The given system time is null.
	 */
	public void setSystemTime(DateTime systemTime) {
		if (systemTime == null)
			throw new IllegalArgumentException("The systemTime cannot be null.");
		this.systemTime = systemTime;
		notifyAllObservers();
	}

	/**
	 * Will advance the system time.
	 * 
	 * @param systemTime
	 *            The system time to which the clock will be set.
	 * 
	 * @post The system time of the clock will be set to the parameter.
	 * 
	 * @throws IllegalArgumentException
	 *             The given system time is null or before the current system
	 *             time.
	 */
	public void advanceSystemTime(DateTime systemTime) {
		if (systemTime == null)
			throw new IllegalArgumentException(
					"The system time cannot be null.");
		if (systemTime.isBefore(this.systemTime))
			throw new IllegalArgumentException(
					"The given system time is before the current system time.");
		setSystemTime(systemTime);
	}

	/**
	 * Will return a new clock memento with the system time of the current
	 * clock.
	 * 
	 * @return Returns a new clock memento object with the system time of the
	 *         current clock.
	 */
	public ClockMemento createMemento() {
		return new ClockMemento(this, systemTime);
	}

	/**
	 * Sets the system time of the clock to the state of the clock memento
	 * parameter
	 * 
	 * @param m
	 *            The clock memento object of which the state will be used.
	 * 
	 * @post The system time of the clock will be set to the state of the clock
	 *       memento object.
	 */
	public void setMemento(ClockMemento m) {
		if(m == null) throw new IllegalArgumentException("The memento cannot be null.");
		systemTime = m.getState();
	}
}
