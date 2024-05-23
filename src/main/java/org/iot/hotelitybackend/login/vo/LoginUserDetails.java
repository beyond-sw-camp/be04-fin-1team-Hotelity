package org.iot.hotelitybackend.login.vo;

import org.iot.hotelitybackend.employee.aggregate.EmployeeEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class LoginUserDetails implements UserDetails {

    private final EmployeeEntity employeeEntity;

    public LoginUserDetails(EmployeeEntity employeeEntity) {
        this.employeeEntity = employeeEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + employeeEntity.getPermissionName()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return employeeEntity.getEmployeeSystemPassword();
    }

    @Override
    public String getUsername() {
        return employeeEntity.getBranchId() + "_" + employeeEntity.getEmployeeCodePk();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
