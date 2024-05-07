package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public StayEntity(
            Integer stayCodePk,
            Date stayCheckinTime,
            Date stayCheckoutTime,
            Integer stayPeopleCount,
            String employeeCodeFk,
            Integer reservationCodeFk
    ) {
        this.stayCodePk = stayCodePk;
        this.stayCheckinTime = stayCheckinTime;
        this.stayCheckoutTime = stayCheckoutTime;
        this.stayPeopleCount = stayPeopleCount;
        this.employeeCodeFk = employeeCodeFk;
        this.reservationCodeFk = reservationCodeFk;
    }
}
