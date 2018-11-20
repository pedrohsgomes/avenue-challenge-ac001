package com.avenuecode.service;

import com.avenuecode.dto.DistanceDTO;
import com.avenuecode.dto.GraphInfoResponse;
import com.avenuecode.dto.RoteDTO;
import com.avenuecode.model.Graph;

/**
 * @author pedro.gomes 2018-08-07 21:20:30
 *
 */
public interface GraphService {

	Graph getById(Integer id);

	/**
	 * @param graph
	 * @return
	 */
	Graph generate(GraphInfoResponse graph);

	/**
	 * @param id
	 * @param firstTown
	 * @param lastTown
	 * @param stops
	 * @return
	 */
	RoteDTO getRoutes(Integer id, String firstTown, String lastTown, Integer stops);

	DistanceDTO getDistance(Integer graph, DistanceDTO path);

	DistanceDTO getDistanceBetween(Integer id, String firstTown, String lastTown);

}
