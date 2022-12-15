package com.joebrooks.showmethecoin.repository.userkey;

import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserKeyService {

    private final UserKeyRepository userKeyRepository;

    public UserKeyEntity getKeySet(UserEntity user, CompanyType companyType) {
        return userKeyRepository.findByUserAndCompanyType(user, companyType);
    }
}
