package taskman.model.memento;

import java.util.ArrayList;

import taskman.model.project.task.Task;

public class ProjectMemento {

	private ArrayList<Task> tasks;
	private String stateName;

	public ProjectMemento(ArrayList<Task> tasks, String stateName) {
		this.tasks = tasks;
		this.stateName = stateName;
	}

	public String getStateName() {
		return stateName;
	}

	public ArrayList<Task> getTasks() {
		return new ArrayList<Task>(tasks);
	}

}