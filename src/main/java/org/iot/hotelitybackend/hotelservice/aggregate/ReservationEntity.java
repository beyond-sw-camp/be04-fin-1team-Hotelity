package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "reservation_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationCodePk;
    private Date reservationDate;
    private Date reservationCheckinDate;
    private Date reservationCheckoutDate;
    private Integer customerCodeFk;
    private String roomCodeFk;
    private String branchCodeFk;
    private Integer reservationCancelStatus;
    private Integer reservationPersonnel;

    @Builder
    public ReservationEntity(
            Integer reservationCodePk,
            Date reservationDate,
            Date reservationCheckinDate,
            Date reservationCheckoutDate,
            Integer customerCodeFk,
            String roomCodeFk,
            String branchCodeFk,
            Integer reservationCancelStatus,
            Integer reservationPersonnel
    ) {
        this.reservationCodePk = reservationCodePk;
        this.reservationDate = reservationDate;
        this.reservationCheckinDate = reservationCheckinDate;
        this.reservationCheckoutDate = reservationCheckoutDate;
        this.customerCodeFk = customerCodeFk;
        this.roomCodeFk = roomCodeFk;
        this.branchCodeFk = branchCodeFk;
        this.reservationCancelStatus = reservationCancelStatus;
        this.reservationPersonnel = reservationPersonnel;
    }
}
