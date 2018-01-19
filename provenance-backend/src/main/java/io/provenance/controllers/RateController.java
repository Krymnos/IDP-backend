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

import io.provenance.model.Rate;
import io.provenance.repo.RateRepository;

@RestController
public class RateController {

    private RateRepository rateRepository;

    @Autowired
    public RateController(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    @RequestMapping(method = RequestMethod.POST, produces="application/json", consumes="application/json", path = "/rate")
    @ResponseBody
    public ResponseEntity<?> addRate(@RequestBody String json) throws JsonParseException, JsonMappingException, IOException
    {
    		try {
	    	    	Rate nh=new Rate();
	    	    	ObjectMapper mapper = new ObjectMapper();
	    	    	nh = mapper.readValue(json, Rate.class);
	    	    	rateRepository.save(nh);   
	    	    	return ResponseEntity.ok(String.format("Added rate."));
	    	    	
    	  } catch(Exception e) {
    		  return ResponseEntity.ok(String.format("failed!"));
    	  }
    }
 
    @RequestMapping(method = RequestMethod.GET, produces="application/json", path = "/rate")
    @ResponseBody
    public ResponseEntity<?> getNodeHealth() throws JsonParseException, JsonMappingException, IOException
    {	
		try {
			Iterable<Rate> nh;
			List<Rate> rates =new ArrayList<Rate>();
		    	nh= rateRepository.findAll();
		    	nh.forEach(rates::add);
		    	String res = new String();
		    	ObjectMapper mapper = new ObjectMapper();
		    	res = mapper.writeValueAsString(rates);
		    		return ResponseEntity.ok(String.format(res));
		    	
		  	} catch(Exception e) {
		  		return ResponseEntity.ok(String.format("failed!"));
		  	}
     }
    
}