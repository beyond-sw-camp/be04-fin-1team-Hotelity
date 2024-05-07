package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "stay_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stayCodePk;
    private Date stayCheckinTime;
    private Date stayCheckoutTime;
    private Integer stayPeopleCount;
    private String employeeCodeFk;
    private Integer reservationCodeFk;
}
