package io.provenance.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import io.provenance.config.Config;

@Configuration
@EnableCassandraRepositories(basePackages = { "io.provenance" })
public class CassandraConfiguration extends AbstractCassandraConfiguration {
 
    @Autowired
 
    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(Config.getDB_IP());
        cluster.setPort(Config.getDB_Port());
        return cluster;
    }
 
    @Override
    protected String getKeyspaceName() {
        return Config.getKEYSPACE();
    }
 
    @Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }
}
