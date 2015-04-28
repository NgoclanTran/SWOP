package taskman.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalTime;

import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class UserHandler {

	public List<Developer> getDevelopers() {
		return new ArrayList<Developer>(developers);
	}
	
	public List<Developer> getAvailableDevelopers(TimeSpan timeSpan) {
		List<Developer> availableDevelopers = new ArrayList<Developer>();
		for (Developer developer : developers) {
			if (developer.isAvailableAt(timeSpan)) {
				availableDevelopers.add(developer);
			}
		}
		return availableDevelopers;
	}

	public void addDeveloper(String name) {
		Developer developer = new Developer(name, new LocalTime(8,0), new LocalTime(17,0));
		addDeveloper(developer);
	}

	private void addDeveloper(Developer developer) {
		developers.add(developer);
	}

	private List<Developer> developers = new ArrayList<Developer>();

}
