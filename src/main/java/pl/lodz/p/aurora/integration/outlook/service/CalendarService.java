package pl.lodz.p.aurora.integration.outlook.service;

import pl.lodz.p.aurora.trainings.domain.entity.Training;

public interface CalendarService {

    boolean createEvent(Training training, String authToken);

    void deleteEvent(Training training, String authToken);

    boolean updateEvent(Training training, String authToken);
}
