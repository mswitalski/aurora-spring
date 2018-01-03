package pl.lodz.p.aurora.configuration.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "mtrEntityManagerFactory",
        transactionManagerRef = "mtrTransactionManager",
        basePackages = { "pl.lodz.p.aurora.trainings.domain.repository" })
public class MtrDataSourceConfig {

    @Bean(name = "mtrDataSource")
    @ConfigurationProperties(prefix = "aurora.mtr.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mtrEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("mtrDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("pl.lodz.p.aurora.trainings.domain.entity",
                        "pl.lodz.p.aurora.users.domain.entity",
                        "pl.lodz.p.aurora.mentors.domain.entity",
                        "pl.lodz.p.aurora.skills.domain.entity")
                .persistenceUnit("mtr")
                .build();
    }

    @Bean(name = "mtrTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("mtrEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}