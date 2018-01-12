package pl.lodz.p.aurora.tasks.web.dto;

import pl.lodz.p.aurora.tasks.domain.entity.Task;

import java.time.LocalDate;

public class StatisticsDto {

    private Integer planned = 0;
    private Integer overdue = 0;
    private Integer doneLastWeek = 0;
    private Integer doneLastMonth = 0;

    public Integer getPlanned() {
        return planned;
    }

    public Integer getOverdue() {
        return overdue;
    }

    public Integer getDoneLastWeek() {
        return doneLastWeek;
    }

    public Integer getDoneLastMonth() {
        return doneLastMonth;
    }

    public void processTask(Task task) {
        if (task.getDoneDate() == null) {
            processUndoneTask(task);

        } else {
            processDoneTask(task);
        }
    }

    private void processUndoneTask(Task task) {
        this.planned++;

        if (task.getDeadlineDate() != null && task.getDeadlineDate().isBefore(LocalDate.now())) {
            this.overdue++;
        }
    }

    private void processDoneTask(Task task) {
        LocalDate lastWeek = LocalDate.now().minusWeeks(1);
        LocalDate lastMonth = LocalDate.now().minusMonths(1);

        if (task.getDoneDate().isAfter(lastWeek) || task.getDoneDate().isEqual(lastWeek)) {
            this.doneLastWeek++;
        }
        if (task.getDoneDate().isAfter(lastMonth) || task.getDoneDate().isEqual(lastMonth)) {
            this.doneLastMonth++;
        }
    }
}
