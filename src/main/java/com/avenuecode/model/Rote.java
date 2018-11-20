/**
 * 
 */
package com.avenuecode.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author pedro.gomes 2018-08-07 02:23:24
 *
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown=true, value = {"id", "graph"})
public class Rote implements Serializable {

	private static final long serialVersionUID = -2206310356719168251L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="source_city_id", referencedColumnName="id")
	@Cascade(value=CascadeType.ALL)
	private City source;
	
	@ManyToOne
	@JoinColumn(name="target_city_id", referencedColumnName="id")
	@Cascade(value=CascadeType.ALL)
	private City target;
	
	private int distance;
	
	@ManyToOne
	@JoinColumn(name="graph_id", referencedColumnName="id")
	@Cascade(value=CascadeType.ALL)
	@JsonBackReference
	private Graph graph;
	
	@JsonCreator
	public Rote() {
	}
	
	@JsonCreator
	public Rote(@JsonProperty("source") String source, @JsonProperty("target") String target, @JsonProperty("distance") int distance) {
		this(new City(source), new City(target), distance, null);
	}

	public Rote(City source, City target, int distance, Graph graph) {
		super();
		this.source = source;
		this.target = target;
		this.distance = distance;
		this.graph = graph;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public City getSource() {
		return source;
	}

	public void setSource(City source) {
		this.source = source;
	}

	public City getTarget() {
		return target;
	}

	public void setTarget(City target) {
		this.target = target;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
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
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		Rote other = (Rote) obj;
		if (graph == null) {
			if (other.graph != null)
				return false;
		} else if (!graph.equals(other.graph))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Rote [id=" + id + ", source=" + source + ", target=" + target + ", distance=" + distance + "]";
	}
}
