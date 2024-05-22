package org.iot.hotelitybackend.common.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseVO {
    private Object data;
    private Integer resultCode;
    private String message;
}
