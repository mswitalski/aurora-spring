package pl.lodz.p.aurora.integration.outlook.dto;

public class EventDate {

    private String dateTime;
    private String timeZone = "Central European Standard Time";

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
