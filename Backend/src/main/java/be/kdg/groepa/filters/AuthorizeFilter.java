package be.kdg.groepa.filters;

import be.kdg.groepa.service.api.UserService;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Created by Thierry on 14/02/14.
 */
@Component("authorizeFilter")
//@WebFilter(urlPatterns = "/authorized/*")
public class AuthorizeFilter implements Filter {

    @Autowired
    //@Qualifier("userService")
    private UserService userService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       //ServletContext servletContext = filterConfig.getServletContext();
       //WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

       //AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();

       //autowireCapableBeanFactory.configureBean(this, "userService");
    }

    //TODO: Is there any way we can test this? MockMVC circumvents filters
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie[] cookies = request.getCookies();
        Logger logger =  Logger.getLogger(AuthorizeFilter.class.getName());
        logger.info("DOING FILTER ");

        if (cookies!= null){
            for (Cookie cookie : cookies ){
                if (cookie.getName().equals("Token")) {
                    logger.info("TOKEN IS "+ cookie.getValue());
                    if (userService.isUserSessionByToken(cookie.getValue())) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        //TODO: This is an ideal place for extending the cookie, but how do we get it to here?
                        return;
                    }
                }
            }
        }
        //TODO: Would be potentially useful for performance if we didn't need to build up those errors every time...
        JSONObject myJson = new JSONObject();
        myJson.put("error","AuthorizationNeeded");
        servletResponse.getWriter().append(myJson.toString());
        Cookie deleteCookie = new Cookie("Token", "");
        deleteCookie.setPath("/");
        deleteCookie.setMaxAge(0);
        ((HttpServletResponse) servletResponse).addCookie(deleteCookie);
        return;
        //return myJson.toString();
        //((HttpServletResponse) servletResponse).sendRedirect("/BackEnd/login"); //TODO How can we avoid hardcoding this?
        // Redirecten

    }

    @Override
    public void destroy() {

    }
}
