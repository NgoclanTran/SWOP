package taskman.model.company;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class UserHandler {
	/**
	 * Gives a copy of a list of the developers
	 * @return the list of developpers
	 */
	public List<Developer> getDevelopers() {
		return new ArrayList<Developer>(developers);
	}
	/**
	 * Will return the list of available developpers for a given time span
	 * @param timeSpan
	 * 			The timespan for which will be checked if the developpers are available or not
	 * @return the list of the available developpers for the given timespan
	 */
	public List<Developer> getAvailableDevelopers(TimeSpan timeSpan) {
		if (timeSpan == null) throw new IllegalArgumentException("The timeSpan cannot be null.");
		List<Developer> availableDevelopers = new ArrayList<Developer>();
		for (Developer developer : developers) {
			if (developer.isAvailableAt(timeSpan)) {
				availableDevelopers.add(developer);
			}
		}
		return availableDevelopers;
	}
	/**
	 * Adds a developper to the list of developpers
	 * @param name
	 * 			The name of the developper to be added
	 * @post the developper will be added to the list
	 */
	public void addDeveloper(String name) {
		if(name == null) throw new IllegalArgumentException("The name cannot be null.");
		Developer developer = new Developer(name, new LocalTime(8,0), new LocalTime(17,0));
		addDeveloper(developer);
	}
	/**
	 * Will add a developper to the list of developpers
	 * @param developer
	 * 			The developper to be added to the list
	 * @post the developper will be added to the list of developpers
	 */
	private void addDeveloper(Developer developer) {
		developers.add(developer);
	}

	private List<Developer> developers = new ArrayList<Developer>();

}
