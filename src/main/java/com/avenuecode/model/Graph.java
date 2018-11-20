/**
 * 
 */
package com.avenuecode.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author pedro.gomes 2018-08-07 02:23:24
 *
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class Graph implements Serializable {
	
	private static final long serialVersionUID = 5382041564588193619L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@OneToMany(mappedBy="graph", orphanRemoval=true)
	@Cascade(value=CascadeType.ALL)
	@JsonIgnore	
	private List<City> cities;
	
	@OneToMany(mappedBy="graph", orphanRemoval=true)
	@Cascade(value=CascadeType.ALL)
	@JsonProperty("data")
	@JsonManagedReference
	private List<Rote> rotes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public List<Rote> getRotes() {
		return rotes;
	}

	public void setRotes(List<Rote> rotes) {
		this.rotes = rotes;
	}

	@Override
	public String toString() {
		return "Graph [id=" + id + ", cities=" + cities + ", rotes=" + rotes + "]";
	}	
}
