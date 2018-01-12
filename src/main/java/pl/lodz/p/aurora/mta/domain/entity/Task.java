package pl.lodz.p.aurora.mta.domain.entity;

import org.hibernate.validator.constraints.NotEmpty;
import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.common.validator.NoHtml;
import pl.lodz.p.aurora.mus.domain.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(indexes = {
        @Index(columnList = "deadlineDate", name = "index_task_deadline_date"),
        @Index(columnList = "doneDate", name = "index_task_done_date")
})
public class Task extends VersionedEntity {

    @Id
    @GeneratedValue(generator = "task_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "task_pk_sequence", sequenceName = "task_id_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false, length = 100)
    @NoHtml
    @NotNull
    @NotEmpty
    @Size(max = 100)
    private String content;

    @Column
    private LocalDate deadlineDate;

    @Column
    private LocalDate doneDate;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public LocalDate getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDate doneDate) {
        this.doneDate = doneDate;
    }
}
