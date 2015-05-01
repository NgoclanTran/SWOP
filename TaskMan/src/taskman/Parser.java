package taskman;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFileChooser;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.UserHandler;
import taskman.model.project.Project;
import taskman.model.project.task.Task;
import taskman.model.resource.Resource;
import taskman.model.resource.ResourceType;
import taskman.model.time.Clock;
import taskman.model.time.DailyAvailability;
import taskman.model.time.TimeSpan;
import taskman.model.user.Developer;

public class Parser {

	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private ProjectHandler projectHandler;
	private ResourceHandler resourceHandler;
	private UserHandler userHandler;

	private List<DailyAvailability> dailyAvailability = new ArrayList<DailyAvailability>();
	private List<Planning> planning = new ArrayList<Planning>();
	private Map<Integer, DailyAvailability> rtda = new HashMap<Integer, DailyAvailability>();
	private int resourceTypeNumber = 0;

	public Parser(ProjectHandler ph, ResourceHandler rh, UserHandler uh) {
		projectHandler = ph;
		resourceHandler = rh;
		userHandler = uh;
	}

	public void parse() {
		List<String> file = readFile();
		int declaring = 0;
		ArrayList<String> dailyAvailabilities = new ArrayList<String>();
		ArrayList<String> resourceTypes = new ArrayList<String>();
		ArrayList<String> resources = new ArrayList<String>();
		ArrayList<String> developers = new ArrayList<String>();
		ArrayList<String> projects = new ArrayList<String>();
		ArrayList<String> plannings = new ArrayList<String>();
		ArrayList<String> tasks = new ArrayList<String>();
		ArrayList<String> reservations = new ArrayList<String>();

		for (String line : file) {
			line = line.trim();
			if (line.startsWith("#")) {
				continue;
			}
			if (line.contains("#")) {
				line = line.substring(0, line.indexOf("#"));
			}

			if (declaring == 0) {
				if (line.startsWith("dailyAvailability")) {
					declaring++;
				} else if (line.startsWith("systemTime")) {
					Clock systemClock = Clock.getInstance();
					int descriptionStart = line.indexOf("\"") + 1;
					int descriptionEnd = line.length() - 1;
					DateTime systemTime = new DateTime(
							parseDate(line.substring(descriptionStart,
									descriptionEnd)));
					systemClock.setSystemTime(systemTime);
				}
			}

			if (declaring == 1) {
				if (line.startsWith("resourceTypes")) {
					declaring++;
				} else {
					dailyAvailabilities.add(line);
				}

			}
			if (declaring == 2) {
				if (line.startsWith("resources")) {
					declaring++;
				} else {
					resourceTypes.add(line);
				}

			}
			if (declaring == 3) {
				if (line.startsWith("developers")) {
					declaring++;
				} else {
					resources.add(line);
				}

			}
			if (declaring == 4) {
				if (line.startsWith("projects")) {
					declaring++;
				} else {
					developers.add(line);
				}

			}
			if (declaring == 5) {
				if (line.startsWith("plannings")) {
					declaring++;
				} else {
					projects.add(line);
				}

			}
			if (declaring == 6) {
				if (line.startsWith("tasks")) {
					declaring++;
				} else {
					plannings.add(line);
				}

			}
			if (declaring == 7) {
				if (line.startsWith("reservations")) {
					declaring++;
				} else {
					tasks.add(line);
				}

			}
			if (declaring == 8) {
				reservations.add(line);
			}

		}
		parseDailyAvailability(dailyAvailabilities);
		parseResourceTypes(resourceTypes);
		parseResources(resources);
		parseDevelopers(developers);
		parseProjects(projects);
		parsePlannings(plannings);
		parseTasks(tasks);
		parseReservations(reservations);
	}

	private List<String> readFile() {
		List<String> file = null;
		try {
			System.out.println("Open the input file.");
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = Files.readAllLines(
						Paths.get(fc.getSelectedFile().getAbsolutePath()),
						Charset.defaultCharset());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;

	}

	private Date parseDate(String line) {
		try {
			return dateFormat.parse(line);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private ArrayList<Integer> parseIntegerList(String line) {
		ArrayList<Integer> integerList = new ArrayList<Integer>();
		int end;
		line = line.replace("[", "");
		line = line.replace("]", "");
		line = line.trim();
		while (line.contains(",")) {
			end = line.indexOf(",");
			integerList.add(Integer.parseInt(line.substring(0, end)));
			line = line.substring(end);
			line = line.replaceFirst(",", "");
			line = line.trim();
		}
		integerList.add(Integer.parseInt(line));
		return integerList;
	}

	private LinkedHashMap<Integer, Integer> parseIntegerMap(String line) {
		LinkedHashMap<Integer, Integer> integerMap = new LinkedHashMap<Integer, Integer>();
		int descriptionStart, descriptionEnd;
		int type, quantity;
		while (line.contains("type")) {
			descriptionStart = line.indexOf("type: ");
			descriptionEnd = line.indexOf(",", descriptionStart);
			type = Integer.parseInt(line.substring(descriptionStart + 6,
					descriptionEnd));
			descriptionStart = line.indexOf("quantity: ", descriptionEnd);
			descriptionEnd = line.indexOf("}", descriptionStart);
			quantity = Integer.parseInt(line.substring(descriptionStart + 10,
					descriptionEnd));
			line = line.substring(descriptionEnd);
			integerMap.put(type, quantity);
		}
		return integerMap;
	}

	private void parseDailyAvailability(ArrayList<String> dailyAvailabilities) {
		boolean describing = false;
		List<String> description = new ArrayList<String>();
		for (String line : dailyAvailabilities) {
			if (line.startsWith("resourceTypes")) {
				describing = false;
				description.clear();
			}
			if (line.startsWith("-") || line.equals("")) {
				line = line.replace("-", "");
				line = line.trim();
				if (description != null && !description.isEmpty()) {
					addDailyAvailability(description);
					description.clear();
				}
				describing = true;
			}
			if (describing && !line.equals("")) {
				description.add(line);
			}
		}
	}

	private void addDailyAvailability(List<String> description) {
		int startHour = 0;
		int startMinute = 0;
		int endHour = 0;
		int endMinute = 0;
		int descriptionStart;
		int descriptionEnd;
		for (String line : description) {
			descriptionStart = line.indexOf("\"") + 1;
			descriptionEnd = line.indexOf("\"", descriptionStart);
			if (line.startsWith("startTime")) {
				startHour = Integer.parseInt(line.substring(descriptionStart,
						descriptionStart + 2));
				startMinute = Integer.parseInt(line.substring(
						descriptionEnd - 2, descriptionEnd));
			} else if (line.startsWith("endTime")) {
				endHour = Integer.parseInt(line.substring(descriptionStart,
						descriptionStart + 2));
				endMinute = Integer.parseInt(line.substring(descriptionEnd - 2,
						descriptionEnd));
			}
		}

		DailyAvailability dailyAvailability = new DailyAvailability(
				new LocalTime(startHour, startMinute), new LocalTime(endHour,
						endMinute));
		this.dailyAvailability.add(dailyAvailability);
	}

	private void parseResourceTypes(ArrayList<String> resourceTypes) {
		boolean describing = false;
		List<String> description = new ArrayList<String>();
		for (String line : resourceTypes) {
			if (line.startsWith("resources")) {
				describing = false;
				description.clear();
			}
			if (line.startsWith("-") || line.equals("")) {
				line = line.replace("-", "");
				line = line.trim();
				if (description != null && !description.isEmpty()) {
					addResourceType(description);
					description.clear();
				}
				describing = true;
			}
			if (describing && !line.equals("")) {
				description.add(line);
			}
		}
	}

	private void addResourceType(List<String> description) {
		int descriptionStart;
		int descriptionEnd;
		String name = null;
		List<Integer> requires = null;
		List<Integer> conflictsWith = null;
		int dailyAvailability = -1;
		boolean conflictsWithSelf = false;
		for (String line : description) {
			if (line.startsWith("name")) {
				descriptionStart = line.indexOf("\"") + 1;
				descriptionEnd = line.length() - 1;
				name = line.substring(descriptionStart, descriptionEnd);
			} else if (line.startsWith("requires") && !line.endsWith(": []")) {
				descriptionStart = line.indexOf("[");
				descriptionEnd = line.length() - 1;
				requires = parseIntegerList(line.substring(descriptionStart,
						descriptionEnd));
			} else if (line.startsWith("conflictsWith")
					&& !line.endsWith(": []")) {
				descriptionStart = line.indexOf("[");
				descriptionEnd = line.length() - 1;
				conflictsWith = parseIntegerList(line.substring(
						descriptionStart, descriptionEnd));
			} else if (line.startsWith("dailyAvailability")
					&& !line.endsWith(":")) {
				descriptionStart = line.indexOf(":") + 2;
				descriptionEnd = line.length();
				dailyAvailability = Integer.parseInt(line.substring(
						descriptionStart, descriptionEnd));
			}
		}

		List<ResourceType> requiredTypes = new ArrayList<ResourceType>();
		List<ResourceType> conflictingTypes = new ArrayList<ResourceType>();

		if (requires != null) {
			for (int i : requires) {
				requiredTypes.add(resourceHandler.getResourceTypes().get(i));
			}
		}
		if (conflictsWith != null) {
			if (conflictsWith.contains(resourceHandler.getResourceTypes()
					.size())) {
				conflictsWith.remove(conflictsWith.size() - 1);
				conflictsWithSelf = true;
			}
			for (int i : conflictsWith) {
				conflictingTypes.add(resourceHandler.getResourceTypes().get(i));
			}
		}

		resourceHandler.addResourceType(name, requiredTypes, conflictingTypes,
				conflictsWithSelf);

		if (dailyAvailability != -1) {
			rtda.put(resourceTypeNumber,
					this.dailyAvailability.get(dailyAvailability));
		}
		resourceTypeNumber++;
	}

	private void parseResources(ArrayList<String> resources) {
		boolean describing = false;
		List<String> description = new ArrayList<String>();
		for (String line : resources) {
			if (line.startsWith("developers")) {
				describing = false;
				description.clear();
			}
			if (line.startsWith("-") || line.equals("")) {
				line = line.replace("-", "");
				line = line.trim();
				if (description != null && !description.isEmpty()) {
					addResource(description);
					description.clear();
				}
				describing = true;
			}
			if (describing && !line.equals("")) {
				description.add(line);
			}
		}
	}

	private void addResource(List<String> description) {
		int descriptionStart;
		int descriptionEnd;
		String name = null;
		int type = -1;
		for (String line : description) {
			if (line.startsWith("name")) {
				descriptionStart = line.indexOf("\"") + 1;
				descriptionEnd = line.length() - 1;
				name = line.substring(descriptionStart, descriptionEnd);
			} else if (line.startsWith("type")) {
				descriptionStart = line.indexOf(":") + 2;
				descriptionEnd = line.length();
				type = Integer.parseInt(line.substring(descriptionStart,
						descriptionEnd));
			}
		}

		LocalTime startTime = null, endTime = null;

		if (rtda.containsKey(type)) {
			startTime = rtda.get(type).getStartTime();
			endTime = rtda.get(type).getEndTime();
		} else {
			startTime = new LocalTime("00:00");
			endTime = new LocalTime("23:59");
		}
		resourceHandler.getResourceTypes().get(type)
				.addResource(name, startTime, endTime);
	}

	private void parseDevelopers(ArrayList<String> developers) {
		boolean describing = false;
		List<String> description = new ArrayList<String>();
		for (String line : developers) {
			if (line.startsWith("projects")) {
				describing = false;
				description.clear();
			}
			if (line.startsWith("-") || line.equals("")) {
				line = line.replace("-", "");
				line = line.trim();
				if (description != null && !description.isEmpty()) {
					addDeveloper(description);
					description.clear();
				}
				describing = true;
			}
			if (describing && !line.equals("")) {
				description.add(line);
			}
		}
	}

	private void addDeveloper(List<String> description) {
		int descriptionStart;
		int descriptionEnd;
		String name = null;
		for (String line : description) {
			if (line.startsWith("name")) {
				descriptionStart = line.indexOf("\"") + 1;
				descriptionEnd = line.length() - 1;
				name = line.substring(descriptionStart, descriptionEnd);
			}
		}
		userHandler.addDeveloper(name);
	}

	private void parseProjects(ArrayList<String> projects) {
		boolean describing = false;
		List<String> description = new ArrayList<String>();
		for (String line : projects) {
			if (line.startsWith("plannings")) {
				describing = false;
				description.clear();
			}
			if (line.startsWith("-") || line.equals("")) {
				line = line.replace("-", "");
				line = line.trim();
				if (description != null && !description.isEmpty()) {
					addProject(description);
					description.clear();
				}
				describing = true;
			}
			if (describing && !line.equals("")) {
				description.add(line);
			}
		}
	}

	private void addProject(List<String> description) {
		int descriptionStart, descriptionEnd;
		String name = "";
		String projectDescription = "";
		Date creationTime = null, dueTime = null;
		for (String line : description) {
			descriptionStart = line.indexOf("\"") + 1;
			descriptionEnd = line.length() - 1;
			if (line.startsWith("name")) {
				name = line.substring(descriptionStart, descriptionEnd);
			} else if (line.startsWith("description")) {
				projectDescription = line.substring(descriptionStart,
						descriptionEnd);
			} else if (line.startsWith("creationTime")) {
				creationTime = parseDate(line.substring(descriptionStart,
						descriptionEnd));
			} else if (line.startsWith("dueTime")) {
				dueTime = parseDate(line.substring(descriptionStart,
						descriptionEnd));
			}
		}
		projectHandler.addProject(name, projectDescription, new DateTime(
				creationTime), new DateTime(dueTime));
	}

	private void parsePlannings(ArrayList<String> plannings) {
		boolean describing = false;
		List<String> description = new ArrayList<String>();
		for (String line : plannings) {
			if (line.startsWith("tasks")) {
				describing = false;
				description.clear();
			}
			if (line.startsWith("-") || line.equals("")) {
				line = line.replaceFirst("-", "");
				line = line.trim();
				if (description != null && !description.isEmpty()) {
					addPlanning(description);
					description.clear();
				}
				describing = true;
			}
			if (describing && !line.equals("")) {
				description.add(line);
			}
		}
	}

	private void addPlanning(List<String> description) {
		int descriptionStart, descriptionEnd;
		ArrayList<Integer> developers = new ArrayList<Integer>();
		LinkedHashMap<Integer, Integer> resources = new LinkedHashMap<Integer, Integer>();
		Date plannedStartTime = null;
		for (String line : description) {
			if (line.startsWith("plannedStartTime")) {
				descriptionStart = line.indexOf("\"") + 1;
				descriptionEnd = line.length() - 1;
				plannedStartTime = parseDate(line.substring(descriptionStart,
						descriptionEnd));
			} else if (line.startsWith("developers")) {
				descriptionStart = line.indexOf("[");
				descriptionEnd = line.length() - 1;
				developers = parseIntegerList(line.substring(descriptionStart,
						descriptionEnd));
			} else if (line.startsWith("resources") && !line.endsWith(": []")) {
				descriptionStart = line.indexOf("[");
				descriptionEnd = line.length();
				resources = parseIntegerMap(line.substring(descriptionStart,
						descriptionEnd));
			}
		}
		Planning planning = new Planning(plannedStartTime, developers,
				resources);
		this.planning.add(planning);
	}

	private void parseTasks(ArrayList<String> tasks) {
		boolean describing = false;
		List<String> description = new ArrayList<String>();
		for (String line : tasks) {
			if (line.startsWith("reservations")) {
				describing = false;
				description.clear();
			}
			if (line.startsWith("-") || line.equals("")) {
				line = line.replace("-", "");
				line = line.trim();
				if (description != null && !description.isEmpty()) {
					addTask(description);
					description.clear();
				}
				describing = true;
			}
			if (describing && !line.equals("")) {
				description.add(line);
			}
		}
	}

	private void addTask(List<String> description) {
		int descriptionStart, descriptionEnd;
		String taskDescription = "";
		int project = 0, estimatedDuration = 0, acceptableDeviation = 0, alternativeFor = -1, planningNumber = -1;
		ArrayList<Integer> prerequisiteTasks = new ArrayList<Integer>();
		String status = "";
		Date startTime = null, endTime = null;
		Map<ResourceType, Integer> resourceTypes = new LinkedHashMap<ResourceType, Integer>();
		List<Task> dependencies = new ArrayList<Task>();
		Task alternativeForTask = null;
		int help = 0;
		int currentProject = 0;
		for (String line : description) {
			descriptionStart = line.indexOf("\"") + 1;
			if (descriptionStart == 0) {
				descriptionStart = line.indexOf(":") + 2;
				descriptionEnd = line.length();
			} else
				descriptionEnd = line.length() - 1;
			if (!line.endsWith(":")) {
				if (line.startsWith("project")) {
					project = Integer.parseInt(line.substring(descriptionStart,
							descriptionEnd));
				} else if (line.startsWith("description")) {
					taskDescription = line.substring(descriptionStart,
							descriptionEnd);
				} else if (line.startsWith("estimatedDuration")) {
					estimatedDuration = Integer.parseInt(line.substring(
							descriptionStart, descriptionEnd));
				} else if (line.startsWith("acceptableDeviation")) {
					acceptableDeviation = Integer.parseInt(line.substring(
							descriptionStart, descriptionEnd));
				} else if (line.startsWith("alternativeFor")) {
					alternativeFor = Integer.parseInt(line.substring(
							descriptionStart, descriptionEnd));
				} else if (line.startsWith("prerequisiteTasks")) {
					prerequisiteTasks = parseIntegerList(line.substring(
							descriptionStart, descriptionEnd));
				} else if (line.startsWith("planning")) {
					planningNumber = Integer.parseInt(line.substring(
							descriptionStart, descriptionEnd));
				} else if (line.startsWith("status")) {
					status = line.substring(descriptionStart, descriptionEnd);
				} else if (line.startsWith("startTime")) {
					startTime = parseDate(line.substring(descriptionStart,
							descriptionEnd));
				} else if (line.startsWith("endTime")) {
					endTime = parseDate(line.substring(descriptionStart,
							descriptionEnd));
				}
			}
		}

		while (currentProject < project) {
			help += projectHandler.getProjects().get(currentProject).getTasks()
					.size();
			currentProject++;
		}

		if (!prerequisiteTasks.isEmpty()) {
			for (int i : prerequisiteTasks) {
				dependencies.add(projectHandler.getProjects().get(project)
						.getTasks().get(i - help));
			}
		}

		if (alternativeFor != -1) {
			Task alt = projectHandler.getProjects().get(project).getTasks()
					.get(alternativeFor - help);
			if (alt.getStatusName().equals("FAILED")) {
				alternativeForTask = alt;
			}
		}

		if (planningNumber != -1) {
			Planning planning = this.planning.get(planningNumber);
			for (Entry<Integer, Integer> entry : planning.getResources()
					.entrySet()) {
				ResourceType resourceType = resourceHandler.getResourceTypes()
						.get(entry.getKey());
				int amount = entry.getValue();
				resourceTypes.put(resourceType, amount);
			}
		}

		projectHandler
				.getProjects()
				.get(project)
				.addTask(taskDescription, estimatedDuration,
						acceptableDeviation, dependencies, alternativeForTask,
						resourceTypes);

		if (planningNumber != -1) {
			Planning planning = this.planning.get(planningNumber);
			Task currentTask = projectHandler
					.getProjects()
					.get(project)
					.getTasks()
					.get(projectHandler.getProjects().get(project).getTasks()
							.size() - 1);
			for (int i : planning.getDevelopers()) {
				Developer d = userHandler.getDevelopers().get(i);
				DateTime start = new DateTime(planning.getPlannedStartTime());
				start = Clock.getInstance().getFirstPossibleStartTime(start);
				DateTime end = Clock.getInstance().addMinutes(start,
						estimatedDuration);
				TimeSpan timespan = new TimeSpan(start, end);
				d.addReservation(currentTask, timespan);
				currentTask.addRequiredDeveloper(d);
			}
			currentTask.updateTaskAvailability();
		}

		if (!status.equals("")) {
			boolean failed = status.equals("failed");
			Task currentTask = projectHandler
					.getProjects()
					.get(project)
					.getTasks()
					.get(projectHandler.getProjects().get(project).getTasks()
							.size() - 1);
			if (currentTask.getStatusName().equals("AVAILABLE")) {
				currentTask.executeTask();
				currentTask.addTimeSpan(failed, new DateTime(startTime),
						new DateTime(endTime));
			}
		}
	}

	private void parseReservations(ArrayList<String> reservations) {
		boolean describing = false;
		List<String> description = new ArrayList<String>();
		for (String line : reservations) {
			if (line.startsWith("-") || line.equals("")) {
				line = line.replace("-", "");
				line = line.trim();
				if (description != null && !description.isEmpty()) {
					addReservation(description);
					description.clear();
				}
				describing = true;
			}
			if (describing && !line.equals("")) {
				description.add(line);
			}
		}
		addReservation(description);
	}

	private void addReservation(List<String> description) {
		int descriptionStart, descriptionEnd;
		int resource = 0, task = 0;
		Date startTime = null, endTime = null;
		for (String line : description) {
			if (line.startsWith("resource")) {
				line = line.substring(line.indexOf(":") + 1);
				line = line.trim();
				resource = Integer.parseInt(line);
			} else if (line.startsWith("task")) {
				line = line.substring(line.indexOf(":") + 1);
				line = line.trim();
				task = Integer.parseInt(line);
			} else if (line.startsWith("startTime")) {
				descriptionStart = line.indexOf("\"") + 1;
				descriptionEnd = line.length() - 1;
				startTime = parseDate(line.substring(descriptionStart,
						descriptionEnd));
			} else if (line.startsWith("endTime")) {
				descriptionStart = line.indexOf("\"") + 1;
				descriptionEnd = line.length() - 1;
				endTime = parseDate(line.substring(descriptionStart,
						descriptionEnd));
			}
		}
		Resource res = null;
		int i = 0;
		for (ResourceType rt : resourceHandler.getResourceTypes()) {
			for (Resource r : rt.getResources()) {
				if (i == resource) {
					res = r;
				}
				i++;
			}
		}
		Task tsk = null;
		i = 0;
		for (Project project : projectHandler.getProjects()) {
			for (Task t : project.getTasks()) {
				if (i == task) {
					tsk = t;
				}
				i++;
			}
		}
		DateTime startDate = Clock.getInstance().getFirstPossibleStartTime(
				new DateTime(startTime));
		DateTime endDate = new DateTime(endTime);
		int hourDifference = res.getDailyAvailability().getStartTime()
				.getHourOfDay()
				- startDate.getHourOfDay();
		if (hourDifference > 0) {
			startDate = startDate.plusHours(hourDifference);
		}
		hourDifference = endDate.getHourOfDay()
				- res.getDailyAvailability().getEndTime().getHourOfDay();
		if (hourDifference > 0) {
			endDate = endDate.minusHours(hourDifference);
		}
		TimeSpan timeSpan = new TimeSpan(startDate, endDate);
		res.addReservation(tsk, timeSpan);
	}

	class Planning {

		private Date plannedStartTime;
		private List<Integer> developers;
		private Map<Integer, Integer> resources;

		protected Planning(Date plannedStartTime, List<Integer> developers,
				Map<Integer, Integer> resources) {
			this.plannedStartTime = plannedStartTime;
			this.developers = developers;
			this.resources = resources;
		}

		protected Date getPlannedStartTime() {
			return plannedStartTime;
		}

		protected List<Integer> getDevelopers() {
			return developers;
		}

		protected Map<Integer, Integer> getResources() {
			return resources;
		}

	}

}
