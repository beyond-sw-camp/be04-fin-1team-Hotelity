package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationDTO {
    private Integer reservationCodePk;
    private Date reservationDate;
    private Date reservationCheckinDate;
    private Date reservationCheckoutDate;
    private Integer customerCodeFk;
    private String roomCodeFk;
    private String roomName;
    private String roomLevelName;
    private Integer branchCodeFk;
    private Integer reservationCancelStatus;
    private Integer reservationPersonnel;
}
