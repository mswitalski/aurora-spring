package pl.lodz.p.aurora.min.outlook.dto;

public class EventAttendee {

    private String type = "required";
    private EventEmail emailAddress = new EventEmail();

    public EventAttendee() {
    }

    public EventAttendee(String name, String email) {
        this.emailAddress.setName(name);
        this.emailAddress.setAddress(email);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EventEmail getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EventEmail emailAddress) {
        this.emailAddress = emailAddress;
    }
}
