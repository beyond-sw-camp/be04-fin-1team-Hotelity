package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "voc_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VocEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vocCodePk;
    private String vocContent;
    private Date vocCreatedDate;
    private Date vocLastUpdatedDate;
    private int customerCodeFk;
    private String vocProcessStatus;
    private String vocCategory;
    private String vocTitle;
    private String employeeCodeFk;
    private int branchCodeFk;
    private String vocImageLink;
}
