package com.avenuecode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avenuecode.dto.DistanceDTO;
import com.avenuecode.dto.GraphInfoResponse;
import com.avenuecode.dto.RoteDTO;
import com.avenuecode.model.Graph;
import com.avenuecode.service.GraphService;

/**
 * @author pedro.gomes 2018-08-07 11:20:46
 *
 */
@RestController
public class ApiRequestController {
    
    @Autowired
    private GraphService graphService;
    
    @PostMapping(value="/graph", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public Graph saveGraph(@RequestBody GraphInfoResponse data, Model model) {
    	Graph graph = graphService.generate(data);
        return graph;
    }
    
    @GetMapping(value="/graph/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody 
    public ResponseEntity<Graph> getGraph(@PathVariable Integer id, Model model) {
    	Graph graph = graphService.getById(id);
        return new ResponseEntity<Graph>(graph, graph != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
    
    @PostMapping(value="/routes/{id}/from/{firstTown}/to/{lastTown}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<RoteDTO> getRoutes(@PathVariable Integer id, @PathVariable String firstTown, @PathVariable String lastTown, 
    		@RequestParam(name="maxStops", required=false) Integer stops, Model model) {
    	RoteDTO rote = graphService.getRoutes(id, firstTown, lastTown, stops);
        return new ResponseEntity<RoteDTO>(rote, rote != null && !rote.getRoutes().isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND);        
    }
    
    @PostMapping(value="/distance/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public ResponseEntity<DistanceDTO> getDistance(@PathVariable Integer id, @RequestBody DistanceDTO path, Model model) {
    	if (id != null && path != null) {
    		path = graphService.getDistance(id, path);
    	}
    	return new ResponseEntity<DistanceDTO>(path, path != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
    
    @PostMapping(value="/distance/{id}/from/{firstTown}/to/{lastTown}")
    @ResponseBody
    public ResponseEntity<DistanceDTO> getDistance(@PathVariable Integer id, @PathVariable String firstTown, @PathVariable String lastTown, Model model) {
    	DistanceDTO path = graphService.getDistanceBetween(id, firstTown, lastTown);
    	return new ResponseEntity<DistanceDTO>(path, path != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
