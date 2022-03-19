package com.joebrooks.showmethecoin.repository.user;

import com.joebrooks.showmethecoin.global.encrypto.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserEntity> login(String id, String pw) throws NoSuchAlgorithmException {
        return userRepository.findByUserIdAndUserPw(id, SHA256.encrypt(pw));
    }

    public Optional<UserEntity> getUser(String id) {
        return userRepository.findByUserId(id);
    }

    public void save(UserEntity user){
        userRepository.save(user);
    }

    public List<UserEntity> getAllUser(){
        return userRepository.findAll();
    }
}
