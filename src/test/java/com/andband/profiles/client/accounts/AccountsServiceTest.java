package com.andband.profiles.client.accounts;

import com.andband.profiles.util.RestApiTemplate;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountsServiceTest {

    @InjectMocks
    private AccountsService accountsService;

    @Mock
    private RestApiTemplate mockAccountsApi;

    @BeforeMethod
    public void init() {
        initMocks(this);
    }

    @Test
    public void testGetAccountFromId() {
        String accountId = "abcd1234";

        Account expectedAccount = new AccountBuilder()
                .withId(accountId)
                .withName("John")
                .withEmail("john@email.com")
                .build();

        when(mockAccountsApi.get("/accounts/" + accountId, Account.class)).thenReturn(expectedAccount);

        Account actualAccount = accountsService.getAccountFromId(accountId);

        assertThat(actualAccount).isEqualTo(expectedAccount);

        verify(mockAccountsApi).get("/accounts/" + accountId, Account.class);
    }

}
