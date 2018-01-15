package io.provenance.repo;

import org.springframework.data.cassandra.repository.CassandraRepository;
import io.provenance.model.NodeHealth;

public interface NodeHealthRepository extends CassandraRepository<NodeHealth>{

}
