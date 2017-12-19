package pl.lodz.p.aurora.trainings.service.unitleader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.trainings.domain.entity.Training;

public interface TrainingUnitLeaderService {

    Training create(Training training);

    void delete(Long trainingId, String eTag);

    Page<Training> findAllByPage(Pageable pageable);

    Training findById(Long trainingId);

    // TODO SEARCH TRAINING

    void update(Long trainingId, Training training, String eTag);
}
