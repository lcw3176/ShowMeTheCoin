package com.joebrooks.showmethecoin.upbit.account;

import com.joebrooks.showmethecoin.upbit.client.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UpBitClient upBitClient;

    public AccountResponse[] getAccountData() {
        String uri = "/accounts";

        return upBitClient.get(uri, true, AccountResponse[].class);
    }
}
