package pl.lodz.p.aurora.skills.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.skills.domain.entity.Skill;

@Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ, readOnly = true, transactionManager = "mskTransactionManager")
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Page<Skill> findAllByOrderByNameAsc(Pageable pageable);

    @Query(value = "select s from Skill s where lower(s.name) like concat('%', lower(?1), '%') order by s.name asc")
    Page<Skill> search(String name, Pageable pageable);
}
