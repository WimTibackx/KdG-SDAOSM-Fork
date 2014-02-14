package be.kdg.groepa.filters;

import be.kdg.groepa.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Thierry on 14/02/14.
 */
@Component("authorizeFilter")
@WebFilter(urlPatterns = "/authorized")
public class AuthorizeFilter implements Filter {

    @Autowired
    @Qualifier("userService")
    private UserService userService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       //ServletContext servletContext = filterConfig.getServletContext();
       //WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

       //AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();

       //autowireCapableBeanFactory.configureBean(this, "userService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("Token")){

            }
        }

    }

    @Override
    public void destroy() {

    }
}
