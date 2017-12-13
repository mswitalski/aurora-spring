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

    @Query(value = "select m from Mentor m inner join m.skill s where lower(s.name) like concat('%', lower(?1), '%')")
    Page<Mentor> search(String skillName, Pageable pageable);
}