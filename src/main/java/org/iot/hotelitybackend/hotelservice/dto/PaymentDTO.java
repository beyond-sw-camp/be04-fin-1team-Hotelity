package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentDTO {
    private Integer paymentCodePk;
    private Integer paymentAmount;
    private Integer reservationCodeFk;
    private Integer paymentTypeCodeFk;
    private String paymentTypeName;
    private String paymentMethod;
    private Date paymentDate;
    private Integer paymentCancelStatus;
    private Integer customerCodeFk;
    private String customerName;
}
