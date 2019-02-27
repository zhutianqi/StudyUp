package edu.studyup.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.serviceImpl.EventServiceImpl;
import edu.studyup.util.Utils;

public class MoveServlet extends HttpServlet {
	
	private static final long serialVersionUID = -5273788106109654170L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String boundingBox = request.getParameter("bounds");
		if (boundingBox == null || boundingBox.isEmpty()) {
			return;
		}
		List<Double> bounds = Arrays.stream(boundingBox.split(",")).map(e -> Double.parseDouble(e.trim()))
				.collect(Collectors.toList());
		response.setContentType("text/plain");
		List<Event> allEvents;
		try {
			EventServiceImpl impl = new EventServiceImpl(Utils.getBaseURL(request.getRequestURL().toString()));
			allEvents = impl.getAllEvents();
			if (allEvents.size() == 0) {
				addDummies(allEvents);
				impl.createEvent(allEvents.get(0).getEventID(), allEvents.get(0));
				impl.createEvent(allEvents.get(1).getEventID(), allEvents.get(1));
			}
		} catch (Exception e) {
			allEvents = new ArrayList<Event>();
			addDummies(allEvents);
		}
		
		allEvents = allEvents.stream().filter(e -> inBounds(e.getLocation(), bounds)).collect(Collectors.toList());
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		String json = gson.toJson(allEvents);
		response.getWriter().write(json);
	}

	@SuppressWarnings("deprecation")
	private void addDummies(List<Event> allEvents) {
		// Create some dummy events by default
		Event dummyEvent1 = new Event();
		dummyEvent1.setEventID(UUID.randomUUID().toString());
		dummyEvent1.setDate(new Date());
		dummyEvent1.setLocation(new Location(-121.75402450718683, 38.547633440738736));
		dummyEvent1.setName("Dummy event 1");
		Student dummyStudent1 = new Student();
		dummyStudent1.setFirstName("Dummy");
		dummyStudent1.setLastName("Student");
		dummyEvent1.setStudents(List.of(dummyStudent1));
		allEvents.add(dummyEvent1);
		
		Event dummyEvent2 = new Event();
		dummyEvent2.setEventID(UUID.randomUUID().toString());
		dummyEvent2.setDate(new Date(119, 3, 1, 15, 0));
		dummyEvent2.setLocation(new Location(-121.79027938947546, 38.54945928238965));
		dummyEvent2.setName("Dummy event 1");
		Student dummyStudent2 = new Student();
		dummyStudent2.setFirstName("Dummy");
		dummyStudent2.setLastName("Student2");
		dummyEvent2.setStudents(List.of(dummyStudent2, dummyStudent1));
		allEvents.add(dummyEvent2);
	}
	
	private boolean inBounds(Location loc, List<Double> bounds) {
		return loc.lat >= bounds.get(0) && loc.lat <= bounds.get(2) && 
				loc.lon >= bounds.get(1) && loc.lon <= bounds.get(3);
	}
}
