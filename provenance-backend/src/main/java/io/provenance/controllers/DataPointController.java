package io.provenance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.provenance.config.Config;
import io.provenance.controllers.helper.ControllerHelper;
import io.provenance.model.Datapoint;
import io.provenance.repo.CassandraConnector;

@RestController
public class DataPointController {
	
	@RequestMapping(method = RequestMethod.GET, path = "/provenance/{id}", produces = "application/json")
	public ResponseEntity<?> getProvenanceDataPoint(@PathVariable(value="id") String id) {
		try {
			ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
			Session session = CassandraConnector.getSession();
			String query = String.format("select * from %s.%s where id = '%s'", Config.getKEYSPACE(), Config.getTABLE(), "%s");
			Datapoint dp = ControllerHelper.queryDatapoint(session, query, id);
			if(dp != null)
				return ResponseEntity.status(200).body(mapper.writeValueAsString(dp));
			else
				return ResponseEntity.status(404).body("No provenance is available.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body( String.format("\"%s\"", e.getMessage()));
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/static/provenance/{id}", produces = "application/json")
	public ResponseEntity<?> getProvenanceDataPointJsonFile(@PathVariable(value="id") String id) {
		try {
			ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
			Session session = CassandraConnector.getSession();
			String query = String.format("select * from %s.%s where id = '%s'", Config.getKEYSPACE(), Config.getTABLE(), "%s");
			Datapoint dp = ControllerHelper.queryDatapoint(session, query, id);
			if(dp != null)
				return ResponseEntity.status(200).header("Content-Disposition", "attachment; filename=\"provenance.json\"").body(mapper.writeValueAsString(dp));
			else
				return ResponseEntity.status(404).body("No provenance is available.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body( String.format("\"%s\"", e.getMessage()));
		}
	}
}
