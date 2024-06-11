package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.Mapping;

@Data
public class ReservationDTO {
    private Integer reservationCodePk;
    private Integer customerCodeFk;
    private String customerName;
    private String customerEnglishName;
    private String roomCodeFk;
    private Integer roomNumber;
    private String roomName;
    private String roomLevelName;
    private Integer roomCapacity;
    private String branchCodeFk;
    private String branchName;
    private LocalDateTime reservationDate;
    private LocalDateTime reservationCheckinDate;
    private LocalDateTime reservationCheckoutDate;
    private Integer reservationCancelStatus;
    private Integer reservationPersonnel;
    private Integer stayStatus;
    private String stayPeriod;
}
