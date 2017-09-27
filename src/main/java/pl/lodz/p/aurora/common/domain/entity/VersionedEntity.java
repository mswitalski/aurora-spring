package pl.lodz.p.aurora.common.domain.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * An abstract entity that will serve as base entity for other versioned entities.
 */
@MappedSuperclass
public abstract class VersionedEntity extends BaseEntity {

    @NotNull
    @Version
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
