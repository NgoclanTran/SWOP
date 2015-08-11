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

	/**
	 * The constructor of the branch office
	 * 
	 * @param 	company
	 * 			The company of the branch office
	 * @param 	location	
	 * 			The location of the branch office
	 * @param 	resourceTypes
	 * 			The resouceTypes of the branc office
	 */
	public BranchOffice(Company company, String location,
			List<ResourceType> resourceTypes) {
		if (company == null)
			throw new IllegalArgumentException("The company cannot be null.");
		if (location == null)
			throw new IllegalArgumentException("The location cannot be null.");
		this.company = company;
		this.location = location;
		clock = new Clock();
		factory = new TaskFactory(this, clock);
		ph = new ProjectHandler(factory);
		dth = new DelegatedTaskHandler(factory);
		rh = new ResourceHandler(resourceTypes);
		uh = new UserHandler();
		mh = new MementoHandler(clock, ph, rh, uh, dth);
	}

	/**
	 * Return the company of the branch office
	 * @return
	 * 			return the name 
	 */
	public Company getCompany() {
		return company;
	}

	private final Company company;

	/**
	 * Return the location of the branch office
	 * 
	 * @return
	 * 			Return the location
	 */
	public String getLocation() {
		return location;
	}

	private final String location;

	/**
	 * Return the clock of the branch officce
	 * @return	
	 * 			Return the clock
	 */
	public Clock getClock() {
		return clock;
	}

	private final Clock clock;
	
	/**
	 * Return the projectHandler of the branch office
	 * @return
	 * 			Retunr the projecthandler
	 */
	public ProjectHandler getPh() {
		return ph;
	}

	private final ProjectHandler ph;
	
	/**
	 * Return the DelegatedTaksHandler of the branch office
	 * @return
	 * 			Return the delegatedTaskHandler
	 */
	public DelegatedTaskHandler getDth() {
		return dth;
	}

	private final DelegatedTaskHandler dth;

	/**
	 * Return de ResourcheHandler of the branch office
	 * @return
	 * 			Return the ResourceHandler
	 */
	public ResourceHandler getRh() {
		return rh;
	}

	private final ResourceHandler rh;

	/**
	 * Return de UserHandler of the branch office
	 * @return
	 * 			Return the UserHandler
	 */
	public UserHandler getUh() {
		return uh;
	}

	private final UserHandler uh;

	/**
	 * Return the MementoHandler of the branch office
	 * @return
	 * 			Return MementoHandler
	 */
	public MementoHandler getMh() {
		return mh;
	}

	private final MementoHandler mh;

	@Override
	/**
	 * Update the delegated Tasks and the tasks that belongs to
	 * 
	 */
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

	@Override
	/**
	 * The toString method for BranchOffice instances.
	 * @Return
	 * 			returns the location of the BranchOffice
	 */
	public String toString() {
		return location;
	}

}
