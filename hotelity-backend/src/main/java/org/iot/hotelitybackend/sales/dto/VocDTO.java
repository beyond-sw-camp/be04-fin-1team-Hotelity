package org.iot.hotelitybackend.sales.dto;

import lombok.Data;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class VocDTO {
    private Integer vocCodePk;
    private String vocTitle;
    private String vocCategory;
    private String vocContent;
    private Integer customerCodeFk;
    private String customerName;
    private LocalDateTime vocCreatedDate;
    private LocalDateTime vocLastUpdatedDate;
    private String branchCodeFk;
    private Integer employeeCodeFk;
    private String PICEmployeeName;
    private Integer vocProcessStatus;
    private String vocResponse;
    private String vocImageLink;
}
