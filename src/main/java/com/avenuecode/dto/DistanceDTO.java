package com.avenuecode.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author pedro.gomes 2018-08-09 22:20:46
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DistanceDTO implements Serializable {

	private static final long serialVersionUID = -5253036867157746215L;
	
	private Integer distance;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<String> path;
	
	public DistanceDTO() {
	}

	public DistanceDTO(Integer distance) {
		super();
		this.distance = distance;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public List<String> getPath() {
		return path;
	}

	public void setPath(List<String> path) {
		this.path = path;
	}
}
