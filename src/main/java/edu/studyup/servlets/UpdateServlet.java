package edu.studyup.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.serviceImpl.EventServiceImpl;
import edu.studyup.util.Utils;

public class UpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 6895667468419020494L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			updateEvent(request);
			response.getWriter().write("Success!");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(e.getMessage() == null ? "" : e.getMessage());
		}
	}
	private void updateEvent(HttpServletRequest request) {
		EventServiceImpl impl = new EventServiceImpl(Utils.getBaseURL(request.getRequestURL().toString()));
		String eventId = request.getParameter("eventId");
		boolean isNew = eventId.equals("0");
		Event event = new Event();
		if (!isNew) {
			event = impl.getEvent(eventId);
			String[] attendees = request.getParameterValues("eventAttendees[]"); // For existing events, you can only add an attendee
			if (event.getStudents() == null) event.setStudents(new ArrayList<>());
			if (attendees.length > event.getStudents().size()) {
				Student student = createStudent(attendees[event.getStudents().size()]);
				event.getStudents().add(student);
			}
			impl.updateEvent(eventId, event);
		}
		else {
			String eventID = UUID.randomUUID().toString();
			event.setEventID(eventId);
			String[] lonlat = request.getParameter("eventLoc").split(",");
			event.setLocation(new Location(Double.parseDouble(lonlat[0]), Double.parseDouble(lonlat[1])));
			event.setName(request.getParameter("eventName"));
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
				event.setDate(simpleDateFormat.parse(request.getParameter("eventDate")));
			} catch (ParseException e) {
				System.err.println(e + "\t" + request.getParameter("eventDate"));
			}
			List<Student> students = new ArrayList<Student>();
			for (String attendee : request.getParameterValues("eventAttendees[]")) {
				students.add(createStudent(attendee));
			}
			event.setStudents(students);
			impl.createEvent(eventID, event);
		}
	}
	
	private Student createStudent(String attendee) {
		Student student = new Student();
		if (attendee.contains(" ")) {
			student.setFirstName(attendee.substring(0, attendee.indexOf(" ")));
			student.setLastName(attendee.substring(attendee.indexOf(" ") + 1));
		}
		else {
			student.setFirstName(attendee);
		}
		return student;
	}
}
