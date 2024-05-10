package org.iot.hotelitybackend.hotelservice.dto;

import lombok.Data;

import java.util.Date;

import org.iot.hotelitybackend.customer.dto.CustomerDTO;

@Data
public class PaymentDTO {
    private Integer paymentAmount;
    private Integer reservationCodeFk;
    private Integer paymentCodePk;
    private Integer paymentTypeCodeFk;
    private String paymentMethod;
    private Date paymentDate;
    private Integer customerCodeFk;
    private Integer paymentCancelStatus;
    private PaymentTypeDTO paymentTypeDTO;
    private CustomerDTO customerDTO;
}
