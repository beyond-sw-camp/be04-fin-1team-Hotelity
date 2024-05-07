package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private Integer branchCodeFk;
    private Integer reservationCancelStatus;
    private Integer reservationPersonnel;
}
