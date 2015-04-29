package taskman.model.memento;

import org.joda.time.DateTime;

public class ClockMemento {

	private DateTime systemClock;

	public ClockMemento(DateTime systemClock) {
		this.systemClock = systemClock;
	}

	public DateTime getState() {
		return this.systemClock;
	}
}