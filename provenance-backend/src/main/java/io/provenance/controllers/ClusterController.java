package io.provenance.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.provenance.config.Config;
import io.provenance.controllers.helper.ControllerHelper;
import io.provenance.exception.InvalidInputException;
import io.provenance.model.Datapoint;
import io.provenance.model.Node;
import io.provenance.model.NodeStat;
import io.provenance.repo.CassandraConnector;

@RestController
public class ClusterController {

	@RequestMapping(method = RequestMethod.GET, path = "/cluster/topology", produces = "application/json")
	public ResponseEntity<?> getTopology() {
		try {
			String query = String.format("Select * from %s.node", Config.getKEYSPACE());
			Session session = CassandraConnector.getSession();
			List<Node> cluster = ControllerHelper.queryTopology(session, query);
			if(cluster.size() > 0)
				return ResponseEntity.status(200).body(new ObjectMapper().writeValueAsString(cluster));
			else
				return ResponseEntity.status(404).body("No Cluster found.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body( "\"" + e.getMessage() + "\"");
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/cluster/stats", produces = "application/json")
	public ResponseEntity<?> getHealth() {
		try {
			String healthQuery = String.format("Select * from %s.heartbeat", Config.getKEYSPACE());
			String rateQuery = String.format("Select * from %s.noderate", Config.getKEYSPACE());
			Session session = CassandraConnector.getSession();
			List<NodeStat> clusterStats = ControllerHelper.queryStats(session, healthQuery, rateQuery);
			if(clusterStats.size() > 0)
				return ResponseEntity.status(200).body(new ObjectMapper().writeValueAsString(clusterStats));
			else
				return ResponseEntity.status(404).body("No stats found.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body( "\"" + e.getMessage() + "\"");
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/cluster/{nodeId}/provenance", produces = "application/json")
	public ResponseEntity<?> getProvenanceData(@PathVariable(value="nodeId") String nodeId, @RequestParam String size) {
		try {
			ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
			int numOfRecords = Integer.parseInt(size);
			if(numOfRecords <0 || numOfRecords > 1000)
				throw new InvalidInputException("Invalid size (Size should be a number <= 1000).");
			if(nodeId.length() != 6)
				throw new InvalidInputException("Invalid node id.");
			String query = String.format("SELECT * FROM %s.%s WHERE nodeid = '%s'LIMIT %d", Config.getKEYSPACE(), Config.getTABLE(), nodeId, numOfRecords);
			Session session = CassandraConnector.getSession();
			List<Datapoint> datapoints = ControllerHelper.queryDatapoints(session, query);
			if(datapoints.size() > 0)
				return ResponseEntity.status(200).body(mapper.writeValueAsString(datapoints));
			else
				return ResponseEntity.status(404).body("No provenance is available.");
		} catch(NumberFormatException nfe) {
			return ResponseEntity.status(400).body( "\"Invalid size (Size should be a number <= 1000).\"");
		} catch(InvalidInputException iie) {
			return ResponseEntity.status(400).body( "\""+ iie.getMessage() +"\"");
		} catch (Exception e) {
			return ResponseEntity.status(500).body( "\"" + e.getMessage() + "\"");
		}
	}
}