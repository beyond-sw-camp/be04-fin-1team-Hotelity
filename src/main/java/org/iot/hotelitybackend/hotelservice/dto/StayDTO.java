package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class StayDTO {
    private Integer stayCodePk;
    private Integer customerCodeFk;
    private String customerName;
    private String roomCode;
    private String roomName;
    private String roomLevelName;
    private Integer roomCapacity;
    private Integer stayPeopleCount;
    private LocalDateTime stayCheckinTime;
    private LocalDateTime stayCheckoutTime;
    private Integer employeeCodeFk;
    private String employeeName;
    private String branchCodeFk;
    private Integer reservationCodeFk;
    private Integer stayCheckoutStatus;
}
