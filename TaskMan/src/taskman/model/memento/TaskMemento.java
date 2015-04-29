package taskman.model.memento;

import java.util.ArrayList;
import java.util.List;

import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;

public class TaskMemento {

	List<Task> dependants;
	String stateName;
	TimeSpan timeSpan;
	Task alternative;

	public TaskMemento(List<Task> dependants, String stateName,
			TimeSpan timeSpan, Task alternative) {
		this.dependants = dependants;
		this.stateName = stateName;
		this.timeSpan = timeSpan;
		this.alternative = alternative;
	}

	public ArrayList<Task> getDependants() {
		return new ArrayList<Task>(dependants);
	}

	public String getStateName() {
		return stateName;
	}

	public TimeSpan getTimeSpan() {
		return timeSpan;
	}

	public Task getAlternative() {
		return alternative;
	}

}
