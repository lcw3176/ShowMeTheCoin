package com.joebrooks.showmethecoin.auth;

import com.joebrooks.showmethecoin.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private static final String NO_EXIST_USER = "존재하지 않는 유저: ";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUserId(username).orElseThrow(() -> {
            throw new AuthException(NO_EXIST_USER + username);
        });
    }
}
