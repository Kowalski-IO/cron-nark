package io.kowalski.cronnark;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class CronNarkTokenGen {

    public static void main(final String[] args) {
        final String compactJws = Jwts.builder()
                .setIssuer("cronnark.kowalski.io")
                .signWith(SignatureAlgorithm.HS512, "hellotwitch!")
                .compact();

        System.out.println(compactJws);
    }

}
