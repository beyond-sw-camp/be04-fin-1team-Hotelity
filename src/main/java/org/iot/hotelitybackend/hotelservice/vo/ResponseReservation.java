package org.iot.hotelitybackend.hotelservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseReservation {
    private Integer reservationCodePk;
    private Date reservationDate;
    private Date reservationCheckinDate;
    private Date reservationCheckoutDate;
    private Integer customerCodeFk;
    private String roomCodeFk;
    private String branchCodeFk;
    private Integer reservationCancelStatus;
    private Integer reservationPersonnel;
}
