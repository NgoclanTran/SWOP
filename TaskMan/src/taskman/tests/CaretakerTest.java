package taskman.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import taskman.model.company.BranchOffice;
import taskman.model.company.Company;
import taskman.model.memento.Caretaker;
import taskman.model.memento.ClockMemento;
import taskman.model.memento.DelegatedTaskHandlerMemento;
import taskman.model.memento.DelegatedTaskMemento;
import taskman.model.memento.ProjectMemento;
import taskman.model.memento.ReservableMemento;
import taskman.model.memento.NormalTaskMemento;
import taskman.model.project.Project;
import taskman.model.resource.ResourceType;
import taskman.model.task.DelegatedTask;
import taskman.model.task.NormalTask;
import taskman.model.task.Task;
import taskman.model.task.NormalTask;
import taskman.model.task.TaskFactory;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;

public class CaretakerTest {
	ClockMemento savedClockMemento;
	private Clock clock = new Clock();

	ArrayList<ProjectMemento> savedProjectMementos = new ArrayList<ProjectMemento>();
	ArrayList<ReservableMemento> savedDeveloperMementos = new ArrayList<ReservableMemento>();
	ArrayList<ReservableMemento> savedResourceMementos = new ArrayList<ReservableMemento>();
	ArrayList<NormalTaskMemento> savedTaskMementos = new ArrayList<NormalTaskMemento>();
	Caretaker c = new Caretaker();

	@Test
	public void addProjectMementoTest() {
		Company company = new Company();
		List<ResourceType> resourceTypes = new ArrayList<ResourceType>();
		Project p = new Project("name", "description", new DateTime(2015, 10,
				12, 10, 10), new DateTime(2015, 10, 12, 12, 00),
				new TaskFactory(new BranchOffice(company, "New York",
						resourceTypes), clock));

		ProjectMemento projectMemento = new ProjectMemento(p,
				(ArrayList<NormalTask>) p.getTasks(), "state");
		c.addProjectMemento(projectMemento);
		assertEquals(c.getProjectMementos().get(0), projectMemento);
	}

	@Test
	public void addDeveloperMementoTest() {

		ReservableMemento d = new ReservableMemento(null, null);
		c.addDeveloperMemento(d);
		assertEquals(c.getDeveloperMementos().get(0), d);
	}

	@Test
	public void addResourceMementoTest() {
		ReservableMemento r = new ReservableMemento(null, null);
		c.addResourceMemento(r);
		assertEquals(c.getResourceMementos().get(0), r);
	}

	@Test
	public void addTaskMementoTest() {
		NormalTask t = new NormalTask(clock, "", 10, 10, null, null, null, 1);
		NormalTask t1 = new NormalTask(clock, "", 10, 10, null, null, null, 1);
		ArrayList<NormalTask> tasks = new ArrayList<NormalTask>();
		NormalTaskMemento t2 = new NormalTaskMemento(t, tasks, "name",
				new TimeSpan(new DateTime(2015, 10, 12, 10, 0), new DateTime(
						2015, 10, 12, 12, 0)), t1);
		c.addTaskMemento(t2);
		assertEquals(c.getNormalTaskMementos().get(0), t2);
	}

	@Test
	public void addClockMementoTest() {
		ClockMemento clock = new ClockMemento(null, new DateTime());
		c.addClockMemento(clock);
		assertEquals(c.getClockMemento(), clock);
	}

	@Test
	public void addDelegatedTaskHandlerMemento_True() {
		DelegatedTaskHandlerMemento m = new DelegatedTaskHandlerMemento(null,
				null);
		c.addDelegatedTaskHandlerMemento(m);
		assertEquals(c.getDelegatedTaskHandlerMemento(), m);

	}

	@Test
	public void addDelegateTaskMemento_True() {
		DelegatedTask delegatedTask = new DelegatedTask(clock, "description",
				10, 1, null, true, 1);
		DelegatedTaskMemento m = new DelegatedTaskMemento(delegatedTask, true,
				"task 1", new TimeSpan(new DateTime(2015, 10, 12, 10, 10),
						new DateTime(2015, 10, 12, 12, 10)));
		c.addDelegatedTaskMemento(m);
		assertEquals(c.getDelegatedTaskMementos().get(0), m);
	}
}
