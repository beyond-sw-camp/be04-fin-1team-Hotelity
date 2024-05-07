package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseNotice {
    private int noticeCodePk;
    private String noticeTitle;
    private String noticeContent;
    private Date noticePostedDate;
    private String employeeCodeFk;
}
