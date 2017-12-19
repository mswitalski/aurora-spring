package pl.lodz.p.aurora.trainings.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.aurora.users.domain.dto.UserBasicDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class TrainingDto {

    private Long id;

    @NotNull(message = "{Default.NotNull}")
    @NotEmpty(message = "{Default.NotEmpty}")
    @Size(max = 100, message = "{Default.Size.Max}")
    private String name;

    @NotNull(message = "{Default.NotNull}")
    @NotEmpty(message = "{Default.NotEmpty}")
    @Size(max = 20, message = "{Default.Size.Max}")
    private String type;

    @NotNull(message = "{Default.NotNull}")
    @NotEmpty(message = "{Default.NotEmpty}")
    @Size(max = 50, message = "{Default.Size.Max}")
    private String location;

    @NotNull(message = "{Default.NotNull}")
    private LocalDateTime startDateTime;

    @NotNull(message = "{Default.NotNull}")
    private LocalDateTime endDateTime;

    @NotNull(message = "{Default.NotNull}")
    private boolean internal = true;

    private Set<UserBasicDto> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        if (startDateTime != null && !startDateTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            startDateTime = startDateTime.replace('T', ' ');
            this.startDateTime = LocalDateTime.parse(startDateTime, formatter);
        }
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        if (endDateTime != null && !endDateTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            endDateTime = endDateTime.replace('T', ' ');
            this.endDateTime = LocalDateTime.parse(endDateTime, formatter);
        }
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public Set<UserBasicDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserBasicDto> users) {
        this.users = users;
    }
}
