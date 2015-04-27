package taskman.model.resource;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import taskman.exceptions.IllegalTimeException;
import taskman.model.project.task.Task;
import taskman.model.time.TimeSpan;


public class Resource {

	String name;
	DailyAvailability dailyAvailability;

	/**
	 * Creates a new resource with the given name.
	 * 
	 * @param name
	 * 
	 * @throws IllegalArgumentException
	 */
	public Resource(String name, LocalTime startTime, LocalTime endTime) throws IllegalTimeException,IllegalTimeException {
		if (name == null)
			throw new IllegalArgumentException("Name can not be null.");
		if (startTime == null && endTime == null)
			this.dailyAvailability = new DailyAvailability(new LocalTime(0, 0), new LocalTime(23, 59));
		else if (startTime != null && endTime != null)
			this.dailyAvailability = new DailyAvailability(startTime, endTime);
		else
			throw new IllegalArgumentException("Both the start time and end time need to be a localtime or null.");
		this.name = name;
	}

	/**
	 * Returns the name of the resource.
	 * 
	 * @return Returns the name of the resource.
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Returns the daily availability of the resource type.
	 * 
	 * @return Returns the daily availability of the resource type.
	 */
	public DailyAvailability getDailyAvailability() {
		return this.dailyAvailability;
	}

	public boolean isAvailableAt(TimeSpan timeSpan) {
		//TODO check if the DailyAvailiblity of this resource
		// SOLVED, NEEDS TO BE CHECKED PROPERLY
		for (Reservation reservation : reservations) {
			
			DateTime reservationStart = reservation.getTimeSpan().getStartTime();
			DateTime reservationEnd = reservation.getTimeSpan().getEndTime();
			
			boolean before = timeSpan.getStartTime().isBefore(reservationStart)
					&& timeSpan.getEndTime().isBefore(reservationStart);
			boolean after = timeSpan.getStartTime().isAfter(reservationEnd)
					&& timeSpan.getEndTime().isAfter(reservationEnd);
			if (!(before || after)) {
				return false;
			}
		}
		if (this.dailyAvailability.getStartTime().getHourOfDay() >= timeSpan.getStartTime().getHourOfDay()){
			return false;
		}
		if (this.dailyAvailability.getEndTime().getHourOfDay() <= timeSpan.getEndTime().getHourOfDay()){
			return false;
		}
		return true;
	}

	public void addReservation(Task task, TimeSpan timeSpan) throws NullPointerException{
		if(task == null) throw new NullPointerException("The given Task is null.");
		if( timeSpan == null) throw new NullPointerException("The given timeSpan is null.");
		if(!this.isAvailableAt(timeSpan)) throw new IllegalTimeException("The resource cannot be reserved at the given timeSpan.");
		
		Reservation reservation = new Reservation(task, timeSpan);
		reservations.add(reservation);
	}

	public List<Reservation> getReservations() {
		return new ArrayList<Reservation>(reservations);
	}

	private List<Reservation> reservations = new ArrayList<Reservation>();

}
