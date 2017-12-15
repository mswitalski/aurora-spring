package pl.lodz.p.aurora.mentors.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, readOnly = true)
public interface MentorRepository extends JpaRepository<Mentor, Long> {

    @Query(value = "select m from Mentor m inner join m.evaluation e inner join e.skill s where lower(s.name) like concat('%', lower(?1), '%')")
    Page<Mentor> searchAll(String skillName, Pageable pageable);

    @Query(value = "select m from Mentor m inner join m.evaluation e inner join e.skill s where lower(s.name) like concat('%', lower(?1), '%') and m.active = true and m.approved = true")
    Page<Mentor> searchActive(String skillName, Pageable pageable);

    Page<Mentor> findAllByActiveTrueAndApprovedTrue(Pageable pageable);

    Mentor findByIdAndActiveTrueAndApprovedTrue(Long id);
}