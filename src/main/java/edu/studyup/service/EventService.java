package edu.studyup.service;

import java.util.List;

import edu.studyup.entity.Event;

/**
 * {@code EventService} holds all CRUD services for class {@link Event}
 * 
 * @author Shivani
 *
 */
public interface EventService {
	
	/**
	 * Returns a single event for the given key
	 * @param eventID the ID of the {@code Event} to be retrieved
	 * @return An Event object decoded from JSON, or null if none exists
	 */
	public Event getEvent(String eventID);
	
	/**
	 * @param event The {@code event} object to be created.
	 * @return {@code key}, returns the key of the event in the store.
	 */
	public void createEvent(String eventID, Event event);
	
	/**
	 * @param event The {@code event} object to be updated.
	 * @param key The {@code key} of the event mapped in the store.
	 * @return {@code Event}, returns the event.
	 */
	public void updateEvent(String eventID, Event event);


	/**
	 * @param key The {@code key} of the specific {@link Event} to delete.
	 * @return 1 if the event is deleted, else 0.
	 */
	public long deleteEvent(String eventID);
	
	/**
	 * Fetches all the events from the store.
	 * 
	 * @return The list of all {@code events}.
	 */
	public List<Event> getAllEvents();
	
	public String deleteAll();
}
