/**
 * 
 */
package com.avenuecode.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avenuecode.model.Graph;

/**
 * @author pedro.gomes 2018-08-07 11:21:55
 *
 */
@Repository
public interface GraphRepository extends JpaRepository<Graph, Integer> {

}
