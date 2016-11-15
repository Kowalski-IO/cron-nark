package io.kowalski.cronnark.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@JWT
@Provider
public class JWTFilter implements ContainerRequestFilter {

    private final String secretKey;
    private final String issuer;

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    public JWTFilter(@Named("secretKey") final String secretKey, @Named("issuer") final String issuer) {
        this.secretKey = secretKey;
        this.issuer = issuer;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        final String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        final String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            final Claims claims = Jwts.parser().requireIssuer(issuer).setSigningKey(secretKey).parseClaimsJws(token).getBody();
            requestContext.setProperty("jwtClaims", claims);
        } catch (final JwtException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
