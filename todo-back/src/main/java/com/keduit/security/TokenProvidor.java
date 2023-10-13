package com.keduit.security;

import com.keduit.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
public class TokenProvidor {
    // 임의로 키 생성
    private static final String SELECT_KEY = "FlRpX30pMqDbiAkmlfArbrmVkDD4RqISskGZmBFax5oGVxzXXWUzTR5JyskiHMIV9M1Oicegkpi46AdvrcX1E6CmTUBc6IFbTPiD";

    public String create(UserEntity userEntity) {
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        // header에 들어갈 내용들을 넣어줌
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SELECT_KEY)
                .setSubject(userEntity.getId())
                .setIssuer("todo app")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SELECT_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
