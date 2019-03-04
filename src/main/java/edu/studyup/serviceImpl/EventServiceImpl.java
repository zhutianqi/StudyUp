package edu.studyup.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import edu.studyup.entity.Event;
import edu.studyup.service.EventService;
import redis.clients.jedis.Jedis;

public class EventServiceImpl implements EventService {
	
	private Gson gson;

	public EventServiceImpl(String URL) {
		this.gson = new Gson();
	}
	
	@Override
	public Event getEvent(String eventID) {
		try (Jedis jedis = new Jedis("database", 8888)) {
			String eventString = jedis.get(eventID);
			return gson.fromJson(eventString, Event.class);
		}
	}
	
	@Override
	public void createEvent(String eventID, Event event) {
		try (Jedis jedis = new Jedis("database", 8888)) {
			String eventString = gson.toJson(event);
			jedis.set(eventID, eventString);
		}
	}

	@Override
	public void updateEvent(String eventID, Event event) {
		try (Jedis jedis = new Jedis("database", 8888)) {
			String eventString = gson.toJson(event);
			jedis.set(eventID, eventString);
		}
	}

	@Override
	public long deleteEvent(String key) {
		try (Jedis jedis = new Jedis("database", 8888)) {
			return jedis.del(String.valueOf(key));
		}
	}

	@Override
	public List<Event> getAllEvents() {
		try (Jedis jedis = new Jedis("database", 8888)) {
			List<Event> eventList = new ArrayList<>();
			Set<String> keys = jedis.keys("*");
			for (String key: keys) {
				Event event = getEvent(key);
				eventList.add(event);
			}
			return eventList;
		}
	}

	@Override
	public String deleteAll() {
		try (Jedis jedis = new Jedis("database", 8888)) {
			return jedis.flushAll();
		}
	}
}
