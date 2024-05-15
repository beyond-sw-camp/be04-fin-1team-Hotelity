package org.iot.hotelitybackend.hotelservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RequestModifyStay {
    private Date stayCheckinTime;
    private Date stayCheckoutTime;
    private Integer stayPeopleCount;
    private Integer employeeCodeFk;
    private Integer reservationCodeFk;
}
