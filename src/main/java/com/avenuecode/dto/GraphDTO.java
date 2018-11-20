package com.avenuecode.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class GraphDTO implements Serializable {
	
	private static final long serialVersionUID = -2479535045393685472L;

	private String source;
	
	private String target;
	
	private int distance;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "GraphDTO [source=" + source + ", target=" + target + ", distance=" + distance + "]";
	}
}
