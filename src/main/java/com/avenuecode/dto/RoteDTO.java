package com.avenuecode.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RoteDTO implements Serializable {

	private static final long serialVersionUID = 8317045792479160569L;
	
	private Set<Map<String, Object>> routes = new HashSet<Map<String, Object>>();
	
	public Set<Map<String, Object>> getRoutes() {
		return routes;
	}
	
	public void setRoutes(Set<Map<String, Object>> rotes) {
		this.routes = rotes;
	}

	@Override
	public String toString() {
		return "RoteDTO [routes=" + routes + "]";
	}
}
