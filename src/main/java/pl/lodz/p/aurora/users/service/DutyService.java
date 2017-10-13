package pl.lodz.p.aurora.users.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.aurora.users.domain.entity.Duty;

/**
 * Interface for service for users duties feature.
 */
public interface DutyService {

    Page<Duty> findAllByPage(Pageable pageable);
    Duty findById(Long id);
}
