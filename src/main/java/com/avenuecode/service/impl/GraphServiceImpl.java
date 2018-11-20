/**
 * 
 */
package com.avenuecode.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avenuecode.dto.DistanceDTO;
import com.avenuecode.dto.GraphDTO;
import com.avenuecode.dto.GraphInfoResponse;
import com.avenuecode.dto.RoteDTO;
import com.avenuecode.model.City;
import com.avenuecode.model.Graph;
import com.avenuecode.model.Rote;
import com.avenuecode.model.repository.CityRepository;
import com.avenuecode.model.repository.GraphRepository;
import com.avenuecode.model.repository.RoteRepository;
import com.avenuecode.service.GraphService;

/**
 * @author pedro.gomes 2018-08-07 11:20:46
 *
 */
@Service
@Transactional
public class GraphServiceImpl implements GraphService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GraphRepository graphRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private RoteRepository roteRepository;
	
	@Override
	public Graph getById(Integer id) {
		LOGGER.info("<< getById: listing {} param", id);
		Optional<Graph> optional = graphRepository.findById(id);
		Graph graph = optional.orElse(null);
		LOGGER.info(">> getById: listing {} record", graph);
		return graph;
	}

	@Override
	public Graph generate(GraphInfoResponse data) {
		LOGGER.info("<< generate: listing {} param", data);
		Graph entity = new Graph();
		entity = graphRepository.saveAndFlush(entity);

		List<Rote> rotes = new ArrayList<Rote>();
		List<City> cities = new ArrayList<City>();

		for (GraphDTO path : data.getData()) {
			City source = new City(path.getSource(), entity);
			City target = new City(path.getTarget(), entity);

			if (!cities.contains(source)) {
				source = cityRepository.saveAndFlush(source);
				cities.add(source);
			} else {
				source = cities.get(cities.indexOf(source));
			}

			if (!cities.contains(target)) {
				target = cityRepository.saveAndFlush(target);
				cities.add(target);
			} else {
				target = cities.get(cities.indexOf(target));
			}

			Rote rote = new Rote(source, target, path.getDistance(), entity);
			if (!rotes.contains(rote)) {
				rote = roteRepository.saveAndFlush(rote);
				rotes.add(rote);
			}
		}
		entity.setRotes(rotes);
		entity.setCities(cities);
		LOGGER.info(">> generate: listing {} result", entity);
		return entity;
	}
	
	@Override
	public RoteDTO getRoutes(Integer id, String firstTown, String lastTown, Integer stops) {
		LOGGER.info("<< getRoutes: param {}, {}, {}, {}. ", id, firstTown, lastTown, stops);
		
		Graph graph = getById(id);
		if (graph == null) {
			return null;
		}
		
		RoteDTO dto = new RoteDTO();
		City city = cityRepository.findByGraphIdAndName(id, firstTown);		

		List<String> obj = new ArrayList<String>();
		
		if (city == null) {
			dto.getRoutes().add(new HashMap<String, Object>());
			return dto;
		}
		
		getCompleteRoute(city, firstTown, lastTown, stops, obj, dto);
		LOGGER.info(">> getRoutes: result {}. ", dto);
		return dto;
	}

	private Object[] getCompleteRouteDistance(City city, String firstTown, String lastTown, Object[] obj) {
		LOGGER.info(">> getCompleteRouteDistance: params {}, {}, {}, {}, {}. ", city, firstTown, lastTown, obj);
		String path = (String) obj[0];
		int distance = (int) obj[1];

		for (Rote sourceRote : city.getSourceRotes()) {
			path = path.concat(city.getName());

			obj[0] = path;
			obj[1] = distance;
			
			if (obj[0].toString().length() > 100) {
				break;
			}

			if (city.getName().equals(lastTown)) {
				return obj;
			} else {
				distance+=sourceRote.getDistance();
				obj[1] = distance;
				return getCompleteRouteDistance(sourceRote.getTarget(), firstTown, lastTown, obj);
			}
		}
		LOGGER.info(">> getCompleteRouteDistance: result {}. ", obj);
		return obj;
	}

	private void getCompleteRoute(City city, String firstTown, String lastTown, Integer stops, List<String> obj,
			RoteDTO dto) {
		for (Rote sourceRote : city.getSourceRotes()) {
			if (!city.getName().equals(firstTown) && obj.contains(city.getName()) && obj.size() == 1) {
				continue;
			}

			if (!(obj.size() > 0 && obj.lastIndexOf(city.getName()) == obj.size() -1 )) {
				obj.add(city.getName());
			}
			if ((stops != null && stops >= 0 && obj.size() -1 <= stops && (city.getName().equals(lastTown) && obj.size() > 1))
					|| (stops == null && city.getName().equals(lastTown))) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("route", String.join("", obj.toArray(new String[obj.size()])));
				map.put("stops", obj.size() - 1);
				dto.getRoutes().add(map);
				if (obj.size() > 1) {
					obj.remove(obj.lastIndexOf(city.getName()));
				}
				continue;
			} else if (stops != null && stops > 0 && obj.size() -1 > stops) {
				if (obj.size() > 1 && obj.lastIndexOf(city.getName()) == obj.size() -1) {
					obj.remove(obj.lastIndexOf(city.getName()));
				}
				continue;
			}
			getCompleteRoute(sourceRote.getTarget(), firstTown, lastTown, stops, obj, dto);
		}
		if (obj.size() > 1 && obj.lastIndexOf(city.getName()) == obj.size() -1) {
			obj.remove(obj.lastIndexOf(city.getName()));
		}
	}

	@Override
	public DistanceDTO getDistance(Integer graphId, DistanceDTO dto) {
		LOGGER.info(">> getDistance: params {}, {}. ", graphId, dto);
		int distance = 0;
		dto.setDistance(distance);

		Graph graph = getById(graphId);
		if (graph == null) {
			return null;
		}

		if (dto.getPath().size() > 0) {
			for (int i = 0; i < dto.getPath().size() - 1; i++) {
				String source = dto.getPath().get(i);
				String target = dto.getPath().get(i + 1);

				Rote rote = roteRepository.findByGraphAndSourceAndTarget(graphId, source, target);

				if (rote == null) {
					dto.setDistance(-1);
					break;
				} else {
					distance += rote.getDistance();
					dto.setDistance(distance);
				}
			}
		}
		dto.setPath(null);
		LOGGER.info(">> getDistance: result {}. ", dto);
		return dto;
	}

	@Override
	public DistanceDTO getDistanceBetween(Integer graphId, String firstTown, String lastTown) {
		LOGGER.info(">> getDistanceBetween: params {}, {}, {}. ", graphId, firstTown, lastTown);
		DistanceDTO dto = new DistanceDTO();
		int distance = 0;
		
		dto.setDistance(distance);

		Graph graph = getById(graphId);
		if (graph == null) {
			return null;
		} else if (firstTown.equals(lastTown)) {
			dto.setDistance(0);
			dto.setPath(Arrays.asList(firstTown));
			return dto;
		}

		List<Rote> rotes = roteRepository.findByGraphAndSource(graphId, firstTown);
		
		if (rotes == null || (rotes != null && rotes.isEmpty())) {
			dto.setDistance(-1);
			dto.setPath(Arrays.asList(new String[]{firstTown, lastTown}));
			return dto;
		}

		List<Map<String, Object>> paths = new ArrayList<Map<String, Object>>();
		
		for (Rote rote : rotes) {
			Object[] obj = { firstTown, rote.getDistance() };
			obj = getCompleteRouteDistance(rote.getTarget(), firstTown, lastTown, obj);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("route", obj[0]);
			map.put("stops", obj[1]);
			paths.add(map);
		}		
		
		List<Map<String, Object>> paths2 = paths.stream().sorted((o1, o2)-> ((Integer)o1.get("stops")).compareTo((int)o2.get("stops"))).collect(Collectors.toList());
		
		dto.setPath(java.util.Arrays.asList(((String)paths2.get(0).get("route")).split("(?!^)") ));
		dto.setDistance((Integer) paths2.get(0).get("stops"));
		
		if (dto.getPath().size() >= 100) {
			dto.setDistance(-1);
			dto.setPath(Arrays.asList(new String[]{firstTown, lastTown}));
			return dto;
		}
		
		LOGGER.info(">> getDistanceBetween: result {}. ", dto);
		return dto;
	}
}
