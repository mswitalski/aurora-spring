package pl.lodz.p.aurora.tasks.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.aurora.common.validator.NoHtml;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskDto {

    private Long id;

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @NotEmpty(message = "{Default.NotEmpty}")
    @Size(max = 100, message = "{Default.Size.Max}")
    private String content;
    
    private LocalDate deadlineDate;
    private LocalDate doneDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public void setDeadlineDate(String date) {
        if (date != null && !date.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = date.replace('T', ' ');
            this.deadlineDate = LocalDate.parse(date, formatter);
        }
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDate doneDate) {
        this.doneDate = doneDate;
    }

    public void setDoneDate(String date) {
        if (date != null && !date.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = date.replace('T', ' ');
            this.doneDate = LocalDate.parse(date, formatter);
        }
    }
}
