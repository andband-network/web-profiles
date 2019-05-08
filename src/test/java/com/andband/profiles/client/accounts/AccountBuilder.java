package com.andband.profiles.client.accounts;

public class AccountBuilder {

    private Account account;

    public AccountBuilder() {
        account = new Account();
    }

    public AccountBuilder withId(String id) {
        account.setId(id);
        return this;
    }

    public AccountBuilder withName(String name) {
        account.setName(name);
        return this;
    }

    public AccountBuilder withEmail(String email) {
        account.setEmail(email);
        return this;
    }

    public Account build() {
        return account;
    }

}
