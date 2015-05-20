package taskman.model.company;

import taskman.model.memento.Caretaker;
import taskman.model.memento.ProjectMemento;
import taskman.model.memento.ReservableMemento;
import taskman.model.memento.TaskMemento;
import taskman.model.project.Project;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.model.user.Developer;

public class MementoHandler {

	private ProjectHandler ph;
	private ResourceHandler rh;
	private UserHandler uh;
	private Caretaker caretaker;
	private Clock clock;

	public MementoHandler(Clock clock, ProjectHandler ph, ResourceHandler rh,
			UserHandler uh) {
		this.ph = ph;
		this.rh = rh;
		this.uh = uh;
		this.clock = clock;
		caretaker = new Caretaker();
	}

	public void saveState() {
		caretaker.addClockMemento(clock.createMemento());

		for (int i = 0; i < ph.getProjects().size(); i++) {
			Project project = ph.getProjects().get(i);
			caretaker.addProjectMemento(project.createMemento());
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
	}

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

		for (TaskMemento tm : caretaker.getTaskMementos()) {
			tm.getObject().setMemento(tm);
		}
	}
}
