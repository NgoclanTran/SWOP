package taskman.model.task;

import java.util.Map;

import org.joda.time.DateTime;

import taskman.model.memento.DelegatedTaskMemento;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;

public class DelegatedTask extends Task {
	/**
	 * The creator for the delegated task
	 * @param clock
	 * 			The clock to be used for this delegated task
	 * @param description
	 * 			The description to be used for this delegated task
	 * @param estimatedDuration
	 * 			The estimated duration to be used for this delegated task
	 * @param acceptableDeviation
	 * 			The acceptable deviation to be used for this delegated task
	 * @param resourceTypes
	 * 			The resource types to be used for this delegated task
	 * @param dependenciesFinished
	 * 			A boolean indicating whether the dependencies are finished for this delegated task
	 * @param developerAmount
	 * 			An integer indicating how many developers are needed for this task
	 * @throws IllegalArgumentException
	 * 			Will throw an exception if one of the parameters are illegal
	 */
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
	/**
	 * Will complete the delegated task
	 * 
	 */
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
	/**
	 * Returns a boolean indicating whether the dependencies are finished or not
	 * @Returns
	 * 			A boolean indicating whether the dependencies are finished or not
	 */
	public boolean dependenciesAreFinished() {
		return dependenciesFinished;
	}
	/**
	 * Will set the boolean indicating whether the dependencies are finished or not to the given value
	 * @param newValue
	 * 			The value to which the boolean will be changed to
	 */
	public void setDependenciesAreFinished(Boolean newValue) {
		dependenciesFinished = newValue;
	}

	private boolean dependenciesFinished;

	@Override
	/**
	 * Returns a boolean indicating whether the alternative is finished or not
	 * @Returns
	 * 			A boolean indicating whether the alternative is finished or not
	 */
	public boolean isAlternativeFinished() {
		return false;
	}
	/**
	 * Creates a memento for the delegated task
	 * @return
	 * 			Returns a memento for the delegated task containing all the needed parameters
	 */
	public DelegatedTaskMemento createMemento() {
		try {
			return new DelegatedTaskMemento(this, dependenciesFinished,
					getStatus().getName(), getTimeSpan(),
					getResponsibleBranchOffice());
		} catch (IllegalStateException e) {
			return new DelegatedTaskMemento(this, dependenciesFinished,
					getStatus().getName(), null, getResponsibleBranchOffice());
		}
	}
	/**
	 * Sets the parameters to the given delegated task memento's parameters values
	 * @param m
	 * 			The delegated task memento from which the values will be taken
	 */
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
		setResponsibleBranchOffice(m.getResponsibleBranch());
	}

	@Override
	protected void performUpdateStatus(Status status) {
		setStatus(status);
	}

}
