package com.idfinance.it.config;

import com.idfinance.it.pojo.CryptoCurrency;
import com.idfinance.it.pojo.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.idfinance.it")
@PropertySource("classpath:db.properties")
public class DatabaseConfiguration {
    @Autowired
    private Environment env;

    @Bean
    public Properties dataSourceProperties() {
        Properties properties = new Properties();
        properties.setProperty("serverTimezone", env.getProperty("serverTimezone"));
        properties.setProperty("useSSL", env.getProperty("useSSL"));
        properties.setProperty("createDatabaseIfNotExist", env.getProperty("createDatabaseIfNotExist"));
        return properties;
    }

    @Bean
    public DataSource customDataSource(Properties dataSourceProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getProperty("custom.task.url"));
        config.setUsername(env.getProperty("custom.task.user"));
        config.setPassword(env.getProperty("custom.task.password"));
        config.setDriverClassName(env.getProperty("jdbc.driver"));
        config.setMaximumPoolSize(env.getProperty("custom.task.pool_size", Integer.class));
        config.setDataSourceProperties(dataSourceProperties);
        return new HikariDataSource(config);
    }

    @Bean
    public LocalSessionFactoryBean customSessionFactory(
            @Qualifier("customDataSource") DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        sessionFactory.setHibernateProperties(properties);
        sessionFactory.setAnnotatedClasses(CryptoCurrency.class, User.class);
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager tx(
            @Qualifier("customSessionFactory") SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}