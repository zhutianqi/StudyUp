package edu.studyup.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import edu.studyup.entity.Event;
import edu.studyup.service.EventService;
import redis.clients.jedis.Jedis;

public class EventServiceImpl implements EventService {
	
	private Jedis redis;
	private Gson gson;

	public EventServiceImpl(String URL) {
		this.redis = new Jedis("database", 8888);
		this.gson = new Gson();
	}
	
	@Override
	public Event getEvent(String eventID) {
		String eventString = this.redis.get(eventID);
		return gson.fromJson(eventString, Event.class);
	}
	
	@Override
	public void createEvent(String eventID, Event event) {
		String eventString = gson.toJson(event);
		this.redis.set(eventID, eventString);
	}

	@Override
	public void updateEvent(String eventID, Event event) {
		String eventString = gson.toJson(event);
		this.redis.set(eventID, eventString);
	}

	@Override
	public long deleteEvent(String key) {
		return this.redis.del(String.valueOf(key));
	}

	@Override
	public List<Event> getAllEvents() {
		List<Event> eventList = new ArrayList<>();
		Set<String> keys = this.redis.keys("*");
		for (String key: keys) {
			Event event = getEvent(key);
			eventList.add(event);
		}
		return eventList;
	}

	@Override
	public String deleteAll() {
		return this.redis.flushAll();
	}
}
