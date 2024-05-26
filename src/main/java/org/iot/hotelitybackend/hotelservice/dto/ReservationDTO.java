package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Integer reservationCodePk;
    private Integer customerCodeFk;
    private String customerName;
    private String customerEnglishName;
    private String roomCodeFk;
    private String roomName;
    private String roomLevelName;
    private Integer roomCapacity;
    private String branchCodeFk;
    private LocalDateTime reservationDate;
    private LocalDateTime reservationCheckinDate;
    private LocalDateTime reservationCheckoutDate;
    private Integer reservationCancelStatus;
    private Integer reservationPersonnel;
}
