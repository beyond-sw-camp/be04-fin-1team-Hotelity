package org.iot.hotelitybackend.sales.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class NoticeDTO {
    private Integer noticeCodePk;
    private String noticeTitle;
    private String noticeContent;
    private Integer employeeCodeFk;
    private String PICEmployeeName;
    private String branchCodeFk;
    private LocalDateTime noticePostedDate;
    private LocalDateTime noticeLastUpdatedDate;
}
