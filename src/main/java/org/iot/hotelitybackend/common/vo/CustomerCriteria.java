package org.iot.hotelitybackend.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerCriteria {
    private Integer customerCodePk;
    private String customerName;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerEnglishName;
    private String customerAddress;
    private Integer customerInfoAgreement;
    private Integer customerStatus;
    private Date customerRegisteredDate;
    private Integer nationCodeFk;
    private String customerGender;
    private String nationName;
    private String customerType;
    private String membershipLevelName;
    private String orderBy;
    private Integer sortBy;
    private Integer pageNum;
}
