package com.joebrooks.showmethecoin.user.userkey;

import com.joebrooks.showmethecoin.trade.CompanyType;
import com.joebrooks.showmethecoin.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserKeyRepository extends JpaRepository<UserKeyEntity, Long> {

    UserKeyEntity findByUserAndCompanyType(UserEntity user, CompanyType companyType);
}
