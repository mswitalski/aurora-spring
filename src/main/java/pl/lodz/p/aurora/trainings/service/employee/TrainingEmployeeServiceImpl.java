package pl.lodz.p.aurora.trainings.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.exception.ActionForbiddenException;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.trainings.domain.entity.Training;
import pl.lodz.p.aurora.trainings.domain.repository.TrainingRepository;
import pl.lodz.p.aurora.users.domain.entity.User;

import java.util.List;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public class TrainingEmployeeServiceImpl extends BaseService implements TrainingEmployeeService {

    private final TrainingRepository repository;

    @Autowired
    public TrainingEmployeeServiceImpl(TrainingRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Training> findAllFinished(User employee, Pageable pageable) {
        return this.repository.findAllFinishedByUser(employee, pageable);
    }

    @Override
    public Page<Training> findAllPlanned(User employee, Pageable pageable) {
        return this.repository.findAllPlannedByUser(employee, pageable);
    }

    @Override
    public Training findById(Long trainingId, User employee) {
        Training storedTraining = repository.findOne(trainingId);

        failIfNoRecordInDatabaseFound(storedTraining, trainingId);
        failIfTriedToAccessNotRelatedTraining(storedTraining, employee);

        return storedTraining;
    }

    private void failIfTriedToAccessNotRelatedTraining(Training training, User employee) {
        List<Training> usersTrainings = repository.findAllByUsersContains(employee);

        if (!usersTrainings.contains(training)) {
            throw new ActionForbiddenException("User " + employee
                    + " tried to access a training that he is not a participant of " + training);
        }
    }
}
