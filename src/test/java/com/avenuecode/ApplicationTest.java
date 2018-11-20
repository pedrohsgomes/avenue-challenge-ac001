package com.avenuecode;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.avenuecode.controller.ApiRequestController;
import com.avenuecode.dto.DistanceDTO;
import com.avenuecode.dto.RoteDTO;
import com.avenuecode.model.Graph;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author pedro.gomes 2018-08-10 09:50:02
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {
	
	@Autowired
	private ApiRequestController apiRequestController;

	@Autowired	
	private TestGeneric<ApiRequestController> testGeneric;
		
	private ObjectMapper mapper = new ObjectMapper();
	private Graph graph;
	
	private ResultMatcher isNotFound = MockMvcResultMatchers.status().isNotFound();
	private ResultMatcher isOK = MockMvcResultMatchers.status().isOk();
	
	@Before
    public void init() throws Exception {
		testGeneric.init(apiRequestController);
		this.graph = saveGraph();		
    }
	
	public Graph saveGraph() throws Exception {
		String path = "/graph";
		String data = "{\"data\":[{\"source\":\"A\",\"target\":\"B\",\"distance\":5},{\"source\":\"B\",\"target\":\"C\",\"distance\":4},{\"source\":\"C\",\"target\":\"D\",\"distance\":8},{\"source\":\"D\",\"target\":\"C\",\"distance\":8},{\"source\":\"D\",\"target\":\"E\",\"distance\":6},{\"source\":\"A\",\"target\":\"D\",\"distance\":5},{\"source\":\"C\",\"target\":\"E\",\"distance\":2},{\"source\":\"E\",\"target\":\"B\",\"distance\":3},{\"source\":\"A\",\"target\":\"E\",\"distance\":7}]}";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("data", data);
		
		String response = testGeneric.postTest(path, data, isOK);
		Graph obj = mapper.readValue(response, Graph.class);
		
		return obj;
	}
		
	/***
	 * Success test for save graph
	 * @throws Exception
	 */
	@Test
	public void generateGraph() throws Exception {
		Graph obj = saveGraph();
		Assert.assertNotNull(">> generateGraph << Graph not generated", obj);
	}
	
	/***
	 * Success test for get graph
	 * @throws Exception
	 */
	@Test
	public void getGraph() throws Exception {
		//ResponseEntity<Graph> obj = mapper.readValue(response, new TypeReference<ResponseEntity<Graph>>() {});
		String path = "/graph/" +this.graph.getId();
		String response = testGeneric.getTest(path, isOK);
		Graph obj = mapper.readValue(response, Graph.class);
		Assert.assertNotNull(">> getGraph << Graph not retrieved", obj);
		Assert.assertEquals(">> getGraph << Graph not retrieved", obj.getId(), this.graph.getId());
	}
	
	/***
	 * Fail test for get graph
	 * @throws Exception
	 */
	@Test
	public void getGraphFail() throws Exception {
		String path = "/graph/999999999";
		String response = testGeneric.getTest(path, isNotFound);
		Assert.assertEquals(">> getGraphFail << Graph retrieved", response, "");
	}
	
	/***
	 * Success test for get roles from path
	 * @throws Exception
	 */
	@Test
	public void getRoutes() throws Exception {
		String path = "/routes/" + this.graph.getId() + "/from/A/to/B";
		String response = testGeneric.postTest(path, "maxStops", "4", isOK);
		RoteDTO dto = mapper.readValue(response, RoteDTO.class);
		Assert.assertNotNull(">> getRoutes << Routes not retrieved", dto);
		Assert.assertNotNull(">> getRoutes << Routes not retrieved", dto.getRoutes());
		Assert.assertNotEquals(">> getRoutes << Routes is empty", dto.getRoutes().isEmpty());
		Assert.assertEquals(">> getRoutes << Routes not valid", dto.getRoutes().size(), 4);
	}
	
	/***
	 * Fail test (notFound graph) for get roles from path
	 * @throws Exception
	 */
	@Test
	public void getRoutesFail() throws Exception {
		String path = "/routes/999999999/from/A/to/B";
		String response = testGeneric.postTest(path, "maxStops", "4", isNotFound);
		Assert.assertEquals(">> getRoutesFail << Routes not valid", response, "");
	}
	
	/***
	 * Fail test (empty rote) for get roles from path
	 * @throws Exception
	 */
	@Test
	public void getRoutesFailEmpty() throws Exception {
		String path = "/routes/" + this.graph.getId() + "/from/K/to/Z";
		String response = testGeneric.postTest(path, "maxStops", "4", isOK);
		RoteDTO dto = mapper.readValue(response, RoteDTO.class);
		Assert.assertNotNull(">> getRoutes << Routes not retrieved", dto);
		Assert.assertNotNull(">> getRoutes << Routes not retrieved", dto.getRoutes());
		Assert.assertEquals(">> getRoutesFailEmpty << Routes is not empty", dto.getRoutes().size(), 1);
		Assert.assertTrue(">> getRoutesFailEmpty << Routes is not empty", dto.getRoutes().iterator().next().isEmpty());
	}
		
	/***
	 * Success test for get distance from a specific path
	 * @throws Exception
	 */
	@Test
	public void getDistance() throws Exception {
		String path = "/distance/" + this.graph.getId();
		String data = "{\"path\":[\"A\",\"E\",\"B\",\"C\",\"D\"]}";
		String response = testGeneric.postTest(path, data, isOK);
		DistanceDTO dto = mapper.readValue(response, DistanceDTO.class);
		Assert.assertNotNull(">> getDistance << Distance not retrieved", dto);
		Assert.assertNotNull(">> getDistance << Distance not calculated", dto.getDistance());
		Assert.assertEquals(">> getDistance << Routes not valid", dto.getDistance().intValue(), 22);
	}
	
	/***
	 * Success test for get distance from empty path
	 * @throws Exception
	 */
	@Test
	public void getDistanceEmpty() throws Exception {
		String path = "/distance/" + this.graph.getId();
		String data = "{\"path\":[]}";
		String response = testGeneric.postTest(path, data, isOK);
		DistanceDTO dto = mapper.readValue(response, DistanceDTO.class);
		Assert.assertNotNull(">> getDistanceEmpty << Distance not retrieved", dto);
		Assert.assertNotNull(">> getDistanceEmpty << Distance not calculated", dto.getDistance());
		Assert.assertEquals(">> getDistanceEmpty << Routes not valid", dto.getDistance().intValue(), 0);
	}
	
	/***
	 * Success test for get distance from sigle element path
	 * @throws Exception
	 */
	@Test
	public void getDistanceSingleElement() throws Exception {
		String path = "/distance/" + this.graph.getId();
		String data = "{\"path\":[\"A\"]}";
		String response = testGeneric.postTest(path, data, isOK);
		DistanceDTO dto = mapper.readValue(response, DistanceDTO.class);
		Assert.assertNotNull(">> getDistanceSingleElement << Distance not retrieved", dto);
		Assert.assertNotNull(">> getDistanceSingleElement << Distance not calculated", dto.getDistance());
		Assert.assertEquals(">> getDistanceSingleElement << Routes not valid", dto.getDistance().intValue(), 0);
	}
	
	/***
	 * Fail test for get distance from not found graph
	 * @throws Exception
	 */
	@Test
	public void getDistanceFail() throws Exception {
		String path = "/distance/999999999";
		String data = "{\"path\":[\"A\",\"E\",\"B\",\"C\",\"D\"]}";
		String response = testGeneric.postTest(path, data, isNotFound);
		Assert.assertEquals(">> getDistanceFail << Routes not valid", response, "");
	}
	
	/***
	 * Success test for get distance between A and C city
	 * @throws Exception
	 */
	@Test
	public void getDistanceBetween() throws Exception {
		String path = "/distance/" + this.graph.getId() + "/from/A/to/C";
		String response = testGeneric.postTest(path, isOK);
		DistanceDTO dto = mapper.readValue(response, DistanceDTO.class);
		Assert.assertNotNull(">> getDistanceBetween << Distance not retrieved", dto);
		Assert.assertNotNull(">> getDistanceBetween << Distance not calculated", dto.getDistance());
		Assert.assertNotNull(">> getDistanceBetween << Path not found", dto.getPath());
		Assert.assertEquals(">> getDistanceBetween << Routes not valid", dto.getDistance().intValue(), 9);
	}
	
	/***
	 * Success test for get no path for distance between B and E city
	 * @throws Exception
	 */
	@Test
	public void getDistanceBetweenNoPath() throws Exception {
		String path = "/distance/" + this.graph.getId() + "/from/B/to/E";
		String response = testGeneric.postTest(path, isOK);
		DistanceDTO dto = mapper.readValue(response, DistanceDTO.class);
		Assert.assertNotNull(">> getDistanceBetweenNoPath << Distance not retrieved", dto);
		Assert.assertNotNull(">> getDistanceBetweenNoPath << Distance not calculated", dto.getDistance());
		Assert.assertNotNull(">> getDistanceBetweenNoPath << Path not found", dto.getPath());
		Assert.assertEquals(">> getDistanceBetweenNoPath << Routes not valid", dto.getDistance().intValue(), -1);
	}
	
	/***
	 * Success test for get for distance from the same city
	 * @throws Exception
	 */
	@Test
	public void getDistanceBetweenZero() throws Exception {
		String path = "/distance/" + this.graph.getId() + "/from/B/to/B";
		String response = testGeneric.postTest(path, isOK);
		DistanceDTO dto = mapper.readValue(response, DistanceDTO.class);
		Assert.assertNotNull(">> getDistanceBetweenZero << Distance not retrieved", dto);
		Assert.assertNotNull(">> getDistanceBetweenZero << Distance not calculated", dto.getDistance());
		Assert.assertNotNull(">> getDistanceBetweenZero << Path not found", dto.getPath());
		Assert.assertEquals(">> getDistanceBetweenZero << Routes not valid", dto.getDistance().intValue(), 0);
	}
	
	/***
	 * Test for get for distance from not exists graph
	 * @throws Exception
	 */
	@Test
	public void getDistanceBetweenNotFound() throws Exception {
		String path = "/distance/999999999/from/A/to/C";
		String response = testGeneric.postTest(path, isNotFound);
		Assert.assertEquals(">> getDistanceBetweenNotFound << Routes not valid", response, "");
	}
	
	/***
	 * Test for get for distance from not exists path or city
	 * @throws Exception
	 */
	@Test
	public void getDistanceBetweenPathNotFound() throws Exception {
		String path = "/distance/" + this.graph.getId() + "/from/Z/to/C";
		String response = testGeneric.postTest(path, isOK);
		DistanceDTO dto = mapper.readValue(response, DistanceDTO.class);
		Assert.assertNotNull(">> getDistanceBetweenPathNotFound << Distance not retrieved", dto);
		Assert.assertNotNull(">> getDistanceBetweenPathNotFound << Distance not calculated", dto.getDistance());
		Assert.assertNotNull(">> getDistanceBetweenPathNotFound << Path not found", dto.getPath());
		Assert.assertEquals(">> getDistanceBetweenPathNotFound << Routes not valid", dto.getDistance().intValue(), -1);
	}
}