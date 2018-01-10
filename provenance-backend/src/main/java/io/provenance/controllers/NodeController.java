package io.provenance.controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.provenance.config.Config;
import io.provenance.repo.CassandraConnector;

@RestController
public class NodeController {

	@RequestMapping(method = RequestMethod.GET, path = "/nodes", produces = "application/json")
	public ResponseEntity<?> getNodes() {
		try {
			Map<String, String> nodeLables = new HashMap<String, String>();
			Map<String, String> nodeMappings = new HashMap<String, String>();
			Map<String, String> nodeLableMappings = new HashMap<String, String>();
			String query = String.format("Select * from %s.node", Config.getKEYSPACE());
			Session session = CassandraConnector.getSession();
			ResultSet rows = session.execute(query);
			for(Row row : rows) {
				nodeLables.put(row.getString("id"), row.getString("name"));
				nodeMappings.put(row.getString("id"), row.getString("successor"));
			}
			for(String key : nodeMappings.keySet()) 
				nodeLableMappings.put(nodeLables.get(key), nodeLables.get(nodeMappings.get(key)));		
			return ResponseEntity.status(200).body(new ObjectMapper().writeValueAsString(nodeLableMappings));
		} catch (Exception e) {
			return ResponseEntity.status(500).body( "\"" + e.getMessage() + "\"");
		}
	}
}
