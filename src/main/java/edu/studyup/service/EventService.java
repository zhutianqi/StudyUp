package edu.studyup.service;

import java.util.List;

import edu.studyup.entity.Event;
import edu.studyup.entity.Student;
import edu.studyup.util.StudyUpException;

/**
 * {@code EventService} holds all CRUD services for class {@link Event}
 * 
 * @author Shivani
 *
 */
public interface EventService {

	/**
	 * Updates the event name by taking in an eventID and the new name.
	 * throws error if any failed constraints.
	 * @param eventID The ID of the specific {@link Event} to be updated.
	 * @param name    The String value to update.
	 * @return        The updated {@code event}
	 * @throws        StudyUpException 
	 */
	public Event updateEventName(int eventID, String name) throws StudyUpException;
	

	/**
	 * Adds a student to a specific event and throws StudyUpException incase of any 
	 * constraint failures.
	 * @param student The {@link Student} to be added.
	 * @param eventID The {@code eventID} of the specific {@link Event} to be
	 *                updated.
	 * @return Event, if the student is added successfully.
	 * @throws StudyUpException 
	 */
	public Event addStudentToEvent(Student student, int eventID) throws StudyUpException;

	/**
	 * Fetches all the active events present. i.e., the events whose {@code Date} is
	 * in the future.
	 * @return The list of all active {@code events}. 
	 */
	public List<Event> getActiveEvents();

	/**
	 * Fetches all the past events. i.e., the events with past {@code Date}.
	 * @return The list of all past {@code events}.
	 */
	public List<Event> getPastEvents();

	/**
	 * @param eventID The {@code eventID} of the specific {@link Event} to delete.
	 * @return {@code Event}, if the event exists and is deleted, else null.
	 */
	public Event deleteEvent(int eventID);

}
