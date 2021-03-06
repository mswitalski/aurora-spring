package pl.lodz.p.aurora.mtr.service.unitleader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.msh.service.BaseService;
import pl.lodz.p.aurora.min.outlook.service.CalendarService;
import pl.lodz.p.aurora.min.outlook.service.CalendarServiceImpl;
import pl.lodz.p.aurora.mtr.exception.OverpeopledTrainingException;
import pl.lodz.p.aurora.mtr.web.dto.TrainingSearchDto;
import pl.lodz.p.aurora.mtr.domain.entity.Training;
import pl.lodz.p.aurora.mtr.domain.repository.TrainingRepository;
import pl.lodz.p.aurora.mtr.exception.InvalidDateTimeException;
import pl.lodz.p.aurora.mus.service.common.UserService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('ROLE_UNIT_LEADER')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ, transactionManager = "mtrTransactionManager")
public class TrainingUnitLeaderServiceImpl extends BaseService implements TrainingUnitLeaderService {

    private final TrainingRepository repository;
    private final UserService userService;
    private final CalendarService outlookService = new CalendarServiceImpl();

    @Autowired
    public TrainingUnitLeaderServiceImpl(TrainingRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Training create(Training training, String outlookAuthToken) {
        failIfInvalidDateTimes(training);
        Training saved = save(training, repository);

        if (outlookAuthToken != null) {
            outlookService.createEvent(saved, outlookAuthToken);
        }

        return saved;
    }

    private void failIfInvalidDateTimes(Training training) {
        LocalDateTime start = training.getStartDateTime();
        LocalDateTime end = training.getEndDateTime();
        Set<InvalidDateTimeException.ERROR> errors = new HashSet<>();

        if (start.isBefore(LocalDateTime.now())) {
            errors.add(InvalidDateTimeException.ERROR.START_BEFORE_NOW);
        }
        if (end.isBefore(LocalDateTime.now())) {
            errors.add(InvalidDateTimeException.ERROR.END_BEFORE_NOW);
        }
        if (end.isBefore(start) || start.isEqual(end)) {
            errors.add(InvalidDateTimeException.ERROR.END_BEFORE_EQUAL_START);
        }

        if (!errors.isEmpty()) {
            throw new InvalidDateTimeException("Unit leader tried to create a training with invalid date(s)", errors);
        }
    }

    @Override
    public void delete(Long trainingId, String eTag, String outlookAuthToken) {
        Training storedTraining = repository.findOne(trainingId);

        failIfNoRecordInDatabaseFound(storedTraining, trainingId);
        failIfEncounteredOutdatedEntity(eTag, storedTraining);
        repository.delete(trainingId);

        if (outlookAuthToken != null) {
            outlookService.deleteEvent(storedTraining, outlookAuthToken);
        }
    }

    @Override
    public Page<Training> findAllByPage(Pageable pageable) {
        return repository.findAllByOrderByStartDateTimeDesc(pageable);
    }

    @Override
    public Training findById(Long trainingId) {
        Training storedTraining = repository.findOne(trainingId);

        failIfNoRecordInDatabaseFound(storedTraining, trainingId);

        return storedTraining;
    }

    @Override
    public Page<Training> search(TrainingSearchDto criteria, Pageable pageable) {
        return repository.search(criteria.getName(),
                criteria.getType(),
                criteria.getLocation(),
                pageable);
    }

    @Override
    public void update(Long trainingId, Training training, String eTag, String outlookAuthToken) {
        Training storedTraining = repository.findOne(trainingId);

        failIfNoRecordInDatabaseFound(storedTraining, trainingId);
        failIfInvalidDateTimes(training);
        failIfExceededParticipantsLimit(training);
        storedTraining.setName(training.getName());
        storedTraining.setType(training.getType());
        storedTraining.setLocation(training.getLocation());
        storedTraining.setStartDateTime(training.getStartDateTime());
        storedTraining.setEndDateTime(training.getEndDateTime());
        storedTraining.setInternal(training.isInternal());
        storedTraining.setParticipantsLimit(training.getParticipantsLimit());
        storedTraining.setDescription(training.getDescription());
        storedTraining.setUsers(training.getUsers().stream()
                .map(u -> userService.findById(u.getId())).collect(Collectors.toSet()));
        save(storedTraining, repository);

        if (outlookAuthToken != null) {
            outlookService.updateEvent(storedTraining, outlookAuthToken);
        }
    }

    private void failIfExceededParticipantsLimit(Training training) {
        if (training.getUsers().size() > training.getParticipantsLimit()) {
            throw new OverpeopledTrainingException("Unit leader tried to assign more users to the training than limit");
        }
    }
}
