package taskman.model.time;

import org.joda.time.DateTime;

public interface IClock {

	public DateTime getSystemTime();
	
	public DateTime getFirstPossibleStartTime(DateTime time);
	
	public DateTime addBreaks(DateTime time);
	
	public DateTime resetSecondsAndMilliSeconds(DateTime time);

}
