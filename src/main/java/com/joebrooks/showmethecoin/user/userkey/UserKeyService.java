package com.joebrooks.showmethecoin.user.userkey;

import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserKeyService {

    private final UserKeyRepository userKeyRepository;

    public UserKeyEntity getKeySet(UserEntity user, CompanyType companyType){
        return userKeyRepository.findByUserAndCompanyType(user, companyType);
    }
}
