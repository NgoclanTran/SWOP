package taskman.model.memento;

import java.util.ArrayList;

public class Caretaker {

	ClockMemento savedClockMemento;
	ArrayList<ProjectMemento> savedProjectMementos = new ArrayList<ProjectMemento>();
	ArrayList<ReservableMemento> savedDeveloperMementos = new ArrayList<ReservableMemento>();
	ArrayList<ReservableMemento> savedResourceMementos = new ArrayList<ReservableMemento>();
	ArrayList<TaskMemento> savedTaskMementos = new ArrayList<TaskMemento>();

	/**
	 * Adds the clockmemento parameter to the arraylist
	 * 
	 * @param m
	 *            The clockmemento to be added
	 * @post the clockmemento will be added to the list
	 */
	public void addClockMemento(ClockMemento m) {
		savedClockMemento = m;
	}

	/**
	 * Will return the list of Clockmementos
	 * 
	 * @return the list of clockmementos
	 */
	public ClockMemento getClockMemento() {
		return savedClockMemento;
	}

	/**
	 * Adds the given project memento to the list of project mementos
	 * 
	 * @param m
	 *            The project memento to be added to the list
	 * @post the project memento will be added to the list
	 */
	public void addProjectMemento(ProjectMemento m) {
		savedProjectMementos.add(m);
	}

	/**
	 * Returns the list of project mementos.
	 * 
	 * @return Returns the list of project mementos.
	 */
	public ArrayList<ProjectMemento> getProjectMementos() {
		return new ArrayList<ProjectMemento>(savedProjectMementos);
	}

	/**
	 * Will add a developper memento to the list of developper memento's
	 * 
	 * @param m
	 *            the developper memento to be added to the list of developper
	 *            memento's
	 * @post the developper memento will be added to the list
	 */
	public void addDeveloperMemento(ReservableMemento m) {
		savedDeveloperMementos.add(m);
	}

	/**
	 * Returns the list of developer mementos.
	 * 
	 * @return Returns the list of developer mementos.
	 */
	public ArrayList<ReservableMemento> getDeveloperMementos() {
		return new ArrayList<ReservableMemento>(savedDeveloperMementos);
	}

	/**
	 * Will add a resource memento to the list of resource memento's
	 * 
	 * @param m
	 *            The resource memento to be added to the list
	 * @post the resource memento will be added to the list
	 */
	public void addResourceMemento(ReservableMemento m) {
		savedResourceMementos.add(m);
	}

	/**
	 * Returns the list of resource mementos.
	 * 
	 * @return Returns the list of resource mementos.
	 */
	public ArrayList<ReservableMemento> getResourceMementos() {
		return new ArrayList<ReservableMemento>(savedResourceMementos);
	}

	/**
	 * Will add the given task memento to the list of task memento's
	 * 
	 * @param m
	 *            The task memento to be added to the list
	 * @post the task memento will be added to the list
	 */
	public void addTaskMemento(TaskMemento m) {
		savedTaskMementos.add(m);
	}

	/**
	 * Returns the list of task mementos.
	 * 
	 * @return Returns the list of task mementos.
	 */
	public ArrayList<TaskMemento> getTaskMementos() {
		return new ArrayList<TaskMemento>(savedTaskMementos);
	}

}