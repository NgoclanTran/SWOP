package taskman.controller.branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.MementoHandler;
import taskman.model.company.ProjectHandler;
import taskman.model.company.ResourceHandler;
import taskman.model.company.UserHandler;
import taskman.model.project.Project;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.view.IView;

public class SimulateSession extends Session {

	private ProjectHandler ph;
	private MementoHandler mh;
	private ResourceHandler rh;
	private UserHandler uh;
	private Clock clock;
	private Company company;
	private LinkedHashMap<Task, BranchOffice> delegations;

	private final List<String> menu = Arrays.asList("Show projects",
			"Create task", "Plan task", "Delegate task",
			"End simulation and discard changes",
			"End simulation and keep changes");

	public SimulateSession(IView cli, ProjectHandler ph, MementoHandler mh,
			ResourceHandler rh, UserHandler uh, Clock clock, Company company)
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
		if (company == null)
			throw new IllegalArgumentException(
					"The delegate task controller needs a Company");
		this.ph = ph;
		this.mh = mh;
		this.rh = rh;
		this.uh = uh;
		this.clock = clock;
		this.company = company;
		delegations = new LinkedHashMap<Task, BranchOffice>();
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
				simulateDelegation();
				break;
			case 5:
				resetState();
				return;
			case 6:
				performDelegations();
				return;
			}
		}
	}

	private void simulateDelegation() {
		Task task = getUnplannedTask();
		BranchOffice branchOffice = getBranchOffice();
		delegations.put(task, branchOffice);
	}

	private Task getUnplannedTask() throws ShouldExitException {
		List<Project> projects = ph.getProjects();
		List<List<Task>> unplannedTasksList = getUnplannedTasksAllProjects(projects);

		if (unplannedTasksList.size() == 0) {
			getUI().displayError("No unplanned tasks.");
			throw new ShouldExitException();
		}

		Project project;
		try {
			project = getUI().getPlanTaskForm().getProjectWithUnplannedTasks(
					projects, unplannedTasksList);
		} catch (ShouldExitException e) {
			throw new ShouldExitException();
		}

		return getUnplannedTask(project);
	}

	private Task getUnplannedTask(Project project) throws ShouldExitException {
		List<Task> tasks = getUnplannedTasks(new ArrayList<Task>(
				project.getTasks()));
		getUI().displayProjectDetails(project);

		if (tasks.size() == 0)
			throw new ShouldExitException();

		Task task;
		try {
			task = getUI().getTask(tasks);
		} catch (ShouldExitException e) {
			throw new ShouldExitException();
		}

		return task;
	}

	private List<List<Task>> getUnplannedTasksAllProjects(List<Project> projects) {
		List<List<Task>> unplannedTasksList = new ArrayList<>();
		List<Task> unplannedTasks = null;

		boolean noUnplannedTasks = true;

		for (Project p : projects) {
			unplannedTasks = getUnplannedTasks(new ArrayList<Task>(p.getTasks()));
			unplannedTasksList.add(unplannedTasks);

			if (noUnplannedTasks && unplannedTasks.size() > 0)
				noUnplannedTasks = false;
		}

		// TODO: Add the delegated tasks

		if (noUnplannedTasks)
			return new ArrayList<>();
		else
			return unplannedTasksList;
	}

	private List<Task> getUnplannedTasks(List<Task> tasks) {
		ArrayList<Task> unplannedTasks = new ArrayList<Task>();
		for (Task task : tasks) {
			if (!task.isPlanned())
				unplannedTasks.add(task);
		}
		return unplannedTasks;
	}

	private BranchOffice getBranchOffice() {
		List<BranchOffice> branchOffices = company.getBranchOffices();
		for (BranchOffice branchOffice : company.getBranchOffices()) {
			if (branchOffice.getPh().equals(ph))
				branchOffices.remove(branchOffice);
		}
		return null;
	}

	private void performDelegations() {
		for (Entry<Task, BranchOffice> entry : delegations.entrySet()) {
			company.delegateTask(entry.getKey(), entry.getValue());
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