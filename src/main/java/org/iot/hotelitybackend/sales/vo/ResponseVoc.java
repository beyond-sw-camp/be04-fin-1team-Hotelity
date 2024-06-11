package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseVoc {
    private Integer vocCodePk;
    private String vocContent;
    private Date vocCreatedDate;
    private Date vocLastUpdatedDate;
    private Integer customerCodeFk;
    private Integer vocProcessStatus;
    private String vocCategory;
    private String vocTitle;
    private String employeeCodeFk;
    private String branchCodeFk;
    private String vocImageLink;
}
