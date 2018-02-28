package pl.lodz.p.aurora.msh.service;

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.aurora.builder.SkillBuilder;
import pl.lodz.p.aurora.msh.domain.entity.VersionedEntity;
import pl.lodz.p.aurora.msh.exception.*;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseServiceTest {

    private VersionedEntity WITH_VERSION_1;
    private TestService sut = new TestService();

    public BaseServiceTest() {
        WITH_VERSION_1 = new SkillBuilder().withVersion(1L).build();
    }

    @Test
    public void shouldNotThrowExceptionWhenVersionsAreEqual() {
        // Arrange
        String fakeETag = WITH_VERSION_1.getVersion().toString();

        // Act
        sut.outdatedEntity(fakeETag, WITH_VERSION_1);
    }

    @Test
    public void shouldThrowExceptionWhenVersionsAreNotEqual() {
        // Arrange
        String fakeETag = WITH_VERSION_1.getVersion().toString() + "1";

        // Assert
        assertThatExceptionOfType(OutdatedEntityModificationException.class)
                .isThrownBy(() -> sut.outdatedEntity(fakeETag, WITH_VERSION_1));
    }

    @Test
    public void shouldThrowExceptionWhenETagIsNotANumber() {
        // Arrange
        String notANumberETag = "ABC";

        // Assert
        assertThatExceptionOfType(InvalidRequestException.class)
                .isThrownBy(() -> sut.outdatedEntity(notANumberETag, WITH_VERSION_1));
    }

    @Test
    public void shouldNotThrowExceptionWhenEntityIsNotNull() {
        // Arrange
        VersionedEntity existingEntity = new SkillBuilder().build();

        // Act
        sut.noRecord(existingEntity, existingEntity.getId());
    }

    @Test
    public void shouldThrowExceptionWhenEntityIsNull() {
        // Arrange
        VersionedEntity notExistingEntity = null;
        Long fakeIdentifier = 0L;

        // Assert
        assertThatExceptionOfType(InvalidResourceRequestedException.class)
                .isThrownBy(() -> sut.noRecord(notExistingEntity, fakeIdentifier));
    }

    @Test
    public void shouldReturnSavedEntityWhenProperlySaved() {
        // Arrange
        VersionedEntity validEntity = new SkillBuilder().withVersion(10L).build();
        JpaRepository mockedRepository = mock(JpaRepository.class);
        when(mockedRepository.saveAndFlush(validEntity)).thenReturn(validEntity);

        // Act
        VersionedEntity savedEntity = sut.save(validEntity, mockedRepository);

        // Assert
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getVersion()).isEqualTo(validEntity.getVersion());
    }

    @Test
    public void shouldThrowExceptionWhenEntityHasInvalidState() {
        // Arrange
        JpaRepository mockedRepository = mock(JpaRepository.class);
        when(mockedRepository.saveAndFlush(WITH_VERSION_1)).thenThrow(ConstraintViolationException.class);

        // Assert
        assertThatExceptionOfType(InvalidEntityStateException.class)
                .isThrownBy(() -> sut.save(WITH_VERSION_1, mockedRepository));
    }

    @Test
    public void shouldThrowExceptionWhenViolatedUniqueConstraintAndMatchedRegExp() {
        // Arrange
        Exception rootCause = new Exception("unique_skill_name");
        Exception intermediateCause = new Exception(rootCause);
        String fakeExceptionMessage = "some msg";
        DataIntegrityViolationException integrityException = new DataIntegrityViolationException(
                fakeExceptionMessage, intermediateCause);
        JpaRepository mockedRepository = mock(JpaRepository.class);
        when(mockedRepository.saveAndFlush(WITH_VERSION_1)).thenThrow(integrityException);

        // Assert
        assertThatExceptionOfType(UniqueConstraintViolationException.class)
                .isThrownBy(() -> sut.save(WITH_VERSION_1, mockedRepository))
                .withCause(integrityException)
                .withMessageContaining("name");
    }

    @Test
    public void shouldThrowExceptionWhenViolatedUniqueConstraintAndDidNotMatchRegExp() {
        // Arrange
        String fakeExceptionMessage = "some msg";
        Exception rootCause = new Exception(fakeExceptionMessage);
        Exception intermediateCause = new Exception(rootCause);
        DataIntegrityViolationException integrityException = new DataIntegrityViolationException(
                fakeExceptionMessage, intermediateCause);
        JpaRepository mockedRepository = mock(JpaRepository.class);
        when(mockedRepository.saveAndFlush(WITH_VERSION_1)).thenThrow(integrityException);

        // Assert
        assertThatExceptionOfType(InvalidApplicationConfigurationException.class)
                .isThrownBy(() -> sut.save(WITH_VERSION_1, mockedRepository));
    }

    /**
     * Dummy class extending BaseService that is used for test purposes.
     */
    private class TestService extends BaseService {
        /**
         * Dummy method that uses failIfEncounteredOutdatedEntity() method.
         * @param eTag ETag value
         * @param entity Entity
         */
        public void outdatedEntity(String eTag, VersionedEntity entity) {
            failIfEncounteredOutdatedEntity(eTag, entity);
        }

        /**
         * Dummy method that uses failIfNoRecordInDatabaseFound() method.
         * @param entity Entity
         * @param identifier Entity's unique identifier
         */
        public void noRecord(Object entity, Object identifier) {
            failIfNoRecordInDatabaseFound(entity, identifier);
        }
    }
}