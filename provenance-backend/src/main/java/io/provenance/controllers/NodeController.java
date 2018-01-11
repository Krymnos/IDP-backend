package io.provenance.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import io.provenance.model.Node;
import io.provenance.repo.CassandraConnector;

@RestController
public class NodeController {

	@RequestMapping(method = RequestMethod.GET, path = "/nodes", produces = "application/json")
	public ResponseEntity<?> getNodes() {
		try {
			Map<String, Node> topology = new HashMap<String, Node>();
			String query = String.format("Select * from %s.node", Config.getKEYSPACE());
			Session session = CassandraConnector.getSession();
			ResultSet rows = session.execute(query);
			for(Row row : rows) {
				String id = row.getString("id"), name = row.getString("name"), successor = row.getString("successor");
				if(topology.containsKey(id)) {
					Node node = topology.get(id);
					node.setId(id);
					node.setName(name);
					node.setSuccessor(successor);
					topology.put(id, node);
				}
				else {
					Node node = new Node(id, name, successor);
					topology.put(id, node);
				}
				if(!id.equals(successor)) {
					if(topology.containsKey(successor)) {
						Node node = topology.get(successor);
						node.setPredecessor(id);
						topology.put(successor, node);
					}
					else
						topology.put(successor, new Node(id));
				}
			}
			List<Node> nodes = new ArrayList<Node>();
			for( String key : topology.keySet())
				nodes.add(topology.get(key));
			return ResponseEntity.status(200).body(new ObjectMapper().writeValueAsString(nodes));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body( "\"" + e.getMessage() + "\"");
		}
	}
}
