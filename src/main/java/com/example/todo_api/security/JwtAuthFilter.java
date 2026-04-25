package com.example.todo_api.security;


import com.example.todo_api.service.UserDetailsServiceImpl;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException, java.io.IOException {
        String authHeader = req.getHeader("Authorization");

        //No bearer token? skip - let spring security block if needed

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            chain.doFilter(req,res);
            return;
        }

        String token = authHeader.substring(7); // strip "bearer
        String username = jwtUtil.extractUsername(token);

        //only authenticate if not already done for this request

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);

            if(jwtUtil.isTokenValid(token)){
                var authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                //mark this request as authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(req,res);
    }
}
