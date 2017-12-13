package pl.lodz.p.aurora.mentors.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.aurora.common.exception.ActionForbiddenException;
import pl.lodz.p.aurora.common.service.BaseService;
import pl.lodz.p.aurora.mentors.domain.entity.Mentor;
import pl.lodz.p.aurora.mentors.domain.repository.MentorRepository;
import pl.lodz.p.aurora.skills.domain.entity.Skill;
import pl.lodz.p.aurora.skills.service.common.SkillService;
import pl.lodz.p.aurora.users.domain.entity.User;

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
public class MentorEmployeeServiceImpl extends BaseService implements MentorEmployeeService {

    private final MentorRepository mentorRepository;
    private final SkillService skillService;

    @Autowired
    public MentorEmployeeServiceImpl(MentorRepository mentorRepository, SkillService skillService) {
        this.mentorRepository = mentorRepository;
        this.skillService = skillService;
    }

    @Override
    public Mentor create(Mentor mentor, User employee) {
        Skill storedSkill = skillService.findById(mentor.getSkill().getId());
        mentor.setSkill(storedSkill);
        mentor.setUser(employee);
        mentor.setApproved(false);
        mentor.setActive(true);

        return save(mentor, mentorRepository);
    }

    @Override
    public void delete(Long mentorId, User employee, String eTag) {
        Mentor storedMentor = mentorRepository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);
        failIfTriedToAccessNotOwnedMentor(employee, storedMentor);
        failIfEncounteredOutdatedEntity(eTag, storedMentor);
        mentorRepository.delete(mentorId);
    }

    private void failIfTriedToAccessNotOwnedMentor(User employee, Mentor mentor) {
        if (!mentor.getUser().getId().equals(employee.getId())) {
            throw new ActionForbiddenException("Employee tried to change not his mentor: " + mentor);
        }
    }

    @Override
    public void update(Long mentorId, Mentor mentor, String eTag, User employee) {
        Mentor storedMentor = mentorRepository.findOne(mentorId);

        failIfNoRecordInDatabaseFound(storedMentor, mentorId);
        failIfTriedToAccessNotOwnedMentor(employee, storedMentor);
        failIfEncounteredOutdatedEntity(eTag, storedMentor);
        storedMentor.setActive(mentor.isActive());
        save(storedMentor, mentorRepository);
    }
}
