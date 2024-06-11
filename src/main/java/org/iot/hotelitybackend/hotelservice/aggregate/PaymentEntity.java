package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.iot.hotelitybackend.customer.aggregate.CustomerEntity;

@Entity
@Table(name = "payment_log_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentCodePk;
    private Integer reservationCodeFk;
    private Integer paymentAmount;

    @Column(name = "payment_type_code_fk")
    private Integer paymentTypeCodeFk;

    private String paymentMethod;

    private Date paymentDate;
    @Column(name = "customer_code_fk")
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

    @ManyToOne
    @JoinColumn(name = "payment_type_code_fk", insertable = false, updatable = false)
    private PaymentTypeEntity paymentType;

    @ManyToOne
    @JoinColumn(name = "customer_code_fk", insertable = false, updatable = false)
    private CustomerEntity customer;

    @Override
    public String toString() {
        return "PaymentEntity{" +
            "paymentCodePk=" + paymentCodePk +
            ", reservationCodeFk=" + reservationCodeFk +
            ", paymentAmount=" + paymentAmount +
            ", paymentTypeCodeFk=" + paymentTypeCodeFk +
            ", paymentMethod='" + paymentMethod + '\'' +
            ", paymentDate=" + paymentDate +
            ", customerCodeFk=" + customerCodeFk +
            ", paymentCancelStatus=" + paymentCancelStatus +
            '}';
    }
}

