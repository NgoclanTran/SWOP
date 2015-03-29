package taskman.model.project.task;

import java.util.ArrayList;
import java.util.List;

import taskman.model.project.Observer;

public class Subject {

	/**
	 * Adds the given observer to the list of observers.
	 * 
	 * @param observer
	 *            The observer object to add.
	 * 
	 * @post The new list of observers contains the given observer.
	 */
	public void attach(Observer observer) {
		observers.add(observer);
	}

	/**
	 * Removes the given observer from the list of observers.
	 * 
	 * @param observer
	 *            The observer object to remove.
	 * 
	 * @post The new list of observers does not contain the given observer.
	 */
	public void detach(Observer observer) {
		observers.remove(observer);
	}
	
	/**
	 * Calls the update function of every observer in the list of observers.
	 * 
	 * @effect Calls the update for all the observers.
	 */
	public void notifyAllObservers() {
		for(Observer o : observers) {
			o.update();
		}
	}

	List<Observer> observers = new ArrayList<Observer>();

}
