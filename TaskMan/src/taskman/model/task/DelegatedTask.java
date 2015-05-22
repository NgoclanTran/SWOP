package taskman.model.task;

import java.util.Map;

import org.joda.time.DateTime;

import taskman.model.memento.DelegatedTaskMemento;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;

public class DelegatedTask extends Task {

	public DelegatedTask(Clock clock, String description,
			int estimatedDuration, int acceptableDeviation,
			Map<ResourceType, Integer> resourceTypes,
			boolean dependenciesFinished, int developerAmount)
					throws IllegalArgumentException {
		super(clock, description, estimatedDuration, acceptableDeviation,
				resourceTypes, developerAmount);
		this.dependenciesFinished = dependenciesFinished;
	}

	@Override
	public void completeTask(boolean failed, DateTime startTime,
			DateTime endTime) {
		if (startTime == null)
			throw new IllegalArgumentException("The startTime is null.");
		if (endTime == null)
			throw new IllegalArgumentException("The endTime is null.");

		addTimeSpan(failed, startTime, endTime);
		endReservations(endTime);

		notifyAllObservers();
	}

	@Override
	public boolean dependenciesAreFinished() {
		return dependenciesFinished;
	}

	public void setDependenciesAreFinished(Boolean newValue) {
		dependenciesFinished = newValue;
	}

	private boolean dependenciesFinished;

	@Override
	public boolean isAlternativeFinished() {
		return false;
	}

	public DelegatedTaskMemento createMemento() {
		try{
			return new DelegatedTaskMemento(this, dependenciesFinished, getStatus()
					.getName(), getTimeSpan());
		}
		catch(IllegalStateException e){
			return new DelegatedTaskMemento(this, dependenciesFinished, getStatus()
					.getName(), null);
		}
	}

	public void setMemento(DelegatedTaskMemento m) {
		dependenciesFinished = m.getDependenciesFinished();
		if (m.getStateName().equals("UNAVAILABLE")) {
			setStatus(new Unavailable());
		} else if (m.getStateName().equals("AVAILABLE")) {
			setStatus(new Available());
		} else if (m.getStateName().equals("EXECUTING")) {
			setStatus(new Executing());
		} else if (m.getStateName().equals("FINISHED")) {
			setStatus(new Finished());
		} else if (m.getStateName().equals("PLANNED")) {
			setStatus(new Planned());
		} else if (m.getStateName().equals("DELEGATED")) {
			setStatus(new Delegated());
		} else {
			setStatus(new Failed());
		}
		setTimeSpan(m.getTimeSpan());
	}

	@Override
	protected void performUpdateStatus(Status status) {
		setStatus(status);
	}

}
