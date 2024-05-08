package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StayDTO {
    private Integer stayCodePk;
    private Date stayCheckinTime;
    private Date stayCheckoutTime;
    private Integer stayPeopleCount;
    private String employeeCodeFk;
    private Integer reservationCodeFk;
}
