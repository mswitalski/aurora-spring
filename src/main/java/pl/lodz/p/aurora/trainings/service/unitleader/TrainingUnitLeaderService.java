package pl.lodz.p.aurora.trainings.service.unitleader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.trainings.domain.dto.TrainingSearchDto;
import pl.lodz.p.aurora.trainings.domain.entity.Training;

public interface TrainingUnitLeaderService {

    Training create(Training training);

    void delete(Long trainingId, String eTag);

    Page<Training> findAllByPage(Pageable pageable);

    Training findById(Long trainingId);

    Page<Training> search(TrainingSearchDto criteria, Pageable pageable);

    void update(Long trainingId, Training training, String eTag);
}
