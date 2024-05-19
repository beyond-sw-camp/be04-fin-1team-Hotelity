package org.iot.hotelitybackend.hotelservice.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.aggregate.RoomEntity;

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

    @ManyToOne
    @JoinColumn(name = "roomCodeFk", insertable = false, updatable = false)
    private RoomEntity room;

    @Column(name = "branch_code_fk")
    private String branchCodeFk;

    @ManyToOne
    @JoinColumn(name = "branch_code_fk", insertable = false, updatable = false)
    private BranchEntity branch;

    private Integer reservationCancelStatus;
    private Integer reservationPersonnel;

    @OneToOne(mappedBy = "reservation")
    private StayEntity stay;

    @Builder
    public ReservationEntity(
            Integer reservationCodePk,
            Date reservationDate,
            Date reservationCheckinDate,
            Date reservationCheckoutDate,
            Integer customerCodeFk,
            String roomCodeFk,
            String branchCodeFk,
            BranchEntity branch,
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
        this.branch = branch;
        this.reservationCancelStatus = reservationCancelStatus;
        this.reservationPersonnel = reservationPersonnel;
    }
}
