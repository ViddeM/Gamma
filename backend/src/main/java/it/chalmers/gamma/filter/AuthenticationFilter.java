package it.chalmers.gamma.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import it.chalmers.gamma.response.InvalidJWTTokenResponse;
import it.chalmers.gamma.service.ITUserService;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

public class AuthenticationFilter extends GenericFilterBean {

    private String secretKey;

    private String issuer;

    private ITUserService itUserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter(ITUserService itUserService, String secretKey, String issuer){
        this.itUserService = itUserService;
        this.secretKey = secretKey;
        this.issuer = issuer;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        String encodedToken = resolveToken((HttpServletRequest) request);
        if (encodedToken != null) {
            Jws<Claims> claim = decodeToken(encodedToken);
            String token = null;
            if (claim != null) {
                token = claim.getBody().getSubject();
            }
            if (token != null && validateToken(encodedToken)) {
                Authentication auth = getAuthentication(token);
                System.out.println(auth);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(String cid) {
        UserDetails userDetails = this.itUserService.loadUserByUsername(cid);
        if (userDetails == null) {
            throw new InvalidJWTTokenResponse();
        }
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
            userDetails.getPassword(), userDetails.getAuthorities());
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> decodeToken(String token) {
        try {
            return Jwts.parser()
                .requireIssuer(this.issuer)
                .setSigningKey(Base64.getEncoder().encodeToString(this.secretKey.getBytes()))
                .parseClaimsJws(token);
        } catch (MalformedJwtException | SignatureException e) {
            LOGGER.warn(e.getMessage());
            return null;
        }
    }

    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return removeBearer(bearerToken);
        }
        return null;
    }

    private String removeBearer(String token) {
        return token.substring(7);
    }

}