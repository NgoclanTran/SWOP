package taskman.controller.planning;

import java.util.Arrays;
import java.util.List;

import taskman.controller.Session;
import taskman.controller.project.CreateTaskSession;
import taskman.controller.project.ShowProjectSession;
import taskman.model.MementoHandler;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.model.time.Clock;
import taskman.view.IView;

public class SimulateSession extends Session {

	private MementoHandler mh;
	private ResourceHandler rh;
	private UserHandler uh;
	private Clock clock;

	private final List<String> menu = Arrays.asList("Show projects",
			"Create task", "Plan task", "End simulation and discard changes",
			"End simulation and keep changes");

	public SimulateSession(IView cli, ProjectHandler ph, MementoHandler mh,
			ResourceHandler rh, UserHandler uh, Clock clock)
			throws IllegalArgumentException {
		super(cli, ph);
		if (!isValidMementoHandler(mh))
			throw new IllegalArgumentException(
					"The simulate session controller needs a MementoHandler");
		if (!isValidResourceHandler(rh))
			throw new IllegalArgumentException(
					"The create task controller needs a ResourceHandler");
		if (!isValidUserHandler(uh))
			throw new IllegalArgumentException(
					"The plan task controller needs a UserHandler");
		if (!isValidClock(clock))
			throw new IllegalArgumentException(
					"The create project controller needs a clock");
		this.mh = mh;
		this.rh = rh;
		this.uh = uh;
		this.clock = clock;
	}

	/**
	 * Checks if the given memento handler is valid.
	 * 
	 * @param mh
	 * 
	 * @return Returns true if the memento handler is different from null.
	 */
	private boolean isValidMementoHandler(MementoHandler mh) {
		if (mh != null)
			return true;
		else
			return false;
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
		mh.saveState();
		getUI().displayInfo("State saved.");
	}

	private void resetState() {
		mh.resetState();
		getUI().displayInfo("State reset.");
	}
}