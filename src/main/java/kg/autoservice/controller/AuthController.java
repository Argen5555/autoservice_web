package kg.autoservice.controller;

import kg.autoservice.dto.LoginRequest;
import kg.autoservice.dto.LoginResponse;
import kg.autoservice.dto.UserDto;
import kg.autoservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Авторизация пользователя
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getUsername());
        LoginResponse response = authService.authenticateUser(loginRequest);
        log.info("Login successful for user: {}", loginRequest.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * Регистрация нового пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        log.info("Registration attempt for user: {}", userDto.getUsername());
        try {
            UserDto registeredUser = authService.registerUser(userDto);
            log.info("Registration successful for user: {}", userDto.getUsername());
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Registration failed for user: {}", userDto.getUsername(), e);
            throw e;
        }
    }
}