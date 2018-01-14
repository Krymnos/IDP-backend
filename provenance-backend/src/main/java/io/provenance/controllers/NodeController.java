package io.provenance.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.provenance.config.Config;
import io.provenance.controllers.helper.ControllerHelper;
import io.provenance.model.Node;
import io.provenance.repo.CassandraConnector;

@RestController
public class NodeController {

	@RequestMapping(method = RequestMethod.GET, path = "/nodes", produces = "application/json")
	public ResponseEntity<?> getNodes() {
		try {
			String query = String.format("Select * from %s.node", Config.getKEYSPACE());
			Session session = CassandraConnector.getSession();
			List<Node> nodes = ControllerHelper.queryTopology(session, query);
			return ResponseEntity.status(200).body(new ObjectMapper().writeValueAsString(nodes));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body( "\"" + e.getMessage() + "\"");
		}
	}
}
