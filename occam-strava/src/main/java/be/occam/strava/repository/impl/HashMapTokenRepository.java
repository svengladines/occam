package be.occam.strava.repository.impl;

import static be.occam.utils.javax.Utils.*;
import java.util.Map;
import java.util.UUID;

import be.occam.strava.repository.TokenEntity;
import be.occam.strava.repository.TokenRepository;

public class HashMapTokenRepository implements TokenRepository {
	
	protected Map<String,TokenEntity> tokens = map();

	@Override
	public TokenEntity findOneByRiderID(String riderID) {
		return tokens.get( riderID );
	}

	@Override
	public TokenEntity save(TokenEntity tokenEntity) {
		tokenEntity.setId( UUID.randomUUID().toString() );
		this.tokens.put(tokenEntity.getRiderID(), tokenEntity);
		return tokenEntity;
	}

}
