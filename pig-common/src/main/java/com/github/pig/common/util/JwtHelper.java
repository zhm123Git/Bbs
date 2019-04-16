package com.github.pig.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

public class JwtHelper {
  
  private static final String  SECRET = "session_secret";
  
  private static final String  ISSUER = "bbs_user";

  /**
   * 生成token
   * @param claims
   * @return
     */
  public static String genToken(Map<String, String> claims){
    try {
      Algorithm algorithm = Algorithm.HMAC256(SECRET);
      JWTCreator.Builder builder = JWT.create().withIssuer(ISSUER).withExpiresAt(DateUtils.addDays(new Date(), 1));
      claims.forEach((k,v) -> builder.withClaim(k, v));//将clains存储到token里
      return builder.sign(algorithm).toString();
    } catch (IllegalArgumentException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 校验token
   * @param token
   * @return
     */
  public static Map<String, String> verifyToken(String token)  {
    Algorithm algorithm = null;
    try {
      algorithm = Algorithm.HMAC256(SECRET);
    } catch (IllegalArgumentException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
    DecodedJWT jwt =  verifier.verify(token);
    Map<String, Claim> map = jwt.getClaims();
    Map<String, String> resultMap = Maps.newHashMap();
    map.forEach((k,v) -> resultMap.put(k, v.asString()));
    return resultMap;
  }

}
