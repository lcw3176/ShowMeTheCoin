package com.joebrooks.showmethecoin.repository.user;

import com.joebrooks.showmethecoin.auth.AuthException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private static final String NO_EXIST_USER = "존재하지 않는 유저: ";
    private final UserRepository userRepository;

    public UserEntity getUser(String id) {
        return userRepository.findByUserId(id).orElseThrow(() -> {
            throw new AuthException(NO_EXIST_USER + id);
        });
    }

    public void save(UserEntity user) {
        userRepository.save(user);
    }

    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

}
