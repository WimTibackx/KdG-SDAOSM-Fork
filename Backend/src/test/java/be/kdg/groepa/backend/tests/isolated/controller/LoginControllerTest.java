package be.kdg.groepa.backend.tests.isolated.controller;

import be.kdg.groepa.backend.tests.util.controller.MockMvcBase;
import be.kdg.groepa.controllers.LoginController;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;

import org.easymock.EasyMock;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
public class LoginControllerTest extends MockMvcBase {
    @Autowired
    private LoginController loginController;
    
    @Autowired
    private UserService userService;
    private UserService userServiceMock;

    private String testUsername = "username@lc.test.com";

    @Before
    public void init(){
    	super.init();
        this.userServiceMock = EasyMock.createMock(UserService.class);
        this.loginController.setUserService(userServiceMock);
    }
    
    @After
    public void cleanup() {
    	super.cleanup();
    	this.loginController.setUserService(userService);
    }

    @Test
    public void succesControllerLogin() throws Exception {
    	User user = new User();	//TODO decent user
    	user.setId(1);
    	EasyMock.expect(this.userServiceMock.checkLogin(testUsername, "Password1")).andReturn(true);
    	EasyMock.expect(this.userServiceMock.getUserSession(testUsername)).andReturn(new SessionObject(user));
    	EasyMock.replay(this.userServiceMock);
    	String reqData = new JSONObject().put("username", testUsername).put("password", "Password1").toString();
        super.doRequest(HttpMethod.POST, "/login", reqData)
    		.andExpect(MockMvcResultMatchers.jsonPath("Token").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("UserId", CoreMatchers.is(1)))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void badRequestJsonError() throws Exception {
    	super.doRequest(HttpMethod.POST, "/login", "")
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("error", CoreMatchers.is("JSONException")));
    }

    @Test
    public void missingDataUsernameError() throws Exception {
    	String reqData = new JSONObject().put("password", "Password1").toString();
    	super.doRequest(HttpMethod.POST, "/login", reqData)
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("error", CoreMatchers.is("MissingDataException")))
            .andExpect(MockMvcResultMatchers.jsonPath("missing", CoreMatchers.is("username")));
    }
    
    @Test
    public void missingDataPasswordError() throws Exception {
    	String reqData = new JSONObject().put("username", testUsername).toString();
    	super.doRequest(HttpMethod.POST, "/login", reqData)
    		.andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("error", CoreMatchers.is("MissingDataException")))
            .andExpect(MockMvcResultMatchers.jsonPath("missing", CoreMatchers.is("password")));
    		
    }

    @Test
    public void loginFailError() throws Exception {
    	EasyMock.expect(this.userServiceMock.checkLogin(testUsername, "Password")).andReturn(false);
    	EasyMock.replay(this.userServiceMock);
    	String reqData = new JSONObject().put("username", testUsername).put("password", "Password").toString();
    	super.doRequest(HttpMethod.POST, "/login", reqData)
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    
    //TODO: Test CheckAuthorization and ResetPassword
}
