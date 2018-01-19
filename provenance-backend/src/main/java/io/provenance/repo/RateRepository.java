package io.provenance.repo;

import org.springframework.data.cassandra.repository.CassandraRepository;
import io.provenance.model.NodeHealth;
import io.provenance.model.Rate;

public interface RateRepository extends CassandraRepository<Rate>{

}
