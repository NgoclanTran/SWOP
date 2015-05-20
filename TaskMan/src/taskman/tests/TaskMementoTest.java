package taskman.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.memento.TaskMemento;
import taskman.model.task.Task;
import taskman.model.time.Clock;
import taskman.model.time.TimeSpan;

public class TaskMementoTest {

	ArrayList<Task> dependants = new ArrayList<Task>();
	String stateName;
	TimeSpan timeSpan = new TimeSpan(new DateTime(), new DateTime());
	Task alternative;
	private TaskMemento t;
	private Clock clock = new Clock();

	@Before
	public void setup() {
		Task t1 = new Task(clock,"", 10, 10, null, null, null);
		alternative = new Task(clock,"", 10, 10, null, null, null);
		dependants.add(t1);
		t = new TaskMemento(t1, dependants, "state", timeSpan, alternative);

	}

	@Test
	public void getDependantsTest() {
		assertEquals(t.getDependants(), dependants);
	}

	@Test
	public void getStateTest() {
		assertEquals(t.getStateName(), "state");
	}

	@Test
	public void getTimeSpanTest() {
		assertEquals(t.getTimeSpan(), timeSpan);
	}

	@Test
	public void getAlternativeTest() {
		assertEquals(t.getAlternative(), alternative);
	}

}
