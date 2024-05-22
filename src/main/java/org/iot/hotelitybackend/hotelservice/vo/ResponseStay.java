package org.iot.hotelitybackend.hotelservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseStay {
    private Integer stayCodePk;
    private Date stayCheckinTime;
    private Date stayCheckoutTime;
    private Integer stayPeopleCount;
    private String employeeCodeFk;
    private Integer reservationCodeFk;
}
