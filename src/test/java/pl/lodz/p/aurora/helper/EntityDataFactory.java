package pl.lodz.p.aurora.helper;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class EntityDataFactory<T> {

    protected JpaRepository<T, ? extends java.io.Serializable> repository;
    public abstract T createSingle();

    /**
     * Provide as many dummy entities as given without saving to database.
     *
     * @return List of dummy entities saved to the database
     */
    public List<T> createMany(Integer howMany) {
        List<T> generatedEntities = new ArrayList<>();
        IntStream.range(0, howMany).forEach(i -> generatedEntities.add(createSingle()));

        return generatedEntities;
    }

    /**
     * Provide a single dummy entity, that was saved to the database.
     *
     * @return Dummy entity saved to database
     */
    public T createAndSaveSingle() {
        return repository.saveAndFlush(createSingle());
    }

    /**
     * Provide as many dummy users entities as given, that were saved to database.
     *
     * @return List of dummy users entities saved to the database
     */
    public List<T> createAndSaveMany(Integer howMany) {
        List<T> generatedEntities = new ArrayList<>();
        IntStream.range(0, howMany).forEach(i -> generatedEntities.add(createAndSaveSingle()));

        return generatedEntities;
    }
}