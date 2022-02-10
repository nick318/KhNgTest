package idv.imonahhov.khngtest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// simplistic protection from stuff like me hit button fast
@Service
@Scope("singleton")
public class TokenMap {
    private static final int TOKEN_LIFESPAN_MS = 30*60*1000;
    private Map<Long,Long> tokens = Collections.synchronizedMap(new HashMap<>());
    private Map<Long,Long> validUntil = Collections.synchronizedMap(new HashMap<>());
    private SecureRandom secure = new SecureRandom();

    public synchronized Long createTokenForId(Long userId){
        Long token = secure.nextLong();
        tokens.put(userId,token);
        validUntil.put(userId,System.currentTimeMillis()+ TOKEN_LIFESPAN_MS);
        return token;
    }

    public synchronized Boolean validateToken(Long userId, Long token){
        boolean valid = false;
        if(token == null){
            return false;
        }
        if(token == tokens.get(userId)){
            valid = validUntil.get(userId) > System.currentTimeMillis();
        }
        tokens.remove(userId);
        validUntil.remove(userId);
        return valid;
    }
}
