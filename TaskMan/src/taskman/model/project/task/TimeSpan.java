package taskman.model.project.task;

import org.joda.time.DateTime;

import taskman.exceptions.IllegalDateException;

public class TimeSpan {

public TimeSpan(DateTime startTime, DateTime endTime){
	//TODO MOETEN BINNEN EEN WERKDAG
	if(startTime.compareTo(endTime) > 0) throw new IllegalArgumentException("The end time cannot be later than de start time.");
	if(startTime.getHourOfDay() < 8) throw new IllegalDateException("The day starts at 8:00");
if(endTime.getMinuteOfDay() > 1020) throw new IllegalDateException("The day ends at 17:00");
	this.startTime = startTime;
	this.endTime = endTime;	
}
private DateTime startTime;
public DateTime getStartTime(){
	return this.startTime;
}

private DateTime endTime;
public DateTime getEndTime(){
	return this.endTime;
}
public int calculatePerformedTime(){
	int hoursStart = this.startTime.getMinuteOfDay();
	int hoursEnd = this.endTime.getMinuteOfDay();
	
	
	return hoursEnd - hoursStart;
}

}
