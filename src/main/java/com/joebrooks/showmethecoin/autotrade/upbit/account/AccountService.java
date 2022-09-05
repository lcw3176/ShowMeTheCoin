package com.joebrooks.showmethecoin.autotrade.upbit.account;

import com.joebrooks.showmethecoin.autotrade.upbit.CoinType;
import com.joebrooks.showmethecoin.autotrade.upbit.client.UpBitClient;
import com.joebrooks.showmethecoin.repository.userkey.UserKeyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UpBitClient upBitClient;
    

    private AccountResponse[] getAccountData(UserKeyEntity userKey) {
        String uri = "/accounts";

        return upBitClient.get(uri, true, userKey, AccountResponse[].class);
    }

    public AccountResponse getKRWCurrency(UserKeyEntity userKey){
        return Arrays.stream(getAccountData(userKey))
                .filter(data -> data.getCurrency().equals("KRW"))
                .findFirst()
                .orElseThrow(() ->{
                    throw new RuntimeException("계좌 정보가 없습니다");
                });
    }

    public AccountResponse getCoinCurrency(UserKeyEntity userKey, CoinType coinType){
        return Arrays.stream(getAccountData(userKey))
                .filter(data -> data.getCurrency().equals(coinType.toString()))
                .findFirst()
                .orElse(AccountResponse.builder().balance("0").build());
    }
}
