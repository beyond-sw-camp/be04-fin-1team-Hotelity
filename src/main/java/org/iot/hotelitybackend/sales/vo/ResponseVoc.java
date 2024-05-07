package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseVoc {
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
