package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class StayDTO {
    private Integer stayCodePk;
    private String roomCode;
    private String roomLevelName;
    private String roomName;
    private Integer stayPeopleCount;
    private Date stayCheckinTime;
    private Date stayCheckoutTime;
    private Integer employeeCodeFk;
    private String employeeName;
    private String branchName;
    private Integer reservationCodeFk;
}
