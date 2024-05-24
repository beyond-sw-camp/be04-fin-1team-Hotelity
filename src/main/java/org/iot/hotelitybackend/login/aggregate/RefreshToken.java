package org.iot.hotelitybackend.login.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import static org.iot.hotelitybackend.common.constant.Constant.REDIS_PREFIX;
import static org.iot.hotelitybackend.common.constant.Constant.REDIS_TIME_TO_LIVE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@RedisHash(value = REDIS_PREFIX, timeToLive = REDIS_TIME_TO_LIVE)
public class RefreshToken {

    @Id
    private String loginCode;       // branchCode + employeeCode
                                    // ex) HQ_1
    @Indexed
    private String refreshToken;
    @Indexed
    private String accessToken;
}

