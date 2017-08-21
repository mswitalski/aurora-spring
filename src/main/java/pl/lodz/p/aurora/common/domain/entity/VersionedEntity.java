package pl.lodz.p.aurora.common.domain.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * An abstract entity that will serve as base entity for other versioned entities.
 */
@MappedSuperclass
public abstract class VersionedEntity extends BaseEntity {

    @Version
    private Long version;

    public Long getVersion() {
        return version;
    }

    public abstract Long getId();

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [" +
                "ID=" + getId() +
                ", version=" + version +
                ']';
    }
}
