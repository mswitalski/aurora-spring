package pl.lodz.p.aurora.min.outlook.dao;

import pl.lodz.p.aurora.min.outlook.dto.Event;
import pl.lodz.p.aurora.min.outlook.dto.ResponseEventsList;

public interface EventDao {

    void create(Event event, String authToken);

    void delete(Event event, String authToken);

    Event findById(String eventId, String authToken);

    ResponseEventsList findAll(String authToken);

    void update(Event event, String authToken);
}
