package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Driver;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

 @Autowired
 private DriverRepository driverRepository;

 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
  Driver driver = driverRepository.findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));

  return User.builder()
          .username(driver.getUsername())
          .password(driver.getPassword())
          .roles("DRIVER") // Automatically prepends "ROLE_"
          .build();
 }
}
