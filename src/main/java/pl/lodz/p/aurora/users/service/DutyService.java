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
    Duty createAsAdmin(Duty duty);
    Duty createAsUnitLeader(Duty duty);
    void updateAsAdmin(String eTag, Duty duty);
    void updateAsUnitLeader(String eTag, Duty duty);
    void deleteAsAdmin(String eTag, Long dutyId);
    void deleteAsUnitLeader(String eTag, Long dutyId);
}
