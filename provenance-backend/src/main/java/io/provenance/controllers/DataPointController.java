package io.provenance.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.provenance.config.Config;
import io.provenance.controllers.helper.ControllerHelper;
import io.provenance.model.Datapoint;
import io.provenance.model.PipelineDatapoint;
import io.provenance.model.ProvenanceResultSet;
import io.provenance.repo.CassandraConnector;

@RestController
public class DataPointController {
	
	@RequestMapping(method = RequestMethod.GET, path = "/provenance/{id}", produces = "application/json")
	public ResponseEntity<?> getProvenanceDataPoint(@PathVariable(value="id") String id, @RequestParam String structure) {
		try {
			ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
			Session session = CassandraConnector.getSession();
			String query = String.format("select * from %s.%s where id = '%s'", Config.getKEYSPACE(), Config.getTABLE(), "%s");
			if(structure.toLowerCase().equals("recursive")) {
				Datapoint dp = ControllerHelper.queryDatapointRecursive(session, query, id);
				if(dp != null)
					return ResponseEntity.status(200).body(mapper.writeValueAsString(dp));
				else
					return ResponseEntity.status(404).body("No provenance is available.");
			} else if (structure.toLowerCase().equals("linear")) {
				List<PipelineDatapoint> datapoints = ControllerHelper.queryPipeLineDatapointLinear(session, query, id);
				if(!datapoints.isEmpty())
					return ResponseEntity.status(200).body(mapper.writeValueAsString(datapoints));
				else
					return ResponseEntity.status(404).body("No provenance is available.");
			} else
				return ResponseEntity.status(400).body( "\"Invalid structure (Supported structures are linear and recursive.).\"");
		} catch (Exception e) {
			return ResponseEntity.status(500).body( String.format("\"%s\"", e.getMessage()));
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/provenance/{id}/static", produces = "application/json")
	public ResponseEntity<?> getProvenanceDataPointJsonFile(@PathVariable(value="id") String id, @RequestParam String structure) {
		try {
			ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			Session session = CassandraConnector.getSession();
			String query = String.format("select * from %s.%s where id = '%s'", Config.getKEYSPACE(), Config.getTABLE(), "%s");
			if(structure.toLowerCase().equals("recursive")) {
				Datapoint dp = ControllerHelper.queryDatapointRecursive(session, query, id);
				if(dp != null)
					return ResponseEntity.status(200).header("Content-Disposition", "attachment; filename=\"provenance.json\"").body(mapper.writeValueAsString(dp));
				else
					return ResponseEntity.status(404).body("No provenance is available.");
			} else if (structure.toLowerCase().equals("linear")) {
				List<Datapoint> datapoints = ControllerHelper.queryDatapointLinear(session, query, id);
				if(!datapoints.isEmpty())
					return ResponseEntity.status(200).header("Content-Disposition", "attachment; filename=\"provenance.json\"").body(mapper.writeValueAsString(datapoints));
				else
					return ResponseEntity.status(404).body("No provenance is available.");
			} else
				return ResponseEntity.status(400).body( "\"Invalid structure (Supported structures are linear and recursive.).\"");
		} catch (Exception e) {
			return ResponseEntity.status(500).body( String.format("\"%s\"", e.getMessage()));
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/provenance", produces = "application/json")
    public ResponseEntity<?> createPizza(@RequestBody(required = true) String query) {
		try {
			ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
			Session session = CassandraConnector.getSession();
			System.out.println(mapper.readTree(query).path("query").asText());
			ProvenanceResultSet resultSet = ControllerHelper.queryData(session, mapper.readTree(query).path("query").asText());
	 	    return ResponseEntity.status(201).body(mapper.writeValueAsString(resultSet));
		} catch (Exception e) {
			return ResponseEntity.status(500).body( String.format("\"%s\"", e.getMessage()));
		}
    }
}