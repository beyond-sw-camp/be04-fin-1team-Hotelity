package org.iot.hotelitybackend.hotelservice.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class RequestModifyStay {
    private LocalDateTime stayCheckinTime;
    private LocalDateTime stayCheckoutTime;
    private Integer stayPeopleCount;
    private Integer employeeCodeFk;
    private Integer reservationCodeFk;
}
