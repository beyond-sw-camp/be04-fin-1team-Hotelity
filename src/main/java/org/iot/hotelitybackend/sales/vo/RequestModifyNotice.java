package org.iot.hotelitybackend.sales.vo;

import lombok.Data;

@Data
public class RequestModifyNotice {
    private String noticeTitle;
    private String noticeContent;
    private Integer employeeCodeFk;
    private String noticeLastUpdatedDate;
    private String branchCodeFk;
}
