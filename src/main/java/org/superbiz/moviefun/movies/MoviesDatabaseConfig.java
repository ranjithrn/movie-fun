package org.superbiz.moviefun.movies;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
import org.superbiz.moviefun.DatabaseServiceCredentials;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class MoviesDatabaseConfig {
    @Bean
    public DataSource moviesDataSource(DatabaseServiceCredentials serviceCredentials) {
        HikariDataSource moviesDataSource = new HikariDataSource();
        moviesDataSource.setJdbcUrl(serviceCredentials.jdbcUrl("movies-mysql", "p-mysql"));
        return moviesDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesEntityManagerFactory(DataSource moviesDataSource, HibernateJpaVendorAdapter jpaVendor){
        LocalContainerEntityManagerFactoryBean entityManageMoviesBean = new LocalContainerEntityManagerFactoryBean();
        entityManageMoviesBean.setDataSource(moviesDataSource);
        entityManageMoviesBean.setJpaVendorAdapter(jpaVendor);
        entityManageMoviesBean.setPackagesToScan("org.superbiz.moviefun.movies");
        entityManageMoviesBean.setPersistenceUnitName("movies-unit");
        return entityManageMoviesBean;
    }

    @Bean
    public PlatformTransactionManager moviesPlatformTransactionManager(EntityManagerFactory moviesEntityManagerFactory){
        return new JpaTransactionManager(moviesEntityManagerFactory);
    }

    @Bean
    public TransactionOperations moviesTransactionOperation(PlatformTransactionManager moviesPlatformTransactionManager){
        return new TransactionTemplate(moviesPlatformTransactionManager);
    }

}
