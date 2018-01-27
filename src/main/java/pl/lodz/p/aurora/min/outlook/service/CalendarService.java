package pl.lodz.p.aurora.min.outlook.service;

import pl.lodz.p.aurora.mtr.domain.entity.Training;

public interface CalendarService {

    boolean createEvent(Training training, String authToken);

    void deleteEvent(Training training, String authToken);

    boolean updateEvent(Training training, String authToken);
}
