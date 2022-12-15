package com.joebrooks.showmethecoin.exchange.upbit.account;

import com.joebrooks.showmethecoin.exchange.upbit.UpBitCoinType;
import com.joebrooks.showmethecoin.exchange.upbit.client.UpBitClient;
import com.joebrooks.showmethecoin.repository.userkey.UserKeyEntity;
import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UpBitClient upBitClient;


    private AccountResponse[] getAccountData(UserKeyEntity userKey) {
        String uri = "/accounts";

        return upBitClient.get(uri, userKey, AccountResponse[].class);
    }

    public AccountResponse getKRWCurrency(UserKeyEntity userKey) {
        return Arrays.stream(getAccountData(userKey))
                .filter(data -> data.getCurrency().equals("KRW"))
                .findFirst()
                .orElseThrow(() -> {
                    throw new NoSuchElementException("계좌 정보가 없습니다");
                });
    }

    public AccountResponse getCoinCurrency(UserKeyEntity userKey, UpBitCoinType upBitCoinType) {
        return Arrays.stream(getAccountData(userKey))
                .filter(data -> data.getCurrency().equals(upBitCoinType.toString()))
                .findFirst()
                .orElseThrow(() -> {
                    throw new NoSuchElementException("코인 정보가 없습니다");
                });
    }
}
