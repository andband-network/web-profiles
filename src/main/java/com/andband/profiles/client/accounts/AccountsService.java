package com.andband.profiles.client.accounts;

import com.andband.profiles.util.RestApiTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

    private RestApiTemplate accountsApi;

    public AccountsService(@Qualifier("accountsApi") RestApiTemplate accountsApi) {
        this.accountsApi = accountsApi;
    }

    public Account getAccountFromId(String accountId) {
        return accountsApi.get("/accounts/" + accountId, Account.class);
    }

}
