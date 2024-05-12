package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    private Integer vocCodePk;
    private String vocContent;
    private Date vocCreatedDate;
    private Date vocLastUpdatedDate;
    private Integer customerCodeFk;
    private String vocProcessStatus;
    private String vocCategory;
    private String vocTitle;
    private String employeeCodeFk;
    private String branchCodeFk;
    private String vocImageLink;

    @Builder
    public VocEntity(Integer vocCodePk, String vocContent, Date vocCreatedDate, Date vocLastUpdatedDate, Integer customerCodeFk,
        String vocProcessStatus, String vocCategory, String vocTitle, String employeeCodeFk, String branchCodeFk,
        String vocImageLink) {
        this.vocCodePk = vocCodePk;
        this.vocContent = vocContent;
        this.vocCreatedDate = vocCreatedDate;
        this.vocLastUpdatedDate = vocLastUpdatedDate;
        this.customerCodeFk = customerCodeFk;
        this.vocProcessStatus = vocProcessStatus;
        this.vocCategory = vocCategory;
        this.vocTitle = vocTitle;
        this.employeeCodeFk = employeeCodeFk;
        this.branchCodeFk = branchCodeFk;
        this.vocImageLink = vocImageLink;
    }
}
