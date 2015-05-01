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
	 * Will return the project memento at the given index in the list
	 * 
	 * @param index
	 *            The element in the list at this index will be used
	 * @return returns the project memento at this index in the list
	 */
	public ProjectMemento getProjectMemento(int index) {
		return savedProjectMementos.get(index);
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
	 * Will return the developper memento at the given index in the list
	 * 
	 * @param index
	 *            The element in the list at this index will be returned
	 * @return the developper memento at the given index in the list
	 */
	public ReservableMemento getDeveloperMemento(int index) {
		return savedDeveloperMementos.get(index);
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
	 * Will return the resource memento at the given index
	 * 
	 * @param index
	 *            The element in the list at this index will be returned
	 * @return returns the element in the list at this index
	 */
	public ReservableMemento getResourceMemento(int index) {
		return savedResourceMementos.get(index);
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
	 * Will return the taskmemento at the given index in the list
	 * 
	 * @param index
	 *            The element in the list at this given index will be returned
	 * @return the element at this index will be returned
	 */
	public TaskMemento getTaskMemento(int index) {
		return savedTaskMementos.get(index);
	}

}