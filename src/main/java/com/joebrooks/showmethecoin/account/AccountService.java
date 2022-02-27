package com.joebrooks.showmethecoin.account;

import com.joebrooks.showmethecoin.common.upbit.UpBitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UpBitClient upBitClient;
    private final String uri = "/accounts";

    public AccountResponse[] getAccountData() {
        return upBitClient.requestAccountData(uri);
    }
}
