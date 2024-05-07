package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "notice_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noticeCodePk;
    private String noticeTitle;
    private String noticeContent;
    private Date noticePostedDate;
    private String employeeCodeFk;
}
