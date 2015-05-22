package taskman.controller.branch;

import java.util.Arrays;
import java.util.List;

import taskman.controller.Session;
import taskman.model.company.MementoHandler;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.company.UserHandler;
import taskman.model.time.Clock;
import taskman.view.IView;

public class SimulateSession extends Session {

	private ProjectHandler ph;
	private MementoHandler mh;
	private ResourceHandler rh;
	private UserHandler uh;
	private Clock clock;
	private boolean simulatedDelegation;

	private final List<String> menu = Arrays.asList("Show projects",
			"Create task", "Plan task", "Delegate task",
			"End simulation and discard changes",
			"End simulation and keep changes");

	public SimulateSession(IView cli, ProjectHandler ph, MementoHandler mh,
			ResourceHandler rh, UserHandler uh, Clock clock)
			throws IllegalArgumentException {
		super(cli);
		if (ph == null)
			throw new IllegalArgumentException(
					"The simulate session controller needs a ProjectHandler");
		if (mh == null)
			throw new IllegalArgumentException(
					"The simulate session controller needs a MementoHandler");
		if (rh == null)
			throw new IllegalArgumentException(
					"The create task controller needs a ResourceHandler");
		if (uh == null)
			throw new IllegalArgumentException(
					"The plan task controller needs a UserHandler");
		if (clock == null)
			throw new IllegalArgumentException(
					"The create project controller needs a clock");
		this.ph = ph;
		this.mh = mh;
		this.rh = rh;
		this.uh = uh;
		this.clock = clock;
		this.simulatedDelegation = false;
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
				new ShowProjectsSession(getUI(), ph).run();
				break;
			case 2:
				new CreateTaskSession(getUI(), ph, rh).run();
				break;
			case 3:
				new PlanTaskSession(getUI(), ph, uh, clock).run();
				break;
			case 4:
				new SimulateDelegationSession().run();
				simulatedDelegation = true;
				break;
			case 5:
				resetState();
				return;
			case 6:
				if (simulatedDelegation) {
					performSimulatedDelegation();
				}
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

	private void performSimulatedDelegation() {

	}
}