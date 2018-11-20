/**
 * 
 */
package com.avenuecode.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.avenuecode.model.Rote;

/**
 * @author pedro.gomes 2018-08-07 11:21:55
 *
 */
@Repository
public interface RoteRepository extends JpaRepository<Rote, Integer> {

	@Query("select r from Rote r where r.graph.id = :graph and r.source.name = :source and r.target.name = :target")
	Rote findByGraphAndSourceAndTarget(Integer graph, String source, String target);
	
	@Query("select r from Rote r where r.graph.id = :graph and r.source.name = :source")
	List<Rote> findByGraphAndSource(Integer graph, String source);
	
	@Query("select r from Rote r where r.graph.id = :graph")
	List<Rote> findByGraph(Integer graph);
}
