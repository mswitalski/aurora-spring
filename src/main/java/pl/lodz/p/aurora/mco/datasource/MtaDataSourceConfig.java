package pl.lodz.p.aurora.mco.datasource;

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
        entityManagerFactoryRef = "mtaEntityManagerFactory",
        transactionManagerRef = "mtaTransactionManager",
        basePackages = {"pl.lodz.p.aurora.mta.domain.repository"})
public class MtaDataSourceConfig {

    @Bean(name = "mtaDataSource")
    @ConfigurationProperties(prefix = "aurora.mta.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mtaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("mtaDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("pl.lodz.p.aurora.mta.domain.entity",
                        "pl.lodz.p.aurora.mus.domain.entity",
                        "pl.lodz.p.aurora.mme.domain.entity",
                        "pl.lodz.p.aurora.msk.domain.entity",
                        "pl.lodz.p.aurora.mtr.domain.entity")
                .persistenceUnit("mta")
                .build();
    }

    @Bean(name = "mtaTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("mtaEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
