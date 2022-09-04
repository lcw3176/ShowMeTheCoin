package com.joebrooks.showmethecoin.repository.userkey;

import com.joebrooks.showmethecoin.repository.CompanyType;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserKeyRepository extends JpaRepository<UserKeyEntity, Long> {

    UserKeyEntity findByUserAndCompanyType(UserEntity user, CompanyType companyType);
}
