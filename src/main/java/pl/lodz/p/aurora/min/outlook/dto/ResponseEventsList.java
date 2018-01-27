package pl.lodz.p.aurora.min.outlook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ResponseEventsList {

    @JsonProperty("value")
    private List<Event> events = new ArrayList<>();

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
