package org.iot.hotelitybackend.sales.dto;

import lombok.Data;
import org.iot.hotelitybackend.customer.dto.CustomerDTO;
import org.iot.hotelitybackend.employee.dto.EmployeeDTO;

import java.util.Date;

@Data
public class VocDTO {
    private Integer vocCodePk;
    private String vocContent;
    private Date vocCreatedDate;
    private Date vocLastUpdatedDate;
    private Integer customerCodeFk;
    private String vocProcessStatus;
    private String vocCategory;
    private String vocTitle;
    private String employeeCodeFk;
    private String branchCodeFk;
    private String vocImageLink;
    private String customerName;
    private String employeeName;
}
