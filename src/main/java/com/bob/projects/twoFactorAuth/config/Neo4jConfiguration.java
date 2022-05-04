package com.bob.projects.twoFactorAuth.config;

import lombok.extern.java.Log;
import org.neo4j.ogm.config.Configuration.Builder;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;

@ComponentScan(basePackages = {
        "com.bob.projects.twoFactorAuth.service",
})
@Configuration
@EnableNeo4jRepositories(basePackages = {
        "com.bob.projects.twoFactorAuth.repository",
})
@Log
public class Neo4jConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(Neo4jConfiguration.class);
    @Value("${db.url}")
    String url;

    @Value("${db.uid}")
    String uid;

    @Value("${db.pwd}")
    String pwd;

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        logger.info("------------------------");
        logger.info(url);
        logger.info("------------------------");
        return new Builder()
                .uri(url)
                .credentials(uid, pwd)
                .autoIndex("update")
                .build();
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory(
                getConfiguration(),
                "com.bob.projects.twoFactorAuth.model"
                );
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }
}


