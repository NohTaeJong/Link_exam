package com.keduit.controller;

import com.keduit.dto.ResponseDto;
import com.keduit.dto.UserDto;
import com.keduit.model.UserEntity;
import com.keduit.security.TokenProvidor;
import com.keduit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvidor providor;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            if(userDto == null || userDto.getPassword() == null) {
                throw new RuntimeException("사용자 혹은 비밀번호가 없습니다.");
            }

            UserEntity user = UserEntity.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();

            UserEntity registerUser = userService.create(user);
            UserDto responseUserDto = UserDto.builder()
                    .id(registerUser.getId())
                    .username(registerUser.getUsername())
                    .build();

            return ResponseEntity.ok().body(responseUserDto);
        } catch (Exception e) {
            ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> athunticate(@RequestBody UserDto userDto) {
        UserEntity user = userService.getByCredentails(userDto.getUsername(),
                userDto.getPassword(), passwordEncoder);

        if(user != null) {
            final String token = providor.create(user);

            final UserDto responseUserDto = UserDto.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseUserDto);
        } else {
            ResponseDto responseDto = ResponseDto.builder()
                    .error("Login Error")
                    .build();

            return ResponseEntity.badRequest().body(responseDto);
        }
    }

}
