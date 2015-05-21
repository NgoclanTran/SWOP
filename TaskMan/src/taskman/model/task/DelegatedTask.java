package taskman.model.task;

import java.util.Map;

import org.joda.time.DateTime;

import taskman.model.memento.NormalTaskMemento;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;

public class DelegatedTask extends Task {

	public DelegatedTask(Clock clock, String description,
			int estimatedDuration, int acceptableDeviation,
			Map<ResourceType, Integer> resourceTypes, boolean dependenciesFinished)
			throws IllegalArgumentException {
		super(clock, description, estimatedDuration, acceptableDeviation,
				resourceTypes);
		this.dependenciesFinished = dependenciesFinished;
	}
	
	@Override
	public void completeTask(boolean failed, DateTime startTime, DateTime endTime) {
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

	@Override
	public NormalTaskMemento createMemento() {
		// TODO Memento Auto-generated method stub
		return null;
	}

	@Override
	public void setMemento(NormalTaskMemento m) {
		// TODO Memento Auto-generated method stub
	}

	@Override
	protected void performUpdateStatus(Status status) {
		setStatus(status);
	}
	
}
