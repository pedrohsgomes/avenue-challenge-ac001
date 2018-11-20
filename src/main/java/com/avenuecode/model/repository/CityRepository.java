/**
 * 
 */
package com.avenuecode.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avenuecode.model.City;

/**
 * @author pedro.gomes 2018-08-07 11:21:55
 *
 */
@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

	City findByGraphIdAndName(Integer graph, String name);
}
