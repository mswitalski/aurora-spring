package pl.lodz.p.aurora.common.domain.entity;

import com.google.common.base.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * An abstract entity that will serve as base entity for other versioned entities.
 */
@MappedSuperclass
public abstract class VersionedEntity {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        VersionedEntity that = (VersionedEntity) o;

        return Objects.equal(getId(), that.getId()) && Objects.equal(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {
        return 117 * getId().hashCode();
    }
}
