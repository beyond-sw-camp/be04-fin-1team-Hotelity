package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RequestReplyVoc {
    private String vocTitle;
    private String vocContent;
    private Date vocCreatedDate;
    private Date vocLastUpdatedDate;
    private int customerCodeFk;
    private String employeeCodeFk;
    private String branchCodeFk;
    private String vocResponse;
    private int vocProcessStatus;
    private String vocImageLink;
}
