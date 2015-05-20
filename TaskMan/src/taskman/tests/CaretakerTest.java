package taskman.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Test;

import taskman.model.memento.Caretaker;
import taskman.model.memento.ClockMemento;
import taskman.model.memento.ProjectMemento;
import taskman.model.memento.ReservableMemento;
import taskman.model.memento.TaskMemento;
import taskman.model.project.Project;
import taskman.model.task.Task;
import taskman.model.task.TaskFactory;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;

public class CaretakerTest {
	ClockMemento savedClockMemento;
	private Clock clock = new Clock();
	
	ArrayList<ProjectMemento> savedProjectMementos = new ArrayList<ProjectMemento>();
	ArrayList<ReservableMemento> savedDeveloperMementos = new ArrayList<ReservableMemento>();
	ArrayList<ReservableMemento> savedResourceMementos = new ArrayList<ReservableMemento>();
	ArrayList<TaskMemento> savedTaskMementos = new ArrayList<TaskMemento>();
	Caretaker c = new Caretaker();

	@Test
	public void addProjectMementoTest() {
		Project p = new Project("name", "description", new DateTime(2015,10,12,10,10), new DateTime(2015,10,12,12,00), new TaskFactory(clock));
		
		ProjectMemento projectMemento = new ProjectMemento(p, (ArrayList<Task>) p.getTasks(), "state");
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
		Task t = new Task(clock,"", 10, 10, null, null, null);
		Task t1 = new Task(clock,"", 10, 10, null, null, null);
		ArrayList<Task> tasks = new ArrayList<Task>();
		TaskMemento t2 = new TaskMemento(t, tasks, "name", new TimeSpan(
				new DateTime(2015, 10, 12, 10, 0), new DateTime(2015, 10, 12,
						12, 0)), t1);
		c.addTaskMemento(t2);
		assertEquals(c.getTaskMementos().get(0), t2);
	}

	@Test
	public void addClockMementoTest() {
		ClockMemento clock = new ClockMemento(null, new DateTime());
		c.addClockMemento(clock);
		assertEquals(c.getClockMemento(), clock);
	}
}
