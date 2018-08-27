package org.superbiz.moviefun.albums;

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
public class AlbumsDatabaseConfig {
    @Bean
    public DataSource albumsDataSource(DatabaseServiceCredentials serviceCredentials) {
        HikariDataSource albumDataSource = new HikariDataSource();
        albumDataSource.setJdbcUrl(serviceCredentials.jdbcUrl("albums-mysql", "p-mysql"));
        return albumDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean albumsEntityManagerFactory(DataSource albumsDataSource, HibernateJpaVendorAdapter jpaVendor){
        LocalContainerEntityManagerFactoryBean entityManageAlbumBean = new LocalContainerEntityManagerFactoryBean();
        entityManageAlbumBean.setDataSource(albumsDataSource);
        entityManageAlbumBean.setJpaVendorAdapter(jpaVendor);
        entityManageAlbumBean.setPackagesToScan(AlbumsDatabaseConfig.class.getPackage().getName());
        entityManageAlbumBean.setPersistenceUnitName("albums-unit");
        return entityManageAlbumBean;
    }

    @Bean
    public PlatformTransactionManager albumsPlatformTransactionManager(EntityManagerFactory albumsEntityManagerFactory){
        return new JpaTransactionManager(albumsEntityManagerFactory);
    }

    @Bean
    public TransactionOperations albumTransactionOperation(PlatformTransactionManager albumsPlatformTransactionManager){
        return new TransactionTemplate(albumsPlatformTransactionManager);
    }
}
