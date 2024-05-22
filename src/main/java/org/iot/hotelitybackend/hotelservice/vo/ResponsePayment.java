package org.iot.hotelitybackend.hotelservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponsePayment {
    private Integer paymentAmount;
    private Integer reservationCodeFk;
    private Integer paymentCodePk;
    private Integer paymentTypeCodeFk;
    private String paymentMethod;
    private Date paymentDate;
    private Integer customerCodeFk;
    private Integer paymentCancelStatus;
}
