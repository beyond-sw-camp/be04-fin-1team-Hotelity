package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReservationDTO {
    private Integer reservationCodePk;
    private LocalDateTime reservationDate;
    private LocalDateTime reservationCheckinDate;
    private LocalDateTime reservationCheckoutDate;
    private Integer customerCodeFk;
    private String customerName;
    private String roomCodeFk;
    private String roomName;
    private String roomLevelName;
    private String branchCodeFk;
    private Integer reservationCancelStatus;
    private Integer reservationPersonnel;
    // status가 0이면 체크인x, 1이면 체크인o
    private Integer reservationCheckinStatus;
    private Integer roomCapacity;
}
