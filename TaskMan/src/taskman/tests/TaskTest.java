package taskman.tests;
import taskman.exceptions.IllegalDateException;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import taskman.model.project.task.Task;
import taskman.model.project.task.TimeSpan;

public class TaskTest {
	private String description;
	private int estimatedDuration;
	private int acceptableDeviation;
	private List<Task> dependencies;
	private TimeSpan timespan;

@Before
public void setup(){
	description = "description";
	estimatedDuration = 10;
	acceptableDeviation = 1;
	dependencies = new ArrayList<Task>();
	
}

@Test(expected = NullPointerException.class)
public void constructor1Test_FalseCase_DescriptionNull(){
	Task t = new Task(null, estimatedDuration, acceptableDeviation,dependencies,null);
	
}
@Test(expected = IllegalArgumentException.class)
public void constructor1Test_FalseCase_EstimatedDuration(){
	Task t = new Task(description, -10, acceptableDeviation,dependencies, null);
	
}
@Test(expected = IllegalArgumentException.class)
public void constructor1Test_FalseCase_acceptableDeviation(){
	Task t = new Task(description, estimatedDuration, -10,dependencies, null);
}
@Test(expected = NullPointerException.class)
public void constructor2Test_FalseCase_DescriptionNull(){
	Task t = new Task(null, estimatedDuration, acceptableDeviation,dependencies,null);
	
}
@Test(expected = IllegalArgumentException.class)
public void constructor2Test_FalseCase_EstimatedDuration(){
	Task t = new Task(description, -10, acceptableDeviation,dependencies,null);
	
}
@Test(expected = IllegalArgumentException.class)
public void constructor2Test_FalseCase_acceptableDeviation(){
	Task t = new Task(description, estimatedDuration, -10,dependencies,null);
}

@Test(expected = IllegalArgumentException.class)
public void constructor2Test_FalseCase_dependencies(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,null,null);
}

@Test
public void constructorTest_TrueCase(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	assertEquals(t.getDescription(), description);
	assertEquals(t.getEstimatedDuration(), estimatedDuration);
	assertEquals(t.getAcceptableDeviation(), acceptableDeviation);
	
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dependencies,null);
	assertEquals(t2.getDescription(), description);
	assertEquals(t2.getEstimatedDuration(), estimatedDuration);
	assertEquals(t2.getAcceptableDeviation(), acceptableDeviation);
	assertEquals(t2.getDependencies(), dependencies);
	//test getStatus
}

@Test
public void getDependenciesTest_TrueCase(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies,null);
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dependencies,null);
	dependencies.add(t2);
	
	//test if the dependencies of our task didn't changed
	assertNotEquals(t.getDependencies(), dependencies);
}
@Test (expected = NullPointerException.class)
public void addTimeSpanTest_FalseCase_StartNull(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime endTime = new DateTime(2015,1,1,1,1);
	t.addTimeSpan(true, null, endTime);
}

@Test (expected = NullPointerException.class)
public void addTimeSpanTest_FalseCase_EndNull(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,1,1);
	t.addTimeSpan(true, startTime, null);
}
@Test (expected = IllegalDateException.class)
public void addTimeSpanTest_FalseCase_Date(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies,null);
	DateTime startTime = new DateTime(2015,1,1,1,1);
	DateTime endTime = new DateTime(2016,1,1,1,1);
	t.addTimeSpan(true, endTime, startTime);
}

@Test (expected = IllegalStateException.class)
public void addTimeSpanTest_TrueCase_StatusUNAVAILABLE(){

	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getStatusName(),"FAILED");
	ArrayList<Task> dep = new ArrayList<Task>();
	dep.add(t);
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dep, null);

	t.addTimeSpan(true, startTime, endTime);
}

@Test
public void addTimeSpanTest_TrueCase_StatusAVAILABLE(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,12,1);
	assertEquals(t.getStatusName(),"AVAILABLE");
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getStatusName(), "FAILED");
}

@Test (expected = IllegalDateException.class)
public void addTimeSpanTest_FalseCase_StartTime(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,6,1);
	DateTime endTime = new DateTime(2016,1,1,12,1);
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getStatusName(), "FAILED");
}

@Test (expected = IllegalDateException.class)
public void addTimeSpanTest_FalseCase_EndTime(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies,null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,18,1);
	assertEquals(t.getStatusName(),"AVAILABLE");
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getStatusName(), "FAILED");
}
  
@Test (expected = NullPointerException.class)
public void addAlternativeTest_FalseCase_AlternativeNull(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	t.addAlternative(null);
}
@Test (expected = IllegalStateException.class)
public void addAlternativeTest_TrueCase_StatusNotFAILED(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	t.addAlternative(t2);
	assertEquals(t.getStatusName(),"UNAVAILABLE");
	assertNull(t.getAlternative());
}

@Test
public void addAlternativeTest_TrueCase_StatusFAILED(){

	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getStatusName(),"FAILED");
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	
	
	t.addAlternative(t2);
	assertEquals(t.getAlternative(),t2);
	assertEquals(t.getStatusName(),"FAILED");
}



@Test (expected = IllegalStateException.class)
public void isAvailableTest_StatusAVAILABLE(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	t.updateTaskAvailability();
	assertTrue(t.isAvailable());
	assertEquals(t.getStatusName(),"AVAILABLE");
	
}
@Test
public void isAvailableTest_StatusUNAVAILABLE(){
	
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getStatusName(),"FAILED");
	ArrayList<Task> dep = new ArrayList<Task>();
	dep.add(t);
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dep, null);
	assertEquals(t2.getStatusName(),"UNAVAILABLE");
	assertFalse(t2.isAvailable());
	assertEquals(t2.getStatusName(),"UNAVAILABLE");
}
@Test
public void isAvailableTest_StatusFINISHED(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	t.addTimeSpan(false, startTime, endTime);
	assertFalse(t.isAvailable());
	assertEquals(t.getStatusName(),"FINISHED");
}
@Test
public void isAvailableTest_StatusFAILED(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	t.addTimeSpan(true, startTime, endTime);
	assertFalse(t.isAvailable());
	assertEquals(t.getStatusName(),"FAILED");
}
@Test
public void isFinishedTest_StatusAVAILABLE(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	assertFalse(t.isFinished());
	assertEquals(t.getStatusName(),"AVAILABLE");
	
}
@Test
public void isFinishedTest_StatusUNAVAILABLE(){

	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getStatusName(),"FAILED");
	ArrayList<Task> dep = new ArrayList<Task>();
	dep.add(t);
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dep, null);
	
	assertFalse(t2.isFinished());
	assertEquals(t2.getStatusName(),"UNAVAILABLE");
}
@Test
public void isFinishedTest_StatusFINISHED(){

	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(false, startTime, endTime);

	assertTrue(t.isFinished());
	assertEquals(t.getStatusName(),"FINISHED");
}
@Test
public void isFinishedTest_StatusFAILED(){

	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(true, startTime, endTime);
	assertFalse(t.isFinished());
	assertEquals(t.getStatusName(),"FAILED");
}

@Test
public void isFailedTest_StatusAVAILABLE(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);

	assertFalse(t.isFailed());
	assertEquals(t.getStatusName(),"AVAILABLE");
	
}
@Test
public void isFailedTest_StatusUNAVAILABLE(){

	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getStatusName(),"FAILED");
	ArrayList<Task> dep = new ArrayList<Task>();
	dep.add(t);
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dep, null);
	
	assertFalse(t2.isFailed());
	assertEquals(t2.getStatusName(),"UNAVAILABLE");
}
@Test
public void isFailedTest_StatusFINISHED(){

	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(false, startTime, endTime);
	assertFalse(t.isFailed());
	
}
@Test
public void isFailedTest_StatusFAILED(){

	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(true, startTime, endTime);
	
	assertTrue(t.isFailed());
	assertEquals(t.getStatusName(),"FAILED");
}

@Test
public void isFailedTest_TrueCase(){
	
}

@Test
public void isFailedTest_FalseCase(){
	
}
@Test
public void isFinishedTest_TrueCase(){
	
}

@Test
public void isFinishedTest_FalseCase(){
	
}

@Test (expected = IllegalStateException.class)
public void updateTaskAvailabilityTest_TrueCase_StatusAVAILABLE(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	t.updateTaskAvailability();
	assertEquals(t.getStatusName(),"AVAILABLE");	
	t.updateTaskAvailability();
}

@Test (expected = IllegalStateException.class)
public void updateTaskAvailabilityTest_TrueCase_StatusFINISHED(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(false, startTime, endTime);
	t.updateTaskAvailability();
	assertEquals(t.getStatusName(),"FINISHED");	
}

@Test (expected = IllegalStateException.class)
public void updateTaskAvailabilityTest_FalseCase_StatusFAILED(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,1);
	DateTime endTime = new DateTime(2016,1,1,10,1);
	
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getStatusName(),"FAILED");
	t.updateTaskAvailability();

}

@Test
public void calculateTotalExecutionTimeTest_TrueCase(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,0);
	DateTime endTime = new DateTime(2015,1,1,16,0);
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getTotalExecutionTime(),6*60);
}

@Test (expected = IllegalStateException.class)
public void calculateTotalExecutionTimeTest_TrueCase_NoTimeSpan(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	int exeTime = t.getTotalExecutionTime();
}

@Test
public void calculateTotalExecutionTimeTest_TrueCase_Alternative(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	Task t2 = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	Task t3 = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,0);
	DateTime endTime = new DateTime(2015,1,1,11,0);
	assertEquals(t.getStatusName(), "AVAILABLE");
	t.addTimeSpan(true, startTime, endTime);
	t.addAlternative(t2);
	assertEquals(t.getTotalExecutionTime(),60);
	
	t2.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getTotalExecutionTime(),120);
	t2.addAlternative(t3);

	t3.addTimeSpan(false, startTime, endTime);
	assertEquals(t.getTotalExecutionTime(),180);
	assertEquals(t3.getTotalExecutionTime(),60);
	
}
@Test (expected = IllegalStateException.class)
public void calculateOverduePercentageTest_TrueCase_NoTimeSpan(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	int overduePerc = t.getOverduePercentage();
}

@Test
public void calculateOverduePercentageTest_TrueCase(){
	Task t = new Task(description, estimatedDuration, acceptableDeviation,dependencies, null);
	DateTime startTime = new DateTime(2015,1,1,10,0);
	DateTime endTime = new DateTime(2015,1,1,11,0);
	t.addTimeSpan(true, startTime, endTime);
	assertEquals(t.getOverduePercentage(),(60-this.estimatedDuration)/this.estimatedDuration);

}

}
