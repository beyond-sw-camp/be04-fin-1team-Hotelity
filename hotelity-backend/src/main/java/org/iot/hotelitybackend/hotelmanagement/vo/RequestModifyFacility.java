package org.iot.hotelitybackend.hotelmanagement.vo;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RequestModifyFacility {
    private String ancillaryName;
    private String branchCodeFk;
    private String ancillaryLocation;
    private LocalTime ancillaryOpenTime;
    private LocalTime ancillaryCloseTime;
    private String ancillaryPhoneNumber;
    private String ancillaryCategoryName;
}
