package pl.lodz.p.aurora.common.domain.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.MappedSuperclass;

/**
 * An abstract entity that will serve as base entity for other entities.
 */
@MappedSuperclass
public abstract class BaseEntity {

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
