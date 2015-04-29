package taskman.model.memento;

import java.util.ArrayList;

public class Caretaker {

	// TODO Documentation

	ClockMemento savedClockMemento;
	ProjectHandlerMemento savedProjectHandlerMemento;
	ArrayList<ProjectMemento> savedProjectMementos = new ArrayList<ProjectMemento>();
	ArrayList<ReservableMemento> savedDeveloperMementos = new ArrayList<ReservableMemento>();
	ArrayList<ReservableMemento> savedResourceMementos = new ArrayList<ReservableMemento>();
	ArrayList<TaskMemento> savedTaskMementos = new ArrayList<TaskMemento>();

	public void addClockMemento(ClockMemento m) {
		savedClockMemento = m;
	}

	public ClockMemento getClockMemento() {
		return savedClockMemento;
	}

	public void addProjectHandlerMemento(ProjectHandlerMemento m) {
		savedProjectHandlerMemento = m;
	}

	public ProjectHandlerMemento getProjectHandlerMemento() {
		return savedProjectHandlerMemento;
	}

	public void addProjectMemento(ProjectMemento m) {
		savedProjectMementos.add(m);
	}

	public ProjectMemento getProjectMemento(int index) {
		return savedProjectMementos.get(index);
	}

	public void addDeveloperMemento(ReservableMemento m) {
		savedDeveloperMementos.add(m);
	}

	public ReservableMemento getDeveloperMemento(int index) {
		return savedDeveloperMementos.get(index);
	}

	public void addResourceMemento(ReservableMemento m) {
		savedResourceMementos.add(m);
	}

	public ReservableMemento getResourceMemento(int index) {
		return savedResourceMementos.get(index);
	}

	public void addTaskMemento(TaskMemento m) {
		savedTaskMementos.add(m);
	}

	public TaskMemento getTaskMemento(int index) {
		return savedTaskMementos.get(index);
	}

}