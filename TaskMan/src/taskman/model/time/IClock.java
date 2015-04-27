package taskman.model.time;

import org.joda.time.DateTime;

public interface IClock {

	public DateTime getSystemTime();
	
	public DateTime getFirstPossibleStartTime(DateTime time);
	
	public DateTime addBreaks(DateTime time);
	
	public DateTime addMinutes(DateTime time, int minutesToAdd);
	
	public DateTime getExactHour(DateTime time);
	
	public DateTime resetSecondsAndMilliSeconds(DateTime time);

}
