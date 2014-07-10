/**
 * 
 */
package be.kdg.groepa.backend.tests.util.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import be.kdg.groepa.TestUtil;

/**
 * @author wim
 *
 */
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public abstract class MockMvcBase {
	protected MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	protected void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	protected void cleanup() { }
	
	protected ResultActions doRequest(HttpMethod method, String url, String content) throws Exception {
		return this.mockMvc.perform(MockMvcRequestBuilders.request(method, url)
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(content));
	}
}