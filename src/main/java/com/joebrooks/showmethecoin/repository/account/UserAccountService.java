package com.joebrooks.showmethecoin.repository.account;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public void changeBalance(UserEntity user, double balance){
        UserAccountEntity accountEntity = userAccountRepository.findByUser(user);
        accountEntity.changeBalance(balance);

        userAccountRepository.save(accountEntity);
    }

    public double getBalance(UserEntity user){
        return userAccountRepository.findByUser(user).getBalance();
    }
}
