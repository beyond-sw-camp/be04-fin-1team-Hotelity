package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

@Data
public class RequestNotice {

    private String noticeTitle;
    private String noticeContent;
    private Integer employeeCodeFk;
}
