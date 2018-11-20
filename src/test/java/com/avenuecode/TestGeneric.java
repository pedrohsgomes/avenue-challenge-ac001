package com.avenuecode;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

@Component
public class TestGeneric<T> {
    
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    private MockMvc mockMvc;
        
    public void init(T controller) throws Exception {
    	this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    
    public String getTest(String path, ResultMatcher matcher) throws Exception{
        LOGGER.info("<< Initializing GET test from path {}", path);
        ResultActions result = this.mockMvc.perform(
        		MockMvcRequestBuilders.get(path).accept(MediaType.ALL)
        		.contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(matcher);
        LOGGER.info(">> Finished GET test");
        return result.andReturn().getResponse().getContentAsString();        
    }
    
    public String postTest(String path, ResultMatcher matcher) throws Exception{
        LOGGER.info(">> Initializing POST test from path {}", path);
        ResultActions result = this.mockMvc.perform(
        		MockMvcRequestBuilders.post(path)
        		.accept(MediaType.ALL)
        		.contentType(MediaType.APPLICATION_JSON)
        );        
        result.andExpect(matcher);        
        LOGGER.info(">> Finished POST test");
        return result.andReturn().getResponse().getContentAsString();
    }
    
    public String postTest(String path,  MultiValueMap<String, String> param, ResultMatcher matcher) throws Exception{
        LOGGER.info(">> Initializing POST test from path {}", path);
        ResultActions result = this.mockMvc.perform(
        		MockMvcRequestBuilders.post(path)
        		.accept(MediaType.ALL)
        		.contentType(MediaType.APPLICATION_JSON)
        		.params(param)
        );
        result.andExpect(matcher);
        LOGGER.info(">> Finished POST test");
        return result.andReturn().getResponse().getContentAsString();        
    }
    
    public String postTest(String path,  String paramName, String param, ResultMatcher matcher) throws Exception{
        LOGGER.info(">> Initializing POST test from path {}", path);
        ResultActions result = this.mockMvc.perform(
        		MockMvcRequestBuilders.post(path)
        		.accept(MediaType.ALL)
        		.contentType(MediaType.APPLICATION_JSON)
        		.param(paramName, param)
        );
        result.andExpect(matcher);
        LOGGER.info(">> Finished POST test");
        return result.andReturn().getResponse().getContentAsString();        
    }
    
    public String postTest(String path,  String content, ResultMatcher matcher) throws Exception{
        LOGGER.info(">> Initializing POST test from path {}", path);
        ResultActions result = this.mockMvc.perform(
        		MockMvcRequestBuilders.post(path)
        		.accept(MediaType.ALL)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(content)
        );        
        //((HandlerMethod)result.andReturn().getHandler()).getMethod().getReturnType()
        //result.andReturn().getResponse().getContentAsString();
        result.andExpect(matcher);        
        LOGGER.info(">> Finished POST test");
        return result.andReturn().getResponse().getContentAsString();
    }
    

}
