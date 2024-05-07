package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "payment_log_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentAmount;
    private Integer reservationCodeFk;
    private Integer paymentCodePk;
    private Integer paymentTypeCodeFk;
    private String paymentMethod;
    private Date paymentDate;
    private Integer customerCodeFk;
    private Integer paymentCancelStatus;

    @Builder
    public PaymentEntity(
            Integer paymentAmount,
            Integer reservationCodeFk,
            Integer paymentCodePk,
            Integer paymentTypeCodeFk,
            String paymentMethod,
            Date paymentDate,
            Integer customerCodeFk,
            Integer paymentCancelStatus
    ) {
        this.paymentAmount = paymentAmount;
        this.reservationCodeFk = reservationCodeFk;
        this.paymentCodePk = paymentCodePk;
        this.paymentTypeCodeFk = paymentTypeCodeFk;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.customerCodeFk = customerCodeFk;
        this.paymentCancelStatus = paymentCancelStatus;
    }
}

