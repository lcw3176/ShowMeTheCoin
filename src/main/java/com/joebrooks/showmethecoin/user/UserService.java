package com.joebrooks.showmethecoin.user;

import com.joebrooks.showmethecoin.auth.AuthException;
import com.joebrooks.showmethecoin.auth.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserEntity login(String id, String pw) {
        return userRepository.findByUserIdAndUserPw(id, SHA256.encrypt(pw)).orElseThrow(() -> {
            throw new AuthException("id: " + id + "  pw:" + pw);
        });
    }

    public UserEntity getUser(String id) {
        return userRepository.findByUserId(id).orElseThrow(() -> {
            throw new AuthException("유저 없음: " + id);
        });
    }

    public void save(UserEntity user){
        userRepository.save(user);
    }

    public List<UserEntity> getAllUser(){
        return userRepository.findAll();
    }
}
