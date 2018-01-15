package io.provenance.repo;

import org.springframework.data.repository.CrudRepository;
import io.provenance.model.NodeHealth;

public interface NodeHealthRepository extends CrudRepository<NodeHealth,String>{

}
