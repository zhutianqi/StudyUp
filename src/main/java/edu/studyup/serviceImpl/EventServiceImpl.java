package edu.studyup.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

import edu.studyup.entity.Event;
import edu.studyup.service.EventService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisKeyCommands;
import io.lettuce.core.api.sync.RedisStringCommands;

public class EventServiceImpl implements EventService {
	
	private RedisClient redis;
	private Gson gson;

	public EventServiceImpl(String URL) {
		this.redis = RedisClient.create("redis://database:8888");
		this.gson = new Gson();
	}
	
	@Override
	public Event getEvent(String eventID) {
		StatefulRedisConnection<String, String> connection = this.redis.connect();
		RedisStringCommands<String, String> sync = connection.sync();
		String eventString = sync.get(eventID);
		return gson.fromJson(eventString, Event.class);
	}
	
	@Override
	public void createEvent(String eventID, Event event) {
		StatefulRedisConnection<String, String> connection = this.redis.connect();
		RedisStringCommands<String, String> sync = connection.sync();
		String eventString = gson.toJson(event);
		sync.set(eventID, eventString);
	}

	@Override
	public void updateEvent(String eventID, Event event) {
		StatefulRedisConnection<String, String> connection = this.redis.connect();
		RedisStringCommands<String, String> sync = connection.sync();
		String eventString = gson.toJson(event);
		sync.set(eventID, eventString);
	}

	@Override
	public long deleteEvent(String key) {
		return 0l; // TODO
	}

	@Override
	public List<Event> getAllEvents() {
		StatefulRedisConnection<String, String> connection = this.redis.connect();
		RedisKeyCommands<String, String> sync = connection.sync();
		List<Event> eventList = new ArrayList<>();
		//ToDo: No the optimized way.
		List<String> keys = sync.keys("*");
		for (String key: keys) {
			Event event = getEvent(key);
			eventList.add(event);
		}
		return eventList;
	}

	@Override
	public String deleteAll() {
		return null; // TODO
	}
}
