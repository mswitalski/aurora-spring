package pl.lodz.p.aurora.min.outlook.service;

import pl.lodz.p.aurora.min.outlook.dao.EventDao;
import pl.lodz.p.aurora.min.outlook.dao.EventDaoImpl;
import pl.lodz.p.aurora.min.outlook.dto.Event;
import pl.lodz.p.aurora.mtr.domain.entity.Training;

import java.util.List;
import java.util.Optional;

public class CalendarServiceImpl implements CalendarService {

    private EventDao dao = new EventDaoImpl();

    @Override
    public void createEvent(Training training, String authToken) {
        Event event = new Event();
        setEventFields(event, training);
        dao.create(event, authToken);
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
        List<Event> eventList = dao.findAll(authToken).getEvents();
        Optional<Event> relatedEvent = eventList.stream()
                .filter(e -> e.getSubject().equals(training.getName()))
                .findFirst();
        relatedEvent.ifPresent(event -> dao.delete(event, authToken));
    }

    @Override
    public void updateEvent(Training training, String authToken) {
        List<Event> eventList = dao.findAll(authToken).getEvents();
        Optional<Event> relatedEvent = eventList.stream()
                .filter(e -> e.getSubject().equals(training.getName()))
                .findFirst();

        if (relatedEvent.isPresent()) {
            Event storedEvent = dao.findById(relatedEvent.get().getId(), authToken);
            setEventFields(storedEvent, training);
            dao.update(storedEvent, authToken);

        } else {
            createEvent(training, authToken);
        }
    }
}
