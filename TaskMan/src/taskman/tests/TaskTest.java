package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.project.task.Status;
import taskman.model.project.task.Task;
import taskman.model.project.task.TimeSpan;

public class TaskTest {
	private String description;
	private int estimatedDuration;
	private int acceptableDeviation;
	private List<Task> dependencies;
	private Status status;
	private TimeSpan timespan;

@Before
public void setup(){
	description = "description";
	estimatedDuration = 1;
	acceptableDeviation = 1;
	dependencies = new ArrayList<Task>();
	
}

@Test
public void constructorTest_TrueCase(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation);
	assertEquals(t.getDescription(), description);
	assertEquals(t.getEstimatedDuration(), estimatedDuration);
	assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
	
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dependencies);
	assertEquals(t2.getDescription(), description);
	assertEquals(t2.getEstimatedDuration(), estimatedDuration);
	assertEquals(t2.getAcceptableDeviation(), acceptableDeviation);
	assertEquals(t2.getDependencies(), dependencies);
}

@Test
public void setStatusTest_TrueCase(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation);
	t.setStatus(Status.FINISHED);
	assertEquals(t.getStatus(), Status.FINISHED);
	
}

@Test
public void updateStatusAndTimeSpanTest_TrueCase(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation);
	DateTime start = new DateTime(2015,1,1,1,1);
	DateTime end = new DateTime(2016,1,1,1,1);
	t.updateStatusAndTimeSpan(Status.FAILED, start, end );
	
	assertEquals(t.getTimeSpan().getStartTime(), start);
	assertEquals(t.getTimeSpan().getEndTime(), end);
	assertEquals(t.getStatus(), Status.FAILED);
	
}

}
