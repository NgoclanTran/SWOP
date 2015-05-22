package taskman.controller.branch;

import java.util.ArrayList;
import java.util.List;

import taskman.controller.Session;
import taskman.exceptions.ShouldExitException;
import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.company.ProjectHandler;
import taskman.model.project.Project;
import taskman.model.task.Task;
import taskman.view.IView;

public class DelegateTaskSession extends Session {

	private ProjectHandler ph;
	private Company company;

	public DelegateTaskSession(IView cli, ProjectHandler ph, Company company)
			throws IllegalArgumentException {
		super(cli);
		if (ph == null)
			throw new IllegalArgumentException(
					"The delegate task controller needs a ProjectHandler");
		if (company == null)
			throw new IllegalArgumentException(
					"The delegate task controller needs a Company");
		this.ph = ph;
		this.company = company;
	}

	@Override
	public void run() {
		delegateTask();
	}

	private void delegateTask() {
		try {
			Task task = getUnplannedTask();
			BranchOffice branchOffice = getBranchOffice();
			
			company.delegateTask(task, branchOffice);
		} catch (ShouldExitException e) {
			return;
		}
	}

	private BranchOffice getBranchOffice() throws ShouldExitException {
		List<BranchOffice> branchOffices = company.getBranchOffices();
		for (BranchOffice branchOffice : company.getBranchOffices()) {
			if (branchOffice.getPh().equals(ph))
				branchOffices.remove(branchOffice);
		}

		if (branchOffices.size() == 0) {
			getUI().displayError("No other branch offices.");
			throw new ShouldExitException();
		}

		BranchOffice branchOffice;

		try {
			branchOffice = getUI().getDelegateTaskForm().getBranchOffice(
					branchOffices);
		} catch (ShouldExitException e) {
			throw new ShouldExitException();
		}

		return branchOffice;
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

}
