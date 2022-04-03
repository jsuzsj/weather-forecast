package com.earthchen.spring.cloud.weather.filter;

import com.auth0.jwt.interfaces.Claim;
import com.earthchen.spring.cloud.weather.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "jwtFilter",urlPatterns = "/secure/*")
public class JwtFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setCharacterEncoding("UTF-8");
        //获取header里的token
        String token=request.getHeader("authorization");
        if ("OPTIONS".equals(request.getMethod())) {              //除了 OPTIONS请求以外, 其它请求应该被JWT检查
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
        }else {
            if (token == null) {
                response.getWriter().write("没有token！");
                return;
            }
        }

        Map<String, Claim> userData = JwtUtil.verifyToken(token);

        if (userData==null){
            response.getWriter().write("token不合法！");
            return;
        }
        //Integer id = userData.get("id").asInt();
        String name = userData.get("name").asString();
        String userName = userData.get("userName").asString();
        //拦截器 拿到用户信息，放到request中
       // request.setAttribute("id", id);
        request.setAttribute("name", name);
        request.setAttribute("userName", userName);
        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {

    }
}
