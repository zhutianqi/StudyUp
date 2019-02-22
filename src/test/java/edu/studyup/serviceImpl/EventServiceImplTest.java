package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student1 = new Student();
		student1.setFirstName("John");
		student1.setLastName("Doe");
		student1.setEmail("JohnDoe@email.com");
		student1.setId(1);
		
		//Create Student2
		Student student2 = new Student();
		student2.setFirstName("Shirley");
		student2.setLastName("Zhai");
		student2.setEmail("zShirley@gmail.com");
		student2.setId(2);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student1);
		eventStudents.add(student2);
		event.setStudents(eventStudents);
		
		DataStorage.getEventdata().put(event.getEventID(), event);
		
		//Create Event2
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(new Date(100));
		event2.setName("Event 2");
		Location location2 = new Location(100, 50);
		event2.setLocation(location2);
  
		DataStorage.getEventdata().put(event2.getEventID(), event2);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.getEventdata().clear();
	}

//	@Test
//	void testUpdateEventName_GoodCase() throws StudyUpException {
//		int eventID = 1;
//		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
//		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
//	}
//	
//	@Test
//	void testUpdateEvent_WrongEventID_BadCase() {
//		int eventID = 3;
//		Assertions.assertThrows(StudyUpException.class, () -> {
//			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
//		  });
//	}
	
	@Test
	void testUpdateEventName_SameName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.getEventdata().get(eventID).getName());
	}
	
	@Test
	 void testUpdateEventName_LengthOfEventNameIsTwentyCharacter_BadCase() throws StudyUpException {
		int eventID = 1;
		String newName = "ILoveSushiILoveSushi";
		Assertions.assertDoesNotThrow(() -> {
			eventServiceImpl.updateEventName(eventID, newName);
		});
	 }
	
	 @Test
	 void testUpdateEventName_WrongEventID_GoodCase() throws StudyUpException {
		 int eventID = 100;
		 Assertions.assertThrows(StudyUpException.class, () -> {
			 eventServiceImpl.updateEventName(eventID, "bala bala");
		 });
	 }
	 
	 @Test
	 void testUpdateEventName_NewName_GoodCase() throws StudyUpException {
		 int eventID = 1;
		 String newName = "ILoveSushi";
		 eventServiceImpl.updateEventName(eventID, newName);
		 Assertions.assertNotNull(DataStorage.getEventdata().get(eventID));
	 }
	 
	 @Test
	 void testGetActiveEvents_ContainPastEvents_BadCase() throws StudyUpException {
		 Event event3 = new Event();
		 event3.setEventID(2);
		
	     SimpleDateFormat dateformat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	     String strdate2 = "02-04-2013 11:35:42";
	     try {
	    	 Date date3 = dateformat.parse(strdate2);
	    	 event3.setDate(date3);
	    	 event3.setName("Event 3");
	    	 Location location3 = new Location(100, 50);
	    	 event3.setLocation(location3);
	    	 DataStorage.getEventdata().put(event3.getEventID(), event3);
	     } catch (ParseException e) {
	    	 e.printStackTrace();
	     }
		
	     List<Event> activeEvents = eventServiceImpl.getActiveEvents();
	     Date cur = new Date();
	     for (Event activeEvent : activeEvents) {
	    	 Assertions.assertTrue(activeEvent.getDate().after(cur));
	     }
	 }
	 
	 @Test 
	 void testAddStudentToEvent_ThreeStudentsInAnEvent_BadCase() throws StudyUpException {
		 int eventID = 1;
		 //create Student3
		 Student student3 = new Student();
		 student3.setFirstName("Walter");
		 student3.setLastName("Zheng");
		 student3.setEmail("zWalter@gmail.com");
		 student3.setId(3);
		 eventServiceImpl.addStudentToEvent(student3, eventID);
		 Event event = DataStorage.getEventdata().get(eventID);
		 List<Student> students = event.getStudents();
		 Assertions.assertFalse(students.size() > 2);
	 }
	 
	 @Test
	 void testAddStudentToEvent_ZeroStudentBefore_GoodCase() throws StudyUpException {
		 int eventID = 2;
		 //create Student3
		 Student student3 = new Student();
		 student3.setFirstName("Walter");
		 student3.setLastName("Zheng");
		 student3.setEmail("zWalter@gmail.com");
		 student3.setId(3);
		 eventServiceImpl.addStudentToEvent(student3, eventID);
		 Assertions.assertNotNull(DataStorage.getEventdata().get(eventID).getStudents());
	 }
	 
	 @Test
	 void testAddStudentToEvent_NullEvent_GoodCase() throws StudyUpException {
		 int eventID = 10;
		 //create Student3
		 Student student3 = new Student();
		 student3.setFirstName("Walter");
		 student3.setLastName("Zheng");
		 student3.setEmail("zWalter@gmail.com");
		 student3.setId(3);
		 Assertions.assertThrows(StudyUpException.class, () -> {
			 eventServiceImpl.addStudentToEvent(student3, eventID);
	    });
	 }
	 
	 @Test
	 void testGetPastEvents_GoodCase() {
		 List<Event> pastEvents = eventServiceImpl.getPastEvents();
		 Assertions.assertTrue(pastEvents.size() > 0);
		 Date cur = new Date();
		 for (Event pastEvent : pastEvents) {
			 Assertions.assertTrue(pastEvent.getDate().before(cur));
		 }
	 }
	 
	 @Test
	 void testDeleteEvent_GoodCase() {
		 int eventID = 2;
		 eventServiceImpl.deleteEvent(eventID);
		 Assertions.assertNull(DataStorage.getEventdata().get(eventID));
	 }
}
