package taskman.model.company;

import java.util.List;

import taskman.model.Observer;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.NormalTask;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;

public class BranchOffice implements Observer {
	
	private final TaskFactory factory;

	public BranchOffice(Company company, String location, List<ResourceType> resourceTypes) {
		this.company = company;
		this.location = location;
		clock = new Clock();
		factory = new TaskFactory(this, clock);
		ph = new ProjectHandler(factory);
		dth = new DelegatedTaskHandler(factory);
		rh = new ResourceHandler(resourceTypes);
		uh = new UserHandler();
		mh = new MementoHandler(clock, ph, rh, uh);
	}

	public Company getCompany() {
		return company;
	}

	private final Company company;

	public String getLocation() {
		return location;
	}

	private final String location;

	public Clock getClock() {
		return clock;
	}

	private final Clock clock;

	public ProjectHandler getPh() {
		return ph;
	}

	private final ProjectHandler ph;

	public DelegatedTaskHandler getDth() {
		return dth;
	}

	private final DelegatedTaskHandler dth;

	public ResourceHandler getRh() {
		return rh;
	}

	private final ResourceHandler rh;

	public UserHandler getUh() {
		return uh;
	}

	private final UserHandler uh;

	public MementoHandler getMh() {
		return mh;
	}

	private final MementoHandler mh;

	@Override
	public void update() {
		for (Project project : ph.getProjects()) {
			for (NormalTask task : project.getTasks()) {
				if (task.isDelegated() && task.dependenciesAreFinished())
					company.setDependenciesFinished(task.getID());
			}
		}
		
		for (DelegatedTask task : dth.getDelegatedTasks()) {
			if (task.isCompleted())
				company.announceCompletion(task);
		}
	}

}
