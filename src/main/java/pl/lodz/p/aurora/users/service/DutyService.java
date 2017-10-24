package pl.lodz.p.aurora.users.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.users.domain.dto.DutySearchDto;
import pl.lodz.p.aurora.users.domain.entity.Duty;

/**
 * Interface for service for users duties feature.
 */
public interface DutyService {

    Page<Duty> findAllByPage(Pageable pageable);
    Duty findById(Long id);
    Duty create(Duty duty);
    void update(String eTag, Duty duty);
    void delete(String eTag, Long dutyId);
    Page<Duty> search(DutySearchDto critieria, Pageable pageable);
}
