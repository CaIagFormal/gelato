package com.tcc.gelato.config;

import com.tcc.gelato.service.S_Jwt;
import com.tcc.gelato.service.S_UserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Deprecated
public class F_Jwt extends OncePerRequestFilter {

    private final S_Jwt s_jwt;
    private final ApplicationContext context;

    public F_Jwt(S_Jwt s_jwt, ApplicationContext context) {
        this.s_jwt = s_jwt;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader==null||!authHeader.startsWith("Beaver ")) {
            filterChain.doFilter(request,response);
            return;
        }
        String token = authHeader.substring(7);
        String username = s_jwt.extrairUsername(token);

        if (username==null|| SecurityContextHolder.getContext().getAuthentication()!=null) {
            filterChain.doFilter(request,response);
            return;
        }

        UserDetails userDetails = context.getBean(S_UserDetails.class).loadUserByUsername(username);

        if (!s_jwt.validarToken(token, username,userDetails)) {
            filterChain.doFilter(request,response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);
    }
}
