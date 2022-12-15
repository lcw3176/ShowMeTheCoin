package com.joebrooks.showmethecoin.repository.userkey;

import com.joebrooks.showmethecoin.exchange.CompanyType;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user_key")
public class UserKeyEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "company_type")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user")
    private UserEntity user;

}
