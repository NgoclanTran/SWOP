package taskman.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import taskman.exceptions.IllegalTimeException;
import taskman.model.resource.*;
import taskman.model.time.TimeSpan;

public class ResourceTypeTest {
	
	private String name;
	private ArrayList<ResourceType> requires;
	private ArrayList<ResourceType> conflictWith;
	private ResourceType requiredResource, conflictResource;
	private LocalTime startTime, endTime;
	@Before
	public void setUp() throws Exception {
		name = "name";
		
		requiredResource = new ResourceType(name, new ArrayList<ResourceType>(), new ArrayList<ResourceType>());
		requires = new ArrayList<ResourceType>();
		requires.add(requiredResource);
		
		conflictResource = new ResourceType(name, new ArrayList<ResourceType>(), new ArrayList<ResourceType>());
		conflictWith = new ArrayList<ResourceType>();
		conflictWith.add(conflictResource);
		startTime = new LocalTime(10,0);
		endTime = new LocalTime(16,0);
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_Name_Null(){
		ResourceType r = new ResourceType(null, requires, conflictWith);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_Requires_Null(){
		ResourceType r = new ResourceType(name, null, conflictWith);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest_ConflictWith_Null(){
		ResourceType r = new ResourceType(name, requires, null);
	}
	
	@Test
	public void constructorTest_TrueCase(){
		ResourceType r = new ResourceType(name, requires, conflictWith);
		assertEquals(r.getName(), name);
		assertEquals(r.getRequires(),requires);
		assertEquals(r.getConflictsWith(), conflictWith);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addResourceTest_Name_Null(){
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource(null, startTime, endTime);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addResourceTypeTest_StartTime_Null(){
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource(name, null, endTime);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void addResourceTest_EndTIme_Null(){
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource(name, startTime, null);
	}
	@Test (expected = IllegalTimeException.class)
	public void addResourceTest_StartTimeAfterEndTime(){	
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource(name, endTime, startTime);
	}
	@Test
	public void addResourceTest_StartAndEndTime_Null(){
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource(name, null, null);
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,12,0));
		ArrayList<Resource> a = (ArrayList<Resource>) r.getAvailableResources(timeSpan);
		assertEquals(a.get(0).getName(), name);
		assertEquals(a.get(0).getDailyAvailability().getStartTime().getMinuteOfHour(),0);
		assertEquals(a.get(0).getDailyAvailability().getStartTime().getHourOfDay(),0);
		assertEquals(a.get(0).getDailyAvailability().getEndTime().getHourOfDay(),0);
		assertEquals(a.get(0).getDailyAvailability().getEndTime().getMinuteOfHour(),0);
		
	}
	
	@Test
	public void addResourceTypeTest_TrueCase(){
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource(name, startTime, endTime);
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,12,0));
		ArrayList<Resource> a = (ArrayList<Resource>) r.getAvailableResources(timeSpan);
		assertEquals(a.get(0).getName(), name);
		assertEquals(a.get(0).getDailyAvailability().getStartTime(), startTime);
		assertEquals(a.get(0).getDailyAvailability().getEndTime(), endTime);
	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void getSuggestedResourcesTest_TimeSpan_Null(){
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.getSuggestedResources(null, 10);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void getSuggestedResourcesTest_Amount_Negative(){
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,12,0));
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.getSuggestedResources(timeSpan, -1);
	}
	
	@Test
	public void getSuggestedResourcesTest_Amount_Smaller_Than_Available(){
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,12,0));
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource(name, startTime, endTime);
		r.addResource(name, startTime, endTime);
		r.addResource(name, startTime, endTime);
		assertEquals(r.getSuggestedResources(timeSpan, 2).size(), 2);
	}
	
	@Test
	public void getSuggestedResourcesTest_Amount_Bigger_Than_Available(){
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,12,0));
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource(name, startTime, endTime);
		r.addResource(name, startTime, endTime);
		r.addResource(name, startTime, endTime);
		assertEquals(r.getSuggestedResources(timeSpan, 1).size(), 1);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void getAvailableResourcesTest_TimeSpan_Null(){
		
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource("name1", startTime, endTime);
		r.addResource("name2", startTime, endTime);
		r.addResource("name3", startTime, endTime);
		r.getAvailableResources(null);
		
	}
	
	@Test
	public void getAvailableResourcesTypeTest(){
		TimeSpan timeSpan = new TimeSpan(new DateTime(2015,10,12,10,0), new DateTime(2015,10,12,12,0));
		ResourceType r = new ResourceType(name, requires, conflictWith);
		r.addResource("name1", startTime, endTime);
		r.addResource("name2", startTime, endTime);
		r.addResource("name3", startTime, endTime);
		assertEquals(r.getAvailableResources(timeSpan).size(), 3);
	}
	


}
