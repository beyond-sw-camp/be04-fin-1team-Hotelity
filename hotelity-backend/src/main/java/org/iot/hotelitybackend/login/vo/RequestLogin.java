package org.iot.hotelitybackend.login.vo;

import lombok.Data;

@Data
public class RequestLogin {
    String branchCode;
    Integer employeeCode;
    String employPassword;
}
