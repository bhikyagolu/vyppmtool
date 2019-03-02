package com.veeryash.ppmtool.secuirty;

import com.veeryash.ppmtool.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.veeryash.ppmtool.secuirty.SecurityConstants.EXPIRATION_TIME;
import static com.veeryash.ppmtool.secuirty.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {
    // generate the token

    public String generateToken(Authentication authentication) {
        User user = (User)authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(user.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims).setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
    // validate the token

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid Jwt Signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid Jwt Token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Invalid Jwt Token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported Jwt Token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWt claims string is empty");
        }
        return false;
    }

    // get userid from token
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody();
        Long id = Long.parseLong((String)claims.get("id"));
        return id;
    }
}
