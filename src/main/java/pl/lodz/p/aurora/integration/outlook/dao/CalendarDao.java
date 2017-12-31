package pl.lodz.p.aurora.integration.outlook.dao;

import org.springframework.http.*;
import pl.lodz.p.aurora.integration.outlook.dto.Event;
import pl.lodz.p.aurora.integration.outlook.dto.ResponseEventsList;

public interface CalendarDao {

    ResponseEntity<Void> createEvent(Event event, String authToken);

    ResponseEntity<Void> deleteEvent(Event event, String authToken);

    ResponseEntity<Event> findById(String eventId, String authToken);

    ResponseEntity<ResponseEventsList> getEventsList(String authToken);

    ResponseEntity<Void> updateEvent(Event event, String authToken);
}
