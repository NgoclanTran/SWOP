package taskman.controller;

import java.util.Arrays;
import java.util.List;

import taskman.controller.planning.AdvanceTimeSession;
import taskman.controller.planning.PlanTaskSession;
import taskman.controller.planning.SimulateSession;
import taskman.controller.project.CreateProjectSession;
import taskman.controller.project.CreateTaskSession;
import taskman.controller.project.ShowProjectSession;
import taskman.controller.project.UpdateTaskStatusSession;
import taskman.model.MementoHandler;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.model.time.Clock;
import taskman.view.IView;

public class MainSession extends Session {

	private final List<String> menu = Arrays.asList("Show projects",
			"Create project", "Create task", "Update task", "Advance time",
			"Plan task", "Start simulation", "Quit");

	private MementoHandler mh;
	private ResourceHandler rh;
	private UserHandler uh;
	private Clock clock;

	/**
	 * Constructor of the main session. This session will start the main menu on
	 * the UI.
	 * 
	 * @param cli
	 *            The command line interface.
	 * @param ph
	 *            The project handler.
	 * @param rh
	 *            The resource handler.
	 * @param uh
	 *            The user handler.
	 * @param mh
	 *            The memento handler.
	 * @param clock
	 *            The system clock.
	 * 
	 * @throws IllegalArgumentException
	 *             Both the given view and the project handler need to be valid.
	 * @throws IllegalArgumentException
	 *             The given resource handler, user handler and clock need to be
	 *             valid.
	 */
	public MainSession(IView cli, ProjectHandler ph, ResourceHandler rh,
			UserHandler uh, MementoHandler mh, Clock clock)
			throws IllegalArgumentException {
		super(cli, ph);
		if (!isValidMementoHandler(mh))
			throw new IllegalArgumentException(
					"The main controller needs a MementoHandler");
		if (!isValidResourceHandler(rh))
			throw new IllegalArgumentException(
					"The main controller needs a ResourceHandler");
		if (!isValidUserHandler(uh))
			throw new IllegalArgumentException(
					"The main controller needs a UserHandler");
		if (!isValidClock(clock))
			throw new IllegalArgumentException(
					"The main controller needs a clock");
		this.rh = rh;
		this.uh = uh;
		this.mh = mh;
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

	/**
	 * Runs the initial main menu asking the user what to do.
	 */
	@Override
	public void run() {
		getUI().displayWelcome();

		while (true) {
			int menuId = getUI().getMainMenuID(menu);

			switch (menuId) {
			case 1:
				new ShowProjectSession(getUI(), getPH()).run();
				break;
			case 2:
				new CreateProjectSession(getUI(), getPH(), clock).run();
				break;
			case 3:
				new CreateTaskSession(getUI(), getPH(), rh).run();
				break;
			case 4:
				new UpdateTaskStatusSession(getUI(), getPH(), uh).run();
				break;
			case 5:
				new AdvanceTimeSession(getUI(), getPH(), clock).run();
				break;
			case 6:
				new PlanTaskSession(getUI(), getPH(), uh, clock).run();
				break;
			case 7:
				new SimulateSession(getUI(), getPH(), mh, rh, uh, clock).run();
				break;
			case 8:
				return;
			}
		}
	}
}
