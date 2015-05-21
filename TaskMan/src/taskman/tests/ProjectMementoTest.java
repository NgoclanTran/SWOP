package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.ProjectMemento;
import taskman.model.project.Project;
import taskman.model.task.Task2;
import taskman.model.task.TaskFactory;
import taskman.model.task.Task2;
import taskman.model.time.Clock;

public class ProjectMementoTest {
	private ArrayList<Task2> tasks = new ArrayList<Task2>();
	private String stateName;
	private ProjectMemento p;
	private Clock clock;
		
	@Before
	public void setup(){
		clock = new Clock();
		Task2 t = new Task2(clock,"",10,10,null,null,null);
		tasks.add(t);
		Project project = new Project("name", "description", new DateTime(2015,10,12,10,10), new DateTime(2015,10,12,12,10), new TaskFactory(clock));
		p = new ProjectMemento(project, tasks, stateName);
	}
	@Test
	public void stateNameTest(){
		assertEquals(p.getStateName(), stateName);
	}
	@Test
	public void tasksTest(){
		assertEquals(p.getTasks(), tasks);
	}
}
