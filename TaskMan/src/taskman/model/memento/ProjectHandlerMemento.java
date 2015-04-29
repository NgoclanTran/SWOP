package taskman.model.memento;

import java.util.ArrayList;

import taskman.model.project.Project;

public class ProjectHandlerMemento {

	private ArrayList<Project> projects;

	public ProjectHandlerMemento(ArrayList<Project> projects) {
		this.projects = projects;
	}

	public ArrayList<Project> getProjects() {
		return new ArrayList<Project>(projects);
	}
}