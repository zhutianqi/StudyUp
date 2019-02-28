package edu.studyup.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
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
		} catch (Exception e) {
			allEvents = new ArrayList<Event>();
		}
		
		allEvents = allEvents.stream().filter(e -> inBounds(e.getLocation(), bounds)).collect(Collectors.toList());
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		String json = gson.toJson(allEvents);
		response.getWriter().write(json);
	}

	private boolean inBounds(Location loc, List<Double> bounds) {
		return loc.lat >= bounds.get(0) && loc.lat <= bounds.get(2) && 
				loc.lon >= bounds.get(1) && loc.lon <= bounds.get(3);
	}
}
