package pl.lodz.p.aurora.min.outlook.dao;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.aurora.min.outlook.dto.Event;
import pl.lodz.p.aurora.min.outlook.dto.ResponseEventsList;

public class CalendarDaoImpl implements CalendarDao {

    private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    private final String eventsUrl = "https://graph.microsoft.com/v1.0/me/events/";

    public ResponseEntity<Void> createEvent(Event event, String authToken) {
        HttpHeaders headers = prepareHeaders(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Event> entity = new HttpEntity<>(event, headers);

        return restTemplate.exchange(eventsUrl, HttpMethod.POST, entity, Void.class);
    }

    private HttpHeaders prepareHeaders(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);

        return headers;
    }

    @Override
    public ResponseEntity<Void> deleteEvent(Event event, String authToken) {
        HttpEntity<Event> entity = new HttpEntity<>(event, prepareHeaders(authToken));

        return restTemplate.exchange(eventsUrl + event.getId(), HttpMethod.DELETE, entity, Void.class);
    }

    @Override
    public ResponseEntity<Event> findById(String eventId, String authToken) {
        HttpEntity entity = new HttpEntity(prepareHeaders(authToken));
        String url = eventsUrl + eventId + "?$select=responseRequested, showAs, type";

        return restTemplate.exchange(url, HttpMethod.GET, entity, Event.class);
    }

    public ResponseEntity<ResponseEventsList> getEventsList(String authToken) {
        HttpEntity entity = new HttpEntity(prepareHeaders(authToken));
        String url = eventsUrl + "?$select=subject";

        return restTemplate.exchange(url, HttpMethod.GET, entity, ResponseEventsList.class);
    }

    public ResponseEntity<Void> updateEvent(Event event, String authToken) {
        HttpEntity<Event> entity = new HttpEntity<>(event, prepareHeaders(authToken));

        return restTemplate.exchange(eventsUrl + event.getId(), HttpMethod.PATCH, entity, Void.class);
    }
}
