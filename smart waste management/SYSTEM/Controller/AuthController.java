package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Controller;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Driver;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public static class LoginRequest {
        public String username;
        public String password;
        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        // (Optional setters if needed)
        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class DriverLoginResponse {
        public String id;
        public String username;
        public String role;

        public DriverLoginResponse(String id, String username, String role) {
            this.id = id;
            this.username = username;
            this.role = role;
        }
    }
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DriverRepository driverRepository;
    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Driver driver = driverRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Driver not found"));

            return ResponseEntity.ok(new DriverLoginResponse(
                    driver.getId(),
                    driver.getUsername(),
                    "DRIVER"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


}
