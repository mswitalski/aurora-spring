package pl.lodz.p.aurora.trainings.domain.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TrainingSearchDto {

    private String name = "";
    private String type = "";
    private String location = "";
    private LocalDate startDate = LocalDate.now();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDateTime(String startDate) {
        if (startDate != null && !startDate.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.startDate = LocalDate.parse(startDate, formatter);
        }
    }
}
