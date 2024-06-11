package org.iot.hotelitybackend.login.service;

import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.hotelmanagement.aggregate.BranchEntity;
import org.iot.hotelitybackend.hotelmanagement.repository.BranchRepository;
import org.iot.hotelitybackend.login.vo.LoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public LoginServiceImpl(EmployeeRepository employeeRepository, BranchRepository branchRepository) {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] employeeInfo = username.split("_");
        String branchCode = employeeInfo[0];
        Integer employeeCode = Integer.valueOf(employeeInfo[1]);

        BranchEntity branch = branchRepository.findById(branchCode).orElseThrow();
        EmployeeEntity employeeWithBranchCode = employeeRepository.findByBranchAndEmployeeCodePk(branch, employeeCode);

        return new LoginUserDetails(employeeWithBranchCode);
    }
}
