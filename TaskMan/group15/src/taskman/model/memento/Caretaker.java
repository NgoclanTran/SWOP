package taskman.model.memento;

import java.util.ArrayList;

public class Caretaker {

	ClockMemento savedClockMemento;
	DelegatedTaskHandlerMemento savedDelegatedTaskHandlerMemento;
	ArrayList<ProjectMemento> savedProjectMementos = new ArrayList<ProjectMemento>();
	ArrayList<ReservableMemento> savedDeveloperMementos = new ArrayList<ReservableMemento>();
	ArrayList<ReservableMemento> savedResourceMementos = new ArrayList<ReservableMemento>();
	ArrayList<NormalTaskMemento> savedNormalTaskMementos = new ArrayList<NormalTaskMemento>();
	ArrayList<DelegatedTaskMemento> savedDelegatedTaskMementos = new ArrayList<DelegatedTaskMemento>();

	/**
	 * Adds the clock memento parameter to the caretaker
	 * 
	 * @param m
	 *            The clockmemento to be saved
	 */
	public void addClockMemento(ClockMemento m) {
		savedClockMemento = m;
	}

	/**
	 * Will return the clock memento
	 * 
	 * @return the saved clock memento
	 */
	public ClockMemento getClockMemento() {
		return savedClockMemento;
	}

	/**
	 * Adds the delegated task handler memento parameter to the caretaker
	 * 
	 * @param m
	 *            The delegated task handler memento to be saved
	 */
	public void addDelegatedTaskHandlerMemento(DelegatedTaskHandlerMemento m) {
		savedDelegatedTaskHandlerMemento = m;
	}

	/**
	 * Will return the delegated task handler memento
	 * 
	 * @return the saved delegated task handler memento
	 */
	public DelegatedTaskHandlerMemento getDelegatedTaskHandlerMemento() {
		return savedDelegatedTaskHandlerMemento;
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
	 * Will add the given normal task memento to the list of task memento's
	 * 
	 * @param m
	 *            The normal task memento to be added to the list
	 * @post the normal task memento will be added to the list
	 */
	public void addTaskMemento(NormalTaskMemento m) {
		savedNormalTaskMementos.add(m);
	}

	/**
	 * Returns the list of task mementos.
	 * 
	 * @return Returns the list of task mementos.
	 */
	public ArrayList<NormalTaskMemento> getNormalTaskMementos() {
		return new ArrayList<NormalTaskMemento>(savedNormalTaskMementos);
	}

	/**
	 * Will add the given delegated task memento to the list of task memento's
	 * 
	 * @param m
	 *            The delegated task memento to be added to the list
	 * @post the delegated task memento will be added to the list
	 */
	public void addDelegatedTaskMemento(DelegatedTaskMemento m) {
		savedDelegatedTaskMementos.add(m);
	}

	/**
	 * Returns the list of delegated task mementos.
	 * 
	 * @return Returns the list of delegated task mementos.
	 */
	public ArrayList<DelegatedTaskMemento> getDelegatedTaskMementos() {
		return new ArrayList<DelegatedTaskMemento>(savedDelegatedTaskMementos);
	}

}