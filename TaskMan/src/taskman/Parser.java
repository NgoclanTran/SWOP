package taskman;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import taskman.model.ProjectHandler;
import taskman.model.ResourceHandler;
import taskman.model.resource.DailyAvailability;
import taskman.model.time.Clock;

public class Parser {

	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private ProjectHandler projectHandler;
	private ResourceHandler resourceHandler;

	private List<DailyAvailability> dailyAvailability = new ArrayList<DailyAvailability>();

	public Parser(ProjectHandler ph, ResourceHandler rh) {
		projectHandler = ph;
		resourceHandler = rh;
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
			descriptionStart = line.indexOf("\"");
			descriptionEnd = line.indexOf("\"", descriptionStart + 1);
			if (line.startsWith("startTime")) {
				startHour = Integer.parseInt(line.substring(
						descriptionStart + 1, descriptionStart + 3));
				startMinute = Integer.parseInt(line.substring(
						descriptionEnd - 2, descriptionEnd));
			}
			if (line.startsWith("endTime")) {
				endHour = Integer.parseInt(line.substring(descriptionStart + 1,
						descriptionStart + 3));
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
		String name;
		List<Integer> requires;
		List<Integer> conflictsWith;
		int dailyAvailability;
		for (String line : description) {
			if (line.startsWith("name")) {
				descriptionStart = line.indexOf("\"");
				descriptionEnd = line.length() - 1;
				name = line.substring(descriptionStart, descriptionEnd);
			}
			if (line.startsWith("requires")) {

			}
			if (line.startsWith("conflictsWith")) {

			}
			if (line.startsWith("dailyAvailability")) {
				if (!line.endsWith(":")) {
					descriptionStart = line.indexOf(":") + 2;
					descriptionEnd = line.length();
					dailyAvailability = Integer.parseInt(line.substring(
							descriptionStart, descriptionEnd));
				}
			}
		}

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
				line = line.replace("-", "");
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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

	}

	private void addReservation(List<String> description) {
		// TODO Auto-generated method stub

	}

}