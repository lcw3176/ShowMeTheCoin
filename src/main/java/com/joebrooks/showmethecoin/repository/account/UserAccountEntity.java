package com.joebrooks.showmethecoin.repository.account;

import com.joebrooks.showmethecoin.repository.CompanyType;
import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "user_account")
public class UserAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_balance")
    private double balance;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user")
    private UserEntity user;

    @Column(name = "company_type")
    @Enumerated(EnumType.STRING)
    private CompanyType companyType;


    public void changeBalance(double balance){
        this.balance = balance;
    }


}
