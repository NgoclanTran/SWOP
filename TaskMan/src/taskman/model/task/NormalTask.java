package taskman.model.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import taskman.model.memento.NormalTaskMemento;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;

public class NormalTask extends Task {
	/**
	 * The creator for the normal task
	 * @param clock
	 * 			The clock for this normal task
	 * @param description
	 * 			The description for this normal task
	 * @param estimatedDuration
	 * 			The estimated duration for this normal task
	 * @param acceptableDeviation
	 * 			The acceptable deviation for this normal task
	 * @param dependencies
	 * 			The dependencies for this normal task
	 * @param alternativeFor
	 * 			The normal task for which this is an alternative for
	 * @param resourceTypes
	 * 			The resource types needed for this normal task
	 * @param developerAmount
	 * 			An integer indicating the developers needed for this normal task
	 * @throws IllegalStateException
	 * 			Throws an exception if the state is illegal
	 * @throws IllegalArgumentException
	 * 			Throws an exception if one of the parameters is illegal
	 */
	public NormalTask(Clock clock, String description, int estimatedDuration,
			int acceptableDeviation, List<NormalTask> dependencies,
			NormalTask alternativeFor,
			Map<ResourceType, Integer> resourceTypes, int developerAmount)
			throws IllegalStateException, IllegalArgumentException {
		super(clock, description, estimatedDuration, acceptableDeviation,
				resourceTypes, developerAmount);

		if (dependencies != null)
			this.dependencies.addAll(dependencies);

		for (NormalTask subject : this.dependencies) {
			subject.attachDependant(this);
		}

		if (alternativeFor != null)
			alternativeFor.addAlternative(this);
	}

	/**
	 * Returns the list of dependencies of the task
	 * 
	 * @return The list of dependencies of the task
	 */
	public List<Task> getDependencies() {
		return new ArrayList<Task>(dependencies);
	}

	/**
	 * Add dependant task
	 * 
	 * @param dependant
	 * 
	 * @throws IllegalArgumentException
	 *             The dependant task is null
	 * 
	 * @post The list of depandants contains the given dependant task
	 */
	public void attachDependant(NormalTask dependant) {
		if (dependant == null)
			throw new IllegalArgumentException(
					"the dependant observer is null.");
		this.dependants.add(dependant);
	}

	/**
	 * When this task changed his status to Finished or failed, notify his
	 * dependants
	 * 
	 */
	private void notifyAllDependants() {
		for (NormalTask dependant : this.dependants) {
			try {
				dependant.update();
			} catch (IllegalStateException e) {
			}
		}
	}

	private List<NormalTask> dependencies = new ArrayList<NormalTask>();
	private List<NormalTask> dependants = new ArrayList<NormalTask>();

	/**
	 * Returns the alternative task for the task
	 * 
	 * @return The alternative task for the task
	 */
	public NormalTask getAlternative() {
		return alternative;
	}

	/**
	 * Add the alternative task for the task
	 *
	 * @param task
	 *            The new alternative task for this task
	 * @throws IllegalArgumentException
	 *             The alternative task is equal to null
	 * 
	 */
	public void addAlternative(NormalTask task) throws IllegalStateException {
		if (task == null)
			throw new IllegalArgumentException("The alternative is null.");

		getStatus().addAlternative(this, task);
	}

	protected void performAddAlternative(NormalTask task) {
		alternative = task;
	}

	private NormalTask alternative = null;

	@Override
	/**
	 * Will complete the normal task
	 */
	public void completeTask(boolean failed, DateTime startTime,
			DateTime endTime) {
		if (startTime == null)
			throw new IllegalArgumentException("The startTime is null.");
		if (endTime == null)
			throw new IllegalArgumentException("The endTime is null.");

		addTimeSpan(failed, startTime, endTime);
		endReservations(endTime);
	}

	@Override
	/**
	 * Will return a boolean indicating whether the dependencies are finished or not
	 * @Returns
	 * 			returns a boolean indicating whether the dependencies are finished or not
	 */
	public boolean dependenciesAreFinished() {
		for (NormalTask task : dependencies) {
			try {
				if (!task.getStatus().isAlternativeFinished(task))
					return false;

			} catch (IllegalStateException e) {
				if (!task.isFinished())
					return false;
			}
		}
		return true;
	}

	@Override
	/**
	 * Returns a boolean indicating whether the alternative for this normal task is finished or not
	 * @Returns
	 * 			returns the boolean indicating whether the alternative for this normal task is finished or not
	 */
	public boolean isAlternativeFinished() {
		if (alternative == null)
			return false;
		if (alternative.isFinished())
			return true;
		if (alternative.isFailed())
			return alternative.getStatus().isAlternativeFinished(alternative);
		return false;
	}
	/**
	 * Creates the memento for this normal task
	 * @return
	 * 			Returns the normal task memento with the correct information of this normal task
	 */
	public NormalTaskMemento createMemento() {
		try {
			return new NormalTaskMemento(this, dependants, getStatus()
					.getName(), getTimeSpan(), alternative,
					getResponsibleBranchOffice());
		} catch (IllegalStateException e) {
			return new NormalTaskMemento(this, dependants, getStatus()
					.getName(), null, alternative, getResponsibleBranchOffice());
		}
	}
	/**
	 * Sets the attributes of the normal tasks to the attributes of the given parameter
	 * @param m
	 * 			The normal task memento which attributes will be used
	 */
	public void setMemento(NormalTaskMemento m) {
		dependants = m.getDependants();
		alternative = m.getAlternative();
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

		notifyAllDependants();
		notifyAllObservers();
	}

}
