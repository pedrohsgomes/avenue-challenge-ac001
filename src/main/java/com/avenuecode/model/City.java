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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author pedro.gomes 2018-08-07 11:19:34
 *
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown=true, value={ "id", "sourceRotes", "targetRotes", "graph" })
public class City implements Serializable {

	private static final long serialVersionUID = 9002381855750802520L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
			
	@JsonValue
	private String name;
	
	@OneToMany(mappedBy="source", orphanRemoval=true)
	@Cascade(value=CascadeType.ALL)
	private List<Rote> sourceRotes;
	
	@OneToMany(mappedBy="target", orphanRemoval=true)
	@Cascade(value=CascadeType.ALL)
	private List<Rote> targetRotes;
	
	@ManyToOne
	@JoinColumn(name="graph_id", referencedColumnName="id")
	@Cascade(value=CascadeType.ALL)
	private Graph graph;

	@JsonCreator
	public City() {
	}
	
	@JsonCreator
	public City(@JsonProperty("name") String name) {
		this(name, null);
	}
	
	@JsonCreator
	public City(String name, Graph graph) {
		super();
		this.name = name;
		this.graph = graph;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Rote> getSourceRotes() {
		return sourceRotes;
	}

	public void setSourceRotes(List<Rote> sourceRotes) {
		this.sourceRotes = sourceRotes;
	}

	public List<Rote> getTargetRotes() {
		return targetRotes;
	}

	public void setTargetRotes(List<Rote> targetRotes) {
		this.targetRotes = targetRotes;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((graph == null) ? 0 : graph.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (graph == null) {
			if (other.graph != null)
				return false;
		} else if (!graph.equals(other.graph))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + "]";
	}
}