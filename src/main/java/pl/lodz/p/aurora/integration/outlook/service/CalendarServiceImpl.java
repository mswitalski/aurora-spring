package pl.lodz.p.aurora.integration.outlook.service;

import org.springframework.http.HttpStatus;
import pl.lodz.p.aurora.integration.outlook.dao.CalendarDao;
import pl.lodz.p.aurora.integration.outlook.dao.CalendarDaoImpl;
import pl.lodz.p.aurora.integration.outlook.dto.Event;
import pl.lodz.p.aurora.integration.outlook.dto.ResponseEventsList;
import pl.lodz.p.aurora.trainings.domain.entity.Training;

import java.util.List;
import java.util.Optional;

public class CalendarServiceImpl implements CalendarService {

    private CalendarDao dao = new CalendarDaoImpl();

    @Override
    public boolean createEvent(Training training, String authToken) {
        Event event = new Event();
        setEventFields(event, training);

        return dao.createEvent(event, authToken).getStatusCode() == HttpStatus.CREATED;
    }

    private void setEventFields(Event event, Training training) {
        event.setSubject(training.getName());
        event.setLocation(training.getLocation());
        event.setStart(training.getStartDateTime().toString());
        event.setEnd(training.getEndDateTime().toString());
        event.setBody(training.getDescription());
        training.getUsers().forEach(u -> event.addAttendee(u.getName() + " " + u.getSurname(), u.getEmail()));
    }

    @Override
    public void deleteEvent(Training training, String authToken) {
        List<Event> eventList = dao.getEventsList(authToken).getBody().getEvents();
        Optional<Event> relatedEvent = eventList.stream()
                .filter(e -> e.getSubject().equals(training.getName()))
                .findFirst();
        relatedEvent.ifPresent(event -> dao.deleteEvent(event, authToken));
    }

    @Override
    public boolean updateEvent(Training training, String authToken) {
        ResponseEventsList xD = dao.getEventsList(authToken).getBody();
        System.out.println(xD == null);
        List<Event> eventList = xD.getEvents();
        Optional<Event> relatedEvent = eventList.stream()
                .filter(e -> e.getSubject().equals(training.getName()))
                .findFirst();

        if (relatedEvent.isPresent()) {
            Event storedEvent = dao.findById(relatedEvent.get().getId(), authToken).getBody();
            setEventFields(storedEvent, training);

            return dao.updateEvent(storedEvent, authToken).getStatusCode() == HttpStatus.OK;

        } else {
            return createEvent(training, authToken);
        }
    }
}
