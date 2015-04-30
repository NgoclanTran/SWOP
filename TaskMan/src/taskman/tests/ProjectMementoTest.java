package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.ProjectMemento;
import taskman.model.project.task.Task;

public class ProjectMementoTest {
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private String stateName;
	private ProjectMemento p;
		
	@Before
	public void setup(){
		Task t = new Task("",10,10,null,null,null);
		tasks.add(t);
		p = new ProjectMemento(tasks, stateName);
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
