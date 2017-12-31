package pl.lodz.p.aurora.integration.outlook.dto;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String id;
    private String subject;
    private boolean responseRequested = true;
    private String showAs = "busy";
    private String type = "singleInstance";
    private EventBody body = new EventBody();
    private EventDate start = new EventDate();
    private EventDate end = new EventDate();
    private EventLocation location = new EventLocation();
    private List<EventAttendee> attendees = new ArrayList<>();

    public void setLocation(String location) {
        this.location.setDisplayName(location);
    }

    public void setStart(String dt) {
        this.start.setDateTime(dt);
    }

    public void setEnd(String dt) {
        this.end.setDateTime(dt);
    }

    public void setBody(String body) {
        this.body.setContent(body);
    }

    public void addAttendee(String name, String email) {
        if (this.attendees.stream().noneMatch(a -> a.getEmailAddress().getAddress().equals(email))) {
            this.attendees.add(new EventAttendee(name, email));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isResponseRequested() {
        return responseRequested;
    }

    public void setResponseRequested(boolean responseRequested) {
        this.responseRequested = responseRequested;
    }

    public String getShowAs() {
        return showAs;
    }

    public void setShowAs(String showAs) {
        this.showAs = showAs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EventBody getBody() {
        return body;
    }

    public void setBody(EventBody body) {
        this.body = body;
    }

    public EventDate getStart() {
        return start;
    }

    public void setStart(EventDate start) {
        this.start = start;
    }

    public EventDate getEnd() {
        return end;
    }

    public void setEnd(EventDate end) {
        this.end = end;
    }

    public List<EventAttendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<EventAttendee> attendees) {
        this.attendees = attendees;
    }

    public EventLocation getLocation() {
        return location;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }
}
