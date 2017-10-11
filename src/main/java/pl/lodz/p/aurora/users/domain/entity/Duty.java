package pl.lodz.p.aurora.users.domain.entity;

import pl.lodz.p.aurora.common.domain.entity.VersionedEntity;

import javax.persistence.*;

@Entity
@Table
public class Duty extends VersionedEntity {

    @Id
    @GeneratedValue(generator = "duty_pk_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "duty_pk_sequence", sequenceName = "duty_id_sequence", allocationSize = 1)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }
}
