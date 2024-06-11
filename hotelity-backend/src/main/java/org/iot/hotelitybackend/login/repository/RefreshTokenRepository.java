package org.iot.hotelitybackend.login.repository;

import org.iot.hotelitybackend.login.aggregate.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Boolean existsByRefreshToken(String refreshToken);

    RefreshToken findByRefreshToken(String refreshToken);
}
