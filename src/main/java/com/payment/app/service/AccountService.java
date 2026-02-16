package com.payment.app.service;

import com.payment.app.dto.AccountDto;
import com.payment.app.model.Account;

public interface AccountService {

    Account findById(String accountNo);

    void save(Account account);

    AccountDto get(String accountNo);

    void deleteAll();

}
