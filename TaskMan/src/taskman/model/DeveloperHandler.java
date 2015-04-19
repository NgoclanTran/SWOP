package taskman.model;

import java.util.ArrayList;
import java.util.List;

public class DeveloperHandler {

	public List<Developer> getDevelopers() {
		return new ArrayList<Developer>(developers);
	}

	public void addDeveloper(String name) {
		Developer developer = new Developer(name);
		addDeveloper(developer);
	}

	private void addDeveloper(Developer developer) {
		developers.add(developer);
	}

	private List<Developer> developers = new ArrayList<Developer>();

}
