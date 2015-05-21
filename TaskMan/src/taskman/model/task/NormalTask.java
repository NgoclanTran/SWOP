package taskman.model.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import taskman.model.memento.NormalTaskMemento;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;

public class NormalTask extends Task {

	public NormalTask(Clock clock, String description, int estimatedDuration,
			int acceptableDeviation, List<NormalTask> dependencies,
			NormalTask alternativeFor, Map<ResourceType, Integer> resourceTypes)
			throws IllegalStateException, IllegalArgumentException {
		super(clock, description, estimatedDuration, acceptableDeviation,
				resourceTypes);

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
	public boolean isAlternativeFinished() {
		if (alternative == null)
			return false;
		if (alternative.isFinished())
			return true;
		if (alternative.isFailed())
			return alternative.getStatus().isAlternativeFinished(alternative);
		return false;
	}

	public NormalTaskMemento createMemento() {
		return new NormalTaskMemento(this, dependants, getStatus().getName(),
				getTimeSpan(), alternative);
	}

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
	}

	@Override
	protected void performUpdateStatus(Status status) {
		setStatus(status);

		notifyAllDependants();
		notifyAllObservers();
	}

}
