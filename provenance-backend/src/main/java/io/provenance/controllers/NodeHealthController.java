package io.provenance.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.provenance.model.NodeHealth;
import io.provenance.repo.NodeHealthRepository;

@RestController
public class NodeHealthController {

    private NodeHealthRepository nodeHealthRepository;

    @Autowired
    public NodeHealthController(NodeHealthRepository nodeHealthRepository) {
        this.nodeHealthRepository = nodeHealthRepository;
    }

    @RequestMapping(method = RequestMethod.POST, produces="application/json", consumes="application/json", path = "/nodeHealth")
    @ResponseBody
    public ResponseEntity<?> addNodeHealth(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException
    {
    		try {
	    	    	NodeHealth nh=new NodeHealth();
	    	    	ObjectMapper mapper = new ObjectMapper();
	    	    	nh = mapper.readValue(json, NodeHealth.class);
	    	    	nodeHealthRepository.save(nh);   
	    	    	return ResponseEntity.ok(String.format("Added nodehealth. Status: %s!", nh.getStatus()));
	    	    	
    	  } catch(Exception e) {
    		  return ResponseEntity.ok(String.format("failed!"));
    	  }
    }
 
    @RequestMapping(method = RequestMethod.GET, produces="application/json", path = "/nodeHealth")
    @ResponseBody
    public ResponseEntity<?> getNodeHealth() throws JsonParseException, JsonMappingException, IOException
    {	
		try {
			Iterable<NodeHealth> nh;
			List<NodeHealth> nodeHealthes =new ArrayList<NodeHealth>();
		    	nh= nodeHealthRepository.findAll();
		    	nh.forEach(nodeHealthes::add);
		    	String res = new String();
		    	ObjectMapper mapper = new ObjectMapper();
		    	res = mapper.writeValueAsString(nodeHealthes);
		    		return ResponseEntity.ok(String.format(res));
		    	
		  	} catch(Exception e) {
		  		return ResponseEntity.ok(String.format("failed!"));
		  	}
     }
    
}