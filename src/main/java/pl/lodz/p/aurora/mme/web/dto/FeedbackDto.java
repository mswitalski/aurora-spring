package pl.lodz.p.aurora.mme.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.lodz.p.aurora.msh.validator.NoHtml;
import pl.lodz.p.aurora.mus.web.dto.UserBasicDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class FeedbackDto {

    private Long id;
    private MentorDto mentor;
    private UserBasicDto user;

    @NotNull(message = "{Default.NotNull}")
    private boolean satisfied = true;

    @NoHtml
    @NotNull(message = "{Default.NotNull}")
    @Size(max = 200, message = "{Default.Size.Max}")
    private String studentFeedback = "";
    private LocalDateTime createDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MentorDto getMentor() {
        return mentor;
    }

    public void setMentor(MentorDto mentor) {
        this.mentor = mentor;
    }

    public UserBasicDto getUser() {
        return user;
    }

    public void setUser(UserBasicDto user) {
        this.user = user;
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }

    public String getStudentFeedback() {
        return studentFeedback;
    }

    public void setStudentFeedback(String studentFeedback) {
        this.studentFeedback = studentFeedback;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }
}
