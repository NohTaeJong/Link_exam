package com.keduit.service;

import com.keduit.model.UserEntity;
import com.keduit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("invalid argument");
        }

        final String username = userEntity.getUsername();
        if(userRepository.existsByUsername(username)) {
            log.warn("이미 등록된 사용자가 있습니다.", username);
            throw new RuntimeException("이미 등록된 사용자가 있습니다.");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentails(final String username, final String password,
                                       final PasswordEncoder encoder) {
        final UserEntity oriUser = userRepository.findByUsername(username);

        if(oriUser != null && encoder.matches(password, oriUser.getPassword())){
            return oriUser;
        } else {
            return null;
        }
    }
}
