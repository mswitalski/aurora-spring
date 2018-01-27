package pl.lodz.p.aurora.min.outlook.dao;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.aurora.min.outlook.dto.Event;
import pl.lodz.p.aurora.min.outlook.dto.ResponseEventsList;
import pl.lodz.p.aurora.min.outlook.exception.OutlookApiException;

public class EventDaoImpl implements EventDao {

    private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    private final String eventsUrl = "https://graph.microsoft.com/v1.0/me/events/";

    public void create(Event event, String authToken) {
        HttpHeaders headers = prepareHeaders(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Event> entity = new HttpEntity<>(event, headers);

        if (restTemplate.exchange(eventsUrl, HttpMethod.POST, entity, Void.class).getStatusCode() != HttpStatus.CREATED) {
            throw new OutlookApiException("New event in the Microsoft Outlook was not properly created");
        }
    }

    private HttpHeaders prepareHeaders(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);

        return headers;
    }

    @Override
    public void delete(Event event, String authToken) {
        HttpEntity<Event> entity = new HttpEntity<>(event, prepareHeaders(authToken));

        if (restTemplate.exchange(eventsUrl + event.getId(), HttpMethod.DELETE, entity, Void.class)
                .getStatusCode() != HttpStatus.NO_CONTENT) {
            throw new OutlookApiException("Event from the Microsoft Outlook was not properly deleted");
        }
    }

    @Override
    public Event findById(String eventId, String authToken) {
        HttpEntity entity = new HttpEntity(prepareHeaders(authToken));
        String url = eventsUrl + eventId + "?$select=responseRequested, showAs, type";

        return restTemplate.exchange(url, HttpMethod.GET, entity, Event.class).getBody();
    }

    public ResponseEventsList findAll(String authToken) {
        HttpEntity entity = new HttpEntity(prepareHeaders(authToken));
        String url = eventsUrl + "?$select=subject";
        ResponseEntity<ResponseEventsList> res = restTemplate
                .exchange(url, HttpMethod.GET, entity, ResponseEventsList.class);

        if (res.getStatusCode() != HttpStatus.OK) {
            throw new OutlookApiException("Could not get events list from the Microsoft Outlook API");
        }

        return res.getBody();
    }

    public void update(Event event, String authToken) {
        HttpEntity<Event> entity = new HttpEntity<>(event, prepareHeaders(authToken));
        ResponseEntity<Void> res = restTemplate.exchange(eventsUrl + event.getId(), HttpMethod.PATCH, entity, Void.class);

        if (res.getStatusCode() != HttpStatus.OK) {
            throw new OutlookApiException("Event in the Microsoft Outlook was not properly updated");
        }
    }
}
