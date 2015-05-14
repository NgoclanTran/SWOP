package taskman.controller.planning;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import taskman.controller.Session;
import taskman.controller.project.CreateTaskSession;
import taskman.controller.project.ShowProjectSession;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.model.memento.Caretaker;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.user.Developer;
import taskman.view.IView;

public class SimulateSession extends Session {

	private ResourceHandler rh;
	private UserHandler uh;
	private Clock clock;
	private Caretaker caretaker;
	private HashMap<Integer, Integer> projectCounter = new HashMap<Integer, Integer>();

	private final List<String> menu = Arrays.asList("Show projects",
			"Create task", "Plan task", "End simulation and discard changes",
			"End simulation and keep changes");

	public SimulateSession(IView cli, ProjectHandler ph, ResourceHandler rh,
			UserHandler uh, Clock clock) throws IllegalArgumentException {
		super(cli, ph);
		if (!isValidResourceHandler(rh))
			throw new IllegalArgumentException(
					"The create task controller needs a ResourceHandler");
		if (!isValidUserHandler(uh))
			throw new IllegalArgumentException(
					"The plan task controller needs a UserHandler");
		if (!isValidClock(clock))
			throw new IllegalArgumentException(
					"The create project controller needs a clock");
		this.rh = rh;
		this.uh = uh;
		this.clock = clock;
	}

	/**
	 * Checks if the given user handler is valid.
	 * 
	 * @param uh
	 * 
	 * @return Returns true if the user handler is different from null.
	 */
	private boolean isValidUserHandler(UserHandler uh) {
		if (uh != null)
			return true;
		else
			return false;
	}

	/**
	 * Checks if the given resource handler is valid.
	 * 
	 * @param rh
	 * 
	 * @return Returns true if the resource handler is different from null.
	 */
	private boolean isValidResourceHandler(ResourceHandler rh) {
		if (rh != null)
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if the given clock is valid.
	 * 
	 * @param clock
	 * 
	 * @return Returns true if the clock is different from null.
	 */
	private boolean isValidClock(Clock clock) {
		if (clock != null)
			return true;
		else
			return false;
	}

	@Override
	public void run() {
		startSimulation();
	}

	private void startSimulation() {
		saveCurrentState();
		while (true) {
			int menuId = getUI().getMainMenuID(menu);

			switch (menuId) {
			case 1:
				new ShowProjectSession(getUI(), getPH()).run();
				break;
			case 2:
				new CreateTaskSession(getUI(), getPH(), rh).run();
				break;
			case 3:
				new PlanTaskSession(getUI(), getPH(), uh, clock).run();
				break;
			case 4:
				resetState();
				return;
			case 5:
				return;
			}
		}
	}

	private void saveCurrentState() {
		caretaker = new Caretaker();
		caretaker.addClockMemento(clock.createMemento());
		for (int i = 0; i < getPH().getProjects().size(); i++) {
			Project project = getPH().getProjects().get(i);
			caretaker.addProjectMemento(project.createMemento());
			projectCounter.put(i, project.getTasks().size());
			for (Task task : project.getTasks()) {
				caretaker.addTaskMemento(task.createMemento());
			}
		}
		for (Developer developer : uh.getDevelopers()) {
			caretaker.addDeveloperMemento(developer.createMemento());
		}
		for (ResourceType resourceType : rh.getResourceTypes()) {
			for (Resource resource : resourceType.getResources())
				caretaker.addResourceMemento(resource.createMemento());
		}
		getUI().displayInfo("State saved.");
	}

	private void resetState() {
		clock.setMemento(caretaker.getClockMemento());
		for (int i = 0; i < uh.getDevelopers().size(); i++) {
			uh.getDevelopers().get(i)
					.setMemento(caretaker.getDeveloperMemento(i));
		}
		for (ResourceType resourceType : rh.getResourceTypes()) {
			int i = 0;
			for (Resource resource : resourceType.getResources()) {
				resource.setMemento(caretaker.getResourceMemento(i));
				i++;
			}
		}

		int k = 0;
		for (Entry<Integer, Integer> entry : projectCounter.entrySet()) {
			int i = entry.getKey();
			int j = 0;
			Project project = getPH().getProjects().get(i);
			while (j < entry.getValue()) {
				project.getTasks().get(j)
						.setMemento(caretaker.getTaskMemento(k));
				j++;
				k++;
			}
			project.setMemento(caretaker.getProjectMemento(i));
		}
		getUI().displayInfo("State reset.");

	}
}