package pl.lodz.p.aurora.users.service.unitleader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.users.domain.dto.DutySearchDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

import java.util.List;

/**
 * Interface for service for users duties feature.
 */
public interface DutyUnitLeaderService {

    Duty create(Duty duty);

    void delete(Long dutyId, String eTag);

    List<Duty> findAll();

    Page<Duty> findAllByPage(Pageable pageable);

    Duty findById(Long id);

    Page<Duty> search(DutySearchDto critieria, Pageable pageable);

    void update(Long dutyId, Duty duty, String eTag);
}
