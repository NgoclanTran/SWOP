package taskman.controller.planning;

import org.joda.time.DateTime;

import taskman.controller.Session;
import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.model.memento.Caretaker;
import taskman.model.time.Clock;
import taskman.view.IView;

public class SimulateSession extends Session {

	ResourceHandler rh;
	UserHandler uh;
	Caretaker caretaker;

	public SimulateSession(IView cli, ProjectHandler ph, ResourceHandler rh,
			UserHandler uh) throws IllegalArgumentException {
		super(cli, ph);
		if (!isValidResourceHandler(rh))
			throw new IllegalArgumentException(
					"The create task controller needs a ResourceHandler");
		if (!isValidUserHandler(uh))
			throw new IllegalArgumentException(
					"The plan task controller needs a UserHandler");

		this.rh = rh;
		this.uh = uh;
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

	@Override
	public void run() {
		saveCurrentState();
	}

	private void saveCurrentState() {
		caretaker = new Caretaker();
		caretaker.addClockMemento(Clock.getInstance().createMemento());
		caretaker.addProjectHandlerMemento(getPH().createMemento());

	}
}
