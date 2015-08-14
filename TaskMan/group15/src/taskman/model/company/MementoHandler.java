package taskman.model.company;

import taskman.model.memento.Caretaker;
import taskman.model.memento.DelegatedTaskMemento;
import taskman.model.memento.NormalTaskMemento;
import taskman.model.memento.ProjectMemento;
import taskman.model.memento.ReservableMemento;
import taskman.model.project.Project;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.NormalTask;
import taskman.model.time.Clock;
import taskman.model.user.Developer;

public class MementoHandler {

	private ProjectHandler ph;
	private ResourceHandler rh;
	private UserHandler uh;
	private DelegatedTaskHandler dth;
	private Caretaker caretaker;
	private Clock clock;
	/**
	 * Starts up the memento pattern.
	 * @param clock
	 * 			The clock to be used
	 * @param ph
	 * 			The project handler to be used
	 * @param rh
	 * 			The resource handler to be used
	 * @param uh
	 * 			The user handler to be used
	 * @param dth
	 * 			The delegated task handler to be used
	 */
	public MementoHandler(Clock clock, ProjectHandler ph, ResourceHandler rh,
			UserHandler uh, DelegatedTaskHandler dth) {
		this.ph = ph;
		this.rh = rh;
		this.uh = uh;
		this.dth = dth;
		this.clock = clock;
		caretaker = new Caretaker();
	}
	/**
	 * This will save the state of the current model in a memento.
	 */
	public void saveState() {
		caretaker.addClockMemento(clock.createMemento());

		for (int i = 0; i < ph.getProjects().size(); i++) {
			Project project = ph.getProjects().get(i);
			caretaker.addProjectMemento(project.createMemento());
			for (NormalTask task : project.getTasks()) {
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

		caretaker.addDelegatedTaskHandlerMemento(dth.createMemento());

		for (DelegatedTask delegatedTask : dth.getDelegatedTasks()) {
			caretaker.addDelegatedTaskMemento(delegatedTask.createMemento());
		}
	}
	/**
	 * This will reset the state of the current model to the previous saved state.
	 */
	public void resetState() {
		caretaker.getClockMemento().getObject()
				.setMemento(caretaker.getClockMemento());

		for (ReservableMemento dm : caretaker.getDeveloperMementos()) {
			dm.getObject().setMemento(dm);
		}

		for (ReservableMemento rm : caretaker.getResourceMementos()) {
			rm.getObject().setMemento(rm);
		}

		for (ProjectMemento pm : caretaker.getProjectMementos()) {
			pm.getObject().setMemento(pm);
		}

		for (NormalTaskMemento ntm : caretaker.getNormalTaskMementos()) {
			ntm.getObject().setMemento(ntm);
		}

		caretaker.getDelegatedTaskHandlerMemento().getObject()
				.setMemento(caretaker.getDelegatedTaskHandlerMemento());

		for (DelegatedTaskMemento dtm : caretaker.getDelegatedTaskMementos()) {
			dtm.getObject().setMemento(dtm);
		}
	}
}
