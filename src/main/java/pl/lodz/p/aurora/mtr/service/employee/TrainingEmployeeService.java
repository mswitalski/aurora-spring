package pl.lodz.p.aurora.mtr.service.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.mtr.domain.entity.Training;
import pl.lodz.p.aurora.mus.domain.entity.User;

public interface TrainingEmployeeService {

    Page<Training> findAllFinished(User employee, Pageable pageable);

    Page<Training> findAllPlanned(User employee, Pageable pageable);

    Training findById(Long trainingId, User employee);
}
