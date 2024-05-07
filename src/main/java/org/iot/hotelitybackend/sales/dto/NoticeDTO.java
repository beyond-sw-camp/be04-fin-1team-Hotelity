package org.iot.hotelitybackend.sales.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeDTO {
    private int noticeCodePk;
    private String noticeTitle;
    private String noticeContent;
    private Date noticePostedDate;
    private String employeeCodeFk;
}
