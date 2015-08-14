package taskman.model.memento;

import java.util.ArrayList;
import java.util.List;

import taskman.model.company.DelegatedTaskHandler;
import taskman.model.task.DelegatedTask;

public class DelegatedTaskHandlerMemento {

	DelegatedTaskHandler dth;
	List<DelegatedTask> delegatedTasks;

	/**
	 * The constructor of the delegated task handler memento class
	 * 
	 * @param dth
	 * 
	 * @param delegatedTasks
	 */
	public DelegatedTaskHandlerMemento(DelegatedTaskHandler dth,
			List<DelegatedTask> delegatedTasks) {
		this.dth = dth;
		this.delegatedTasks = delegatedTasks;
	}

	/**
	 * Returns the delegated task handler the memento is made for
	 * 
	 * @return The delegated task handler the memento is made for
	 */
	public DelegatedTaskHandler getObject() {
		return this.dth;
	}

	/**
	 * Returns the list of delegated tasks stored in the memento
	 * 
	 * @return The list of delegated tasks stored in the memento
	 */
	public List<DelegatedTask> getDelegatedTasks() {
		return new ArrayList<DelegatedTask>(delegatedTasks);
	}

}
