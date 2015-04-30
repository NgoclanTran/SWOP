package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import taskman.model.memento.Caretaker;
import taskman.model.memento.ClockMemento;
import taskman.model.memento.ProjectMemento;
import taskman.model.memento.ReservableMemento;
import taskman.model.memento.TaskMemento;
import taskman.model.project.task.Task;
import taskman.model.resource.Resource;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class CaretakerTest {
	ClockMemento savedClockMemento;
	ArrayList<ProjectMemento> savedProjectMementos = new ArrayList<ProjectMemento>();
	ArrayList<ReservableMemento> savedDeveloperMementos = new ArrayList<ReservableMemento>();
	ArrayList<ReservableMemento> savedResourceMementos = new ArrayList<ReservableMemento>();
	ArrayList<TaskMemento> savedTaskMementos = new ArrayList<TaskMemento>();
	Caretaker c = new Caretaker();
	@Test
	public void addProjectMementoTest(){
		Task t = new Task("",10,10,null,null,null);
		ArrayList<Task> tasks = new ArrayList<Task>();
		ProjectMemento projectMemento = new ProjectMemento(tasks, "state");
		c.addProjectMemento(projectMemento);
		assertEquals(c.getProjectMemento(0), projectMemento);
	}
	@Test
	public void addDeveloperMementoTest(){

		ReservableMemento d = new ReservableMemento(null);
		c.addDeveloperMemento(d);
		assertEquals(c.getDeveloperMemento(0), d);
	}
	@Test
	public void addResourceMementoTest(){
		ReservableMemento r = new ReservableMemento(null);
		c.addResourceMemento(r);
		assertEquals(c.getResourceMemento(0),r);
	}
	@Test
	public void addTaskMementoTest(){
		Task t = new Task("",10,10,null,null,null);
		Task t1 = new Task("",10,10,null,null,null);
		ArrayList<Task> tasks = new ArrayList<Task>();
		TaskMemento t2 = new TaskMemento(tasks, "name", new TimeSpan(new DateTime(), new DateTime()), t1);
		c.addTaskMemento(t2);
		assertEquals(c.getTaskMemento(0),t2);
	}
	@Test
	public void addClockMementoTest(){
		ClockMemento clock  = new ClockMemento(new DateTime());
		c.addClockMemento(clock);
		assertEquals(c.getClockMemento(),clock);
	}
}
