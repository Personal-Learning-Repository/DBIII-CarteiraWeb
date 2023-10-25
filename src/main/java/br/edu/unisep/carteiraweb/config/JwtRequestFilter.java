package br.edu.unisep.carteiraweb.config;

import br.edu.unisep.carteiraweb.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String requesTokenHeader = request.getHeader("Authorization");

        String email = null;
        String jwtToken = null;

        if (requesTokenHeader != null && requesTokenHeader.startsWith("Bearer ")) {
            jwtToken = requesTokenHeader.substring(7);
            try {
                email = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.err.println("Não foi possível obter o token");
            } catch (ExpiredJwtException e) {
                System.err.println("Token expirado");
            }
        } else {
            System.err.println("JWT Token não começa com Bearer");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails uDet = this.jwtUserDetailsService.loadUserByUsername(email);
            if (jwtTokenUtil.validateToken(jwtToken, uDet)) {
                UsernamePasswordAuthenticationToken uPassAuthToken = new
                        UsernamePasswordAuthenticationToken(uDet, null, uDet.getAuthorities());
                uPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(uPassAuthToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
